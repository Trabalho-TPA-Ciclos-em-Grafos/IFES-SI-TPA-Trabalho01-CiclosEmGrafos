package br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.utils;

import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.Vertice;
import java.util.List;

public class Utils {

    /**
     * Retorna um vertice dado o seu id e uma lista de vertices
     *
     * @param vertices
     * @param id
     * @return
     */
    public static Vertice findVertice(List<Vertice> vertices, int id) {
        for (Vertice v : vertices) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    /**
     * Retorna a maior coordenada x dado um conjunto de Vertices
     *
     * @param vertices
     * @return
     */
    public static double findMaiorX(List<Vertice> vertices) {
        double maior = 0.0;
        for (Vertice v : vertices) {
            if (v.getX() > maior) {
                maior = v.getX();
            }
        }

        return maior;
    }

    /**
     * Retorna a maior coordenada y dado um conjunto de Vertices
     *
     * @param vertices
     * @return
     */
    public static double findMaiorY(List<Vertice> vertices) {
        double maior = 0.0;
        for (Vertice v : vertices) {
            if (v.getY() > maior) {
                maior = v.getY();
            }
        }

        return maior;
    }

    /**
     * Retorna a menor coordenada x dado um conjunto de Vertices
     *
     * @param vertices
     * @return
     */
    public static double findMenorX(List<Vertice> vertices) {
        double menor = vertices.get(0).getX();
        for (Vertice v : vertices) {
            if (v.getX() < menor) {
                menor = v.getX();
            }
        }

        return menor;
    }

    /**
     * Retorna a menor coordenada y dado um conjunto de Vertices
     *
     * @param vertices
     * @return
     */
    public static double findMenorY(List<Vertice> vertices) {
        double menor = vertices.get(0).getY();
        for (Vertice v : vertices) {
            if (v.getY() < menor) {
                menor = v.getY();
            }
        }

        return menor;
    }

}
