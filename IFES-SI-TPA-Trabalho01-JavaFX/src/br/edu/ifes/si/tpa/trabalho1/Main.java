package br.edu.ifes.si.tpa.trabalho1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage palco) throws Exception {
        //Vértice 1
        Circle circle1 = new Circle();
        circle1.setCenterX(100.0f);
        circle1.setCenterY(100.0f);
        circle1.setRadius(15.0f);
        circle1.setStroke(Color.BLACK);
        circle1.setFill(Color.WHITE);
        Text text1 = new Text(circle1.getCenterX()-4, circle1.getCenterY()+4, "1");

        //Vértice 2
        Circle circle2 = new Circle(15);
        circle2.setCenterX(200.0f);
        circle2.setCenterY(100.0f);
        circle2.setStroke(Color.BLACK);
        circle2.setFill(Color.WHITE);
        Text text2 = new Text(circle2.getCenterX()-4, circle2.getCenterY()+4, "2");
        
        //Vértice 3
        Circle circle3 = new Circle(15);
        circle3.setCenterX(150.0f);
        circle3.setCenterY(50.0f);
        circle3.setStroke(Color.BLACK);
        circle3.setFill(Color.WHITE);
        Text text3 = new Text(circle3.getCenterX()-4, circle3.getCenterY()+4, "3");

        //Linha entre Vértice 1 e Vértice 2
        Line line1 = new Line();
        line1.setStartX(circle1.getCenterX());
        line1.setStartY(circle1.getCenterY());
        line1.setEndX(circle2.getCenterX());
        line1.setEndY(circle2.getCenterY());
        
        //Linha entre Vértice 2 e Vértice 3
        Line line2 = new Line();
        line2.setStartX(circle2.getCenterX());
        line2.setStartY(circle2.getCenterY());
        line2.setEndX(circle3.getCenterX());
        line2.setEndY(circle3.getCenterY());
        
        //Linha entre Vértice 3 e Vértice 1
        Line line3 = new Line();
        line3.setStartX(circle3.getCenterX());
        line3.setStartY(circle3.getCenterY());
        line3.setEndX(circle1.getCenterX());
        line3.setEndY(circle1.getCenterY());
        line3.setStroke(Color.RED);
        
        Group componentes = new Group();
        componentes.getChildren().addAll(line1, line2, line3, circle1, circle2, circle3, text1, text2, text3);

        Scene cena = new Scene(componentes, 500, 500);
        palco.setTitle("IFES - SI - TPA - Trabalho 01: Algoritmos em Grafos");
        palco.setScene(cena);
        palco.show();
    }
}
