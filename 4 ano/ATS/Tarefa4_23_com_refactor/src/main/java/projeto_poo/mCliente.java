
import java.util.logging.Level;import java.util.logging.Logger; import java.time.LocalDateTime;

public class mCliente extends mPrincipal {

  private transient Logger logger = Logger.getLogger(this.getClass().getName());  private String codigoE;
  private String codigoL;
  private String codigoT;
  private String codigoTia;
  private String codigoRegisto;
  private boolean flag;
  private boolean flagPE;
  private boolean flagTM;
  private String email;
  private String password;
  private Ponto gps;

  public mCliente() {
    super();
    this.codigoE = "";
    this.codigoL = "";
    this.codigoT = "";
    this.codigoTia = "";
    this.codigoRegisto = "";
    this.flag = false;
    this.flagPE = false;
    this.flagTM = false;
    this.email = "";
    this.password = "";
    this.gps = new Ponto();
  }

  // Método que imprime e lê a opção pretendida no menu de cliente 
  public void escolhaMenu() {
    logger.log(Level.WARNING,"1: A minha encomenda\n");
    logger.log(Level.WARNING,"2: Definições\n");
    logger.log(Level.WARNING,"3: Informações adicionais\n");
    logger.log(Level.WARNING,"\n4: Log out\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      escolhaMenu();
    }
    else
      setOpcao(opcaoAUX);
  }


    /* MENU ENCOMENDAS */

