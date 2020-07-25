package tech.helloworldchao.screenshot.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import tech.helloworldchao.screenshot.listener.OnDialogCloseListener;
import tech.helloworldchao.screenshot.utils.SnapUtil;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class MainController {

    @FXML
    private GridPane mMainView;
    @FXML
    private TextField mTfNote;
    @FXML
    private TextField mTfPath;

    private Stage stage;
    private File path = FileSystemView.getFileSystemView().getHomeDirectory();

    public void setStage(Stage primaryStage) {
        this.stage = primaryStage;
    }

    @FXML
    private void snap() {
        if (stage == null) return;
        try {
            stage.hide();
            Thread.sleep(270);
            String filePath = SnapUtil.SnapAndSave(mTfNote.getText(), path);
            showMessageDialog("提示",
                    "截图成功！已保存在" + filePath + "！",
                    () -> stage.show());
        } catch (Exception err) {
            showMessageDialog("提示",
                    "截图失败，请尝试将截图保存在其他磁盘或使用管理员权限运行！",
                    () -> stage.show());
        }
    }

    @FXML
    private void chooseFilePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(mMainView.getScene().getWindow());
        if (file != null) {
            this.path = file;
            mTfPath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void showAboutMessage() {
        showMessageDialog("屏幕截图软件v1.0.1",
                "作者：helloworldchao\n" +
                        "邮箱：helloworldchao@outlook.com", null);
    }

    private void showMessageDialog(String title, String content, OnDialogCloseListener l) {
        Dialog dialog = new Dialog();
        dialog.setTitle(title);
        dialog.setContentText(content);
        Window window = dialog.getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> {
            if (l == null) window.hide();
            else l.onDialogClose();
        });
        dialog.show();
    }
}
