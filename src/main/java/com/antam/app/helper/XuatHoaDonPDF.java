package com.antam.app.helper;

import com.antam.app.entity.ChiTietHoaDon;
import com.antam.app.entity.ChiTietPhieuDatThuoc;
import com.antam.app.entity.Thuoc;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * @description
 * @author: Pham Dang Khoa
 * @date: 18/12/2025
 * @version: 1.0
 */

public class XuatHoaDonPDF {

    private static final String FONT_PATH = "c:/windows/fonts/arial.ttf";
    /**
     * @description Phương thức xuất hóa đơn bán thuốc ra file PDF với ArrayList
     * @param file      File PDF đích
     * @param dsThuoc  Danh sách chi tiết hóa đơn
     * @param thue      Số tiền thuế
     * @param tongTien  Tổng số tiền thanh toán
     * @throws Exception Nếu có lỗi trong quá trình xuất file
     */
    public static void xuatFilePDF(
            File file,
            List<ChiTietHoaDon> dsThuoc,
            double thue,
            double tongTien
    ) throws Exception {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        BaseFont bf = BaseFont.createFont(
                FONT_PATH,
                BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED
        );

        Font titleFont = new Font(bf, 18, Font.BOLD);
        Font normalFont = new Font(bf, 12);
        Font boldFont = new Font(bf, 12, Font.BOLD);

        // ===== TIÊU ĐỀ =====
        Paragraph title = new Paragraph("HÓA ĐƠN BÁN THUỐC", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Ngày: " + LocalDate.now(), normalFont));
        document.add(new Paragraph(" "));

        // ===== BẢNG CHI TIẾT =====
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 4, 2, 2, 2});

        addHeader(table, "STT", boldFont);
        addHeader(table, "Tên thuốc", boldFont);
        addHeader(table, "SL", boldFont);
        addHeader(table, "Đơn giá", boldFont);
        addHeader(table, "Thành tiền", boldFont);

        int stt = 1;
        for (ChiTietHoaDon ct : dsThuoc) {
            Thuoc t = ct.getMaCTT().getMaThuoc();
            double thanhTien = ct.getSoLuong() * t.getGiaBan();

            table.addCell(String.valueOf(stt++));
            table.addCell(t.getTenThuoc());
            table.addCell(String.valueOf(ct.getSoLuong()));
            table.addCell(formatTien(t.getGiaBan()));
            table.addCell(formatTien(thanhTien));
        }

        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thuế: " + formatTien(thue), normalFont));
        document.add(new Paragraph("TỔNG THANH TOÁN: " + formatTien(tongTien), titleFont));

        document.close();
    }

    // ===== HỖ TRỢ =====
    private static void addHeader(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
    }

    private static String formatTien(double tien) {
        return String.format("%,.0f đ", tien);
    }

}
