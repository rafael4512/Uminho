import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BDVoluntarios implements Serializable {
    private Map<String, Voluntario> voluntarios;
    private Set<String> codigos;

    public BDVoluntarios(){
        this.voluntarios = new HashMap<>();
        this.codigos = new TreeSet<>();
    }

    public BDVoluntarios(Map<String, Voluntario> voluntario, Set<String> codigos){
        setVoluntarios(voluntario);
        setCodigos(codigos);
    }

    public BDVoluntarios(BDVoluntarios r){
        setVoluntarios(r.getVoluntarios());
        setCodigos(r.getCodigos());
    }

    public Map<String, Voluntario> getVoluntarios() {
        return this.voluntarios.entrySet().stream().collect(Collectors.toMap(r -> r.getKey(), r -> r.getValue().clone()));
    }

    public void setVoluntarios(Map<String, Voluntario> voluntarios) {
        this.voluntarios = new HashMap<>();
        voluntarios.entrySet().forEach(e -> this.voluntarios.put(e.getKey(), e.getValue().clone()));
    }

    public Set<String> getCodigos() {
        return this.codigos.stream().collect(Collectors.toSet());
    }

    public void setCodigos(Set<String> codigos) {
        this.codigos = new TreeSet<>();
        this.codigos.addAll(codigos);
    }

    public BDVoluntarios clone(){
        return new BDVoluntarios(this);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Total de Voluntarios: " + "\n");
        sb.append(this.voluntarios);

        return sb.toString();
    }

    public boolean equals(Object obj){
        if(obj == this) {
			return true;
		}
        if(obj == null || obj.getClass() != getClass()) {
			return false;
		}
        BDVoluntarios r = (BDVoluntarios) obj;
        return this.voluntarios.equals(r.getVoluntarios());
    }
    /**
     * Método que verifica se um email já está registado.
     * @param email
     * @return
     */
    public boolean existeEmail(String email){
        return this.voluntarios.containsKey(email);
    }

    public boolean existe(Voluntario v){
        return this.voluntarios.containsKey(v.getEmail());
    }
    public boolean existeCodigo(String s){
        return this.codigos.contains(s);
    }

    /**
     * Método que adiciona um voluntário.
     * @param v
     */

    public void add(Voluntario v){
        this.voluntarios.put(v.getEmail(), v.clone());
        this.codigos.add(v.getCodigo());
    }

    /**
     * Método que efetua o login de um voluntário.
     * @param email
     * @param password
     * @return
     */
    public Voluntario tryLogin(String email, String password){
        Voluntario aux = this.voluntarios.get(email);
        if(aux == null) {
			System.out.println("Não existe esse voluntário");
		} else if(aux.getPassword().equals(password)){
		    System.out.println("Login feito com sucesso");
		}
		else{
		    System.out.println("Password incorreta");
		    return null;
		}
        return aux;
    }

    /**
     * Método que devolve os voluntário a imprimir.
     * @return
     */
    public String printVoluntario(){
        StringBuilder sb = new StringBuilder();
        for(String s: this.voluntarios.keySet()){
            sb.append(this.voluntarios.get(s).clone().getCodigo()).append(" ---> ").append(this.voluntarios.get(s).clone().getNome()).append(" || RATE --> ").append(this.voluntarios.get(s).clone().getClassificacao()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Métodos que devolve os voluntário disponíveis para se deslocarem a uma loja.
     * @param j
     * @return
     */

    public String printVoluntarioLoja(Loja j){
        StringBuilder sb = new StringBuilder();
        for(String s: this.voluntarios.keySet()){
            Voluntario v = this.voluntarios.get(s);
            double dist = DistanceCalculator.distance(j.getLatitude(), v.getLatitude(), j.getLongitude(), v.getLongitude());
            if(dist <= v.getRaio_acao()) {
                sb.append(this.voluntarios.get(s).getCodigo()).append(" ---> ").append(this.voluntarios.get(s).getNome()).append(" RATE --> ").append(this.voluntarios.get(s).getClassificacao()).append(" KMS: ")
						.append(dist).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Método que devolve a lista de voluntário disponíveis para se deslocarem a uma determinada loja.
     * @param j
     * @return
     */
    public List<Voluntario> voluntariosDisponíveis(Loja j, Utilizador u) {
        List<Voluntario> ret = new ArrayList<>();
        for (String s : this.voluntarios.keySet()) {
            Voluntario v = this.voluntarios.get(s).clone();
            Double dist1 = DistanceCalculator.distance(j.getLatitude(), v.getLatitude(), j.getLongitude(), v.getLongitude());
            Double dist2 = DistanceCalculator.distance(j.getLatitude(), u.getLatitude(), j.getLongitude(), u.getLongitude());
            if (dist1 <= v.getRaio_acao() && dist2 <= v.getRaio_acao() && v.getDisponibilidade()) {
                ret.add(v.clone());
            }
        }
        return ret;
    }

    public List<Voluntario> voluntariosDisponíveisMed(Loja j , Utilizador u) {
        List<Voluntario> ret = new ArrayList<>();
        for (String s : this.voluntarios.keySet()) {
            Voluntario v = this.voluntarios.get(s).clone();
            Double dist1 = DistanceCalculator.distance(j.getLatitude(), v.getLatitude(), j.getLongitude(), v.getLongitude());
            Double dist2 = DistanceCalculator.distance(j.getLatitude(), u.getLatitude(), j.getLongitude(), u.getLongitude());
            if (dist1 <= v.getRaio_acao() && dist2 <= v.getRaio_acao() && v.getDisponibilidade() && v.aceitoTransporteMedicamentos()) {
                ret.add(v.clone());
            }
        }
        return ret;
    }

    /**
     * Lista de voluntários disponíveis para se deslocarem a uma loja, excetuando o voluntário passado como argumento.
     * @param j
     * @param v
     * @return
     */
    public List<Voluntario> voluntariosDisponíveis2(Loja j, Voluntario v) {
        List<Voluntario> ret = new ArrayList<>();
        for (String s : this.voluntarios.keySet()) {
            Voluntario v2 = this.voluntarios.get(s).clone();
            double dist = DistanceCalculator.distance(j.getLatitude(), v.getLatitude(), j.getLongitude(), v.getLongitude());
            if (dist <= v2.getRaio_acao() && v2.getDisponibilidade() && !v.getCodigo().equals(v2.getCodigo())) {
                ret.add(v2);
            }
        }
        return ret;
    }

    /**
     * Método que devolve o email de um voluntário.
     * @param cod
     * @return
     * @throws VoluntarioNotFoundException
     */
    public String getEmail(String cod) throws VoluntarioNotFoundException{
        for(String s: this.voluntarios.keySet()){
            if(this.voluntarios.get(s).clone().getCodigo().equals(cod)) {
				return this.voluntarios.get(s).clone().getEmail();
			}
        }
        throw new VoluntarioNotFoundException();
    }

    /**
     * Método que atualiza a classificação de um voluntário.
     * @param v
     * @param classificao
     */

    public void updateVoluntario(Voluntario v, double classificao){
        v.updateRate(classificao);
        this.voluntarios.put(v.getEmail(), v);
    }

    /**
     * Método que atualiza o voluntário v.
     * @param v
     */
    public void updateVoluntario2(Voluntario v){
        this.voluntarios.put(v.getEmail(), v);
    }

    /**
     * Método que devolve o voluntário que tem a encomenda com o código enc.
     * @param enc
     * @return
     * @throws EncomendaNotFoundException
     */
    public Voluntario encontraEnc(String enc) throws EncomendaNotFoundException{
        Voluntario aux;
        for(String s: this.voluntarios.keySet()){
            aux = this.voluntarios.get(s).clone();
            if(aux.existe(enc)) {
				return aux;
			}
        }
        throw new EncomendaNotFoundException();
    }

    /**
     * Método que diz se a encomenda existe na BDVoluntários.
     * @param enc
     * @return
     */
    public boolean existeEnc(String enc){
        Voluntario aux;
        for(String s: this.voluntarios.keySet()){
            aux = this.voluntarios.get(s).clone();
            if(aux.existe(enc)) {
				return true;
			}
        }
        return false;
    }
}
