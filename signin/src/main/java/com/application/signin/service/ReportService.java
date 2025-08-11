package com.application.signin.service;

import com.application.signin.entity.User;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import jakarta.mail.internet.MimeMessage;

import com.itextpdf.kernel.pdf.PdfDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Service
public class ReportService {

    @Autowired
    private JavaMailSender mailSender;

    public void generateAndSendReport(User user) throws Exception {
        ByteArrayOutputStream pdfOut = generatePdf(user);
        ByteArrayOutputStream excelOut = generateExcel(user);
        ByteArrayOutputStream wordOut = generateWord(user);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(user.getEmail());
        helper.setSubject("Your Login Report");
        helper.setText("Attached are your login reports in PDF, Excel, and Word formats.", false);

        helper.addAttachment("login-report.pdf", new ByteArrayResource(pdfOut.toByteArray()));
        helper.addAttachment("login-report.xlsx", new ByteArrayResource(excelOut.toByteArray()));
        helper.addAttachment("login-report.docx", new ByteArrayResource(wordOut.toByteArray()));

        mailSender.send(mimeMessage);
    }

    public ByteArrayOutputStream generatePdf(User user) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Login Report"));
        document.add(new Paragraph("Username: " + user.getUsername()));
        document.add(new Paragraph("Role: " + user.getRole()));
        document.add(new Paragraph("Email: " + user.getEmail()));
        document.add(new Paragraph("Login Time: " + LocalDateTime.now()));

        document.close();
        return out;
    }

    public ByteArrayOutputStream generateExcel(User user) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Login Report");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Field");
        header.createCell(1).setCellValue("Value");

        Row row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("Username");
        row1.createCell(1).setCellValue(user.getUsername());

        Row row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("Role");
        row2.createCell(1).setCellValue(user.getRole());

        Row row3 = sheet.createRow(3);
        row3.createCell(0).setCellValue("Email");
        row3.createCell(1).setCellValue(user.getEmail());

        Row row4 = sheet.createRow(4);
        row4.createCell(0).setCellValue("Login Time");
        row4.createCell(1).setCellValue(LocalDateTime.now().toString());

        try {
            workbook.write(out);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public ByteArrayOutputStream generateWord(User user) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XWPFDocument document = new XWPFDocument();

        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText("Login Report");
        run.setBold(true);
        run.setFontSize(14);

        XWPFParagraph p1 = document.createParagraph();
        p1.createRun().setText("Username: " + user.getUsername());

        XWPFParagraph p2 = document.createParagraph();
        p2.createRun().setText("Role: " + user.getRole());

        XWPFParagraph p3 = document.createParagraph();
        p3.createRun().setText("Email: " + user.getEmail());

        XWPFParagraph p4 = document.createParagraph();
        p4.createRun().setText("Login Time: " + LocalDateTime.now());

        try {
            document.write(out);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }
}
