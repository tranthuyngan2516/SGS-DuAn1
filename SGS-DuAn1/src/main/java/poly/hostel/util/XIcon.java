package poly.hostel.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import com.formdev.flatlaf.extras.FlatSVGIcon; // Import FlatSVGIcon

public class XIcon {
    /**
     * Đọc icon từ resource, file hoặc SVG
     * @param path đường dẫn file, đường dẫn resource hoặc tên resource
     * @return ImageIcon (có thể là FlatSVGIcon hoặc ImageIcon truyền thống)
     */
    public static ImageIcon getIcon(String path) {
        if (!path.contains("/") && !path.contains("\\")) { // resource name
            return XIcon.getIcon("/poly/hostel/icons/" + path);
        }

        // Kiểm tra nếu là file SVG
        if (path.toLowerCase().endsWith(".svg")) {
            try {
                // Thử tải SVG từ resource
                java.net.URL imageUrl = XIcon.class.getResource(path);
                if (imageUrl != null) {
                    return new FlatSVGIcon(imageUrl);
                }
                // Thử tải SVG từ đường dẫn file tuyệt đối
                File file = new File(path);
                if (file.exists()) {
                    return new FlatSVGIcon(file);
                }
            } catch (Exception e) {
                System.err.println("Error loading SVG icon: " + path + " - " + e.getMessage());
                // Fallback hoặc trả về null
            }
        }

        // Nếu không phải SVG hoặc lỗi khi tải SVG, thử tải ImageIcon truyền thống
        String resourcePath = path.startsWith("/") ? path : "/" + path;
        java.net.URL imageUrl = XIcon.class.getResource(resourcePath);
        if (imageUrl != null) {
            return new ImageIcon(imageUrl);
        } else {
            File file = new File(path);
            if (file.exists()) {
                return new ImageIcon(path);
            } else {
                return null;
            }
        }
    }

    /**
     * Đọc icon theo kích thước (đặc biệt hữu ích cho PNG/JPG, SVG tự scale)
     * @param path đường dẫn file hoặc tài nguyên
     * @param width chiều rộng
     * @param height chiều cao
     * @return Icon
     */
    public static ImageIcon getIcon(String path, int width, int height) {
        ImageIcon originalIcon = getIcon(path);
        if (originalIcon == null) {
            return null;
        }

        // Nếu là FlatSVGIcon, nó sẽ tự scale, bạn có thể thiết lập kích thước trực tiếp
        if (originalIcon instanceof FlatSVGIcon) {
            FlatSVGIcon svgIcon = (FlatSVGIcon) originalIcon;
            return svgIcon.derive(width, height); // derive để tạo icon mới với kích thước mong muốn
        } else {
            // Nếu là ImageIcon truyền thống, scale hình ảnh
            Image image = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        }
    }

    // Các phương thức còn lại của XIcon không thay đổi
    public static void setIcon(JLabel label, String path) {
        int width = label.getWidth() > 0 ? label.getWidth() : 150; // Default width
        int height = label.getHeight() > 0 ? label.getHeight() : 150; // Default height
        ImageIcon icon = XIcon.getIcon(path, width, height);
        label.setIcon(icon);
    }

    public static void setIcon(JLabel label, File file) {
        if (file != null && file.exists()) {
            try {
                int width = label.getWidth() > 0 ? label.getWidth() : 150;
                int height = label.getHeight() > 0 ? label.getHeight() : 150;
                
                ImageIcon icon = new ImageIcon(file.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                System.err.println("Error setting icon: " + e.getMessage());
                // Set default icon nếu có lỗi
                setIcon(label, "icons/default-user.png");
            }
        } else {
            label.setIcon(null); // hoặc icon mặc định
        }
    }

    public static File copyTo(File fromFile, String folder) {
        String fileExt = fromFile.getName().substring(fromFile.getName().lastIndexOf("."));
            File toFile = new File(folder, java.util.UUID.randomUUID().toString().replaceAll("-", "") + fileExt);
//          File toFile = new File(folder, XStr.getKey() + fileExt); // Assuming XStr.getKey() exists
            toFile.getParentFile().mkdirs();
        try {
            Files.copy(fromFile.toPath(), toFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return toFile;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static File copyTo(File fromFile) {
        return copyTo(fromFile, "images");
    }
}