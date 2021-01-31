import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Voluntario extends UtilizadorSistema implements Serializable {
    private boolean disponivel;
    private boolean transporteMedico;
    private int velocidade;
    private int minutosDeEspera;
    private LocalDate horaDeRegisto;
    private double raio_acao;
    private double classificacao;
    private int avaliacoes;
    private List<Encomenda> historico;

    /** Construtores de classe Construtor de classe por omissão. */
    public Voluntario(){
        this.disponivel = false;
        this.horaDeRegisto = LocalDate.now();
        this.raio_acao = 0;
        this.classificacao = 0;
        this.avaliacoes = 0;
        this.historico = new ArrayList<>();
        this.transporteMedico = false;
        this.velocidade = 0;
        this.minutosDeEspera = 0;
    }
    /** Construtor de classe por clone. */
    public Voluntario(Voluntario a){
        super(a);
        this.disponivel = a.getDisponibilidade();
        this.horaDeRegisto = a.getInicio_transporte();
        this.raio_acao = a.getRaio_acao();
        this.classificacao = a.getClassificacao();
        this.avaliacoes = a.getAvaliacoes();
        setHistorico(a.getHistorico());
        this.transporteMedico = a.aceitoTransporteMedicamentos();
        this.minutosDeEspera = a.getMinutosDeEspera();
        this.velocidade = a.getVelocidade();
    }

    /** Construtor parametrizado. */
    public Voluntario(String email, String password, String a, String b, boolean c, double d, double e, LocalDate f, double g, List<Encomenda> h, double classificacao,
                      int avaliacoes, boolean transporteMedico, int velocidade, int minutosDeEspera){
        super(email, password, "Voluntario", b, a, d, e);
        this.disponivel = c;
        this.horaDeRegisto = f;
        this.raio_acao = g;
        this.classificacao = classificacao;
        this.avaliacoes = avaliacoes;
        setHistorico(h);
        this.transporteMedico = transporteMedico;
        this.velocidade = velocidade;
        this.minutosDeEspera = minutosDeEspera;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public int getMinutosDeEspera() {
        return minutosDeEspera;
    }

    public boolean aceitoTransporteMedicamentos(){
        return this.transporteMedico;
    }

    public void aceitaMedicamentos(boolean state){
        this.transporteMedico = state;
    }

    /** Métodos de obtenção de variáveis. */
    public String getNome(){
      return super.getNome();
    }

    public double getClassificacao() {
        return classificacao;
    }
    public String getCodigo(){
      return super.getCodigo();
    }

    public boolean getDisponibilidade(){
        return this.disponivel;
    }

    public double getLatitude(){
        return super.getLatitude();
    }

    public double getLongitude(){
        return super.getLongitude();
    }

    public double getRaio_acao(){
        return this.raio_acao;
    }

    public LocalDate getInicio_transporte(){
        return this.horaDeRegisto;
    }

    public int getAvaliacoes() {
        return this.avaliacoes;
    }

    public List<Encomenda> getHistorico(){
        List<Encomenda> res = new ArrayList<>();
        for(Encomenda s: this.historico) {
			res.add(s.clone());
		}
        return res;
    }

    public void setMinutosDeEspera(int minutosDeEspera) {
        this.minutosDeEspera = minutosDeEspera;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public void setAvaliacoes(int avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public void setClassificacao(double classificacao) {
        this.classificacao = classificacao;
    }

    public void setDisponibilidade(boolean a){
        this.disponivel = a;
    }

    public void setInicio_Transporte(LocalDate a){
        this.horaDeRegisto = a;
    }

    public void setRaio_acao(double a){
        this.raio_acao = a;
    }

    public void setHistorico(List<Encomenda> a){
        this.historico = new ArrayList<>();
        for(Encomenda s: a) {
			this.historico.add(s.clone());
		}
    }

    /** Método de clonar um objeto. */
    public Voluntario clone(){
        return new Voluntario(this);
    }

    /** Public equals. */
    public boolean equals(Object o){
        if (o == this) {
			return true;
		}
        if (o == null || getClass() != o.getClass()) {
			return false;
		}
        Voluntario v = (Voluntario) o;
        return super.equals(o)
        && this.disponivel ==  v.getDisponibilidade()
        && this.horaDeRegisto.equals(v.getInicio_transporte())
        && this.raio_acao == v.getRaio_acao()
        && this.historico.equals(v.getHistorico());
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Nome: ");
        sb.append(getNome()).append("\n");
        sb.append("Código de voluntário: ");
        sb.append(getCodigo()).append("\n");
        sb.append("Disponível: ");
        sb.append(this.disponivel).append("\n");
        sb.append("Latitude: ");
        sb.append(getLatitude()).append("\n");
        sb.append("Longitude: ");
        sb.append(getLongitude()).append("\n");
        sb.append("Hora de registo: ");
        sb.append(this.horaDeRegisto).append("\n");
        sb.append("Raio de ação: ");
        sb.append(this.raio_acao).append("\n");
        sb.append("Registos de encomendas: ");
        sb.append(this.historico);

        return sb.toString();
    }

    /**
     * Método que atualiza a classificação.
     * @param classificacao
     */
    public void updateRate(Double classificacao){
        double total = this.classificacao * this.avaliacoes + classificacao;
        this.avaliacoes++;
        this.classificacao = total / this.avaliacoes;
    }

    /**
     * Método que adiciona uma encomenda.
     * @param e
     */
    public void addEncomenda (Encomenda e){
        this.historico.add(e.clone());
    }

    /**
     * Método que remove uma encomenda.
     * @param cod
     * @return
     */
    public Encomenda removeEncomenda(String cod){
        for(Encomenda s: this.historico){
            if(cod.equals(s.getCodigo())){
                this.historico.remove(s);
                return s;
            }
        }
        return null;
    }

    /**
     * Método que devolve a encomenda com o código cod.
     * @param cod
     * @return
     */
    public Encomenda getEncomenda(String cod) throws EncomendaNotFoundException{
        for(Encomenda s: this.historico){
            if(cod.equals(s.getCodigo())) {
				return s;
			}
        }
        throw  new EncomendaNotFoundException();
    }

    /**
     * Método que define uma encomenda como entregue.
     * @param enc
     */
    public void updateEncomenda(Encomenda enc){
       List<Encomenda> aux = new ArrayList<>();
       enc.setEntregue(true);
       aux.add(enc);
       for(Encomenda e: this.historico){
           if(!e.getCodigo().equals(enc.getCodigo())){
               aux.add(e);
           }
       }
       setHistorico(aux);
    }

    /**
     * Método que define uma encomenda como levantada de uma loja.
     * @param enc
     */
    public void updateEncomendaLoja(Encomenda enc){
        List<Encomenda> aux = new ArrayList<>();
        enc.setLevantada(true);
        aux.add(enc);
        for(Encomenda e: this.historico){
            if(!e.getCodigo().equals(enc.getCodigo())){
                aux.add(e);
            }
        }
        setHistorico(aux);
    }

    /**
     * Método que define uma encomenda como estando preparada.
     * @param enc
     */
    public void updateEncomendaPreparada(Encomenda enc){
        List<Encomenda> aux = new ArrayList<>();
        enc.setPreparada(true);
        aux.add(enc);
        for(Encomenda e: this.historico){
            if(!e.getCodigo().equals(enc.getCodigo())){
                aux.add(e);
            }
        }
        setHistorico(aux);
    }

    /**
     * Método que devolve as encomendas ainda não entregues, mas já levantas da loja.
     * @return
     */
    public String getNaoEntregue(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Encomenda s: this.historico){
            if(!s.isEntregue() && s.isLevantada()){
                sb.append(s);
                count++;
            }
        }
        if(count == 0) {
			sb.append("0");
		}
        return sb.toString();
    }

    /**
     * Método que devolve as encomendas prepradas e prontas a serem levantas.
     * @return
     */
    public String getPreparadas(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Encomenda s: this.historico){
            if(!s.isEntregue() && !s.isLevantada() && s.isPreparada()){
                sb.append(s);
                count++;
            }
        }
        if(count == 0) {
			sb.append("0");
		}
        return sb.toString();
    }

    /**
     * Método que verifica se uma encomenda existe num voluntário.
     * @param enc
     * @return
     */

    public boolean existe(String enc){
        for(Encomenda e: this.historico){
            if(e.getCodigo().equals(enc)) {
				return true;
			}
        }
        return false;
    }

    /**
     * Método que devolve o número de encomendas efetuadas pelo voluntário entre 2 datas.
     * @param d1
     * @param d2
     * @return
     */
    public String getInfoEncomendas(LocalDateTime d1, LocalDateTime d2){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        Set<String> lojas = new TreeSet<>();
        for(Encomenda e: this.historico){
            LocalDateTime date = e.getData();
            if(date.compareTo(d1) >= 0 && date.compareTo(d2) <= 0){
                lojas.add(e.getCodigo_loja());
                count++;
            }
        }
        String s1 = d1.getDayOfMonth() + "/" + d1.getMonthValue() + "/" + d1.getYear();
        String s2 = d2.getDayOfMonth() + "/" + d2.getMonthValue() + "/" + d2.getYear();

        sb.append("Entre as datas ").append(s1).append(" e ").append(s2).append(" foram realizadas ").append(count).append(" encomendas pelo voluntário ")
				.append(getNome()).append("\n");
        sb.append("Efetuou encomendas em ").append(lojas.size()).append(" lojas");
        return sb.toString();
    }

    /**
     * Retorna o número de encomendas por entregar.
     * @return
     */

    public int porEntregar(){
        int i = 0;
        for(Encomenda e: this.historico){
            if(e.isLevantada() && !e.isEntregue()) {
				i++;
			}
        }
        return i;
    }

    /**
     * Retorna o número de encomendas por levantar.
     * @return
     */
    public int porLevantar(){
        int i = 0;
        for(Encomenda e: this.historico){
            if(!e.isLevantada() && e.isPreparada()) {
				i++;
			}
        }
        return i;
    }

    /**
     * Método que preve alguns atrasos, por causa das condições atmosféricas, e retorna, em minutos, o tempo perdido;.
     * @return
     */
    int calculaAtrasos(){
        Random random = new Random();
        int clima = random.nextInt(100);
        if(clima <= 75){
            return 0;
        }
        else if(clima > 75 && clima <= 94){
            return 30;
        }
        return 60;
    }
}
