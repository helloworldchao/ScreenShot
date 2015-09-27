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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

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
					JOptionPane.showMessageDialog(Frame, "�Բ��𣬽�ͼʧ�ܣ������ԣ�");
					e1.printStackTrace();
					
				} catch (InterruptedException e1) {
					JOptionPane.showMessageDialog(Frame, "�Բ��𣬽�ͼʧ�ܣ������ԣ�");
					e1.printStackTrace();
					
				}		

			}
		});
	}
	
	public void SnapAndSave(String name, String id) throws AWTException, IOException {
		Robot robot = new Robot();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        image = robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
        saveImage = image.getSubimage(0, 0, d.width, d.height);
        
        Graphics g = saveImage.getGraphics();
        Font font = new Font("����", Font.BOLD, 30);
        ImageIcon imgIcon = new ImageIcon(); 
        Image img = imgIcon.getImage();
        String strId = id;
        String strName = name;

        g.drawImage(img, 0, 0, null);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(strId, 30, 30);
        g.drawString(strName, 125, 60);
        g.dispose();

		File path = FileSystemView.getFileSystemView().getHomeDirectory();
        String format = "jpg";
        File file = new File(path + File.separator + id + "-" + name + "." + format);
        if(file.exists()){
        	JOptionPane.showMessageDialog(Frame, "�������ͬ���ļ������ȸ�����");
		} else {
        	ImageIO.write(saveImage, format, file);
        	JOptionPane.showMessageDialog(Frame, "��ͼ�ɹ����ѱ��������棡");
        }
	}
}
