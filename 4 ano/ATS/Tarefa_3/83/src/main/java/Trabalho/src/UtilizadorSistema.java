import java.io.Serializable;

public abstract class  UtilizadorSistema implements Serializable {
    private String email;
    private String password;
    private String typeUser;
    private String codigo;
    private String nome;
    private double latitude;
    private double longitude;

    /** Construtores. */
    public UtilizadorSistema(){
        this.email = "";
        this.password = "";
        this.typeUser = "";
        this.codigo = "";
        this.nome = "";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public  UtilizadorSistema(String email, String password, String typeUser, String codigo, String nome, double latitude, double longitude){
        this.email = email;
        this.password = password;
        this.typeUser = typeUser;
        this.codigo = codigo;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UtilizadorSistema(UtilizadorSistema a){
        this.email = a.getEmail();
        this.password = a.getPassword();
        this.typeUser = a.getTypeUser();
        this.codigo = a.getCodigo();
        this.nome = a.getNome();
        this.latitude = a.getLatitude();
        this.longitude = a.getLongitude();
    }

    /** Getters. */
    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    /** Setters. */
    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public abstract UtilizadorSistema clone();

    public boolean equals(Object obj){
        if(obj == this) {
			return true;
		}
        if(obj == null || obj.getClass() != getClass()) {
			return false;
		}
        UtilizadorSistema u = (UtilizadorSistema) obj;
        return this.password.equals(u.getPassword()) &&
                this.typeUser.equals(u.getTypeUser()) &&
                this.email.equals(u.getEmail());
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(" <--- O utilizador é: " + "\n");
        sb.append(this.typeUser).append("\n");
        sb.append("\n");

        return sb.toString();
    }
}
