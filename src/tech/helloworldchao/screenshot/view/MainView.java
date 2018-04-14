package tech.helloworldchao.screenshot.view;

import tech.helloworldchao.screenshot.listener.OnChooseFileClickListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView {

    private JFrame mFrame;
    private JTextField mNoteTextField = new JTextField();
    private JTextField pathText = new JTextField("默认保存在桌面");
    private JButton choosePathBtn = new JButton("改变保存路径");
    private JButton aboutBtn = new JButton("关于作者");
    private JButton startBtn = new JButton("开始截图");

    public MainView() {
        // 创建主界面
        mFrame = new JFrame("屏幕截图软件");
        mFrame.setSize(265, 140);
        mFrame.setLocation(600, 300);
        mFrame.setResizable(false);
        mFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // 添加信息界面
        JPanel topPanel = new JPanel(new GridLayout(3, 2));
        mFrame.add(topPanel, BorderLayout.NORTH);

        JLabel noteLabel = new JLabel("备注：");
        topPanel.add(noteLabel);
        topPanel.add(mNoteTextField);

        // 选择路径界面
        JLabel pathLabel = new JLabel("路径：");
        topPanel.add(pathLabel);
        topPanel.add(pathText);
        pathText.setEditable(false);

        JPanel choosePanel = new JPanel(new GridLayout(1, 1));
        mFrame.add(choosePanel, BorderLayout.CENTER);
        choosePanel.add(choosePathBtn);

        // 操作按钮界面
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        mFrame.add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(aboutBtn);
        bottomPanel.add(startBtn);
    }

    public void setOnChooseFileButtonClickListener(ActionListener l) {
        choosePathBtn.addActionListener(l);
    }

    public void setOnAboutButtonClickListener(ActionListener l) {
        aboutBtn.addActionListener(l);
    }

    public void setOnSnapButtonClickListener(ActionListener l) {
        startBtn.addActionListener(l);
    }

    public void show() {
        mFrame.setVisible(true);
    }

    public void hide() {
        mFrame.setVisible(false);
    }

    public void showMessageDialog(String message) {
        JOptionPane.showMessageDialog(mFrame, message);
    }

    public void showAboutMessage() {
        showMessageDialog("屏幕截图软件\n" +
                "版本： v1.0.1\n" +
                "作者：helloworldchao\n" +
                "邮箱：helloworldchao@outlook.com");
    }

    public void showChooseFileDialog(OnChooseFileClickListener l) {
        JFileChooser choosePath = new JFileChooser();
        choosePath.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (choosePath.showSaveDialog(mFrame) == 0 && (l != null)) {
            l.onChooseFile(choosePath.getSelectedFile());
        }
    }

    public void setPath(String path) {
        pathText.setText(path);
    }

    public String getNote() {
        return mNoteTextField.getText();
    }
}
