package com.mco.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.mco.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeDocumentService {

    public byte[] generateEmployeePdf(Employee employee) {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                writeEmployeeDocument(contentStream, page, employee);
            }

            document.save(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to generate PDF for employee " + employee.getEmployeeId(), ex);
        }
    }

    public byte[] generateEmployeesZip(List<Employee> employees) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {

            for (Employee employee : employees) {
                String entryName = buildPdfFileName(employee);
                zipOutputStream.putNextEntry(new ZipEntry(entryName));
                zipOutputStream.write(generateEmployeePdf(employee));
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to generate employee PDF archive", ex);
        }
    }

    private void writeEmployeeDocument(PDPageContentStream contentStream, PDPage page, Employee employee) throws IOException {
        float margin = 50f;
        float y = page.getMediaBox().getHeight() - margin;
        float leading = 16f;
        float width = page.getMediaBox().getWidth() - (2 * margin);
        org.apache.pdfbox.pdmodel.font.PDFont bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
        contentStream.newLineAtOffset(margin, y);
        contentStream.showText("Employee Record");
        contentStream.endText();

        y -= 28f;
        List<String> lines = buildDocumentLines(employee);

        contentStream.beginText();
        contentStream.setFont(bodyFont, 11);
        contentStream.newLineAtOffset(margin, y);

        for (String line : lines) {
            for (String wrappedLine : wrapLine(line, width, bodyFont, 11f)) {
                contentStream.showText(wrappedLine);
                contentStream.newLineAtOffset(0, -leading);
            }
        }

        contentStream.endText();
    }

    private List<String> buildDocumentLines(Employee employee) {
        List<String> lines = new ArrayList<>();
        lines.add("Employee ID: " + valueOf(employee.getEmployeeId()));
        lines.add("First Name: " + valueOf(employee.getFirstName()));
        lines.add("Last Name: " + valueOf(employee.getLastName()));
        lines.add("Email: " + valueOf(employee.getEmail()));
        lines.add("Phone Number: " + valueOf(employee.getPhoneNumber()));
        lines.add("Hire Date: " + valueOf(employee.getHireDate()));
        lines.add("Job ID: " + valueOf(employee.getJobId()));
        lines.add("Salary: " + valueOf(employee.getSalary()));
        lines.add("Manager ID: " + valueOf(employee.getManagerId()));
        lines.add("Department ID: " + valueOf(employee.getDepartmentId()));
        lines.add("Designation: " + valueOf(employee.getDesignation()));
        lines.add("Employment Type: " + valueOf(employee.getEmploymentType()));
        lines.add("Gender: " + valueOf(employee.getGender()));
        lines.add("Status: " + valueOf(employee.getStatus()));
        lines.add("Address: " + valueOf(employee.getAddress()));
        lines.add("City: " + valueOf(employee.getCity()));
        lines.add("State: " + valueOf(employee.getState()));
        lines.add("Country: " + valueOf(employee.getCountry()));
        lines.add("Date of Birth: " + valueOf(employee.getDateOfBirth()));
        lines.add("Last Working Date: " + valueOf(employee.getLastWorkingDate()));
        lines.add("Bonus: " + valueOf(employee.getBonus()));
        lines.add("Manager Name: " + valueOf(employee.getManagerName()));
        return lines;
    }

    private List<String> wrapLine(String line, float maxWidth, org.apache.pdfbox.pdmodel.font.PDFont font, float fontSize) throws IOException {
        List<String> wrapped = new ArrayList<>();
        String[] words = line.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String candidate = currentLine.length() == 0 ? word : currentLine + " " + word;
            if (font.getStringWidth(candidate) / 1000 * fontSize <= maxWidth) {
                currentLine.setLength(0);
                currentLine.append(candidate);
            } else {
                if (currentLine.length() > 0) {
                    wrapped.add(currentLine.toString());
                }
                currentLine.setLength(0);
                currentLine.append(word);
            }
        }

        if (currentLine.length() > 0) {
            wrapped.add(currentLine.toString());
        }

        if (wrapped.isEmpty()) {
            wrapped.add("");
        }

        return wrapped;
    }

    private String buildPdfFileName(Employee employee) {
        String firstName = safeFileToken(employee.getFirstName());
        String lastName = safeFileToken(employee.getLastName());
        String employeeId = employee.getEmployeeId() == null ? "new" : String.valueOf(employee.getEmployeeId());
        return "employee-" + employeeId + "-" + firstName + "-" + lastName + ".pdf";
    }

    private String safeFileToken(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }
        return value.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
    }

    private String valueOf(Object value) {
        if (value == null) {
            return "N/A";
        }
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal.toPlainString();
        }
        if (value instanceof LocalDate localDate) {
            return localDate.toString();
        }
        return String.valueOf(value);
    }
}