  // Método que imprime e lê a opção pretendida no menu de encomenda 
  public void menuEncomenda() {
    logger.log(Level.WARNING,"1: Ver estado da encomenda\n");
    logger.log(Level.WARNING,"2: Inserir pedido de encomenda\n");
    logger.log(Level.WARNING,"3: Pedir transporte\n");
    logger.log(Level.WARNING,"4: Classificar o serviço de transporte\n");
    logger.log(Level.WARNING,"\n5: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuEncomenda();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que imprime e lê a opção pretendida no menu de registo de encomendas 
  public void menuInserirEncomenda() {
    logger.log(Level.WARNING,"Código da encomenda\n");
    this.codigoE = leString();
    logger.log(Level.WARNING,"\nCódigo da loja\n");
    this.codigoL = leString();

    logger.log(Level.WARNING,"\nA sua encomenda contém medicamentos? (Yes/No)\n");
    String tm = leYesOrNo();

    if (tm.equals("null")) {
      clearScreen();
      logger.log(Level.WARNING,"Input inválido. Introduza apenas Yes ou No.\n\n");
      menuInserirEncomenda();
    }
    else {
      if (tm.equals("true"))
        this.flagTM = true;
      else
        if (tm.equals("false"))
          this.flagTM = false;
    }
  }

  // Método que imprime o estado da encomenda 
  public void menuEstadoEncomenda() {
    if (flag)
      logger.log(Level.WARNING,"Tempo estimado de espera\n");
    else
      logger.log(Level.WARNING,"Não inseriu nenhuma encomenda.\n");
  }

  // Método que imprime e lê a opção pretendida no menu de pedidos para entrega
  public void menuPedirEntrega() {
    if (flag) {
      logger.log(Level.WARNING,"1: Entrega realizada por transportadora\n");
      logger.log(Level.WARNING,"2: Entrega realizada por voluntário\n");
      logger.log(Level.WARNING,"\n3: Voltar atrás\n");

      int opcaoAUX = leOpcao();
      if (opcaoAUX == Integer.MAX_VALUE) {
        clearScreen();
        logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
        menuPedirEntrega();
      }
      else
        setOpcao(opcaoAUX);
    }
    else
      logger.log(Level.WARNING,"Ainda não inseriu nenhuma encomenda.\n");
  }

  // Método lê a transportadora pretendida para entregar a sua encomenda 
  public void menuEntregaT() {
    logger.log(Level.WARNING,"Código da transportadora\n");
    this.codigoT = leString();
    this.flagPE = true;
    this.flag = false;
  }


    /* MENU INFORMAÇÕES ADICIONAIS */

  // Método que imprime e lê a opção pretendida no menu de informações adicionais (cliente)  
  public void menuInformacoesAdicionais() {
    logger.log(Level.WARNING,"1: Registo das encomendas de um voluntário/transportadora\n");
    logger.log(Level.WARNING,"\n2: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuInformacoesAdicionais();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que imprime e lê a opção pretendida no menu de registo de encomendas«
  public void menuRegistoEscolha() {
    logger.log(Level.WARNING,"1: Até à data\n");
    logger.log(Level.WARNING,"2: Num certo período de tempo\n");
    logger.log(Level.WARNING,"\n3: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuRegistoEscolha();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que imprime e lê o código pretendido para saber o registo das encomendas
  public void menuRegistoV() {
    logger.log(Level.WARNING,"Código\n");
    this.codigoRegisto = leString();
  }


    /* MENU CLASSIFICACOES */

  // Método que imprime e lê a opção pretendida no menu de leaderboards
  public void menuClassificacoes() {
    logger.log(Level.WARNING,"1: Top 10 - Utilizadores\n");
    logger.log(Level.WARNING,"2: Top 10 - Voluntários\n");
    logger.log(Level.WARNING,"3: Top 10 - Transportadoras\n");
    logger.log(Level.WARNING,"\n4: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuClassificacoes();
    }
    else
      setOpcao(opcaoAUX);
  }


    /* DATAS */

  // Método que lê a uma data inicial
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
  

    /* MENU DEFINICOES */

  // Método que imprime e lê a opção pretendida no menu de definições de cliente 
  public void menuDefinicoes() {
    logger.log(Level.WARNING,"1: Localização atual\n");
    logger.log(Level.WARNING,"2: Histórico de encomendas realizadas\n");
    logger.log(Level.WARNING,"3: Alterar email\n");
    logger.log(Level.WARNING,"4: Alterar password\n");
    logger.log(Level.WARNING,"5: Alterar localização\n");
    logger.log(Level.WARNING,"\nAconselhamos a mudar regularmente a sua password.\n");
    logger.log(Level.WARNING,"\n6: Voltar atrás\n");

    int opcaoAUX = leOpcao();
    if (opcaoAUX == Integer.MAX_VALUE) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma opcção inválida. Introduza apenas números.\n\n");
      menuDefinicoes();
    }
    else
      setOpcao(opcaoAUX);
  }

  // Método que altera o email de um cliente
  public void menuAlterarEmail() {
    logger.log(Level.WARNING,"Novo email\n");
    this.email = leString();
  }

  // Método que altera a password de um cliente
  public void menuAlterarPW() {
    logger.log(Level.WARNING,"Nova password\n");
    this.password = leString();
  }

  // Método que altera a localização de um cliente
  public void menuAlterarLocalizacao() {
    logger.log(Level.WARNING,"Nova localização\n");
    logger.log(Level.WARNING,"X: ");
    float gpsX = leFloat();
    logger.log(Level.WARNING,"\nY: ");
    float gpsY = leFloat();

    if (gpsX == Float.POSITIVE_INFINITY || gpsY == Float.POSITIVE_INFINITY) {
      clearScreen();
      logger.log(Level.WARNING,"Introduziu uma localização inválida. Introduza apenas números.\n\n");
      menuAlterarLocalizacao();
    }
    else
      this.gps = new Ponto(gpsX, gpsY);
  }

  
  /* GET functions */

  public String getCodigoE() {
    return this.codigoE;
  }

  public String getCodigoL() {
    return this.codigoL;
  }

  public String getCodigoT() {
    return this.codigoT;
  }

  public String getCodigoTia() {
    return this.codigoTia;
  }

  public String getCodigoRegisto() {
    return this.codigoRegisto;
  }

  public boolean getFlag() {
    return this.flag;
  }

  public boolean getFlagPE() {
    return this.flagPE;
  }

  public boolean getFlagTM() {
    return this.flagTM;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPassword() {
    return this.password;
  }

  public Ponto getGPS() {
    return this.gps;
  }

  
  /* SET functions */

  public void setFlag(boolean newFlag) {
    this.flag = newFlag;
  }

  public void setFlagPE(boolean newFlagPE) {
    this.flagPE = newFlagPE;
  }
}
