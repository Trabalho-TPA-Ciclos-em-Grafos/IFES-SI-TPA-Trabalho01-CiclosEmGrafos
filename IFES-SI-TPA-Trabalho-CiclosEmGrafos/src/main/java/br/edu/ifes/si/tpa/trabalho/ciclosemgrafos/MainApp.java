package br.edu.ifes.si.tpa.trabalho.ciclosemgrafos;

import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.controller.FXMLCicloEmGrafosController;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/FXMLCicloEmGrafos.fxml"));
        Parent root = (Parent) fxmlLoader.load();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setTitle("Ciclos em Grafos");

        FXMLCicloEmGrafosController controller = fxmlLoader.<FXMLCicloEmGrafosController>getController();
        controller.initData(stage);

        stage.setOnCloseRequest((WindowEvent t) -> {
            t.consume();
            stage.close();
            Platform.exit();
            System.exit(0);
        });

        stage.show();
    }
}
