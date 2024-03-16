package com.example.GastroProject.util;

import com.example.GastroProject.dto.TreatmentDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfExporter {

    public static byte[] exportTreatmentListToPdfWithBackground(String patientName, List<TreatmentDto> treatmentList) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();


        Image background = Image.getInstance("classpath:static/images/treatment-pdf.jpg"); // Înlocuiește "background.jpg" cu numele imaginii tale de fundal
        background.setAbsolutePosition(0, 0);
        background.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
        document.add(background);

        Paragraph patientNameParagraph = new Paragraph("Name: " + patientName);
        document.add(patientNameParagraph);

        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        addRows(table, treatmentList);
        document.add(table);

        document.close();
        writer.close();

        return outputStream.toByteArray();
    }


    private static void addTableHeader(PdfPTable table) {
        String[] headers = {"Name", "Dose", "Medicine Type", "Administration", "Date", "Time"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(new BaseColor(220, 252, 247));
            cell.setBorderWidth(2);
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }

    private static void addRows(PdfPTable table, List<TreatmentDto> treatmentList) {
        for (TreatmentDto treatment : treatmentList) {
            table.addCell(treatment.getName());
            table.addCell(treatment.getDose());
            table.addCell(treatment.getMedicineType().toString());
            table.addCell(treatment.getAdministration().toString());
            table.addCell(treatment.getLocalDatePart().toString());
            table.addCell(treatment.getLocalTimePart().toString());
        }
    }
}
