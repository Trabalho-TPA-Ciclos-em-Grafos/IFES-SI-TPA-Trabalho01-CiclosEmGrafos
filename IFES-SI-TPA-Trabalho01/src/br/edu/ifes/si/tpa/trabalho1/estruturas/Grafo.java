package br.edu.ifes.si.tpa.trabalho1.estruturas;

/*******************************************************************************
 *  Compilação:        javac Grafo.java
 *  Execução:          java Grafo dados.txt
 *  Dependências:      Aresta.java
 *  Arquivos de dados: Grafo1.txt
 *  Link dos dados:    https://drive.google.com/open?id=0B3q56TwNCeXoMlQ1c1dGOXJRbG8
 *
 *  Um grafo de arestas, implementado utilizando listas de adjacências.
 *
 *  % java Grafo Grafo1.txt
 *  13 13
 *  0: 6  2  1  5  
 *  1: 0  
 *  2: 0  
 *  3: 5  4  
 *  4: 5  6  3  
 *  5: 3  4  0  
 *  6: 0  4  
 *  7: 8  
 *  8: 7  
 *  9: 11  10  12  
 *  10: 9  
 *  11: 9  12  
 *  12: 11  9
 *
 ******************************************************************************/


import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe implementa a representação do grafo com lista de adjacências.
 * Para documentação adicional, acesse:
 * <a href="http://algs4.cs.princeton.edu/41grafo/">Section 4.1</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class Grafo {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;         // número de vértices no grafo
    private int A;               // número de arestas no grafo
    private List<Aresta>[] adj;  // adj[v1] = lista de adjacência do vértice v1

    /**
     * Inicializa um dígrafo com V vertices e 0 arestas.
     * @param  V o número de vértices
     * @throws IllegalArgumentException se V < 0
     */
    public Grafo(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Número de vértices no grafo deve ser não negativo");
        }
        this.V = V;
        this.A = 0;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    /**  
     * Inicializa um grafo à partir de um arquivo de dados.
     * O formato é o número de vértices V e o número de arestas A
     * seguido por pares de pares de vértices.
     * @param  in o arquivo de entrada de dados
     * @throws IndexOutOfBoundsException se os pontos finais de qualquer borda estão fora da área prescrita
     * @throws IllegalArgumentException se o número de vértices ou arestas for negativo
     */
    public Grafo(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) {
            throw new IllegalArgumentException("Número de arestas deve ser não negativo");
        }
        for (int i = 0; i < E; i++) {
            int v1 = in.readInt();
            int v2 = in.readInt();
            double peso = 0;
            addAresta(new Aresta(v1, v2, peso));
        }
    }

    /**
     * Retorna o número de vértices do dígrafo.
     * @return o número de vértices do dígrafo
     */
    public int V() {
        return V;
    }

    /**
     * Retorna o número de arestas do dígrafo.
     * @return o número de arestas do dígrafo
     */
    public int A() {
        return A;
    }

    /**
     * Valida vértice do dígrafo.
     * @throws IndexOutOfBoundsException caso v não seja 0 <= v < V
     */
    private void validaVertice(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vértice " + v + " não está entre 0 e " + (V-1));
    }

    /**
     * Adiciona aresta direcionada a no dígrafo.
     * @param  a a aresta
     * @throws IndexOutOfBoundsException caso extremidades não estejam entre 0 e V-1
     */
    public void addAresta(Aresta a) {
        int v1 = a.umVertice();
        int v2 = a.outroVertice(v1);
        validaVertice(v1);
        validaVertice(v2);
        adj[v1].add(0, a);
        Aresta a2 = new Aresta(a.getV2(), a.getV1(), a.peso());
        adj[v2].add(0, a2);
        A++;
    }

    /**
     * Retorna as arestas incidentes no vértice v.
     * @param  v o vértice
     * @return as arestas incidentes no vértice v como um Iterable
     * @throws IndexOutOfBoundsException caso v não seja 0 <= v < V
     */
    public List<Aresta> adj(int v) {
        validaVertice(v);
        return adj[v];
    }

    /**
     * Retorna o grau do vértice v.
     * @param v o vértice
     * @return o grau do vértice v
     * @throws IndexOutOfBoundsException caso não seja 0 <= v < V
     */
    public int grau(int v) {
        validaVertice(v);
        return adj[v].size();
    }

    /**
     * Retorna todas as arestas neste grafo.
     * @return todas as arestas neste grafo, como um Iterable
     */
    public List<Aresta> arestas() {
        List<Aresta> lista = new ArrayList<Aresta>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Aresta a : adj(v)) {
                if (a.outroVertice(v) > v) {
                    lista.add(a);
                    lista.add(v, a);
                }
                else if (a.outroVertice(v) == v) {
                    if (selfLoops % 2 == 0) {
                        lista.add(a);
                    }
                    selfLoops++;
                }
            }
        }
        return lista;
    }

    /**
     * Retorna uma representação String deste grafo.
     * @return uma representação String deste grafo
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + A + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Aresta a : adj[v]) {
                s.append(a + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    /**
     * Testa a classe Grafo.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Grafo G = new Grafo(in);
        System.out.println(G);

    }

}
