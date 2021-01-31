
import java.util.logging.Level;
import java.util.logging.Logger;

public class mInicial extends mPrincipal {

  private transient Logger logger = Logger.getLogger(this.getClass().getName());
  public mInicial() {
    super();
  }
  
  // Método que imprime e lê a opção pretendida no menu inicial 
  public void mostrarMenuInicial() {
    logger.log(Level.WARNING,"1: Log in");
    logger.log(Level.WARNING,"2: Sign up");
    logger.log(Level.WARNING,"3: Leaderboards");
    logger.log(Level.WARNING,"4: Sair");
    logger.log(Level.WARNING,"\nNunca te esqueças que a tua primeira password é sempre 'TrazAqui'!!!");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      logger.log(Level.WARNING,"\tTraz Aqui\n\n");
      mostrarMenuInicial();
    }
    else
      setOpcao(opcaoAUX);
  }
}
