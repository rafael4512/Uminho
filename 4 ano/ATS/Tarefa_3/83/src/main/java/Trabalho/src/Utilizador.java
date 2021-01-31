import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Utilizador extends UtilizadorSistema implements Serializable {
      private List<Encomenda> encomendas_realizadas;

    /** Construtores. */
      public Utilizador(){
          this.encomendas_realizadas = new ArrayList<>();
      }

      public Utilizador(String email, String password, String codigo, String nome, double latitude, double longitude, ArrayList<Encomenda> encomendas_realizadas){
          super(email,password,"Utilizador", codigo, nome, latitude, longitude);
          setEncomendas(encomendas_realizadas);
      }

      public Utilizador(Utilizador user){
          super(user);
          setEncomendas(user.getEncomendas());
      }

      /** Getters. */
      public String getCodigo(){
        return super.getCodigo();
      }

      public String getNome(){
         return  super.getNome();
      }

      public double getLatitude(){
          return super.getLatitude();
      }

      public double getLongitude(){
          return super.getLongitude();
      }

      public ArrayList<Encomenda> getEncomendas(){
          ArrayList<Encomenda> res = new ArrayList<>();
          for(Encomenda e: this.encomendas_realizadas) {
			res.add(e.clone());
		}
          return res;
      }

    public void setEncomendas(ArrayList<Encomenda> enc){
          this.encomendas_realizadas = new ArrayList<>();
          for(Encomenda e: enc) {
			this.encomendas_realizadas.add(e.clone());
		}
      }

    /** Clone. */
      public Utilizador clone(){
        return new Utilizador(this);
      }

      /** Equals. */
      public boolean equals(Object o){
          return super.equals(o);
      }

      public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append(super.toString());
        sb.append("Código: ").append(getCodigo()).append("\n");
        sb.append("Nome: ").append(getNome()).append("\n");
        sb.append("Latitude: ").append(getLatitude()).append("\n");
        sb.append("Longitude: ").append(getLongitude()).append("\n");

        return sb.toString();
      }

    /**
     * Método que imprime as encomendas do utilizador.
     * @return
     */
      public String printEncomendasRecebidas(){
          StringBuilder sb = new StringBuilder();
          if(!this.encomendas_realizadas.stream().anyMatch(e -> e.isEntregue())) {
			sb.append("Não existem encomendas recebidas\n");
		} else {
              System.out.println("ENCOMENDAS REALIZADAS PELO USER: ");
              this.encomendas_realizadas.stream().filter(e -> e.isEntregue()).forEach(e -> sb.append(e));
          }
          return sb.toString();
      }

    public String printEncomendasPorEntregar(){
        StringBuilder sb = new StringBuilder();
        if(this.encomendas_realizadas.stream().allMatch(e -> e.isEntregue())) {
			sb.append("Não existem encomendas por entregar\n");
		} else {
            System.out.println("ENCOMENDAS REALIZADAS PELO USER: ");
            this.encomendas_realizadas.stream().filter(e -> !e.isEntregue()).forEach(e -> sb.append(e));
        }
        return sb.toString();
    }

    /**
     * Método que adiciona uma encoemenda.
     * @param e
     */
      public void addEncomenda(Encomenda e){
          this.encomendas_realizadas.add(e.clone());
      }

    public void updateEncomendaLoja(Encomenda enc){
        ArrayList<Encomenda> aux = new ArrayList<>();
        enc.setLevantada(true);
        aux.add(enc);
        for(Encomenda e: this.encomendas_realizadas){
            if(!e.getCodigo().equals(enc.getCodigo())){
                aux.add(e);
            }
        }
        setEncomendas(aux);
    }

    public void updateEncomendaPronta(Encomenda enc){
        ArrayList<Encomenda> aux = new ArrayList<>();
        enc.setPreparada(true);
        aux.add(enc);
        for(Encomenda e: this.encomendas_realizadas){
            if(!e.getCodigo().equals(enc.getCodigo())){
                aux.add(e);
            }
        }
        setEncomendas(aux);
    }

    /**
     * Método que atualiza uma encomenda.
     * @param enc
     */
    public void updateEncomenda(Encomenda enc){
        ArrayList<Encomenda> aux = new ArrayList<>();
        enc.setEntregue(true);
        aux.add(enc);
        for(Encomenda e: this.encomendas_realizadas){
            if(!e.getCodigo().equals(enc.getCodigo())){
                aux.add(e);
            }
        }
        setEncomendas(aux);
    }

    /**
     * Método que devolve uma encomenda com o código cod.
     * @param cod
     * @return
     */
    public Encomenda devolveEncomenda(String cod) throws EncomendaNotFoundException{
        for(Encomenda e: this.encomendas_realizadas){
            if(e.getCodigo().equals(cod)) {
				return e.clone();
			}
        }
        throw new EncomendaNotFoundException();
    }
}
