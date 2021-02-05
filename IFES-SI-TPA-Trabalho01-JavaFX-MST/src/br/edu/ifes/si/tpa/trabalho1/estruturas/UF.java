package br.edu.ifes.si.tpa.trabalho1.estruturas;

/******************************************************************************
 *  Compilation:  javac UF.java
 *  Execution:    java UF < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   http://algs4.cs.princeton.edu/15uf/tinyUF.txt
 *                http://algs4.cs.princeton.edu/15uf/mediumUF.txt
 *                http://algs4.cs.princeton.edu/15uf/largeUF.txt
 *
 *  Weighted quick-union by rank with path compression by halving.
 *
 *  % java UF < tinyUF.txt
 *  4 3
 *  3 8
 *  6 5
 *  9 4
 *  2 1
 *  5 0
 *  7 2
 *  6 1
 *  2 components
 *
 ******************************************************************************/




/**
 *  The <tt>UF</tt> class represents a <em>junta-encontra data type</em>
 *  (also known as the <em>disjoint-sets data type</em>).
 *  It supports the <em>junta</em> and <em>encontra</em> operations,
 *  along with a <em>conectado</em> operation for determining whether
 *  two sites are in the same component and a <em>quantidade</em> operation that
 *  returns the total number of components.
 *  <p>
  The junta-encontra data type models connectivity among a set of <em>N</em>
 *  sites, named 0 through <em>n</em> &ndash; 1.
 *  The <em>is-conectado-to</em> relation must be an 
 *  <em>equivalence relation</em>:
 *  <ul>
 *  <p><li> <em>Reflexive</em>: <em>p</em> is conectado to <em>p</em>.
 *  <p><li> <em>Symmetric</em>: If <em>p</em> is conectado to <em>q</em>,
 *          then <em>q</em> is conectado to <em>p</em>.
 *  <p><li> <em>Transitive</em>: If <em>p</em> is conectado to <em>q</em>
 *          and <em>q</em> is conectado to <em>r</em>, then
 *          <em>p</em> is conectado to <em>r</em>.
 *  </ul>
 *  <p>
 *  An equivalence relation partitions the sites into
 *  <em>equivalence classes</em> (or <em>components</em>). In this case,
  two sites are in the same component if and only if they are conectado.
  Both sites and components are identified with integers between 0 and
  <em>N</em> &ndash; 1. 
 *  Initially, there are <em>n</em> components, with each site in its
 *  own component.  The <em>component identifier</em> of a component
 *  (also known as the <em>root</em>, <em>canonical element</em>, <em>leader</em>,
 *  or <em>set representative</em>) is one of the sites in the component:
 *  two sites have the same component identifier if and only if they are
 *  in the same component.
 *  <ul>
 *  <p><li><em>junta</em>(<em>p</em>, <em>q</em>) adds a
 *         connection between the two sites <em>p</em> and <em>q</em>.
 *         If <em>p</em> and <em>q</em> are in different components,
         then it replaces
         these two components with a new component that is the junta of
         the two.
  <p><li><em>encontra</em>(<em>p</em>) returns the component
 *         identifier of the component containing <em>p</em>.
 *  <p><li><em>conectado</em>(<em>p</em>, <em>q</em>)
 *         returns true if both <em>p</em> and <em>q</em>
 *         are in the same component, and false otherwise.
 *  <p><li><em>quantidade</em>() returns the number of components.
 *  </ul>
 *  <p>
 *  The component identifier of a component can change
 *  only when the component itself changes during a call to
 *  <em>junta</em>&mdash;it cannot change during a call
 *  to <em>encontra</em>, <em>conectado</em>, or <em>quantidade</em>.
 *  <p>
  This implementation uses weighted quick junta by posto with path compression
  by halving.
  Initializing a data structure with <em>n</em> sites takes linear time.
 *  Afterwards, the <em>junta</em>, <em>encontra</em>, and <em>conectado</em> 
 *  operations take logarithmic time (in the worst case) and the
 *  <em>quantidade</em> operation takes constant time.
 *  Moreover, the amortized time per <em>junta</em>, <em>encontra</em>,
 *  and <em>conectado</em> operation has inverse Ackermann complexity.
 *  For alternate implementations of the same API, see
 *  {@link QuickUnionUF}, {@link QuickFindUF}, and {@link WeightedQuickUnionUF}.
 *
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class UF {

    private int[] pai;  // pai[i] = pai of i
    private byte[] posto;   // posto[i] = posto of subtree rooted at i (never more than 31)
    private int quantidade;     // number of components

    /**
     * Initializes an empty junta-encontra data structure with <tt>n</tt> sites
     * <tt>0</tt> through <tt>n-1</tt>. Each site is initially in its own 
     * component.
     *
     * @param  n the number of sites
     * @throws IllegalArgumentException if <tt>n &lt; 0</tt>
     */
    public UF(int n) {
        if (n < 0) throw new IllegalArgumentException();
        quantidade = n;
        pai = new int[n];
        posto = new byte[n];
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            posto[i] = 0;
        }
    }

    /**
     * Returns the component identifier for the component containing site <tt>p</tt>.
     *
     * @param  p the integer representing one site
     * @return the component identifier for the component containing site <tt>p</tt>
     * @throws IndexOutOfBoundsException unless <tt>0 &le; p &lt; N</tt>
     */
    public int encontra(int p) {
        valida(p);
        while (p != pai[p]) {
            pai[p] = pai[pai[p]];    // path compression by halving
            p = pai[p];
        }
        return p;
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between <tt>1</tt> and <tt>n</tt>)
     */
    public int quantidade() {
        return quantidade;
    }
  
    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @return <tt>true</tt> if the two sites <tt>p</tt> and <tt>q</tt> are in the same component;
     *         <tt>false</tt> otherwise
     * @throws IndexOutOfBoundsException unless
     *         both <tt>0 &le; p &lt; n</tt> and <tt>0 &le; q &lt; n</tt>
     */
    public boolean conectado(int p, int q) {
        return encontra(p) == encontra(q);
    }
  
    /**
     * Merges the component containing site <tt>p</tt> with the 
     * the component containing site <tt>q</tt>.
     *
     * @param  p the integer representing one site
     * @param  q the integer representing the other site
     * @throws IndexOutOfBoundsException unless
     *         both <tt>0 &le; p &lt; n</tt> and <tt>0 &le; q &lt; n</tt>
     */
    public void junta(int p, int q) {
        int rootP = encontra(p);
        int rootQ = encontra(q);
        if (rootP == rootQ) return;

        // make root of smaller posto point to root of larger posto
        if      (posto[rootP] < posto[rootQ]) pai[rootP] = rootQ;
        else if (posto[rootP] > posto[rootQ]) pai[rootQ] = rootP;
        else {
            pai[rootQ] = rootP;
            posto[rootP]++;
        }
        quantidade--;
    }

    // valida that p is a valid index
    private void valida(int p) {
        int n = pai.length;
        if (p < 0 || p >= n) {
            throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n-1));  
        }
    }

    /**
     * Reads in a an integer <tt>n</tt> and a sequence of pairs of integers
     * (between <tt>0</tt> and <tt>n-1</tt>) from standard input, where each integer
     * in the pair represents some site;
     * if the sites are in different components, merge the two components
     * and print the pair to standard output.
     */
    public static void main(String[] args) {
        int n = StdIn.readInt();
        UF uf = new UF(n);
        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.conectado(p, q)) continue;
            uf.junta(p, q);
            System.out.println(p + " " + q);
        }
        System.out.println(uf.quantidade() + " components");
    }
}
