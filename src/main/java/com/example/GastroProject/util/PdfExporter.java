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
import java.time.LocalDate;
import java.util.List;

public class PdfExporter {

    public static byte[] exportTreatmentListToPdf(String patientName, List<TreatmentDto> treatmentList) throws DocumentException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());

        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        Image background = Image.getInstance("classpath:static/images/treatment-pdf.jpg");
        background.rotate();
        background.setAbsolutePosition(0, 0);
        background.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
        document.add(background);


        Paragraph patientNameParagraph = new Paragraph("Name: " + patientName);
        document.add(patientNameParagraph);

        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(8);
        table.setTotalWidth(document.getPageSize().getWidth());
        table.setLockedWidth(true);
        table.setSpacingBefore(10f);
        table.getDefaultCell().setBackgroundColor(new BaseColor(255, 255, 255, 200));
        addTableHeader(table);
        addRows(table, treatmentList);
        document.add(table);

        document.close();
        writer.close();

        return outputStream.toByteArray();
    }


    private static void addTableHeader(PdfPTable table) throws DocumentException {

        float[] columnWidths = {4, 4, 4, 4, 4, 4, 4, 4};

        table.setWidths(columnWidths);

        String[] headers = {"Name", "Dose", "Medicine Type", "Administration", "Start Treatment", "End Treatment","Duration","Description"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(new BaseColor(217, 163, 208));
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
            table.addCell(treatment.getStartTreatment().toString());

            LocalDate endDate = treatment.getStartTreatment().plusDays(treatment.getDurationInDays()).minusDays(1);
            table.addCell(endDate.toString());
            table.addCell(treatment.getDurationInDays().toString());
            table.addCell(treatment.getDescription());
        }
    }

}
