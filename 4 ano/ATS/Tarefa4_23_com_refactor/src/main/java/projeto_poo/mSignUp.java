
import java.util.logging.Level;import java.util.logging.Logger; 
public class mSignUp extends mPrincipal {

  private transient Logger logger = Logger.getLogger(this.getClass().getName());   private String nome;
  private String codigo;
  private String email;
  private Ponto gps;
  private double raio;
  private boolean transporteMedicamentos;
  private String NIF;
  private double precoPorKm;

  public mSignUp() {
    super();
    this.nome = "";
    this.codigo = "";
    this.email = "";
    this.gps = new Ponto();
    this.raio = 0;
    this.transporteMedicamentos = false;
    this.NIF = "";
    this.precoPorKm = 0;
  }

  // Método que imprime e lê a opção pretendida no menu de signup e efetua o mesmo consoante o seu estatuto 
  public void escolhaMenu() {
    logger.log(Level.WARNING,"1: Utilizador");
    logger.log(Level.WARNING,"2: Loja");
    logger.log(Level.WARNING,"3: Transportadora");
    logger.log(Level.WARNING,"4: Voluntário");
    logger.log(Level.WARNING,"\n5: Voltar atrás");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      escolhaMenu();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que imprime e lê o menu de signup de um utilizador
  public void mostrarMenuSignUpU() {
    logger.log(Level.WARNING,"Nome");
    this.nome = leString();
    logger.log(Level.WARNING,"\nCódigo de utilizador");
    this.codigo = leString();
    logger.log(Level.WARNING,"\nEmail");
    this.email = leString();

    logger.log(Level.WARNING,"\nLocalização X");
    float gpsX = leFloat();
    logger.log(Level.WARNING,"\nLocalização Y");
    float gpsY = leFloat();

    if (gpsX == Float.POSITIVE_INFINITY || gpsY == Float.POSITIVE_INFINITY) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma localização inválida. Introduza apenas números.\n\n");
      mostrarMenuSignUpU();
    }
    else
      this.gps = new Ponto(gpsX, gpsY);
  }

  // Método que imprime e lê o menu de signup de uma loja
  public void mostrarMenuSignUpL() {
    logger.log(Level.WARNING,"Nome");
    this.nome = leString();

    logger.log(Level.WARNING,"\nLocalização X");
    float gpsX = leFloat();
    logger.log(Level.WARNING,"\nLocalização Y");
    float gpsY = leFloat();
    
    if (gpsX == Float.POSITIVE_INFINITY || gpsY == Float.POSITIVE_INFINITY) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma localização inválida. Introduza apenas números.\n\n");
      mostrarMenuSignUpL();
    }
    else
      this.gps = new Ponto(gpsX, gpsY);
  }

  // Método que imprime e lê o menu de signup de uma transportadora
  public void mostrarMenuSignUpT() {
    logger.log(Level.WARNING,"Nome");
    this.nome = leString();

    logger.log(Level.WARNING,"\nNIF");
    this.NIF = leString();

    logger.log(Level.WARNING,"\nRaio");
    double raioAUX = leDouble();

    logger.log(Level.WARNING,"\nPreço por km");
    double precoPorKMaux = leDouble();

    logger.log(Level.WARNING,"\nTransporte de medicamentos (Yes/No)");
    String transporteMedicamentosAUX = leYesOrNo();

    logger.log(Level.WARNING,"\nLocalização X");
    float gpsX = leFloat();
    logger.log(Level.WARNING,"\nLocalização Y");
    float gpsY = leFloat();
    
    if (gpsX == Float.POSITIVE_INFINITY || gpsY == Float.POSITIVE_INFINITY || raioAUX == Double.POSITIVE_INFINITY || precoPorKMaux == Double.POSITIVE_INFINITY || transporteMedicamentosAUX.equals("null")) {
      clearScreen();
      logger.log(Level.WARNING,"Um dos valores que introduziu é inválido. Introduza apenas números quando necessário.\n\n");
      mostrarMenuSignUpT();
    }
    else {
      this.raio = raioAUX;
      this.precoPorKm = precoPorKMaux;
      this.gps = new Ponto(gpsX, gpsY);
      if (transporteMedicamentosAUX.equals("true"))
        this.transporteMedicamentos = true;
      else
        if (transporteMedicamentosAUX.equals("false"))
          this.transporteMedicamentos = false;
    }
  }

  // Método que imprime e lê o menu de signup de um voluntário
  public void mostrarMenuSignUpV() {
    logger.log(Level.WARNING,"Nome");
    this.nome = leString();

    logger.log(Level.WARNING,"\nRaio");
    double raioAUX = leDouble();

    logger.log(Level.WARNING,"\nTransporte de medicamentos (Yes/No)");
    String transporteMedicamentosAUX = leYesOrNo();

    logger.log(Level.WARNING,"\nLocalização X");
    float gpsX = leFloat();
    logger.log(Level.WARNING,"\nLocalização Y");
    float gpsY = leFloat();
    
    if (gpsX == Float.POSITIVE_INFINITY || gpsY == Float.POSITIVE_INFINITY || raioAUX == Double.POSITIVE_INFINITY || transporteMedicamentosAUX.equals("null")) {
      clearScreen();
      logger.log(Level.WARNING,"Um dos valores que introduziu é inválido. Introduza apenas números quando necessário.\n\n");
      mostrarMenuSignUpV();
    }
    else {
      this.raio = raioAUX;
      this.gps = new Ponto(gpsX, gpsY);
      if (transporteMedicamentosAUX.equals("true"))
        this.transporteMedicamentos = true;
      else
        if (transporteMedicamentosAUX.equals("false"))
          this.transporteMedicamentos = false;
    }
  }


  /* GET functions */

  public String getNome() {
    return this.nome;
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getEmail() {
    return this.email;
  }

  public Ponto getGPS() {
    return this.gps;
  }

  public double getRaio() {
    return this.raio;
  }

  public boolean getTransporteMedicamentos() {
    return this.transporteMedicamentos;
  }

  public String getNIF() {
    return this.NIF;
  }

  public double getPrecoPorKM() {
    return this.precoPorKm;
  }
}
