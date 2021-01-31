
import java.util.logging.Level;import java.util.logging.Logger; public class mLogIn extends mPrincipal {

  private transient Logger logger = Logger.getLogger(this.getClass().getName());  private String email;
  private String codigo;
  private String password;

  public mLogIn() {
    super();
    this.email = "";
    this.codigo = "";
    this.password = "";
  }


    /* GERAL */

  // Método que imprime e lê a opção pretendida no menu de login e efetua o mesmo consoante o seu estatuto
  public void escolhaMenuGERAL() {
    logger.log(Level.WARNING,"1: Utilizador\n");
    logger.log(Level.WARNING,"2: Loja\n");
    logger.log(Level.WARNING,"3: Transportadora\n");
    logger.log(Level.WARNING,"4: Voluntário\n");
    logger.log(Level.WARNING,"\n5: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      escolhaMenuGERAL();
    }
    else
      setOpcao(opcaoAUX);
  }


    /* MENU UTILIZADOR */

  // Método que imprime e lê a opção pretendida no menu de login de cliente
  public void escolhaMenuLogIn() {
    logger.log(Level.WARNING,"\n1: Email");
    logger.log(Level.WARNING,"\n2: Código\n");
    logger.log(Level.WARNING,"\n3: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      escolhaMenuLogIn();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que efetua o login de um cliente atraves do email 
  public void mostrarMenuLogInE() {
    logger.log(Level.WARNING,"Email");
    this.email = leString();

    logger.log(Level.WARNING,"\nPassword");
    this.password = leString();
  }

  // Método que efetua o login de um cliente atraves do código
  public void mostrarMenuLogInC() {
    logger.log(Level.WARNING,"Código");
    this.codigo = leString();

    logger.log(Level.WARNING,"\nPassword");
    this.password = leString();
  }


    /* MENU LOJAS/TRANSPORTADORAS/VOLUNTARIOS */

  // Método que efetua o login de uma plataforma de entrega/loja
  public void mostrarMenuLogInExtra() {
    logger.log(Level.WARNING,"Código\n");
    this.codigo = leString();

    logger.log(Level.WARNING,"\nPassword\n");
    this.password = leString();
  }

  
  /* GET functions */

  public String getEmail() {
    return this.email;
  }

  public String getCodigo() {
    return this.codigo;
  }

  public String getPassword() {
    return this.password;
  }
}
