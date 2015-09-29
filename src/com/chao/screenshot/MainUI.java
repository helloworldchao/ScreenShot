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
		final JFrame Frame = new JFrame("��Ļ��ͼ���");
		UI ui = new UI(Frame);
		new ScreenShot(Frame, ui);	
		
		ui.AboutBtn.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(Frame, "��Ļ��ͼ���\n�汾�� v1.0\n���ߣ��೬"
						+ "\n���䣺helloworldchao@outlook.com");
			}
		});
		
		Frame.setSize(265, 140);
		Frame.setLocation(600, 300);
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
	
	JPanel TopPanel = new JPanel(new GridLayout(3, 2)),
		ChoosePanel = new JPanel(new GridLayout(1, 1)),
		BottomPanel = new JPanel(new GridLayout(1, 2));
	
	JLabel IdLabel = new JLabel("ѧ�ţ�"),
		 NameLabel = new JLabel("������"),
		 PathLabel = new JLabel("·����");
	
	JButton StartBtn = new JButton("��ʼ��ͼ"),
			AboutBtn = new JButton("��������"),
	   ChoosePathBtn = new JButton("�ı䱣��·��");
	
	JTextField IdText = new JTextField(),
			 NameText = new JTextField(),
			 PathText = new JTextField("Ĭ�ϱ���������");

	public UI(JFrame Frame) {
		this.Frame = Frame;
		
		Frame.add(TopPanel, BorderLayout.NORTH);
		TopPanel.add(IdLabel);
		TopPanel.add(IdText);
		TopPanel.add(NameLabel);
		TopPanel.add(NameText);
		TopPanel.add(PathLabel);
		TopPanel.add(PathText);
		PathText.setEditable(false);
		
		Frame.add(ChoosePanel, BorderLayout.CENTER);
		ChoosePanel.add(ChoosePathBtn);
		
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
