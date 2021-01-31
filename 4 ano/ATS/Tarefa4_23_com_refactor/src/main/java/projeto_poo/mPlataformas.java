
import java.util.logging.Level;import java.util.logging.Logger; 
import java.time.LocalDateTime;

public class mPlataformas extends mPrincipal {

  private transient Logger logger = Logger.getLogger(this.getClass().getName()); private String password;
  private double raio;
  private int capacidade;
  private double precoPorKm;
  private boolean transporteMedicamentos;

  public mPlataformas() {
    super();
    this.password = "";
    this.raio = 0;
    this.capacidade = 0;
    this.precoPorKm = 0;
    this.transporteMedicamentos = false;
  }


    /* MENU LOJAS */

  // Método que imprime e lê a opção pretendida no menu de uma loja
  public void menuLoja() {
    logger.log(Level.WARNING,"1: Número de pessoas atualmente na fila\n");
    logger.log(Level.WARNING,"2: Registo de encomendas entregues até à data\n");
    logger.log(Level.WARNING,"3: Registo de encomendas entregues num certo período de tempo\n");
    logger.log(Level.WARNING,"4: Alterar password\n");
    logger.log(Level.WARNING,"\n5: Log out\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuLoja();
    }
    else
      setOpcao(opcaoAUX);
  }

  
    /* MENU TRANSPORTADORAS */

