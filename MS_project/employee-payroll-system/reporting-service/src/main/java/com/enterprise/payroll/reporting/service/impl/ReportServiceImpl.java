package com.enterprise.payroll.reporting.service.impl;

import com.enterprise.payroll.reporting.dto.ReportResponse;
import com.enterprise.payroll.reporting.entity.Report;
import com.enterprise.payroll.reporting.entity.ReportStatus;
import com.enterprise.payroll.reporting.entity.ReportType;
import com.enterprise.payroll.reporting.exception.ResourceNotFoundException;
import com.enterprise.payroll.reporting.repository.ReportRepository;
import com.enterprise.payroll.reporting.service.ReportGenerator;
import com.enterprise.payroll.reporting.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final ReportRepository reportRepository;
    private final ReportGenerator reportGenerator;

    public ReportServiceImpl(ReportRepository reportRepository, ReportGenerator reportGenerator) {
        this.reportRepository = reportRepository;
        this.reportGenerator = reportGenerator;
    }

    @Override
    public Long triggerPayrollReport(String month) {
        // Deliberately NOT @Transactional: repository.save() below commits on its
        // own as soon as this call returns. If this method carried its own
        // @Transactional, the row wouldn't be committed (and visible to other
        // connections) until triggerPayrollReport itself returns - but the
        // reportGenerator.generatePayrollSummary() call just below dispatches to
        // a *different thread* that could start (and fail findById()) before
        // that commit happens. Keeping this method transaction-free means the
        // insert is durable before we ever hand off to the async executor.
        Report report = reportRepository.save(Report.builder()
                .type(ReportType.PAYROLL_SUMMARY)
                .parameter(month)
                .status(ReportStatus.PENDING)
                .build());

        log.info("Queued payroll report id={} month={}", report.getId(), month);
        reportGenerator.generatePayrollSummary(report.getId(), month);
        return report.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        return new ReportResponse(
                report.getId(), report.getType(), report.getParameter(), report.getStatus(),
                report.getResultSummary(), report.getErrorMessage(), report.getCreatedAt(), report.getCompletedAt());
    }
}
