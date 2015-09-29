package com.chao.screenshot;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

class Global {
	public static File path = FileSystemView.getFileSystemView().getHomeDirectory();
}

public class ScreenShot {
	JFrame Frame;
	UI ui;
	BufferedImage image, saveImage;
	
	public ScreenShot(final JFrame Frame, final UI ui) {
		this.Frame = Frame;
		this.ui = ui;
		
		ui.StartBtn.addActionListener(new ActionListener() {	
			public void actionPerformed(ActionEvent e) {
				ui.setNameText(ui.NameText.getText().trim());
				ui.setIdText(ui.IdText.getText().trim());
				
				String name = ui.getNameText(),
						 id = ui.getIdText(); 
				try {
					Frame.setVisible(false);
					Thread.sleep(270);
					SnapAndSave(name, id);
					Frame.setVisible(true);
					
				} catch (AWTException | IOException e1) {
					JOptionPane.showMessageDialog(Frame, "对不起，截图失败，请重试！");
					e1.printStackTrace();
					
				} catch (InterruptedException e1) {
					JOptionPane.showMessageDialog(Frame, "对不起，截图失败，请重试！");
					e1.printStackTrace();
					
				}		

			}
		});
		
		ui.ChoosePathBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser choosePath = new JFileChooser();
				choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				choosePath.showSaveDialog(Frame);
				Global.path = choosePath.getSelectedFile();
				ui.PathText.setText(Global.path.getAbsolutePath());
			}
		});
	}
	
	public void SnapAndSave(String name, String id) throws AWTException, IOException {
		Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
        saveImage = image.getSubimage(0, 0, d.width, d.height);
        
        Graphics g = saveImage.getGraphics();
        Font font = new Font("宋体", Font.BOLD, 30);
        ImageIcon imgIcon = new ImageIcon(); 
        Image img = imgIcon.getImage();
        String strId = id;
        String strName = name;
        String strComputerId = 	System.getenv().get("COMPUTERNAME");

        g.drawImage(img, 0, 0, null);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(strComputerId, 30, 30);
        g.drawString(strName, 30, 60);
        g.drawString(strId, 30, 90);
        g.dispose();

        String format = "jpg";
        File file = new File(Global.path  + File.separator + id + "-" + name + "." + format);
        if(file.exists()){
        	JOptionPane.showMessageDialog(Frame, "目标路径存在同名文件，请先改名！");
		} else {
        	ImageIO.write(saveImage, format, file);
        	JOptionPane.showMessageDialog(Frame, "截图成功！已保存在" + Global.path + "！");
        }
	}
}
