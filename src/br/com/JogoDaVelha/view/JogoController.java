/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.JogoDaVelha.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author OCTI01
 */
public class JogoController implements Initializable {

    
    @FXML
    private AnchorPane apPrincipal;
    @FXML
    private AnchorPane apGame;

    @FXML
    private Button bt00;
    @FXML
    private Button bt01;
    @FXML
    private Button bt02;
    @FXML
    private Button bt10;
    @FXML
    private Button bt11;
    @FXML
    private Button bt12;
    @FXML
    private Button bt20;
    @FXML
    private Button bt21;
    @FXML
    private Button bt22;
    @FXML
    private ToggleButton btX;
    @FXML
    private ToggleButton btO;
    private Button[][] botoes;
    @FXML
    private Label lbMensagem;
    @FXML
    private GridPane gpBotoes;
    private Line line;
    private String textoPlayer;
    private String textoComputer;
    private FadeTransition desaparecer;
    private FadeTransition surgir;
    private Timeline jogar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botoes = new Button[3][3];
        botoes[0][0] = bt00;
        botoes[0][1] = bt01;
        botoes[0][2] = bt02;
        botoes[1][0] = bt10;
        botoes[1][1] = bt11;
        botoes[1][2] = bt12;
        botoes[2][0] = bt20;
        botoes[2][1] = bt21;
        botoes[2][2] = bt22;
        desaparecer = new FadeTransition(Duration.seconds(3), lbMensagem);
        desaparecer.setFromValue(1d);
        desaparecer.setToValue(0d);
        surgir = new FadeTransition(Duration.seconds(3), lbMensagem);
        surgir.setFromValue(0d);
        surgir.setToValue(1d);
        surgir.setOnFinished((ActionEvent event) -> {
            desaparecer.play();
        });
        disableAllBotoes(true);
        jogar = new Timeline(new KeyFrame(Duration.seconds(3), (ActionEvent aee) -> {
            jogar();
            if (validar(textoComputer)) {
                if (empate()) {
                    disableAllBotoes(true);
                    lbMensagem.setText("Deu Velha");
                    surgir.play();
                }
                btO.setDisable(false);
                btX.setDisable(false);
            }
        }));
        line = new Line();

    }

    @FXML
    private void btComecarActionEvent(ActionEvent actionEvent) {
        textoPlayer = btX.isSelected() ? btX.getText() : btO.getText();
        textoComputer = btO.isSelected() ? btX.getText() : btO.getText();
        disableAllBotoes(false);
        lbMensagem.setText("Então vamos começar...");
        surgir.play();
        btO.setDisable(true);
        btX.setDisable(true);
        apGame.getChildren().remove(line);
        jogar.stop();
        for (Button[] botoe : botoes) {
            for (Button button : botoe) {
                button.setText("");
                button.setOnAction((ActionEvent ae) -> {
                    if (button.getText().isEmpty()) {
                        button.setText(textoPlayer);
                        disableAllBotoes(true);
                        if (validar(textoPlayer)) {
                            if (empate()) {
                                disableAllBotoes(true);
                                lbMensagem.setText("Deu Velha");
                                surgir.play();
                            }
                            btO.setDisable(false);
                            btX.setDisable(false);
                        } else {
                            lbMensagem.setText("Aguarde o outro jogador...");
                            surgir.play();
                            jogar.play();
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Você não pode sobreescrever uma jogada").show();
                    }
                });
            }
        }
    }

    public boolean validar(String player) {
        if (botao(bt00, player) && botao(bt10, player) && botao(bt20, player)) {
            return ganhou(bt00, bt20);
        } else if (botao(bt01, player) && botao(bt11, player) && botao(bt21, player)) {
            return ganhou(bt01, bt21);
        } else if (botao(bt02, player) && botao(bt12, player) && botao(bt22, player)) {
            return ganhou(bt02, bt22);
        } else if (botao(bt00, player) && botao(bt01, player) && botao(bt02, player)) {
            return ganhou(bt00, bt02);
        } else if (botao(bt10, player) && botao(bt11, player) && botao(bt12, player)) {
            return ganhou(bt10, bt12);
        } else if (botao(bt20, player) && botao(bt21, player) && botao(bt22, player)) {
            return ganhou(bt20, bt22);
        } else if (botao(bt00, player) && botao(bt11, player) && botao(bt22, player)) {
            return ganhou(bt00, bt22);
        } else if (botao(bt02, player) && botao(bt11, player) && botao(bt20, player)) {
            return ganhou(bt02, bt20);
        }
        return empate();
    }

    public boolean ganhou(Button comeco, Button fim) {
        line.setLayoutX(comeco.getLayoutX() + (comeco.getWidth() / 2));
        line.setLayoutY(comeco.getLayoutY() + (comeco.getHeight() / 2));
        line.setEndX(fim.getLayoutX() - comeco.getLayoutX());
        line.setEndY(fim.getLayoutY() - comeco.getLayoutY());
        line.setDisable(true);
        apGame.getChildren().add(line);
        disableAllBotoes(true);
        lbMensagem.setText(comeco.getText().equals(textoComputer) ? "Você perdeu tente novamente" : "Parabéns você ganhou!");
        surgir.play();
        return true;
    }

    private boolean botao(Button button, String player) {
        return button.getText().equals(player);
    }

    public void disableAllBotoes(boolean disable) {
        for (Node node : gpBotoes.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(disable);
            }
        }
    }

    private void jogar() {
        if (vitoria()) {
            System.out.println("Vitoria e clara hahaha");
        } else if (bloqueio()) {
            System.out.println("Bloquiei o bobo haha");
        } else if (ia()) {
            System.out.println("Using Inteligence articial");
        }
    }

    private boolean jogadaMaquina(Button button) {
        if (podeJogar(button)) {
            button.setText(textoComputer);
            lbMensagem.setText("Sua vez");
            surgir.play();
            disableAllBotoes(false);
            return true;
        }
        return false;
    }

    private boolean podeJogar(Button button) {
        return button.getText().isEmpty();
    }

    private boolean botaoPlayer(Button button) {
        return button.getText().equals(textoPlayer);
    }

    private boolean botaoComputer(Button button) {
        return button.getText().equals(textoComputer);
    }

    private boolean bloqueio() {
        //Duas Primeiras casas horiozontal por linha
        if (botaoPlayer(bt00) && botaoPlayer(bt01)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        if (botaoPlayer(bt10) && botaoPlayer(bt11)) {
            if (podeJogar(bt12)) {
                return jogadaMaquina(bt12);
            }
        }
        if (botaoPlayer(bt20) && botaoPlayer(bt21)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
            //Meio horizontal
        }
        if (botaoPlayer(bt00) && botaoPlayer(bt02)) {
            if (podeJogar(bt01)) {
                return jogadaMaquina(bt01);
            }
        }
        if (botaoPlayer(bt10) && botaoPlayer(bt12)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoPlayer(bt20) && botaoPlayer(bt22)) {
            if (podeJogar(bt21)) {
                return jogadaMaquina(bt21);
            }
            //Comeco fechando
        }
        if (botaoPlayer(bt01) && botaoPlayer(bt02)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        if (botaoPlayer(bt11) && botaoPlayer(bt12)) {
            if (podeJogar(bt10)) {
                return jogadaMaquina(bt10);
            }
        }
        if (botaoPlayer(bt21) && botaoPlayer(bt22)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
            //Cima pra baixo por colunar ultima coluna
        }
        if (botaoPlayer(bt00) && botaoPlayer(bt10)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }
        if (botaoPlayer(bt01) && botaoPlayer(bt11)) {
            if (podeJogar(bt21)) {
                return jogadaMaquina(bt21);
            }
        }
        if (botaoPlayer(bt02) && botaoPlayer(bt12)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
            //Cima pra baixo por colunar meio coluna
        }
        if (botaoPlayer(bt00) && botaoPlayer(bt20)) {
            if (podeJogar(bt10)) {
                return jogadaMaquina(bt10);
            }
        }
        if (botaoPlayer(bt01) && botaoPlayer(bt21)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoPlayer(bt02) && botaoPlayer(bt22)) {
            if (podeJogar(bt12)) {
                return jogadaMaquina(bt12);
            }
            //Cima para baixo coluna primeira
        }
        if (botaoPlayer(bt10) && botaoPlayer(bt20)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        if (botaoPlayer(bt11) && botaoPlayer(bt21)) {
            if (podeJogar(bt01)) {
                return jogadaMaquina(bt01);
            }
        }
        if (botaoPlayer(bt12) && botaoPlayer(bt22)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
            //Diagonais
            //Principal
        }
        if (botaoPlayer(bt00) && botaoPlayer(bt11)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
        }
        if (botaoPlayer(bt00) && botaoPlayer(bt22)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoPlayer(bt11) && botaoPlayer(bt22)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
            //Inversa
        }
        if (botaoPlayer(bt02) && botaoPlayer(bt11)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }
        if (botaoPlayer(bt02) && botaoPlayer(bt20)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoPlayer(bt11) && botaoPlayer(bt20)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        return false;
    }

    private boolean vitoria() {
        if (botaoComputer(bt00) && botaoComputer(bt01)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        if (botaoComputer(bt10) && botaoComputer(bt11)) {
            if (podeJogar(bt12)) {
                return jogadaMaquina(bt12);
            }
        }
        if (botaoComputer(bt20) && botaoComputer(bt21)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
            //Meio horizontal
        }
        if (botaoComputer(bt00) && botaoComputer(bt02)) {
            if (podeJogar(bt01)) {
                return jogadaMaquina(bt01);
            }
        }
        if (botaoComputer(bt10) && botaoComputer(bt12)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoComputer(bt20) && botaoComputer(bt22)) {
            if (podeJogar(bt21)) {
                return jogadaMaquina(bt21);
            }
            //Comeco fechando
        }
        if (botaoComputer(bt01) && botaoComputer(bt02)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        if (botaoComputer(bt11) && botaoComputer(bt12)) {
            if (podeJogar(bt10)) {
                return jogadaMaquina(bt10);
            }
        }
        if (botaoComputer(bt21) && botaoComputer(bt22)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
            //Cima pra baixo por colunar ultima coluna
        }
        if (botaoComputer(bt00) && botaoComputer(bt10)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }
        if (botaoComputer(bt01) && botaoComputer(bt11)) {
            if (podeJogar(bt21)) {
                return jogadaMaquina(bt21);
            }
        }
        if (botaoComputer(bt02) && botaoComputer(bt12)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
            //Cima pra baixo por colunar meio coluna
        }
        if (botaoComputer(bt00) && botaoComputer(bt20)) {
            if (podeJogar(bt10)) {
                return jogadaMaquina(bt10);
            }
        }
        if (botaoComputer(bt01) && botaoComputer(bt21)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoComputer(bt02) && botaoComputer(bt22)) {
            if (podeJogar(bt12)) {
                return jogadaMaquina(bt12);
            }
            //Cima para baixo coluna primeira
        }
        if (botaoComputer(bt10) && botaoComputer(bt20)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        if (botaoComputer(bt11) && botaoComputer(bt21)) {
            if (podeJogar(bt01)) {
                return jogadaMaquina(bt01);
            }
        }
        if (botaoComputer(bt12) && botaoComputer(bt22)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
            //Diagonais
            //Principal
        }
        if (botaoComputer(bt00) && botaoComputer(bt11)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
        }
        if (botaoComputer(bt00) && botaoComputer(bt22)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoComputer(bt11) && botaoComputer(bt22)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
            //Inversa
        }
        if (botaoComputer(bt02) && botaoComputer(bt11)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }
        if (botaoComputer(bt02) && botaoComputer(bt20)) {
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoComputer(bt11) && botaoComputer(bt20)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        return false;
    }

    private boolean ia() {
        //Estrategias de vitoria
        //Superior
        if (botaoComputer(bt10) && botaoComputer(bt11) && podeJogar(bt00) && podeJogar(bt02)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }
        if (botaoComputer(bt10) && botaoComputer(bt11) && podeJogar(bt20) && podeJogar(bt22)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        //Esquerda
        if (botaoComputer(bt01) && botaoComputer(bt11) && podeJogar(bt00) && podeJogar(bt20)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        if (botaoComputer(bt01) && botaoComputer(bt11) && podeJogar(bt02) && podeJogar(bt22)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        //Direita
        if (botaoComputer(bt21) && botaoComputer(bt11) && podeJogar(bt00) && podeJogar(bt20)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
        }
        if (botaoComputer(bt21) && botaoComputer(bt11) && podeJogar(bt22) && podeJogar(bt02)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }

        //Inferior
        if (botaoComputer(bt12) && botaoComputer(bt11) && podeJogar(bt20) && podeJogar(bt00)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }
        }
        if (botaoComputer(bt12) && botaoComputer(bt11) && podeJogar(bt22) && podeJogar(bt20)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        //Estrategias contra vitórias
        if ((botaoPlayer(bt00) && botaoPlayer(bt22)) || (botaoPlayer(bt02) && botaoPlayer(bt20))) {
            System.out.println("    Duas pontas opostas selecionadas vamos pegar meio+lateral?");
            List<Integer> numerosInvocados = new ArrayList<>();
            int random = new Random().nextInt(4);
            numerosInvocados.add(random);
            for (int i = 0; i < 4; i++) {
                switch (random) {
                    case 0:
                        if (podeJogar(bt12)) {
                            return jogadaMaquina(bt12);
                        }
                    case 1:
                        if (podeJogar(bt10)) {
                            return jogadaMaquina(bt10);
                        }
                    case 2:
                        if (podeJogar(bt01)) {
                            return jogadaMaquina(bt01);
                        }
                    case 3:
                        if (podeJogar(bt21)) {
                            return jogadaMaquina(bt21);
                        }
                }
                do {
                    random = new Random().nextInt(4);
                } while (numerosInvocados.contains(random));
                numerosInvocados.add(random);
            }
        }
        if (botaoPlayer(bt00) || botaoPlayer(bt20) || botaoPlayer(bt02) || botaoPlayer(bt22)) {
            System.out.println("    As pontas? pega o meio");
            if (podeJogar(bt11)) {
                return jogadaMaquina(bt11);
            }
        }
        if (botaoPlayer(bt11)) {
            System.out.println("    O meio foi seu? cercando as pontas");
            List<Integer> numerosInvocados = new ArrayList<>();
            int random = new Random().nextInt(4);
            numerosInvocados.add(random);
            for (int i = 0; i < 4; i++) {
                switch (random) {
                    case 0:
                        if (podeJogar(bt00)) {
                            return jogadaMaquina(bt00);
                        }
                    case 1:
                        if (podeJogar(bt20)) {
                            return jogadaMaquina(bt20);
                        }
                    case 2:
                        if (podeJogar(bt02)) {
                            return jogadaMaquina(bt02);
                        }
                    case 3:
                        if (podeJogar(bt22)) {
                            return jogadaMaquina(bt22);
                        }
                }
                do {
                    random = new Random().nextInt(4);
                } while (numerosInvocados.contains(random));
                numerosInvocados.add(random);
            }
        }
        System.out.println("    Testando Bloqueio de duas no meio");
        if (botaoPlayer(bt21) && botaoPlayer(bt12)) {
            if (podeJogar(bt22)) {
                return jogadaMaquina(bt22);
            }

        }
        if (botaoPlayer(bt01) && botaoPlayer(bt10)) {
            if (podeJogar(bt00)) {
                return jogadaMaquina(bt00);
            }
        }
        if (botaoPlayer(bt10) && botaoPlayer(bt21)) {
            if (podeJogar(bt20)) {
                return jogadaMaquina(bt20);
            }
        }
        if (botaoPlayer(bt01) && botaoPlayer(bt12)) {
            if (podeJogar(bt02)) {
                return jogadaMaquina(bt02);
            }
        }
        //Random
        System.out.println("    Conferindo meio");
        if (podeJogar(bt11)) {
            return jogadaMaquina(bt11);
        }
        System.out.println("    Random");
        List<Integer> numerosInvocados = new ArrayList<>();
        int random = new Random().nextInt(4);
        numerosInvocados.add(random);
        for (int i = 0; i < 4; i++) {
            switch (random) {
                case 0:
                    if (podeJogar(bt01)) {
                        return jogadaMaquina(bt01);
                    }
                case 1:
                    if (podeJogar(bt10)) {
                        return jogadaMaquina(bt10);
                    }
                case 2:
                    if (podeJogar(bt12)) {
                        return jogadaMaquina(bt12);
                    }
                case 3:
                    if (podeJogar(bt21)) {
                        return jogadaMaquina(bt21);
                    }
            }
            do {
                random = new Random().nextInt(4);
            } while (numerosInvocados.contains(random));
            numerosInvocados.add(random);
        }
        return false;
    }

    private boolean empate() {
        for (Button[] botoe : botoes) {
            for (Button button : botoe) {
                if (button.getText().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }
}
