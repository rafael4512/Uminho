import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EncomendasAceites implements Serializable {
    private List<String> aceites;

    /** Construtores. */
    public EncomendasAceites(){
        this.aceites = new ArrayList<>();
    }

    public EncomendasAceites(List<String> encomendas){
        setAceites(encomendas);
    }

    public EncomendasAceites(EncomendasAceites ea){
        setAceites(ea.getAceites());
    }

    /** Getters. */
    public List<String> getAceites(){
        return this.aceites.stream().collect(Collectors.toList());
    }

    /** Setters. */
    public void setAceites(List<String> aceites){
        this.aceites = new ArrayList<>();
        this.aceites.addAll(aceites);
    }

    /** Clone. */
    public EncomendasAceites clone(){
      return new EncomendasAceites(this);
    }

    /** Equals. */
    public boolean equals(Object o){
      if (o == null || getClass() != o.getClass()) {
		return false;
	}
      if (this == o) {
		return true;
	}
      EncomendasAceites ea = (EncomendasAceites) o;
      return this.aceites.equals(ea.getAceites());
    }

    public String toString(){
      StringBuilder sb = new StringBuilder();
      sb.append("Códigos de encomendas aceites: " + "\n");
      this.aceites.forEach(s -> sb.append(s).append("\n"));
      sb.append("\n");

      return sb.toString();
    }

    /**
     * Método que adiciona uma nova encomenda aceite.
     * @param cod
     */
    public void updateAceites(String cod){
        this.aceites.add(cod);
    }
}