  // Método que imprime e lê a opção pretendida no menu de uma transportadora
  public void menuTransportadoras() {
    logger.log(Level.WARNING,"1: Total faturado até à data\n");
    logger.log(Level.WARNING,"2: Total faturado num certo período\n");
    logger.log(Level.WARNING,"3: Classificação atual da empresa\n");
    logger.log(Level.WARNING,"4: Registo de entregas realizadas até à data\n");
    logger.log(Level.WARNING,"5: Registo de entregas realizadas num certo período de tempo\n");
    logger.log(Level.WARNING,"6: Definições\n");
    logger.log(Level.WARNING,"\n7: Log out\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuTransportadoras();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que imprime e lê a opção pretendida no menu de definições de uma transportadora
  public void menuTransportadorasDefinicoes() {
    logger.log(Level.WARNING,"1: Alterar password\n");
    logger.log(Level.WARNING,"2: Alterar raio\n");
    logger.log(Level.WARNING,"3: Alterar capacidade da empresa\n");
    logger.log(Level.WARNING,"4: Alterar preço por km\n");
    logger.log(Level.WARNING,"5: Validar o transporte de medicamentos\n");
    logger.log(Level.WARNING,"\n6: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuTransportadorasDefinicoes();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que altera a password de uma plataforma 
  public void menuAlterarPW() {
    logger.log(Level.WARNING,"Nova password\n");
    this.password = leString();
  }

  // Método que altera o raio de uma plataforma 
  public void menuAlterarRaio() {
    logger.log(Level.WARNING,"Novo raio\n");
    double raioAUX = leDouble();

    if (raioAUX == Double.POSITIVE_INFINITY) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu um raio inválido. Introduza apenas número.\n\n");
      menuAlterarRaio();
    }
    else
      this.raio = raioAUX;
  }

  // Método que altera a capacidade de uma transportadora (número de trabalhadores de uma empresa)
  public void menuAlterarCapacidade() {
    logger.log(Level.WARNING,"Nova capacidade\n");
    int capacidadeAUX = leInt();

    if (capacidadeAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Indroduziu uma capacidade inválida. Introduza apenas números.\n\n");
      menuAlterarCapacidade();
    }
    else
      this.capacidade = capacidadeAUX;
  }

  // Método que altera o preço por km de uma transportadora 
  public void menuAlterarPrecoPorKM() {
    logger.log(Level.WARNING,"Novo preço\n");
    double precoPorKMaux = leDouble();

    if (precoPorKMaux == Double.POSITIVE_INFINITY) {
      clearScreen();
      logger.log(Level.WARNING,"Indroduziu um preço por km inválido. Introduza apenas números.\n\n");
      menuAlterarPrecoPorKM();
    }
    else
      this.precoPorKm = precoPorKMaux;
  }

  // Método que altera a validade do transporte de medicamentos de uma plataforma de entrega
  public void menuValidarTM() {
    logger.log(Level.WARNING,"Transporte de medicamentos (Yes/No)\n");
    String transporteMedicamentosAUX = leYesOrNo();

    if (transporteMedicamentosAUX.equals("null")) {
      clearScreen();
      logger.log(Level.WARNING,"Input inválido. Introduza apenas Yes ou No.\n\n");
      menuValidarTM();
    }
    else {
      if (transporteMedicamentosAUX.equals("true"))
        this.transporteMedicamentos = true;
      else
        if (transporteMedicamentosAUX.equals("false"))
          this.transporteMedicamentos = false;
    }
  }

  // Método que lê uma data inicial
  public void menuDataI() {
    logger.log(Level.WARNING,"Data inicial\n\n");
    logger.log(Level.WARNING,"Ano\n");
    setAno(leInt());

    logger.log(Level.WARNING,"\nMês\n");
    setMes(leInt());

    logger.log(Level.WARNING,"\nDia\n");
    setDia(leInt());

    logger.log(Level.WARNING,"\nHora\n");
    setHora(leInt());

    logger.log(Level.WARNING,"\nMinuto\n");
    setMinuto(leInt());

    if (getAno() == Integer.MAX_VALUE || getMes() == Integer.MAX_VALUE || getDia() == Integer.MAX_VALUE || getHora() == Integer.MAX_VALUE || getMinuto() == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma data inválida. Introduza apenas números.\n\n");
      menuDataI();
    }
    else {
      if (!verificaData()) {
        logger.log(Level.WARNING,"\nData inválida.\n");
        logger.log(Level.WARNING,"\nInsira um caracter para voltar a inserir uma data.\n");
        String i = leString();
        clearScreen();
        menuDataI();
      }
      else
        setDataI(LocalDateTime.of(getAno(), getMes(), getDia(), getHora(), getMinuto()));
    }
  }

  // Método que lê uma data final 
  public void menuDataF() {
    logger.log(Level.WARNING,"Data final\n\n");
    logger.log(Level.WARNING,"Ano\n");
    setAno(leInt());

    logger.log(Level.WARNING,"\nMês\n");
    setMes(leInt());

    logger.log(Level.WARNING,"\nDia\n");
    setDia(leInt());

    logger.log(Level.WARNING,"\nHora\n");
    setHora(leInt());

    logger.log(Level.WARNING,"\nMinuto\n");
    setMinuto(leInt());

    if (getAno() == Integer.MAX_VALUE || getMes() == Integer.MAX_VALUE || getDia() == Integer.MAX_VALUE || getHora() == Integer.MAX_VALUE || getMinuto() == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma data inválida. Introduza apenas números.\n\n");
      menuDataF();
    }
    else {
      if (!verificaData()) {
        logger.log(Level.WARNING,"\nData inválida.\n");
        logger.log(Level.WARNING,"\nInsira um caracter para voltar a inserir uma data.\n");
        String i = leString();
        clearScreen();
        menuDataF();
      }
      else
        setDataF(LocalDateTime.of(getAno(), getMes(), getDia(), getHora(), getMinuto()));
    }
  }


    /* MENU VOLUNTARIOS */

  // Método que imprime e lê a opção pretendida no menu de um voluntário
  public void menuVoluntarios() {
    logger.log(Level.WARNING,"1: Classificação atual\n");
    logger.log(Level.WARNING,"2: Registo das entregas realizadas até à data\n");
    logger.log(Level.WARNING,"3: Registo das entregas realizadas num certo período de tempo\n");
    logger.log(Level.WARNING,"4: Definições\n");
    logger.log(Level.WARNING,"\n5: Log out\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuVoluntarios();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que imprime e lê a opção pretendida no menu de definições de um voluntário
  public void menuVoluntariosDefinicoes() {
    logger.log(Level.WARNING,"1: Alterar password\n");
    logger.log(Level.WARNING,"2: Alterar raio\n");
    logger.log(Level.WARNING,"3: Validar o transporte de medicamentos\n");
    logger.log(Level.WARNING,"\n4: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuVoluntariosDefinicoes();
    }
    else
      setOpcao(opcaoAUX);
  }
  

  /* GET functions */

  public String getPW() {
    return this.password;
  }

  public double getRaio() {
    return this.raio;
  }

  public int getCapacidade() {
    return this.capacidade;
  }

  public double getPrecoPorKM() {
    return this.precoPorKm;
  }

  public boolean getTM() {
    return this.transporteMedicamentos;
  }
}
