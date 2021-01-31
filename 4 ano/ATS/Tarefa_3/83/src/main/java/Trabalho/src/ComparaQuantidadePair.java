import java.io.Serializable;
import java.util.Comparator;

public class ComparaQuantidadePair implements Comparator<Pair>, Serializable {
    /**
     * Método que compara dois pares no seu segundo parâmetro.
     *
     * @param v1 Primeiro argumento da comparação
     * @param v2 Segundo argumento da comparação
     * @return Devolve o que for mais pequeno lexicologicamente no segundo parametre ou no primeiro se os segundo forem iguais
     */
    public int compare(Pair v1, Pair v2) {
        if(v1.getSnd() == v2.getSnd()) {
			return v1.getFst().compareTo(v2.getFst());
		} else {
			return v2.getSnd() - v1.getSnd();
		}
    }
}