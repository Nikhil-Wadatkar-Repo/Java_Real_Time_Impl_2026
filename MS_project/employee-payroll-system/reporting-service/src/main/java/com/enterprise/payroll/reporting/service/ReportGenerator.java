package com.enterprise.payroll.reporting.service;

import com.enterprise.payroll.reporting.client.PayrollClient;
import com.enterprise.payroll.reporting.client.dto.PayrollDto;
import com.enterprise.payroll.reporting.entity.Report;
import com.enterprise.payroll.reporting.entity.ReportStatus;
import com.enterprise.payroll.reporting.repository.ReportRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The actual report-generation work, run on the {@code reportTaskExecutor}
 * pool. Split into its own Spring bean (rather than a method on
 * ReportServiceImpl) because {@code @Async} is proxy-based - a self-invoked
 * call from within the same class would silently run synchronously instead
 * of being intercepted and dispatched to the executor.
 */
@Component
public class ReportGenerator {

    private static final Logger log = LoggerFactory.getLogger(ReportGenerator.class);

    private final ReportRepository reportRepository;
    private final PayrollClient payrollClient;

    public ReportGenerator(ReportRepository reportRepository, PayrollClient payrollClient) {
        this.reportRepository = reportRepository;
        this.payrollClient = payrollClient;
    }

    @Async("reportTaskExecutor")
    public void generatePayrollSummary(Long reportId, String month) {
        log.info("[{}] Generating payroll summary report id={} month={}",
                Thread.currentThread().getName(), reportId, month);
        try {
            List<PayrollDto> payrolls = payrollClient.getPayrollsForMonth(month, 1000).content();

            BigDecimal totalNet = payrolls.stream()
                    .map(PayrollDto::netSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal avgNet = payrolls.isEmpty()
                    ? BigDecimal.ZERO
                    : totalNet.divide(BigDecimal.valueOf(payrolls.size()), 2, RoundingMode.HALF_UP);

            String summary = "employeesProcessed=" + payrolls.size()
                    + ", totalNetSalary=" + totalNet
                    + ", averageNetSalary=" + avgNet;

            updateReport(reportId, ReportStatus.COMPLETED, summary, null);
            log.info("[{}] Completed report id={}: {}", Thread.currentThread().getName(), reportId, summary);
        } catch (FeignException ex) {
            log.error("Report id={} failed - payroll-service call failed", reportId, ex);
            updateReport(reportId, ReportStatus.FAILED, null, "payroll-service is currently unavailable");
        } catch (Exception ex) {
            log.error("Report id={} failed unexpectedly", reportId, ex);
            updateReport(reportId, ReportStatus.FAILED, null, "Unexpected error: " + ex.getMessage());
        }
    }

    // Not @Transactional - repository.save() already manages its own transaction,
    // and a self-invoked call here couldn't be proxy-intercepted anyway.
    private void updateReport(Long reportId, ReportStatus status, String summary, String errorMessage) {
        Report report = reportRepository.findById(reportId).orElseThrow();
        report.setStatus(status);
        report.setResultSummary(summary);
        report.setErrorMessage(errorMessage);
        report.setCompletedAt(LocalDateTime.now());
        reportRepository.save(report);
    }
}
