/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poly.hostel.util;


import java.awt.print.PrinterJob;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import poly.hostel.entity.BillDetail;


/**
 *
 * @author admin
 */
public class BillGenerator {

    
    public void printBill(String billId, List<BillDetail> billDetails,
            double totalAmount, String checkinTime, String username) {
        // Hiển thị thông báo đang xử lý (tùy chọn)
        JOptionPane.showMessageDialog(null, "Đang chuẩn bị hóa đơn để in. Vui lòng chờ...", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Tạo đối tượng Printable với dữ liệu hóa đơn
                BillPrintable printableBill = new BillPrintable(billId, billDetails, totalAmount, checkinTime, username);

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printableBill);

                // Mở hộp thoại in. printDialog() chạy trên EDT, nhưng vì nó được gọi từ doInBackground,
                // nó sẽ không làm đơ UI chính. Tuy nhiên, bản thân hộp thoại đó vẫn là modal.
                boolean doPrint = job.printDialog();

                if (doPrint) {
                    job.print(); // Thực hiện in trên luồng nền
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Hủy bỏ in hóa đơn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // Kiểm tra xem có ngoại lệ nào xảy ra trong doInBackground không
                    
                    JOptionPane.showMessageDialog(null, "Hóa đơn đã được gửi đến máy in (hoặc bị hủy).", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi in hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }.execute(); // Bắt đầu thực thi SwingWorker
    }

    // Ví dụ sử dụng (main method chỉ để test, sẽ xóa hoặc không dùng trong ứng dụng thực tế)
}
