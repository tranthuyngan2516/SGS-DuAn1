package poly.hostel.util;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.util.List;
import poly.hostel.entity.BillDetail;

/**
 *
 * @author admin
 */
public class BillPrintable implements Printable{
    
    private String billId;
    private List<BillDetail> billDetails;
    private double totalAmount;
    private String checkinTime;
    private String checkoutTime; // Thêm thời gian thanh toán nếu cần
    private String username; // Tên nhân viên

    public BillPrintable(String billId, List<BillDetail> billDetails,
            double totalAmount, String checkinTime, String username) {
        this.billId = billId;
        this.billDetails = billDetails;
        this.totalAmount = totalAmount;
        this.checkinTime = checkinTime;
        this.username = username;
        // Nếu bạn muốn hiển thị checkout time, bạn có thể truyền thêm vào đây
        // this.checkoutTime = checkoutTime;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
       if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        int paperWidth = (int) (pageFormat.getImageableWidth());
        int y = 0;
        int lineHeight = 15;
        int margin = 10;

        // *************** Thông tin cửa hàng ***************
        g2d.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2d.drawString("POLY CAFE", margin, y += lineHeight);
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g2d.drawString("Dia chi: 123 Duong ABC, Quan Cái Răng, TP.Cần Thơ", margin, y += lineHeight);
        g2d.drawString("Dien thoai: 0941-223-154", margin, y += lineHeight);
        y += lineHeight;

        // *************** Tiêu đề hóa đơn ***************
        g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
        g2d.drawString("HOA DON THANH TOAN", (paperWidth - g2d.getFontMetrics().stringWidth("HOA DON THANH TOAN")) / 2, y += lineHeight);
        y += lineHeight;

        // *************** Thông tin hóa đơn ***************
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g2d.drawString("Ma Hoa Don: " + billId, margin, y += lineHeight);
        g2d.drawString("Ngay/Gio: " + checkinTime, margin, y += lineHeight);
        g2d.drawString("Nhan Vien: " + username, margin, y += lineHeight);

        y += lineHeight;
        g2d.drawString("----------------------------------------", margin, y += lineHeight);

        // *************** Bảng chi tiết hóa đơn ***************
        g2d.setFont(new Font("Monospaced", Font.BOLD, 10));
        // Điều chỉnh lại độ rộng cột nếu cần để phù hợp với tên sản phẩm dài
        // %-20s cho Ten SP, %-5s cho SL, %-8s cho Don Gia, %-5s cho Giam, %-10s cho Thanh Tien
        String headerFormat = "%-20s %-5s %-8s %-10s";
        g2d.drawString(String.format(headerFormat, "Ten SP", "SL", "Don Gia", "Thanh Tien"), margin, y += lineHeight);
        g2d.drawString("----------------------------------------", margin, y += lineHeight);

        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
        for (BillDetail detail : billDetails) {
            double itemTotal = detail.getDonGia()* detail.getSoLuong();

            // Gộp tất cả thông tin vào MỘT DÒNG DUY NHẤT và sử dụng cùng định dạng với header
            String itemLine = String.format(headerFormat,
                    truncateString(detail.getMaSP(), 20), // Cắt bớt tên nếu quá dài
                    detail.getSoLuong(),
                    String.format("%,.0f", detail.getDonGia()),
                    String.format("%,.0f", itemTotal));
            
            g2d.drawString(itemLine, margin, y += lineHeight); // Vẽ nguyên dòng itemLine
        }

        g2d.drawString("----------------------------------------", margin, y += lineHeight);

        // *************** Tổng cộng ***************
        g2d.setFont(new Font("Monospaced", Font.BOLD, 12));
        String totalStr = "TONG CONG: " + String.format("%,.0f VNĐ", totalAmount);
        g2d.drawString(totalStr, paperWidth - g2d.getFontMetrics().stringWidth(totalStr) - margin, y += lineHeight);

        y += lineHeight * 2;

        // *************** Chân hóa đơn ***************
        g2d.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g2d.drawString("Cam on quy khach!", (paperWidth - g2d.getFontMetrics().stringWidth("Cam on quy khach!")) / 2, y += lineHeight);
        g2d.drawString("Hen gap lai!", (paperWidth - g2d.getFontMetrics().stringWidth("Hen gap lai!")) / 2, y += lineHeight);
        y += lineHeight;

        g2d.drawString("", margin, y + lineHeight * 2);

        return PAGE_EXISTS;
    }
     private String truncateString(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength);
        }
        return text;
    }

  
}
