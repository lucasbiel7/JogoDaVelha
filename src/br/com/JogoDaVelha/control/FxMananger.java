/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.JogoDaVelha.control;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author OCTI01
 */
public class FxMananger {

    public enum Tipo {
        MAXIMIZADO, RESIZABLE, FULLSCREEN, EXIT_ON_CLOSE;

        @Override
        public String toString() {
            return super.toString(); //To change body of generated methods, choose Tools | Templates.
        }

    }

    public static final String VIEW = "/br.com.JogoDaVelha.control/".replace(".", "/").replace("control", "view");

    public static Parent carregarComponente(String FXML) {
        try {
            return FXMLLoader.load(FxMananger.class.getResource(VIEW + FXML + ".fxml"));
        } catch (IOException ex) {
            System.out.println("Error ao carregar FXML");
            return null;
        }
    }

    public static Stage showWindow(Parent parent, String title, Tipo... tipos) {
        Stage stage = new Stage();
        stage.setTitle(title);
        for (Tipo tipo : tipos) {
            switch (tipo) {
                case MAXIMIZADO:
                    stage.setMaximized(true);
                    break;
                case RESIZABLE:
                    stage.setResizable(true);
                    break;
                case FULLSCREEN:
                    stage.setFullScreen(true);
                    break;
                case EXIT_ON_CLOSE:
                    stage.setOnCloseRequest((WindowEvent event) -> {
                        Platform.exit();
                        System.exit(0);
                    });
                    break;
            }
        }
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        return stage;
    }

    public static Stage showWindow(Parent parent, String title, Object data, Tipo... tipos) {
        Stage stage = showWindow(parent, title, tipos);
        parent.setUserData(data);
        return stage;
    }

    public static void inserirPainel(ScrollPane scrollPane, Parent parent) {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(parent);
    }
    
}
