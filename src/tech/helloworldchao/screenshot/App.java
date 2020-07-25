package tech.helloworldchao.screenshot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tech.helloworldchao.screenshot.controller.MainController;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view/main_view.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("屏幕截图软件");
        primaryStage.setScene(new Scene(root, 265, 140));
        primaryStage.setResizable(false);

        MainController mainController = fxmlLoader.getController();
        mainController.setStage(primaryStage);

        primaryStage.show();
    }
}
