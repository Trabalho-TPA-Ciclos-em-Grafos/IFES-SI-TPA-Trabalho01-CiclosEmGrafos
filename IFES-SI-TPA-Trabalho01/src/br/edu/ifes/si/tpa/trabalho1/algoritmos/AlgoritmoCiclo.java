package br.edu.ifes.si.tpa.trabalho1.algoritmos;

import br.edu.ifes.si.tpa.trabalho1.estruturas.In;
import br.edu.ifes.si.tpa.trabalho1.estruturas.Grafo;
import br.edu.ifes.si.tpa.trabalho1.estruturas.Aresta;
import br.edu.ifes.si.tpa.trabalho1.estruturas.Pilha;

/******************************************************************************
 *  Compilation:  javac AlgoritmoCiclo.java
 *  Execution:    java  AlgoritmoCiclo dados.txt
 *  Dependencies: Grafo.java Aresta.java Pilha.java
 *  Identifica um ciclo.
 ******************************************************************************/

public class AlgoritmoCiclo {
    private boolean[] marcado;
    private int[] arestaPara;
    private Pilha<Integer> ciclo;

    /**
     * Determina se o grafo não direcionado G tem um ciclo e, se assim for, encontra um tal ciclo.
     * @param G o grafo não direcionado
     */
    public AlgoritmoCiclo(Grafo G) {
        if (temAutoLoop(G)) return;
        if (temArestasParalelas(G)) return;
        marcado = new boolean[G.V()];
        arestaPara = new int[G.V()];
        for (int v = 0; v < G.V(); v++)
            if (!marcado[v])
                dfs(G, -1, v);
    }

    // este grafo tem um auto loop?
    private boolean temAutoLoop(Grafo G) {
        for (int v = 0; v < G.V(); v++) {
            for (Aresta a : G.adj(v)) {
                int w = a.getV2();
                if (v == w) {
                    ciclo = new Pilha<Integer>();
                    ciclo.empilha(v);
                    ciclo.empilha(v);
                    return true;
                }
            }
        }
        return false;
    }

    // este grafo tem duas arestas paralelas?
    private boolean temArestasParalelas(Grafo G) {
        marcado = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {

            // verifica se há aretas paralelas incidentes no vértice v
            for (Aresta a : G.adj(v)) {
                int w = a.getV2();
                if (marcado[w]) {
                    ciclo = new Pilha<Integer>();
                    ciclo.empilha(v);
                    ciclo.empilha(w);
                    ciclo.empilha(v);
                    return true;
                }
                marcado[w] = true;
            }

            // reinicia marcado[v] = false para todos arestas incidentes em v
            for (Aresta a : G.adj(v)) {
                int w = a.getV2();
                marcado[w] = false;
            }
        }
        return false;
    }

    /**
     * Retorna true se o grafo G tem um ciclo.
     * @return true se o grafo tem um ciclo; false, caso contrário
     */
    public boolean temCiclo() {
        return ciclo != null;
    }

     /**
     * Retorna um ciclo no grafo G.
     * @return um ciclo se o grafo G tem um ciclo, e null, caso contrário
     */
    public Iterable<Integer> ciclo() {
        return ciclo;
    }

    private void dfs(Grafo G, int u, int v) {
        marcado[v] = true;
        for (Aresta a : G.adj(v)) {
            int w = a.getV2();
            // finaliza a chamada recursiva se o ciclo tiver sido encontrado
            if (ciclo != null) return;

            if (!marcado[w]) {
                arestaPara[w] = v;
                dfs(G, v, w);
            }

            // verifica a existência de ciclo (mas desconsidera a aresta invertida para v)
            else if (w != u) {
                ciclo = new Pilha<Integer>();
                for (int x = v; x != w; x = arestaPara[x]) {
                    ciclo.empilha(x);
                }
                ciclo.empilha(w);
                ciclo.empilha(v);
            }
        }
    }

    /**
     * Testa a classe AlgoritmoCiclo.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Grafo G = new Grafo(in);
        AlgoritmoCiclo finder = new AlgoritmoCiclo(G);
        if (finder.temCiclo()) {
            for (int v : finder.ciclo()) {
                System.out.println(v + " ");
            }
            System.out.println();
        }
        else {
            System.out.println("Grafo é acíclico");
        }
    }


}

