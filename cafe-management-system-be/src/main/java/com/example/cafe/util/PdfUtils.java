package com.example.cafe.util;

import com.example.cafe.model.dto.BillDto;
import com.example.cafe.model.dto.ReportItemDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class PdfUtils {

    public static void generateAndSaveBillReport(BillDto billDto, String storeLocation) throws FileNotFoundException, DocumentException, JsonProcessingException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(storeLocation + billDto.getUuid() + ".pdf"));

        document.open();
        document.add(createRectangle());
        document.add(createHeader());
        document.add(createDataSection(billDto));
        document.add(createItemsTable(billDto.getProductDetail()));
        document.add(createFooter(billDto));
        document.close();
    }

    private static Font getFont(String type) {
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private static Rectangle createRectangle() {
        final Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        return rect;
    }

    private static Paragraph createHeader() {
        final Paragraph header = new Paragraph("Cafe Management System", getFont("Header"));
        header.setAlignment(Element.ALIGN_CENTER);
        return header;
    }

    private static Paragraph createDataSection(BillDto billDto) {
        final String data = String.format(
                "Name: %s \nContact Number: %s \nEmail: %s \nPayment Method: %s \n",
                billDto.getName(), billDto.getContactNumber(), billDto.getEmail(), billDto.getPaymentMethod());
        return new Paragraph(data + "\n \n", getFont("Data"));
    }

    private static PdfPTable createItemsTable(String productDetail) throws JsonProcessingException {
        final PdfPTable table = new PdfPTable(5);

        //Add table header
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });

        //Add rows
        ObjectMapper objectMapper = new ObjectMapper();
        List<ReportItemDto> reportItems = objectMapper.readValue(productDetail, new TypeReference<>() {
        });

        for (ReportItemDto item : reportItems) {
            table.addCell(item.getName());
            table.addCell(item.getCategory());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.valueOf(item.getPrice()));
            table.addCell(String.valueOf(item.getTotal()));
        }

        return table;
    }

    private static Paragraph createFooter(BillDto billDto) {
        return new Paragraph(
                String.format("Total : %s \nThank you for visiting. Please visit again!!", billDto.getTotal()),
                getFont("Data"));
    }
}