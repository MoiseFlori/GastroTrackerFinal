package com.example.GastroProject.util;

import com.example.GastroProject.dto.TreatmentDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfExporter {

    public static void exportTreatmentListToPdf(List<TreatmentDto> treatmentList, String filePath) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            PdfPTable table = new PdfPTable(6);

            addTableHeader(table);
            addRows(table, treatmentList);

            document.add(table);
            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void addTableHeader(PdfPTable table) {
        String[] headers = {"Name", "Dose", "Medicine Type", "Administration", "Date", "Time"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell();
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
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
