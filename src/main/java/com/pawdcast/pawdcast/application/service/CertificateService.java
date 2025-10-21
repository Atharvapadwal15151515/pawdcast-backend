package com.pawdcast.pawdcast.application.service;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CertificateService {

    public byte[] generateAdoptionCertificate(
            String fullName,
            String email,
            String phone,
            String address,
            List<String> submittedDocuments,
            int seekerId,
            byte[] profilePhoto
    ) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add header with logo and title
            addHeader(document, profilePhoto);
            
            // Add main content
            addMainContent(document, fullName, email, phone, address, submittedDocuments, seekerId);
            
            // Add footer
            addFooter(document);
            
            document.close();
            return baos.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating certificate", e);
        }
    }

    private void addHeader(Document document, byte[] profilePhoto) {
        // Title
        Paragraph title = new Paragraph("ADOPTION APPLICATION RECEIPT")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(20);
        document.add(title);

        // Logo and date row
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{30, 40, 30}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        // Left cell - Profile photo
        if (profilePhoto != null) {
            try {
                Image image = new Image(ImageDataFactory.create(profilePhoto));
                image.setMaxWidth(80);
                image.setMaxHeight(80);
                headerTable.addCell(new Cell().add(image).setBorder(null));
            } catch (Exception e) {
                headerTable.addCell(new Cell().add(new Paragraph("Profile Photo\nNot Available")).setBorder(null));
            }
        } else {
            headerTable.addCell(new Cell().add(new Paragraph("Profile Photo\nNot Available")).setBorder(null));
        }

        // Middle cell - Organization info
        Paragraph orgInfo = new Paragraph()
                .add(new Text("PawdCast Adoption Center\n").setBold().setFontSize(14))
                .add("Official Adoption Application Receipt\n")
                .add("Certificate of Application Submission")
                .setTextAlignment(TextAlignment.CENTER);
        headerTable.addCell(new Cell().add(orgInfo).setBorder(null));

        // Right cell - Date
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        Paragraph date = new Paragraph("Date: " + currentDate)
                .setTextAlignment(TextAlignment.RIGHT);
        headerTable.addCell(new Cell().add(date).setBorder(null));

        document.add(headerTable);
        document.add(new Paragraph("\n"));
    }

    private void addMainContent(Document document, String fullName, String email, String phone, 
                              String address, List<String> submittedDocuments, int seekerId) {
        
        // Applicant Information Section
        Paragraph sectionTitle = new Paragraph("APPLICANT INFORMATION")
                .setBold()
                .setFontSize(16)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(10);
        document.add(sectionTitle);

        // Applicant details table
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginBottom(20);

        addTableRow(infoTable, "Full Name:", fullName != null ? fullName : "Not provided");
        addTableRow(infoTable, "Email Address:", email != null ? email : "Not provided");
        addTableRow(infoTable, "Phone Number:", phone != null ? phone : "Not provided");
        addTableRow(infoTable, "Address:", address != null ? address : "Not provided");
        addTableRow(infoTable, "Application ID:", "PAW-" + String.format("%06d", seekerId));
        addTableRow(infoTable, "Submission Date:", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        document.add(infoTable);

        // Submitted Documents Section
        Paragraph docsTitle = new Paragraph("SUBMITTED DOCUMENTS")
                .setBold()
                .setFontSize(16)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(10)
                .setMarginTop(20);
        document.add(docsTitle);

        if (submittedDocuments != null && !submittedDocuments.isEmpty()) {
            // Create a simple list without advanced formatting
            for (String doc : submittedDocuments) {
                Paragraph docItem = new Paragraph("• " + doc)
                        .setMarginBottom(5);
                document.add(docItem);
            }
        } else {
            document.add(new Paragraph("No documents submitted").setItalic());
        }

        // Status and Notes
        Paragraph statusSection = new Paragraph("\nAPPLICATION STATUS")
                .setBold()
                .setFontSize(16)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMarginBottom(10)
                .setMarginTop(20);
        document.add(statusSection);

        Paragraph status = new Paragraph()
                .add("Status: ")
                .add(new Text("RECEIVED - UNDER REVIEW").setBold().setFontColor(ColorConstants.BLUE))
                .add("\n\nThis receipt confirms that your adoption application has been successfully submitted. " +
                     "Our team will review your application and contact you within 3-5 business days. " +
                     "Please keep this receipt for your records.")
                .setMarginBottom(20);
        document.add(status);
    }

    private void addFooter(Document document) {
        document.add(new Paragraph("\n\n"));

        // Contact Information
        Paragraph contactInfo = new Paragraph()
                .add("For inquiries, please contact:\n")
                .add(new Text("PawdCast Adoption Center\n").setBold())
                .add("Email: adoptions@pawdcast.org | Phone: (555) 123-4567\n")
                .add("Website: www.pawdcast.org")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY);
        document.add(contactInfo);

        // Footer note
        Paragraph footerNote = new Paragraph(
                "This is an official receipt of your adoption application submission. " +
                "Please retain this document for your records.")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(9)
                .setFontColor(ColorConstants.GRAY)
                .setMarginTop(10);
        document.add(footerNote);
    }

    private void addTableRow(Table table, String label, String value) {
        table.addCell(new Cell().add(new Paragraph(label).setBold()).setBorder(null));
        table.addCell(new Cell().add(new Paragraph(value != null ? value : "Not provided")).setBorder(null));
    }
}