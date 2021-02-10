package br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas;

public class Vertice {

    private int id;
    private double x;
    private double y;

    public Vertice(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
