package tech.helloworldchao.screenshot.controller;

import tech.helloworldchao.screenshot.utils.SnapUtil;
import tech.helloworldchao.screenshot.view.MainView;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class MainController {

    private MainView mainView;
    private File path = FileSystemView.getFileSystemView().getHomeDirectory();

    public MainController() {
        mainView = new MainView();
        init();
    }

    private void init() {
        mainView.setOnChooseFileButtonClickListener(e -> mainView.showChooseFileDialog(path -> {
            this.path = path;
            mainView.setPath(path.getAbsolutePath());
        }));

        mainView.setOnAboutButtonClickListener(e -> {
            mainView.showAboutMessage();
            System.out.println(mainView.getNote());
        });

        mainView.setOnSnapButtonClickListener(e -> {
            try {
                mainView.hide();
                Thread.sleep(270);
                String filePath = SnapUtil.SnapAndSave(mainView.getNote(), path);
                mainView.showMessageDialog("截图成功！已保存在" + filePath + "！");
            } catch (Exception err) {
                mainView.showMessageDialog("截图失败，请尝试将截图保存在其他磁盘或使用管理员权限运行！");
            } finally {
                mainView.show();
            }
        });

        mainView.show();
    }
}
