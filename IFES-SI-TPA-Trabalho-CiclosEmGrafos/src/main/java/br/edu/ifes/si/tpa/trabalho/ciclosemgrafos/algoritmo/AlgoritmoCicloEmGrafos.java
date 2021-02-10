package br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.algoritmo;

import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.GrafoPonderado;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.Aresta;
import br.edu.ifes.si.tpa.trabalho.ciclosemgrafos.estruturas.Pilha;

/**
 * ****************************************************************************
 * Compilation: javac AlgoritmoCiclo.java Execution: java AlgoritmoCiclo
 * dados.txt Dependencies: Grafo.java Aresta.java Pilha.java Identifica um
 * ciclo.
 *****************************************************************************
 */
public class AlgoritmoCicloEmGrafos {

    private boolean[] marcado;
    private int[] arestaPara;
    private Pilha<Integer> ciclo;

    /**
     * Determina se o grafo não direcionado G tem um ciclo e, se assim for,
     * encontra um tal ciclo.
     *
     * @param G o grafo não direcionado
     */
    public AlgoritmoCicloEmGrafos(GrafoPonderado G) {
        if (temAutoLoop(G)) {
            return;
        }
        if (temArestasParalelas(G)) {
            return;
        }
        marcado = new boolean[G.V()];
        arestaPara = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marcado[v]) {
                dfs(G, -1, v);
            }
        }
    }

    // este grafo tem um auto loop?
    private boolean temAutoLoop(GrafoPonderado G) {
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
    private boolean temArestasParalelas(GrafoPonderado G) {
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
     *
     * @return true se o grafo tem um ciclo; false, caso contrário
     */
    public boolean temCiclo() {
        return ciclo != null;
    }

    /**
     * Retorna um ciclo no grafo G.
     *
     * @return um ciclo se o grafo G tem um ciclo, e null, caso contrário
     */
    public Iterable<Integer> ciclo() {
        return ciclo;
    }

    private void dfs(GrafoPonderado G, int u, int v) {
        marcado[v] = true;
        for (Aresta a : G.adj(v)) {
            int w = a.getV2();
            // finaliza a chamada recursiva se o ciclo tiver sido encontrado
            if (ciclo != null) {
                return;
            }

            if (!marcado[w]) {
                arestaPara[w] = v;
                dfs(G, v, w);
            } // verifica a existência de ciclo (mas desconsidera a aresta invertida para v)
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
}
