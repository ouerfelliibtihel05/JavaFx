package com.gestion.projet;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileOutputStream;
import java.io.IOException;

public class PDFGenerator {

    public void generatePatientPDF(Patient patient) {
        String dest = "patient_details.pdf";
        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Patient Details"));
            document.add(new Paragraph("CIN: " + patient.getCin()));
            document.add(new Paragraph("Nom: " + patient.getNom()));
            document.add(new Paragraph("Prénom: " + patient.getPrenom()));
            document.add(new Paragraph("Téléphone: " + patient.getTelephone()));
            document.add(new Paragraph("Sexe: " + patient.getSexe()));

            document.close();
            System.out.println("PDF created: " + dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
