import java.io.Serializable;
import java.util.Objects;

public class Pair implements Serializable {
    private String fst;
    private int snd;

    /** Construtores para objetos da classe Pair. */
    public Pair(){
        this.fst =  "";
        this.snd =  0;
    }

    public Pair(String fst, int snd){
        this.fst = fst;
        this.snd = snd;
    }

    public Pair(Pair a){
        this.fst = a.getFst();
        this.snd = a.getSnd();
    }

    public String getFst(){
        return fst;
    }

    public void setFst(String fst){
        this.fst = fst;
    }

    public int getSnd(){
        return snd;
    }

    public void setSecond(int snd){
        this.snd = snd;
    }

    /**
     * Implementação do método de igualdade entre dois Pair.
     *
     * @param o Pair que é comparado com o recetor
     * @return boolean true ou false
     */
    public boolean equals(Object o){
        if(o instanceof Pair){
            Pair p = (Pair) o;
            return Objects.equals(this.fst, p.fst) && Objects.equals(snd, p.snd);
        }
        return false;
    }

    /**
     * Implementação do método toString para a classe Pair.
     *
     * @return String com a informação textoal do objeto Pair
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(getFst()).append(" ---> ").append(getSnd());

        return sb.toString();
    }

    /**
     * Implementação do método de clonagem de um Pair.
     *
     * @return Objeto do tipo Pair
     */
    public Pair clone(){
        return new Pair(this);
    }
}
