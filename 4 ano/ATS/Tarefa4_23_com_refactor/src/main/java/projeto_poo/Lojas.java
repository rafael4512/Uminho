
import java.util.*;
import java.util.stream.Collectors;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Lojas implements Serializable {

  /* STARTING functions */

  private String codigo;
  private String password;
  private String nome;
  private Ponto gps;
  private int numeroEncomendas;
  private Map<String, Encomenda> registoEncomendas;

  public Lojas() {
    this.codigo = "";
    this.password = "";
    this.nome = "";
    this.gps = new Ponto();
    this.numeroEncomendas = 0;
    this.registoEncomendas = new HashMap<>();
  }

  public Lojas(String codigo, String password, String nome, Ponto gps, int numeroEncomendas, Map<String, Encomenda> registoEncomendas) {
    this.codigo = codigo;
    this.password = password;
    this.nome = nome;
    this.gps = gps;
    this.numeroEncomendas = numeroEncomendas;
    setRegistoEncomendas(registoEncomendas);
  }

  public Lojas(Lojas l) {
    this.codigo = l.getCodigo();
    this.password = l.getPW();
    this.nome = l.getNome();
    this.gps = l.getGPS();
    this.numeroEncomendas = l.getNumeroEncomendas();
    setRegistoEncomendas(l.getRegistoEncomendas());
  }


  /* GET functions */

  public String getCodigo() {
    return this.codigo;
  }

  public String getPW() {
    return this.password;
  }

  public String getNome() {
    return this.nome;
  }

  public Ponto getGPS() {
    return this.gps;
  }

  public int getNumeroEncomendas() {
    return this.numeroEncomendas;
  }

  public Map<String, Encomenda> getRegistoEncomendas() {
    return this.registoEncomendas;
  }


  /* SET functions */

  public void setCodigo(String newCodigo) {
    this.codigo = newCodigo;
  }

  public void setPW(String newPassword) {
    this.password = newPassword;
  }

  public void setNome(String newNome) {
    this.nome = newNome;
  }

  public void setGPS(Ponto newGPS) {
    this.gps = newGPS;
  }

  public void setNumeroEncomendas(int newNumeroEncomendas) {
    this.numeroEncomendas = newNumeroEncomendas;
  }

  public void setRegistoEncomendas(Map<String, Encomenda> newRegistoEncomendas) {
    this.registoEncomendas = newRegistoEncomendas;
  }


  /* OTHER functions */


  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;
    if (obj == null || obj.getClass() != this.getClass())
      return false;
    
    Lojas l = (Lojas) obj;

    return l.getCodigo().equals(this.codigo);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("Código: ").append(this.codigo).append("\n");
    sb.append("Nome: ").append(this.nome).append("\n");
    sb.append("gps: ").append(this.gps).append("\n");
    sb.append("Número de pessoas em fila: ").append(this.numeroEncomendas).append("\n");

    return sb.toString();
  }

  public String toStringCSV() {
    StringBuilder sb = new StringBuilder();

    sb.append("Loja:");
    sb.append(this.codigo).append(",");
    sb.append(this.nome).append(",");
    sb.append(this.gps.getX()).append(",");
    sb.append(this.gps.getY());

    return sb.toString();
  }

  
  /* REQUIRED functions */

  // Método que adiciona uma encomenda ao registo de encomendas da loja
  public void addEncomenda(Encomenda e) {
    this.registoEncomendas.put(e.getCodEncomenda(), e);
  }

  // Método que devolve o histórico de encomendas até à data
  public List<Encomenda> historicoL() {
    return this.registoEncomendas.values().stream().collect(Collectors.toList());
  }

  // Método que devolve o historico de encomendas entre datas
  public List<Encomenda> historicoLdata(LocalDateTime dtinicio, LocalDateTime dtfinal) {
    List<Encomenda> nEncomendas = new ArrayList<>();

    for (Encomenda e: this.registoEncomendas.values())
      if (e.getData().isAfter(dtinicio) && e.getData().isBefore(dtfinal))
        nEncomendas.add(e);

    return nEncomendas;
  }

  @Override
  public int hashCode() {
    return Objects.hash(codigo, password, nome, gps, numeroEncomendas, registoEncomendas);
  }
}
