package br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.controller;

import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.algoritmo.AlgoritmoCicloEmGrafos;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.Aresta;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.GrafoPonderado;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.In;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.Vertice;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.utils.Utils;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLCicloEmGrafosController implements Initializable {

    @FXML
    private Canvas areaGrafo;
    @FXML
    private Slider sldProporcao;
    @FXML
    private Slider sldLayoutX;
    @FXML
    private Label labelCiclo;
    @FXML
    private TableView<Integer> tbCaminho;
    @FXML
    private TableColumn<String, String> tbColumnVertice;

    private Stage stage;

    private AlgoritmoCicloEmGrafos grafoInicial;

    private GrafoPonderado grafo;

    private Double xLayout = 0.0;

    private Double maiorX;

    private Double maiorY;

    private Double menorX;

    private Double menorY;

    private Color corLinha = Color.GREEN;

    private Color corColuna = Color.GREEN;

    private ObservableList<Integer> ciclo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sldProporcao.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            this.limpaArea();
            this.desenharGrafo(grafo, true);
        });
        sldLayoutX.valueProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
            xLayout = sldLayoutX.getValue();
            this.limpaArea();
            this.desenharGrafo(grafo, true);
        });

        tbCaminho.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) tbCaminho.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });

        configPropertyTab();
    }

    public void initData(Stage stage) {
        this.stage = stage;
    }

    private void configPropertyTab() {
        tbColumnVertice.setCellValueFactory(f -> new ReadOnlyObjectWrapper<>(f.getValue()));
        tbCaminho.setItems(ciclo);
    }

    @FXML
    private void verificarCiclo(ActionEvent event) {

        ciclo.clear();
        this.grafoInicial = new AlgoritmoCicloEmGrafos(grafo);
        System.out.println("Tem ciclo: " + this.grafoInicial.temCiclo());

        if (this.grafoInicial.temCiclo()) {
            int i = 1;
            for (Integer v : grafoInicial.ciclo()) {
                System.out.println("V" + i + ": " + v);
                i++;
                ciclo.add(v);
            }
        }
        tbCaminho.setItems(ciclo);

        if (grafoInicial.temCiclo()) {
            corLinha = Color.GREY;
            corColuna = Color.GREY;
            labelCiclo.setText("Esse Grafo tem CICLO!");
            limpaArea();
            desenharGrafo(grafo, true);
        } else {
            labelCiclo.setText("Esse Grafo não tem CICLO!");
        }
    }

    @FXML
    private void abrirArquivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Escolha o Arquivo .txt!");
        File f = fileChooser.showOpenDialog(stage);
        if (f != null) {
            In in = new In(f);
            GrafoPonderado G = new GrafoPonderado(in);
            this.grafo = G;
            this.maiorX = Utils.findMaiorX(G.vertices());
            this.menorX = Utils.findMenorX(G.vertices());
            this.maiorY = Utils.findMaiorY(G.vertices());
            this.menorY = Utils.findMenorY(G.vertices());

            corLinha = Color.GREY;
            corColuna = Color.GREY;
            limpaArea();
            desenharGrafo(G, false);
            centralizarDesenho();
            ciclo = FXCollections.observableArrayList();
        }
    }

    public void limpaArea() {
        GraphicsContext gc = areaGrafo.getGraphicsContext2D();
        gc.clearRect(0, 0, areaGrafo.getWidth(), areaGrafo.getHeight());
    }

    public void desenharGrafo(GrafoPonderado grafo, Boolean destacarCiclo) {
        GraphicsContext gc = areaGrafo.getGraphicsContext2D();

        Double tam = sldProporcao.getValue();
        double tamOval = tam * 100;
        double proporcao = tam;

        double somarX = areaGrafo.getLayoutX() + 10;
        double somarY = areaGrafo.getLayoutY() - 30;

        for (Aresta a : grafo.arestas()) {
            gc.setStroke(corLinha);

            gc.setLineWidth(tamOval * 0.1);

            Vertice v1 = Utils.findVertice(grafo.vertices(), a.getV1());
            Vertice v2 = Utils.findVertice(grafo.vertices(), a.getV2());

            if (destacarCiclo) {
                pintarArestaCiclo(v1, v2, gc);
            }

            double v1X = (int) ((((((maiorX - v1.getX()) * areaGrafo.getWidth()) / (maiorX - menorX)) * proporcao) - proporcao) + somarX);
            double v1Y = (int) ((((((maiorY - v1.getY()) * areaGrafo.getWidth()) / (maiorY - menorY)) * proporcao) - proporcao) + somarY);
            double v2X = (int) ((((((maiorX - v2.getX()) * areaGrafo.getWidth()) / (maiorX - menorX)) * proporcao) - proporcao) + somarX);
            double v2Y = (int) ((((((maiorY - v2.getY()) * areaGrafo.getWidth()) / (maiorY - menorY)) * proporcao) - proporcao) + somarY);

            gc.strokeLine(v1X + (tamOval / 2) + xLayout, v1Y + (tamOval / 2), v2X + (tamOval / 2) + xLayout, v2Y + (tamOval / 2));
        }

        for (Vertice v : grafo.vertices()) {
            double x = (int) ((((((maiorX - v.getX()) * areaGrafo.getWidth()) / (maiorX - menorX)) * proporcao) - proporcao) + somarX) + xLayout;
            double y = (int) ((((((maiorY - v.getY()) * areaGrafo.getWidth()) / (maiorY - menorY)) * proporcao) - proporcao) + somarY);

            gc.setStroke(corColuna);

            if (destacarCiclo && this.grafoInicial != null) {
                pintarVerticeCiclo(v, gc);
            }

            gc.setFill(Color.WHITE);
            gc.fillOval(x, y, tamOval, tamOval);

            gc.setLineWidth(tamOval * 0.1);
            gc.strokeOval(x, y, tamOval, tamOval);

            String id = String.valueOf(v.getId());
            gc.setLineWidth(tamOval / tamOval);
            gc.setStroke(Color.BLACK);
            double tamFont = tamOval * 0.5;
            double corX = x + (tamOval / 3);
            double corY = y + (tamOval / 3) * 2;
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Tahoma", FontWeight.BOLD, tamFont));
            gc.fillText(id, corX, corY);
        }
    }

    private void pintarVerticeCiclo(Vertice v, GraphicsContext gc) {

        if (grafoInicial != null && grafoInicial.temCiclo()) {
            for (Integer integer : grafoInicial.ciclo()) {
                if (v.getId() == integer) {
                    gc.setStroke(Color.GREEN);
                }
            }
        }
    }

    private void pintarArestaCiclo(Vertice v1, Vertice v2, GraphicsContext gc) {

        if (grafoInicial != null && grafoInicial.temCiclo()) {

            List<Integer> listinha = this.pegarCiclo();

            for (int i = 0; i < listinha.size(); i++) {
                Integer vc1 = listinha.get(i);
                Integer j = i + 1;
                if (j < listinha.size()) {
                    Integer vc2 = listinha.get(j);

                    if ((v1.getId() == vc1 || v1.getId() == vc2) && (v2.getId() == vc2 || v2.getId() == vc1)) {
                        gc.setStroke(Color.GREEN);
                    }
                }
            }
        }
    }

    private List<Integer> pegarCiclo() {

        List<Integer> listaConvertidaDoIterator = new ArrayList<>();

        if (this.grafoInicial != null) {
            this.grafoInicial.ciclo().iterator().forEachRemaining(n -> listaConvertidaDoIterator.add(n));

        }

        return listaConvertidaDoIterator;
    }

    public void centralizarDesenho() {
        sldLayoutX.setMax((areaGrafo.getWidth() - maiorX) - 40);
        sldLayoutX.setValue(((areaGrafo.getWidth() - maiorX) / 2) - 30);
    }

}
