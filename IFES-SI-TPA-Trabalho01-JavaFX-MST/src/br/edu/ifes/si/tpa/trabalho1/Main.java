package br.edu.ifes.si.tpa.trabalho1;

import br.edu.ifes.si.tpa.trabalho1.algoritmos.AlgoritmoMSTPrimLazyGrafoPonderado;
import br.edu.ifes.si.tpa.trabalho1.estruturas.Vertice;
import br.edu.ifes.si.tpa.trabalho1.estruturas.In;
import br.edu.ifes.si.tpa.trabalho1.estruturas.Aresta;
import br.edu.ifes.si.tpa.trabalho1.estruturas.GrafoPonderado;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    public static GrafoPonderado G;
    public static AlgoritmoMSTPrimLazyGrafoPonderado mst;
    Group componentes = new Group();
    Button button = new Button("Executar");

    public static void main(String[] args) {
        String arquivo = args[0].substring(9);

        In in = new In(arquivo);
        G = new GrafoPonderado(in);

        launch();
    }

    @Override
    public void start(Stage palco) throws Exception {

        //Adicionando a ação ao button
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mst = new AlgoritmoMSTPrimLazyGrafoPonderado(G);
                desenharMST(G, mst);
            }
        });

        componentes.getChildren().add(button); //Adicionando o button a tela principal

        desenharGrafo(G);

        Scene cena = new Scene(componentes, 500, 500);
        palco.setTitle("IFES - SI - TPA - Trabalho 01: Exemplo Árvore Geradora Mínima");
        palco.setScene(cena);
        palco.show();
    }

    public void desenharGrafo(GrafoPonderado G) {
        List<Vertice> vertices = G.vertices();

        //Desenhando as arestas
        for (Aresta a : G.arestas()) {
            Line line = new Line();
            line.setStartX(vertices.get(a.getV1()).getX());
            line.setStartY(vertices.get(a.getV1()).getY());
            line.setEndX(vertices.get(a.getV2()).getX());
            line.setEndY(vertices.get(a.getV2()).getY());
            line.setStroke(Color.BLACK);
            componentes.getChildren().add(line);
        }

        //Desenhando os vértices
        for (int v = 0; v < G.V(); v++) {
            Circle circle = new Circle();
            circle.setCenterX(vertices.get(v).getX());
            circle.setCenterY(vertices.get(v).getY());
            circle.setRadius(15.0f);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            Text text = new Text(circle.getCenterX() - 4, circle.getCenterY() + 4, String.valueOf(v));
            componentes.getChildren().add(circle);
            componentes.getChildren().add(text);
        }
    }

    public void desenharMST(GrafoPonderado G, AlgoritmoMSTPrimLazyGrafoPonderado mst) {
        List<Vertice> vertices = G.vertices();

        //Desenhando a árvore geradora mínima - MST
        for (Aresta a : mst.arestas()) {
            Line line = new Line();
            line.setStartX(vertices.get(a.getV1()).getX());
            line.setStartY(vertices.get(a.getV1()).getY());
            line.setEndX(vertices.get(a.getV2()).getX());
            line.setEndY(vertices.get(a.getV2()).getY());
            line.setStroke(Color.RED);
            line.setStrokeWidth(5);
            componentes.getChildren().add(line);
        }

        //Desenhando os vértices
        for (int v = 0; v < G.V(); v++) {
            Circle circle = new Circle();
            circle.setCenterX(vertices.get(v).getX());
            circle.setCenterY(vertices.get(v).getY());
            circle.setRadius(15.0f);
            circle.setStroke(Color.BLACK);
            circle.setFill(Color.WHITE);
            Text text = new Text(circle.getCenterX() - 4, circle.getCenterY() + 4, String.valueOf(v));
            componentes.getChildren().add(circle);
            componentes.getChildren().add(text);
        }
    }
}
