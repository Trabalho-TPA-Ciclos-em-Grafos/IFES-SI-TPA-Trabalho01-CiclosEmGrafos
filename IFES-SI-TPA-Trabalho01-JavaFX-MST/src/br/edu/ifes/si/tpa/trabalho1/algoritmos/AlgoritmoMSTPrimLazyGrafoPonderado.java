package br.edu.ifes.si.tpa.trabalho1.algoritmos;

import br.edu.ifes.si.tpa.trabalho1.estruturas.Fila;
import br.edu.ifes.si.tpa.trabalho1.estruturas.FilaPrioridadeMin;
import br.edu.ifes.si.tpa.trabalho1.estruturas.In;
import br.edu.ifes.si.tpa.trabalho1.estruturas.Aresta;
import br.edu.ifes.si.tpa.trabalho1.estruturas.UF;
import br.edu.ifes.si.tpa.trabalho1.estruturas.GrafoPonderado;

/******************************************************************************
 *  Compilação:         javac AlgoritmoMSTPrimLazyGrafoPonderado.java
 *  Execução:           java AlgoritmoMSTPrimLazyGrafoPonderado dados.txt
 *  Dependências:       GrafoPonderado.java Aresta.java Fila.java
 *                      FilaPrioridadeMinIndex.java UF.java In.java
 *  Arquivo de dados:   GrafoPonderado1.txt
 *  Link dos dados:     https://drive.google.com/open?id=0B3q56TwNCeXoenFyMnlzX2ZyXzg
 *
 *  Calcula a árvore geradora mínima (MTS) utilizando o algoritmo de Prim na versão preguiçosa (lazy).
 *
 *  %  java AlgoritmoMSTPrimGrafoPonderado GrafoPonderado1.txt
 *  7-1 0.19000
 *  0-2 0.26000
 *  2-3 0.17000
 *  5-4 0.35000
 *  7-5 0.28000
 *  2-6 0.40000
 *  0-7 0.16000
 *  1.81000
 ******************************************************************************/



/**
 * Esta classe implementa a geração da árvore geradora mínima utilizando o algoritmo de Prim na versão preguiçosa (lazy).
 * Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/43mst">Section 4.3</a>
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class AlgoritmoMSTPrimLazyGrafoPonderado {
    private static final double FLOATING_POINT_EPSILON = 1E-12;

    private double peso;                  // peso total da árvore geradora mínima (MST)
    private Fila<Aresta> mst;             // arestas na árvore geradora mínima (MST)
    private boolean[] marcado;            // marcado[v] = verdadeiro se o vértice v está na árvore
    private FilaPrioridadeMin<Aresta> pq; // arestas com um dos vértices na árvore

    /**
     * Calcula a árvore geradora mínima do grafo ponderado.
     * @param G o grafo ponderado
     */
    public AlgoritmoMSTPrimLazyGrafoPonderado(GrafoPonderado G) {
        mst = new Fila<Aresta>();
        pq = new FilaPrioridadeMin<Aresta>();
        marcado = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)     // executa Prim à partir de cada vértice para obter a árvore geradora mínima
            if (!marcado[v]) prim(G, v);    

        // checa condições de otimização
        assert checa(G);
    }

    /**
     * Método para rodar o algoritmo prim no grafo G, iniciado do vértice de origem vo
     * @param G o grafo
     * @param vo o vértice origem
     */
    private void prim(GrafoPonderado G, int vo) {
        escanea(G, vo);
        while (!pq.isEmpty()) {                               // melhor parar quando MST V-1 arestas
            Aresta a = pq.delMin();                           // menor aresta no pq
            int v1 = a.umVertice(), v2 = a.outroVertice(v1);  // dois pontos de extremidade
            assert marcado[v1] || marcado[v2];
            if (marcado[v1] && marcado[v2]) continue;         // preguiçoso, tanto v1 quanto v2 já escaneados
            mst.enfileira(a);                                 // adiciona aresta 'a' em MST
            peso += a.peso();
            if (!marcado[v1]) escanea(G, v1);                 // v1 torna-se parte da árvore
            if (!marcado[v2]) escanea(G, v2);                 // v2 torna-se parte da árvore
        }
    }

    /**
     * Adiciona todas as arestas incidentes para v em pq se o terminal outroVertice ainda não foi digitalizado
     * @param G o grafo ponderado
     * @param vo o vértice a ser escaneado
     */
    private void escanea(GrafoPonderado G, int v) {
        assert !marcado[v];
        marcado[v] = true;
        for (Aresta e : G.adj(v))
            if (!marcado[e.outroVertice(v)]) pq.insere(e);
    }
        
    /**
     * Retorna as arestas na árvore geradora mínima.
     * @return as arestas na árvore geradora mínima como iterable de arestas
     */
    public Iterable<Aresta> arestas() {
        return mst;
    }

    /**
     * Retorna a soma das arestas ponderadas na árvore geradora mínima.
     * @return a soma das arestas ponderadas na árvore geradora mínima.
     */
    public double peso() {
        return peso;
    }

    /**
     * Checa as condições de otimização 
     * @param G o grafo ponderado
     * @return verdadeiro se as condições forem satisfeitas, e falso, caso contrário
     */
    private boolean checa(GrafoPonderado G) {

        // checa peso
        double pesoTotal = 0.0;
        for (Aresta a : arestas()) {
            pesoTotal += a.peso();
        }
        if (Math.abs(pesoTotal - peso()) > FLOATING_POINT_EPSILON) {
            System.err.printf("Peso de arestas não é igual peso(): %f vs. %f\n", pesoTotal, peso());
            return false;
        }

        // checa que é acíclico
        UF uf = new UF(G.V());
        for (Aresta a : arestas()) {
            int v1 = a.umVertice(), v2 = a.outroVertice(v1);
            if (uf.conectado(v1, v2)) {
                System.err.println("Não é floresta");
                return false;
            }
            uf.junta(v1, v2);
        }

        // chega que é uma árvores geradora
        for (Aresta a : G.arestas()) {
            int v1 = a.umVertice(), v2 = a.outroVertice(v1);
            if (!uf.conectado(v1, v2)) {
                System.err.println("Não é uma árvore geradora mínima");
                return false;
            }
        }

        // checa que é uma árvore geradora mínima (cortar condições de otimização)
        for (Aresta a : arestas()) {

            // todas arestas na árvores geradora mínima (MST) exceto 'a'
            uf = new UF(G.V());
            for (Aresta f : mst) {
                int x = f.umVertice(), y = f.outroVertice(x);
                if (f != a) uf.junta(x, y);
            }

            // checa que é aresta de peso min em corte de cruzamento
            for (Aresta f : G.arestas()) {
                int x = f.umVertice(), y = f.outroVertice(x);
                if (!uf.conectado(x, y)) {
                    if (f.peso() < a.peso()) {
                        System.err.println("Aresta " + f + " viola as condições de corte de optimização");
                        return false;
                    }
                }
            }

        }

        return true;
    }
    
    
    /**
     * Testa a classe AlgoritmoMSTPrimLazyGrafoPonderado
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        GrafoPonderado G = new GrafoPonderado(in);
        AlgoritmoMSTPrimLazyGrafoPonderado mst = new AlgoritmoMSTPrimLazyGrafoPonderado(G);
        for (Aresta a : mst.arestas()) {
            System.out.println(a);
        }
        System.out.printf("%.5f\n", mst.peso());
    }

}
