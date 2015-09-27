package com.chao.screenshot;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainUI {
	public static void main(String[] args) {
		final JFrame Frame = new JFrame("屏幕截图软件");
		UI ui = new UI(Frame);
		new ScreenShot(Frame, ui);	
		
		ui.AboutBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(Frame, "屏幕截图软件\n版本： Beta 1.0\n作者：余超"
						+ "\n邮箱：helloworldchao@outlook.com");
			}
		});
		
		Frame.setSize(280, 170);
		Frame.setLocation(550, 250);
		Frame.setResizable(false);
		Frame.setVisible(true);
		
		Frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
	}
}

class UI {
	JFrame Frame;
	String name, id;
	
	JPanel TopPanel = new JPanel(new GridLayout(4, 1)),
		BottomPanel = new JPanel(new GridLayout(1, 2));
	
	JLabel IdLabel = new JLabel("学号："),
		 NameLabel = new JLabel("姓名：");
	
	JButton StartBtn = new JButton("开始截图"),
			AboutBtn = new JButton("关于作者");
	
	JTextField IdText = new JTextField(),
			 NameText = new JTextField();

	public UI(JFrame Frame) {
		this.Frame = Frame;
		
		Frame.add(TopPanel, BorderLayout.NORTH);
		TopPanel.add(IdLabel);
		TopPanel.add(IdText);
		TopPanel.add(NameLabel);
		TopPanel.add(NameText);
		
		Frame.add(BottomPanel, BorderLayout.SOUTH);
		BottomPanel.add(StartBtn);
		BottomPanel.add(AboutBtn);

	}
	
	public void setNameText(String name) {
		this.name = name;
	}
	
	public void setIdText(String id) {
		this.id = id;
	}
	
	public String getNameText() {
		return name;
	}
	
	public String getIdText() {
		return id;
	}
}
