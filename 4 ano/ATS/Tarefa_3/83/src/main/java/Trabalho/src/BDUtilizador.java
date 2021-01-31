import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class BDUtilizador implements Serializable {
    private Map<String, Utilizador> users;
    private Set<String> codigos;

    public BDUtilizador() {
        this.users = new HashMap<>();
        this.codigos = new TreeSet<>();
    }

    public BDUtilizador(Map<String, Utilizador> user, Set<String> codigos) {
        setUsers(user);
        setCodigos(codigos);
    }

    public BDUtilizador(BDUtilizador r) {
        setUsers(r.getUsers());
        setCodigos(r.getCodigos());
    }

    public Map<String, Utilizador> getUsers() {
        return this.users.entrySet().stream().collect(Collectors.toMap(r -> r.getKey(), r -> r.getValue().clone()));
    }

    public Set<String> getCodigos() {
        return this.codigos.stream().collect(Collectors.toSet());
    }

    public void setCodigos(Set<String> codigos) {
        this.codigos = new TreeSet<>();
        this.codigos.addAll(codigos);
    }

    public void setUsers(Map<String, Utilizador> users) {
        this.users = new HashMap<>();
        users.entrySet().forEach(e -> this.users.put(e.getKey(), e.getValue().clone()));
    }

    public BDUtilizador clone() {
        return new BDUtilizador(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total de utilizadores: " + "\n");
        sb.append(this.users);

        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
			return true;
		}
        if (obj == null || obj.getClass() != getClass()) {
			return false;
		}
        BDUtilizador r = (BDUtilizador) obj;
        return this.users.equals(r.getUsers());
    }

    /**
     * Método que verifica se um email já está registado.
     * @param email
     * @return
     */
    public boolean existeEmail(String email){
        return this.users.containsKey(email);
    }

    /**
     * Método que verifica se um utiizador existe.
     * @param v
     * @return
     */

    public boolean existe(Utilizador v){
        return this.users.containsKey(v.getEmail());
    }

    /**
     * Método que adiciona um utilizador.
     * @param u
     */
    public void add(Utilizador u) {
        this.users.put(u.getEmail(), u.clone());
        this.codigos.add(u.getCodigo());
    }

    /**
     * Método que verifica se um código de utilizador existe.
     * @param s
     * @return
     */
    public boolean existeCodigo(String s){
        return this.codigos.contains(s);
    }

    /**
     * Método que adiciona uma encomenda a um utilizador.
     * @param e
     * @param u
     */
    public void updateUser(Encomenda e, Utilizador u){
        u.addEncomenda(e);
        this.users.put(u.getEmail(), u);
    }

    /**
     * Método que efetua o login de um utilizador.
     * @param email
     * @param password
     * @return
     */
    public Utilizador tryLogin(String email, String password){
        if(!this.users.containsKey(email)) {
			return null;
		} else{
            Utilizador aux = this.users.get(email).clone();
            if(aux.getPassword().equals(password)){
                System.out.println("Login feito com sucesso");
                return aux;
            }
            else{
                System.out.println("Password incorreta");
                return null;
            }
        }
    }

    public String getEmail(String cod) throws UserNotFoundException{
        for(String s: this.users.keySet()){
            if(this.users.get(s).clone().getCodigo().equals(cod)) {
				return this.users.get(s).getEmail();
			}
        }
        throw new UserNotFoundException();
    }

    /**
     * Método que atualiza um utilizador.
     * @param u
     */
    public void updateUser2(Utilizador u){
        this.users.put(u.getEmail(), u.clone());
    }
}
