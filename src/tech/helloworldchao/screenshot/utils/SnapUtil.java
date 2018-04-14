package tech.helloworldchao.screenshot.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SnapUtil {
    public static String SnapAndSave(String note, File path) throws IOException, AWTException {
        Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
        BufferedImage saveImage = image.getSubimage(0, 0, d.width, d.height);

        Graphics g = saveImage.getGraphics();
        Font font = new Font("宋体", Font.BOLD, 30);
        ImageIcon imgIcon = new ImageIcon();
        Image img = imgIcon.getImage();

        // 添加水印
        g.drawImage(img, 0, 0, null);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(note, 30, 60);
        g.dispose();

        File defaultPath = (null == path ? FileSystemView.getFileSystemView().getHomeDirectory() : path);
        String format = "jpg";
        String filePath = defaultPath + File.separator + "ScreenShot." + format;
        File file = new File(filePath);
        ImageIO.write(saveImage, format, file);
        return filePath;
    }
}
