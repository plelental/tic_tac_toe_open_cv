package TicTacToe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

public class TicTacToe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainStage.fxml"));
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(new Scene(fxmlLoader.load(), 600, 300));
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Controller controller = fxmlLoader.getController();
            if (controller.isRunning()) {
                controller.turnOff();
                controller.initializeCamera(controller);
            }
            System.exit(0);
        });

    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        launch(args);
    }
}
