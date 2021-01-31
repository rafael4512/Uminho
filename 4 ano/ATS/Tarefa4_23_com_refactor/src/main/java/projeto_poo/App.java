import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class App implements Serializable {
  private static final long serialVersionUID = 1905122041950251207L;
  private TrazAqui ta;
  private mInicial menuInicial;
  private mLogIn menuLogIn;
  private mSignUp menuSignUp;
  private mCliente menuCliente;
  private mPlataformas menuPlataformas;
  private String codigoE;
  private String codigoPE;
  private String codigoU;
  public static EnergyCheckUtils ec;
  static final String voluntario = "Voluntario";
  private transient Logger logger = Logger.getLogger(this.getClass().getName());
  static final String CONST1="A opcão que escolheu é inválida.\n\n\n";
  static final String CONST2="A redireciona-lo para o menu inicial...\n\n\n";
  static final String CONST3="Código da encomenda: ";
  static final String CONST4="\nCódigo do utilizador: ";
  static final String CONST5="\nProdutos: ";
  static final String CONST6="\nPeso total: ";
  static final String CONST7="\nData: ";
  static final String CONST8="Não foi feito\n";


  public static void main(String[] args) {
	  ec = new EnergyCheckUtils();

        double start = System.nanoTime() ;
        double[] before = ec.getEnergyStats();

    new App().startInicial();

    double[] after = ec.getEnergyStats();

        double total = (double)(System.nanoTime()) - start;
        double tempo = (double)(total / 1000000000);
        System.out.println(tempo + " segundos");
        System.out.println("Info de app: \n");
        for(int i = 0; i < ec.socketNum; i++) {
                        System.out.println("Energy(joules): dram " + (after[0] - before[0])  + " cpu " + (after[1] - before[1]) + " package " + (after[2] - before[2]) );

                        System.out.println("Power: dram " + (after[0] - before[0])/tempo  + " cpu " + (after[1] - before[1])/tempo + " package " + (after[2] - before[2])/tempo);
        }

               ec.ProfileDealloc();

  }

  // Método que lê o ficheiro object (se este nao existir lê o ficheiro csv)  
  public App() {
	  double start = System.nanoTime() ;
        double[] before = ec.getEnergyStats();

    ta = new TrazAqui();
    System.out.println("AQUUIIIIIIIIII");

    logger.log(Level.WARNING,"\nNão existe nenhum ficheiro compatível.\nA iniciar...\n");
    ta.parseTotal("/home/ril/Desktop/ATS/Tarefa4_23_com_refactor/src/main/java/projeto_poo/grande.csv");

	double[] after = ec.getEnergyStats();

        double total = (double)(System.nanoTime()) - start;
        double tempo = (double)(total / 1000000000);
        System.out.println(tempo + " segundos");
        System.out.println("Info de parsing: \n");
        for(int i = 0; i < ec.socketNum; i++) {
                        System.out.println("Energy(joules): dram " + (after[0] - before[0])  + " cpu " + (after[1] - before[1]) + " package " + (after[2] - before[2]) );

                        System.out.println("Power: dram " + (after[0] - before[0])/tempo  + " cpu " + (after[1] - before[1])/tempo + " package " + (after[2] - before[2])/tempo);
        }

    this.menuInicial = new mInicial();
    this.menuLogIn = new mLogIn();
    this.menuSignUp = new mSignUp();
    this.menuCliente = new mCliente();
    this.menuPlataformas = new mPlataformas();
  }

  // Método que lê a opção pretendida do menu inicial e redireciona para essa função (login/signup/leaderbords)
  public void startInicial() {
    boolean flagB = true;
    this.menuInicial.setOpcao(0);
    this.codigoE = "";
    this.codigoPE = "";
    this.codigoU = "";

    while (this.menuInicial.getOpcao() != 4) {
      logger.log(Level.WARNING,"\tTraz Aqui\n\n");
      this.menuInicial.mostrarMenuInicial();

      flagB = isFlagB(flagB);
    }

    if (flagB) {
      logger.log(Level.WARNING,"\nA desligar...\nAté breve!!!\n");

      try {
        this.ta.gravaCSV("/home/ril/Desktop/ATS/Tarefa4_23_com_refactor/src/main/java/projeto_poo/grande.csv");
      } catch (FileNotFoundException e) {
        logger.log(Level.WARNING,e.getMessage());
      }

      try {
        this.ta.gravaFicheiro("/home/ril/Desktop/ATS/Tarefa4_23_com_refactor/src/main/java/projeto_poo/TrazAqui.object");
      } catch (IOException e) {
        logger.log(Level.WARNING,e.getMessage());
      }
    }
  }

  private boolean isFlagB(boolean flagB) {
    // Sair
    if (this.menuInicial.getOpcao() == 1) { // Log in
      this.menuInicial.clearScreen();
      startLogIn();

      flagB = false;
    } else if (this.menuInicial.getOpcao() == 2) { // Sign up
      this.menuInicial.clearScreen();
      startSignUp();

      flagB = false;
    } else if (this.menuInicial.getOpcao() == 3) { // Leaderboards
      this.menuInicial.clearScreen();
      startClassificacoes();

      flagB = false;
    }else {
      this.menuInicial.clearScreen();
      logger.log(Level.WARNING, CONST1);

    }
    return flagB;
  }


  // Método que lê a opção pretendida do menu de signup podendo fazer signup com qualquer um dos tipos de utilizadores da plataforma
  public void startSignUp() {
    boolean flagB = true;
    boolean flagSignUp = true;
    this.menuSignUp.setOpcao(0);
    this.menuSignUp.clearScreen();
    String codigo;
    String email;

    while (this.menuSignUp.getOpcao() != 5 && flagSignUp) {
      this.menuSignUp.escolhaMenu();

      if (this.menuSignUp.getOpcao() == 1) { // Utilizador
        this.menuSignUp.clearScreen();
        this.menuSignUp.mostrarMenuSignUpU();

        try {
          codigo = this.menuSignUp.getCodigo();
           ta.codigoSignUp(codigo);
        } catch (exCodigoAlreadyExists e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        try {
          email = this.menuSignUp.getEmail();

          String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
          boolean flag = email.matches(regex);

          if (flag)
            ta.emailSignUp(email);
          else {
            menuSignUp.clearScreen();
            logger.log(Level.WARNING, "\nInseriu um email inválido.\n");
            logger.log(Level.WARNING, CONST2);
            startInicial();
            return;
          }
        } catch (exEmailAlreadyExists e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        Ponto gpsu = this.menuSignUp.getGPS();
        String nomeU = this.menuSignUp.getNome();
        Map<String, Encomenda> encomendasGuardadasU = new HashMap<>();
        Utilizadores u = new Utilizadores(codigo, nomeU, email, "TrazAqui", gpsu, encomendasGuardadasU);
        ta.addUtilizador(u);

        flagSignUp = false;
        menuSignUp.clearScreen();
        logger.log(Level.WARNING, "Registado com sucesso!!\nA redireciona-lo para página inicial...\n\n\n");
        startInicial();

        flagB = false;
      } else if (this.menuSignUp.getOpcao() == 2) { // Loja
        this.menuSignUp.clearScreen();
        this.menuSignUp.mostrarMenuSignUpL();

        try {
          ta.lojaVerifica(this.menuSignUp.getNome(), this.menuSignUp.getGPS());
        } catch (exLojaAlreadyExists e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        String nomeL = this.menuSignUp.getNome();
        String codigoL = nomeL.replaceAll(" ", "");
        Ponto gpsl = this.menuSignUp.getGPS();
        Map<String, Encomenda> registoEncomendas = new HashMap<>();
        Lojas l = new Lojas(codigoL, "TrazAqui", nomeL, gpsl, 0, registoEncomendas);
        ta.addLoja(l);

        flagSignUp = false;
        menuSignUp.clearScreen();
        logger.log(Level.WARNING, "Registado com sucesso!!\nA redireciona-lo para página inicial...\n\n\n");
        startInicial();

        flagB = false;
      } else if (this.menuSignUp.getOpcao() == 3) { // Transportadora
        this.menuSignUp.clearScreen();
        this.menuSignUp.mostrarMenuSignUpT();

        try {
          ta.peVerifica(this.menuSignUp.getNome(), this.menuSignUp.getGPS());
        } catch (exPEAlreadyExists e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        try {
           ta.verificaValoresDOUBLE(this.menuSignUp.getRaio());
           ta.verificaValoresDOUBLE(this.menuSignUp.getPrecoPorKM());
        } catch (exNegativeValues e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        try {
          ta.verificaNIF(this.menuSignUp.getNIF());
        } catch (exInvalidNIF e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        String nomeT = this.menuSignUp.getNome();
        String codigoT = nomeT.replaceAll(" ", "");
        Ponto gpst = this.menuSignUp.getGPS();
        double raioT = this.menuSignUp.getRaio();
        boolean transporteMedicamentosT = this.menuSignUp.getTransporteMedicamentos();
        List<Integer> classificacoesT = new ArrayList<>();
        Map<String, Encomenda> encomendasGuardadasT = new HashMap<>();
        String nif = this.menuSignUp.getNIF();
        Double precoPorKMT = this.menuSignUp.getPrecoPorKM();
        Transportadoras t = new Transportadoras(codigoT, "TrazAqui", nomeT, gpst, raioT, transporteMedicamentosT, true, classificacoesT, encomendasGuardadasT, nif, precoPorKMT, 5);
        ta.addPlataforma(t);

        flagSignUp = false;
        menuSignUp.clearScreen();
        logger.log(Level.WARNING, "Registado com sucesso!!\nA redireciona-lo para página inicial...\n\n\n");
        startInicial();

        flagB = false;
      } else if (this.menuSignUp.getOpcao() == 4) { // Voluntário
        this.menuSignUp.clearScreen();
        this.menuSignUp.mostrarMenuSignUpV();

        try {
         ta.peVerifica(this.menuSignUp.getNome(), this.menuSignUp.getGPS());
        } catch (exPEAlreadyExists e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        try {
           ta.verificaValoresDOUBLE(this.menuSignUp.getRaio());
        } catch (exNegativeValues e) {
          this.menuSignUp.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        String nomeV = this.menuSignUp.getNome();
        String codigoV = nomeV.replaceAll(" ", "");
        Ponto gpsv = this.menuSignUp.getGPS();
        double raioV = this.menuSignUp.getRaio();
        boolean transporteMedicamentosV = this.menuSignUp.getTransporteMedicamentos();
        List<Integer> classificacoesV = new ArrayList<>();
        Map<String, Encomenda> encomendasGuardadasV = new HashMap<>();
        Voluntarios v = new Voluntarios(codigoV, "TrazAqui", nomeV, gpsv, raioV, transporteMedicamentosV, true, classificacoesV, encomendasGuardadasV);
        ta.addPlataforma(v);

        flagSignUp = false;
        menuSignUp.clearScreen();
        logger.log(Level.WARNING, "Registado com sucesso!!\nA redireciona-lo para página inicial...\n\n\n");
        startInicial();

        flagB = false;
      } else if (this.menuSignUp.getOpcao() == 5) {
        logger.log(Level.WARNING, CONST8);
      } else {
        menuInicial.clearScreen();
        logger.log(Level.WARNING, CONST1);
        startInicial();


      }
    }

    if (flagB) {
      this.menuLogIn.clearScreen();
      logger.log(Level.WARNING,CONST2);
      startInicial();
    }
  }

  // Método que lê a opção pretendida do menu de login podendo fazer login com qualquer um dos tipos de utilizadores da plataforma
  public void startLogIn() {
    boolean flagB = true;
    boolean flagLogIn = true;
    this.menuLogIn.clearScreen();
    this.menuLogIn.setOpcao(0);
    String codigo = "";
    String email = "";
    String password = "";

    while (this.menuLogIn.getOpcao() != 5 && flagLogIn) {
      this.menuLogIn.escolhaMenuGERAL();

      // Voltar atrás
      if (this.menuLogIn.getOpcao() == 1) { // Utilizador
        this.menuLogIn.clearScreen();
        this.menuLogIn.escolhaMenuLogIn();

        if (this.menuLogIn.getOpcao() == 1) { // Email
          menuLogIn.clearScreen();
          menuLogIn.mostrarMenuLogInE();

          try {
            email = this.menuLogIn.getEmail();
            ta.emailLogIn(email);
          } catch (exEmailDoesNotExist e) {
            this.menuSignUp.clearScreen();
            logger.log(Level.WARNING, e.getMessage());
            logger.log(Level.WARNING, CONST2);
            startInicial();
            return;
          }

          try {
            password = this.menuLogIn.getPassword();
            ta.pwVerificaE(email, password);
          } catch (exPWIncorrect e) {
            this.menuLogIn.clearScreen();
            logger.log(Level.WARNING, e.getMessage());
            logger.log(Level.WARNING, CONST2);
            startInicial();
            return;
          }

          Utilizadores u = ta.getUtilizador(email);
          this.codigoU = u.getCodigo();

          flagLogIn = false;
          this.menuLogIn.clearScreen();
          startCliente();
        }

        if (this.menuLogIn.getOpcao() == 2) { // Código
          try {
            this.menuLogIn.clearScreen();
            this.menuLogIn.mostrarMenuLogInC();
            codigo = this.menuLogIn.getCodigo();
            ta.codigoLogIn(codigo, 1);
          } catch (exCodigoDoesNotExist e) {
            this.menuSignUp.clearScreen();
            logger.log(Level.WARNING, e.getMessage());
            logger.log(Level.WARNING, CONST2);
            startInicial();
            return;
          }

          try {
            password = this.menuLogIn.getPassword();
             ta.pwVerificaC(codigo, password, 1);
          } catch (exPWIncorrect e) {
            this.menuLogIn.clearScreen();
            logger.log(Level.WARNING, e.getMessage());
            logger.log(Level.WARNING, CONST2);
            startInicial();
            return;
          }

          this.codigoU = codigo;

          flagLogIn = false;
          this.menuLogIn.clearScreen();
          startCliente();
        }

        if (this.menuLogIn.getOpcao() == 3) {
          this.menuLogIn.clearScreen();
          logger.log(Level.WARNING, "A redireciona-lo para o menu de log in...\n\n\n");
          startLogIn();
        }

        flagB = false;
      } else if (this.menuLogIn.getOpcao() == 2 || this.menuLogIn.getOpcao() == 3 || this.menuLogIn.getOpcao() == 4) { // Loja|Utilizadores|Voluntários
        int opcaoAUX = this.menuLogIn.getOpcao();


        try {
          this.menuLogIn.clearScreen();
          this.menuLogIn.mostrarMenuLogInExtra();

          codigo = this.menuLogIn.getCodigo();
          if (opcaoAUX == 2)
             ta.codigoLogIn(codigo, 2);
          if (opcaoAUX == 3)
             ta.codigoLogIn(codigo, 3);
          if (opcaoAUX == 4)
             ta.codigoLogIn(codigo, 4);
        } catch (exCodigoDoesNotExist e) {
          this.menuLogIn.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        try {
          password = this.menuLogIn.getPassword();
          if (opcaoAUX == 2)
             ta.pwVerificaC(codigo, password, 2);
          if (opcaoAUX == 3)
           ta.pwVerificaC(codigo, password, 3);
          if (opcaoAUX == 4)
             ta.pwVerificaC(codigo, password, 4);
        } catch (exPWIncorrect e) {
          this.menuLogIn.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, CONST2);
          startInicial();
          return;
        }

        flagLogIn = false;
        this.codigoU = codigo;
        this.menuLogIn.clearScreen();

        if (opcaoAUX == 2)
          startLoja();
        if (opcaoAUX == 3)
          startTransportadora();
        if (opcaoAUX == 4)
          startVoluntarios();

        flagB = false;
      } else if (this.menuLogIn.getOpcao() == 5) {
        return;
      } else {
        menuLogIn.clearScreen();
        logger.log(Level.WARNING, CONST1);

      }
    }

    if (flagB) {
      this.menuLogIn.clearScreen();
      logger.log(Level.WARNING,CONST2);
      startInicial();
    }
  }


  // Método que lê a opção pretendida do menu de loja podendo verificar o número de pessoas na fila, ver as encomendas feitas ate à data ou periodo de tempo
  // e acesso as defeniçoes de uma transportadora
  public void startLoja() {
    boolean flagB = true;
    Lojas l = ta.getLoja(codigoU);
    this.menuPlataformas.setOpcao(0);

    while (this.menuPlataformas.getOpcao() != 5) {
      logger.log(Level.WARNING,"\n\t"+ l.getNome() +"\n\n");
      this.menuPlataformas.menuLoja();

      // Log out
      if (this.menuPlataformas.getOpcao() == 1) { // Número de pessoas na fila
        this.menuPlataformas.clearScreen();

        int fila = l.getNumeroEncomendas();
        if (fila == 1)
          logger.log(Level.WARNING, "Neste momento está " + fila + " pessoa na fila.\n");
        else
          logger.log(Level.WARNING, "Neste momento estão " + fila + " pessoas na fila.\n");

        logger.log(Level.WARNING, "\nInsira um caracter para voltar para o menu principal.\n");
        this.menuCliente.leString();
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A redireciona-lo para o menu principal...\n\n\n");


      } else if (this.menuPlataformas.getOpcao() == 2) { // Registo de encomendas até à data
        this.menuPlataformas.clearScreen();

        List<Encomenda> historicoL = l.historicoL();
        if (historicoL.isEmpty()) {
          logger.log(Level.WARNING, "Histórico de encomendas realizadas:\n\n");
          for (Encomenda e : historicoL)
            logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
        } else
          logger.log(Level.WARNING, "Ainda não realizou qualquer encomenda.");

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
         this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 3) { // Registo de encomendas num certo período de tempo
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataI();
        LocalDateTime dataI = this.menuPlataformas.getDataI();

        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataF();
        LocalDateTime dataF = this.menuPlataformas.getDataF();

        List<Encomenda> historicoLD = l.historicoLdata(dataI, dataF);
        this.menuPlataformas.clearScreen();
        if (!historicoLD.isEmpty()) {
          logger.log(Level.WARNING, "Histórico de encomendas realizadas:\n");
          logger.log(Level.WARNING, "\tDe " + dataI + " até " + dataF + "\n\n");
          for (Encomenda e : historicoLD)
            logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
        } else
          logger.log(Level.WARNING, "Ainda não realizou qualquer encomenda.");

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 4) { // Alterar password
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarPW();

        String pwN = this.menuPlataformas.getPW();
        l.setPW(pwN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Password modificada com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 5) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, CONST1);

      }
    }

    if (flagB) {
      this.menuPlataformas.clearScreen();
      logger.log(Level.WARNING,"Sessão encerrada com sucesso.\n\n\n");
      startInicial();
    }
  }

  // Método que lê a opção pretendida do menu de transportadora podendo verificar as encomendas feitas ou total faturado ate à data ou periodo de tempo,
  // classificações da empresa e ainda aceder às definições
  public void startTransportadora() {
    boolean flagB = true;
    PlataformaEntrega pe = ta.getPlataformaEntrega(codigoU);
    Transportadoras t = (Transportadoras) pe;
    this.menuPlataformas.setOpcao(0);

    while (this.menuPlataformas.getOpcao() != 7) {
      logger.log(Level.WARNING,"\n\t"+ t.getNome() +"\n\n");
      this.menuPlataformas.menuTransportadoras();

      // Log out
      if (this.menuPlataformas.getOpcao() == 1) { // Faturado até à data
        this.menuPlataformas.clearScreen();

        double faturadoT = ta.totalDinheiro(codigoU);
        logger.log(Level.WARNING, "O total faturado é de " + faturadoT + ".\n");

        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 2) { // Faturado certo período de tempo
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataI();
        LocalDateTime dataI = this.menuPlataformas.getDataI();

        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataF();
        LocalDateTime dataF = this.menuPlataformas.getDataF();

        if (dataI.equals(dataF)) {
          this.menuPlataformas.clearScreen();
          logger.log(Level.WARNING, "No período de tempo escolhido, não houve qualquer ganho.\n");
        } else if (dataI.isBefore(dataF)) {
          this.menuPlataformas.clearScreen();
          double faturadoD = ta.totalDinheiroTempo(codigoU, dataI, dataF);
          logger.log(Level.WARNING, "O total faturado de " + dataI + " até " + dataF + " é de " + faturadoD + ".\n");
        } else {
          this.menuPlataformas.clearScreen();
          double faturadoD = ta.totalDinheiroTempo(codigoU, dataF, dataI);
          logger.log(Level.WARNING, "O total faturado de " + dataF + " até " + dataI + " é de " + faturadoD + ".\n");
        }

        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 3) { // Classificação atual
        this.menuPlataformas.clearScreen();

        logger.log(Level.WARNING, "A classificação atual da sua empresa é de " + t.mediaClassificacao(t.getClassificacoes()) + ".\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 4) { // Registo de entregas realizadas até à data
        this.menuPlataformas.clearScreen();

        List<Encomenda> historicoT = t.historicoPE();
        if (!historicoT.isEmpty()) {
          logger.log(Level.WARNING, "Histórico de entregas realizadas:\n\n");
          for (Encomenda e : historicoT)
            logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + "\nCódigo da loja: " + e.getCodLoja() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
        } else
          logger.log(Level.WARNING, "Ainda não realizou qualquer entrega.");

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 5) { // Registo de entregas realizadas num certo período
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataI();
        LocalDateTime dataIe = this.menuPlataformas.getDataI();

        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataF();
        LocalDateTime dataFe = this.menuPlataformas.getDataF();

        List<Encomenda> historicoPEd = t.historicoPEdata(dataIe, dataFe);
        this.menuPlataformas.clearScreen();
        if (!historicoPEd.isEmpty()) {
          logger.log(Level.WARNING, "Histórico de encomendas realizadas:\n");
          logger.log(Level.WARNING, "\tDe " + dataIe + " até " + dataFe + "\n\n");
          for (Encomenda e : historicoPEd)
            logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + "\nCódigo da loja: " + e.getCodLoja() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
        } else
          logger.log(Level.WARNING, "Ainda não realizou qualquer encomenda.");

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 6) { // Definições
        this.menuPlataformas.clearScreen();
        startDefinicoesT();

        flagB = false;
      } else if (this.menuPlataformas.getOpcao() == 7) {
      } else {
        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, CONST1);


      }
    }

    if (flagB) {
      this.menuPlataformas.clearScreen();
      logger.log(Level.WARNING,"Sessão encerrada com sucesso.\n\n\n");
      startInicial();
    }
  }

  // Método que lê a opção pretendida do menu de voluntário podendo consultar a sua clasificação, ver as encomendas feitas ate à data ou periodo de tempo
  // e ainda aceder às definições
  public void startVoluntarios() {
    boolean flagB = true;
    PlataformaEntrega pe = ta.getPlataformaEntrega(codigoU);
    Voluntarios v = (Voluntarios) pe;
    this.menuPlataformas.setOpcao(0);

    while (this.menuPlataformas.getOpcao() != 5) {
      logger.log(Level.WARNING,"\n\t"+ v.getNome() +"\n\n");
      this.menuPlataformas.menuVoluntarios();

      // Log out
      if (this.menuPlataformas.getOpcao() == 1) { // Classificação
        this.menuPlataformas.clearScreen();

        logger.log(Level.WARNING, "A sua classificação atual é de " + v.mediaClassificacao(v.getClassificacoes()) + ".\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 2) { // Registo das entregas até à data
        this.menuPlataformas.clearScreen();

        List<Encomenda> historicoV = v.historicoPE();
        if (historicoV.isEmpty()) {
          logger.log(Level.WARNING, "Histórico de entregas realizadas:\n\n");
          for (Encomenda e : historicoV)
            logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + "\nCódigo da loja: " + e.getCodLoja() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
        } else
          logger.log(Level.WARNING, "Ainda não realizou qualquer entrega.");

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 3) { // Registo das entregas num certo período de tempo
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataI();
        LocalDateTime dataI = this.menuPlataformas.getDataI();

        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuDataF();
        LocalDateTime dataF = this.menuPlataformas.getDataF();

        List<Encomenda> historicoVd = v.historicoPEdata(dataI, dataF);
        this.menuPlataformas.clearScreen();
        if (historicoVd.isEmpty()) {
          logger.log(Level.WARNING, "Histórico de entregas realizadas:\n");
          logger.log(Level.WARNING, "\tDe " + dataI + " até " + dataF + "\n\n");
          for (Encomenda e : historicoVd)
            logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + "\nCódigo da loja: " + e.getCodLoja() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
        } else
          logger.log(Level.WARNING, "Ainda não realizou qualquer entrega.");

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
        this.menuPlataformas.leString();
        this.menuPlataformas.clearScreen();


      } else if (this.menuPlataformas.getOpcao() == 4) { // Definições
        this.menuPlataformas.clearScreen();
        startDefinicoesV();

        flagB = false;
      } else if (this.menuPlataformas.getOpcao() == 5) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, CONST1);

      }
    }

    if (flagB) {
      this.menuPlataformas.clearScreen();
      logger.log(Level.WARNING,"Sessão encerrada com sucesso.\n\n\n");
      startInicial();
    }
  }

  // Método que lê a opção pretendida do menu de cliente podendo aceder ao menu de encomenda, definições de cliente e informações adicionais
  public void startCliente() {
    boolean flagB = isFlagB();

    if (flagB) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"Sessão encerrada com sucesso.\n\n\n");
      startInicial();
    }
  }

  private boolean isFlagB() {

    boolean flagB = true;
    Utilizadores u = ta.getUtilizador(codigoU);
    this.menuCliente.setOpcao(0);

    while (this.menuCliente.getOpcao() != 4) {
      logger.log(Level.WARNING,"\n\t"+ u.getNome() +"\n\n");
      this.menuCliente.escolhaMenu();

      // LogOut
      if (this.menuCliente.getOpcao() == 1) { // Encomenda
        this.menuCliente.clearScreen();
        startEncomenda();

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 2) { // Definiçoes
        this.menuCliente.clearScreen();
        startDefinicoesC();

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 3) { // Informações adicionais
        this.menuCliente.clearScreen();
        startInformacoesAdicionais();

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 4) {
      } else {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, CONST1);
      }
    }
    return flagB;
  }

  // Método que lê a opção pretendida do menu de encomenda podendo um cliente efetuar uma encomenda, ver o estado da mesma, pedir a entrega da encomenda
  // depois de a ter efetuado e classificar 
  public void startEncomenda() {
	   double start = System.nanoTime() ;
        double[] before = ec.getEnergyStats();

    boolean flagB = true;
    this.menuCliente.setOpcao(0);

    while (this.menuCliente.getOpcao() != 5) {
      this.menuCliente.menuEncomenda();

      // Voltar atrás
      if (this.menuCliente.getOpcao() == 1) { // Estado da encomenda
        this.menuCliente.clearScreen();
        this.menuCliente.menuEstadoEncomenda();

        if (this.menuCliente.getFlag()) {
          String codigoL = this.menuCliente.getCodigoL();
          ta.getLoja(codigoL);
          int tempoM = ta.tempoMedioAtendimento(codigoL);
          float custoEncomenda = ta.getEncomenda(codigoE).custoEncomendaTotal();
          logger.log(Level.WARNING, "\t" + tempoM + " minutos\n\nCusto da encomenda\n\t" + custoEncomenda + " €\n");
        }

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar atrás.\n");
        this.menuCliente.leString();
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");


      } else if (this.menuCliente.getOpcao() == 2) { // Inserir encomenda
        this.menuCliente.clearScreen();
        this.menuCliente.menuInserirEncomenda();

        String codigoEAUX = this.menuCliente.getCodigoE();
        Encomenda e = new Encomenda();

        try {
          e = ta.getEncomenda(codigoEAUX);
          ta.verificaEncomenda(codigoEAUX);
        } catch (exEncomendaDoesNotExist ex) {
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, ex.getMessage());
          logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");
          startEncomenda();
        }

        try {
          String lCodigo = e.getCodLoja();
          ta.verificaLoja(lCodigo);
        } catch (exLojaDoesNotExist ex) {
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, ex.getMessage());
          logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");
          startEncomenda();
        }

        if (e.getCodUtilizador().equals(codigoU)) {
          this.menuCliente.setFlag(true);
          this.codigoE = codigoEAUX;
          ta.encomendaAceite(e);
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, "Encomenda adicionada com sucesso!\n");
        } else {
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, "Inseriu uma encomenda inválida.\n");
        }

        logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");


      } else if (this.menuCliente.getOpcao() == 3) { // Pedir entrega
        this.menuCliente.clearScreen();
        startEntrega();

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 4) { // Classificar serviço entrega
        this.menuCliente.clearScreen();
        boolean flagRating = true;

        if (this.menuCliente.getFlagPE())
          logger.log(Level.WARNING, "Avalie o serviço de 1 a 10\n");

        while (flagRating) {
          flagRating = isFlagRating(flagRating);
        }

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "Obrigado pela avaliação!\n");
        logger.log(Level.WARNING, "A redireciona-lo para o menu cliente...\n\n\n");
        startCliente();

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 5) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, CONST1);


      }
    }
    double[] after = ec.getEnergyStats();

        double total = (double)(System.nanoTime()) - start;
        double tempo = (double)(total / 1000000000);
        System.out.println(tempo + " segundos");
        System.out.println("Info de Encomenda: \n");
        for(int i = 0; i < ec.socketNum; i++) {
                        System.out.println("Energy(joules): dram " + (after[0] - before[0])  + " cpu " + (after[1] - before[1]) + " package " + (after[2] - before[2]) );

                        System.out.println("Power: dram " + (after[0] - before[0])/tempo  + " cpu " + (after[1] - before[1])/tempo + " package " + (after[2] - before[2])/tempo);
        }

    if (flagB) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu cliente...\n\n\n");
      startCliente();
    }
  }

  private boolean isFlagRating(boolean flagRating) {
    if (this.menuCliente.getFlagPE()) {
      int rating = this.menuCliente.leInt();
      if (rating < 1 || rating > 10) {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING,"Por favor, introduza uma classificação entre 1 e 10.\n");
      }
      else {
        ta.classificarPlataformaEntrega(codigoPE, rating);
        flagRating = false;
      }
    }

    else {
      logger.log(Level.WARNING,"Ainda não pediu nenhum transporte para a sua encomenda.\n");
      logger.log(Level.WARNING,"\nInsira um caracter para voltar atrás.\n");
      this.menuCliente.leString();
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu de encomenda...\n\n\n");
      startEncomenda();
    }
    return flagRating;
  }

  // Método que lê a opção pretendida do menu de informações adicionais podendo um cliente ver o registo de encomenda até à data e entre datas 
  public void startInformacoesAdicionais() {
    this.menuCliente.setOpcao(0);
    boolean flagB = true;

    while (this.menuCliente.getOpcao() != 2) {
      this.menuCliente.menuInformacoesAdicionais();

      // Voltar atrás
      if (this.menuCliente.getOpcao() == 1) { // Registo de encomendas de voluntário/transportadora
        this.menuCliente.clearScreen();
        this.menuCliente.menuRegistoEscolha();

        int opcaoAUX = this.menuCliente.getOpcao();

        if (opcaoAUX == 1) {
          this.menuCliente.clearScreen();
          this.menuCliente.menuRegistoV();
          List<Encomenda> historicoE = getEncomendas();

          this.menuCliente.clearScreen();
          if (historicoE.isEmpty()) {
            logger.log(Level.WARNING, "Histórico de entregas realizadas:\n\n");
            for (Encomenda e : historicoE)
              logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + "\nCódigo da loja: " + e.getCodLoja() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
          } else
            logger.log(Level.WARNING, "Ainda não realizou qualquer entrega.");
        }

        if (opcaoAUX == 2) {
          this.menuCliente.clearScreen();
          this.menuCliente.menuRegistoV();
          String codigoRegisto = this.menuCliente.getCodigoRegisto();

          this.menuCliente.clearScreen();
          this.menuCliente.menuDataI();
          LocalDateTime dataI = this.menuCliente.getDataI();

          this.menuCliente.clearScreen();
          this.menuCliente.menuDataF();
          LocalDateTime dataF = this.menuCliente.getDataF();


          List<Encomenda> historicoE = new ArrayList<>();
          PlataformaEntrega pe = ta.getPlataformaEntrega(codigoRegisto);

          try {
            String codigoPEAUX = codigoRegisto;
            ta.verificaT(codigoPEAUX);
          } catch (exTransportadoraDoesNotExist e) {
            this.menuCliente.clearScreen();
            logger.log(Level.WARNING, "Código inválido.\n");
            logger.log(Level.WARNING, "A redireciona-lo para o menu de informações adicionais...\n\n\n");
            startInformacoesAdicionais();
          }

          if (voluntario.equals(pe.tipoPlataformaEntrega())) {
            Voluntarios v = (Voluntarios) pe;
            historicoE = v.historicoPEdata(dataI, dataF);
          } else {
            Transportadoras t = (Transportadoras) pe;
            historicoE = t.historicoPEdata(dataI, dataF);
          }

          this.menuCliente.clearScreen();
          if (!historicoE.isEmpty()) {
            logger.log(Level.WARNING, "Histórico de entregas realizadas:\n\n");
            for (Encomenda e : historicoE)
              logger.log(Level.WARNING, CONST3 + e.getCodEncomenda() + "\nCódigo da loja: " + e.getCodLoja() + CONST4 + e.getCodUtilizador() + CONST6 + e.getPeso() + CONST7 + e.getData() + CONST5 + e.numeroProdutos());
          } else
            logger.log(Level.WARNING, "Ainda não realizou qualquer entrega.");
        }

        if (opcaoAUX == 3) {
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, "A redireciona-lo para o menu principal...\n\n\n");
          startCliente();
        }

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu principal.\n");
        this.menuCliente.leString();
        this.menuCliente.clearScreen();


      } else if (this.menuCliente.getOpcao() == 2) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, CONST1);

      }
    }

    if (flagB) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu cliente...\n\n\n");
      startCliente();
    }
  }

  private List<Encomenda> getEncomendas() {

    String codigoRegisto = this.menuCliente.getCodigoRegisto();

    PlataformaEntrega pe = ta.getPlataformaEntrega(codigoRegisto);

    try {
      String codigoPEAUX = codigoRegisto;
      ta.verificaT(codigoPEAUX);
    } catch (exTransportadoraDoesNotExist e) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"Código inválido.\n");
      logger.log(Level.WARNING,"A redireciona-lo para o menu de informações adicionais...\n\n\n");
      startInformacoesAdicionais();
    }

    if (voluntario.equals(pe.tipoPlataformaEntrega())) {
      Voluntarios v = (Voluntarios) pe;
      return v.historicoPE();
    }
    else {
      Transportadoras t = (Transportadoras) pe;
      return t.historicoPE();
    }
  }

  // Método que lê a opção pretendida do menu de entrega podendo um cliente escolher a empresa, ver o estado da mesma, pedir a entrega da encomenda
  // depois de a ter efetuado e classificar 
  public void startEntrega() {
    this.menuCliente.setOpcao(0);
    boolean flagB = true;

    if (!this.menuCliente.getFlag()) {
      logger.log(Level.WARNING,"Ainda não inseriu nenhuma encomenda.\n");
      logger.log(Level.WARNING,"\nInsira um caracter para voltar atrás.\n");
      this.menuCliente.leString();
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu de encomenda...\n\n\n");
      startEncomenda();
    }

    while (this.menuCliente.getOpcao() != 3) {
      this.menuCliente.clearScreen();
      this.menuCliente.menuPedirEntrega();

      // Voltar atrás
      if (this.menuCliente.getOpcao() == 1) { // Transportadora
        this.menuCliente.clearScreen();

        Utilizadores u1 = ta.getUtilizador(codigoU);
        Encomenda e1 = ta.getEncomenda(codigoE);

        Transportadoras tC = new Transportadoras();
        Transportadoras tB = new Transportadoras();
        if (this.menuCliente.getFlagTM()) {
          tC = ta.recomendarClassificada(e1, true);
          tB = ta.recomendarBarata(e1, true);
        } else {
          tC = ta.recomendarClassificada(e1, false);
          tB = ta.recomendarBarata(e1, false);
        }

        if (tC == null || tB == null) {
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, "Não existem transportadoras disponíveis.\n");
          logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");
          startEncomenda();
        }

        logger.log(Level.WARNING, "Transportadoras disponíveis\n\n");
        List<Transportadoras> lT = ta.listPodeTransportarT(e1);
        for (Transportadoras t : lT) {
          if (!this.menuCliente.getFlagTM())
            logger.log(Level.WARNING, "Código: " + t.getCodigo() + "\nNome: " + t.getNome() + "\nTransporte de medicamentos: " + t.getTransporteMedicamentos() + "\nClassificação: " + t.mediaClassificacao(t.getClassificacoes()) + "\nPreço para a sua entrega: " + ta.custoTransporte(t.getCodigo(), e1) + "\n\n");
          else if (this.menuCliente.getFlagTM() && t.getTransporteMedicamentos())
            logger.log(Level.WARNING, "Código: " + t.getCodigo() + "\nNome: " + t.getNome() + "\nTransporte de medicamentos: " + t.getTransporteMedicamentos() + "\nClassificação: " + t.mediaClassificacao(t.getClassificacoes()) + "\nPreço para a sua entrega: " + ta.custoTransporte(t.getCodigo(), e1) + "\n\n");
        }

        if (tB != null && tC != null) {
          logger.log(Level.WARNING, "Recomendações para si\n\tMais barata: " + tB.getCodigo() + "\n\tMais bem classificada: " + tC.getCodigo() + "\n\n");
        }

        this.menuCliente.menuEntregaT();

        try {
          String codigoPEAUX = this.menuCliente.getCodigoT();
          ta.verificaT(codigoPEAUX);
        } catch (exTransportadoraDoesNotExist e) {
          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, e.getMessage());
          logger.log(Level.WARNING, "A redireciona-lo para o menu de entrega...\n\n\n");
          this.menuCliente.setFlag(true);
          startEntrega();
        }

        this.codigoPE = this.menuCliente.getCodigoT();
        ta.iniciarEntregaT(codigoPE);
        ta.finalizarEntregaT(codigoPE, e1);
        u1.adicionarEncomenda(e1);
        ta.addEncomendaAceite(e1);
        ta.encomendaFinalizada(e1);

        int tempoT = ta.tempoTransporteLoja(codigoPE, e1) + ta.tempoTransporteUtilizador(codigoPE, e1);
        double custoTotal = ta.custoTransporte(codigoPE, e1);

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "Tempo estimado de entrega\n\t" + tempoT + " minutos\n");
        logger.log(Level.WARNING, "\nCusto total de entrega\n\t" + custoTotal + " €\n");
        logger.log(Level.WARNING, "\nObrigado pela sua escolha!!\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu de encomenda.\n");
        this.menuCliente.leString();

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");
        startEncomenda();

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 2) { // Voluntário
        this.menuCliente.clearScreen();

        Utilizadores u2 = ta.getUtilizador(codigoU);
        Encomenda e2 = ta.getEncomenda(codigoE);

        try {
          String codigoV = ta.escolhaVoluntario(e2);
          PlataformaEntrega pe = ta.getPlataformaEntrega(codigoV);
          Voluntarios v = (Voluntarios) pe;

          this.menuCliente.setFlag(false);
          this.menuCliente.setFlagPE(true);
          this.codigoPE = codigoV;
          ta.iniciarEntregaV(codigoV);
          ta.finalizarEntregaV(codigoV, e2);
          u2.adicionarEncomenda(e2);
          ta.addEncomendaAceite(e2);
          ta.encomendaFinalizada(e2);

          int tempoV = ta.tempoTransporteLoja(codigoV, e2) + ta.tempoTransporteUtilizador(codigoV, e2);
          logger.log(Level.WARNING, "Foi escolhido um voluntário para entregar a sua encomenda.\n\tNome: " + v.getNome() + "\n\tTempo estimado para a sua entrega: " + tempoV);
          logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu de encomenda.\n");
          this.menuCliente.leString();

          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");
          startEncomenda();
        } catch (exPENotAvailable ex) {
          logger.log(Level.WARNING, ex.getMessage());
          logger.log(Level.WARNING, "Insira um caracter para voltar ao menu de encomenda.\n");
          this.menuCliente.leString();

          this.menuCliente.clearScreen();
          logger.log(Level.WARNING, "A redireciona-lo para o menu de encomenda...\n\n\n");
          startEncomenda();
        }

        flagB = false;
      } else if (this.menuCliente.getOpcao() == 3) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A opcão que escolheu é inválida.\n");

      }
    }

    if (flagB) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu de encomenda...\n");
      startEntrega();
    }
  }

  // Método que lê a opção pretendida do menu das leaderboards podendo verificar quais os Top 10 clientes,
  // voluntários e transportadoras
  public void startClassificacoes() {
    boolean flagB = true;
    this.menuCliente.clearScreen();
    this.menuCliente.setOpcao(0);

    while (this.menuCliente.getOpcao() != 4) {
      this.menuCliente.menuClassificacoes();

      // Voltar atrás
      if (this.menuCliente.getOpcao() == 1) { // TOP 10 - Utilizadores
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "\tTOP 10 - Utilizadores\n\n");

        int i = 1;
        List<Utilizadores> lU = ta.ordenadoTop10U();

        if (lU.isEmpty())
          logger.log(Level.WARNING, "Nenhum utilizador efetuou qualquer encomenda.");

        for (Utilizadores u : lU) {
          if (u.getEncomendasGuardadas().size() == 1)
            logger.log(Level.WARNING, i + ". " + u.getNome() + " - " + u.getEncomendasGuardadas().size() + " encomenda\n");
          else
            logger.log(Level.WARNING, i + ". " + u.getNome() + " - " + u.getEncomendasGuardadas().size() + " encomendas\n");

          i++;
        }

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu das leaderboards.\n");
        this.menuCliente.leString();

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A redireciona-lo para o menu das leaderboards...\n\n\n");


      } else if (this.menuCliente.getOpcao() == 2) { // TOP 10 - Voluntários
        int i;
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "\tTOP 10 - Voluntários\n\n");

        i = 1;
        List<PlataformaEntrega> lPEv = ta.ordenadoTop10V();

        if (lPEv.isEmpty())
          logger.log(Level.WARNING, "Nenhum voluntário efetuou qualquer entrega.");

        for (PlataformaEntrega pe : lPEv) {
          if (pe.getEncomendasGuardadas().size() == 1)
            logger.log(Level.WARNING, i + ". " + pe.getNome() + " - " + pe.getEncomendasGuardadas().size() + " entrega\n");
          else
            logger.log(Level.WARNING, i + ". " + pe.getNome() + " - " + pe.getEncomendasGuardadas().size() + " entregas\n");

          i++;
        }

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu das leaderboards.\n");
        this.menuCliente.leString();

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A redireciona-lo para o menu das leaderboards...\n\n\n");


      } else if (this.menuCliente.getOpcao() == 3) { // TOP 10 - Transportadoras
        int i;
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "\tTOP 10 - Transportadoras\n\n");

        i = 1;
        List<PlataformaEntrega> lPEt = ta.ordenadoTop10T();

        if (lPEt.isEmpty())
          logger.log(Level.WARNING, "Nenhuma transportadora efetuou qualquer entrega.");

        for (PlataformaEntrega pe : lPEt) {
          if (pe.getEncomendasGuardadas().size() == 1)
            logger.log(Level.WARNING, i + ". " + pe.getNome() + " - " + pe.getEncomendasGuardadas().size() + " entrega\n");
          else
            logger.log(Level.WARNING, i + ". " + pe.getNome() + " - " + pe.getEncomendasGuardadas().size() + " entregas\n");

          i++;
        }

        logger.log(Level.WARNING, "\n\nInsira um caracter para voltar ao menu das leaderboards.\n");
        this.menuCliente.leString();

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A redireciona-lo para o menu das leaderboards...\n\n\n");


      } else if (this.menuCliente.getOpcao() == 4) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A opcão que escolheu é inválida.\n");

      }
    }

    if (flagB) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu de encomenda...\n");
      startInicial();
    }
  }

  // Método que lê a opção pretendida do menu de definições de cliente podendo verificar/alterar
  public void startDefinicoesC() {
    boolean flagB = true;
    Utilizadores u = ta.getUtilizador(codigoU);
    this.menuCliente.setOpcao(0);

    while (this.menuCliente.getOpcao() != 6) {
      this.menuCliente.clearScreen();
      this.menuCliente.menuDefinicoes();

      // Voltar atrás
      if (this.menuCliente.getOpcao() == 1) { // Localização atual
        this.menuCliente.clearScreen();
        String gpsA = ta.getUtilizador(codigoU).getGPS().toString();
        logger.log(Level.WARNING, "Localização definida: " + gpsA + "\n");

        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuCliente.getOpcao() == 2) { // Histórico de encomendas realizadas
        this.menuCliente.clearScreen();

        if (u.getEncomendasGuardadas().isEmpty())
          logger.log(Level.WARNING, "Ainda não realizou nenhuma encomenda.\n");
        else
          logger.log(Level.WARNING, "\tHistórico de encomendas:\n\n" + u.historicoEncomenda().toString());

        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuCliente.getOpcao() == 3) { // Alterar email
        this.menuCliente.clearScreen();
        this.menuCliente.menuAlterarEmail();

        String emailN = this.menuCliente.getEmail();
        u.setEmail(emailN);

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "Email modificado, para " + emailN + ", com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuCliente.getOpcao() == 4) { // Alterar password
        this.menuCliente.clearScreen();
        this.menuCliente.menuAlterarPW();

        String pwN = this.menuCliente.getPassword();
        u.setPW(pwN);

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "Password modificada com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuCliente.getOpcao() == 5) { // Alterar localização
        this.menuCliente.clearScreen();
        this.menuCliente.menuAlterarLocalizacao();

        Ponto gpsN = this.menuCliente.getGPS();
        u.setGPS(gpsN);

        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "Localização modificada, para " + gpsN.toString() + ", com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuCliente.getOpcao() == 6) {
        logger.log(Level.WARNING, CONST8);
      } else {
        this.menuCliente.clearScreen();
        logger.log(Level.WARNING, "A opcão que escolheu é inválida.\n");

      }
    }

    if (flagB) {
      this.menuCliente.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu principal...\n");
      startCliente();
    }
  }

  // Método que lê a opção pretendida do menu de definições de transportadora podendo verificar/alterar
  public void startDefinicoesT() {
    boolean flagB = true;
    PlataformaEntrega pe = ta.getPlataformaEntrega(codigoU);
    Transportadoras t = (Transportadoras) pe;
    this.menuPlataformas.setOpcao(0);

    while (this.menuPlataformas.getOpcao() != 6) {
      this.menuPlataformas.clearScreen();
      this.menuPlataformas.menuTransportadorasDefinicoes();

      // Voltar atrás
      if (this.menuPlataformas.getOpcao() == 1) { // Alterar password
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarPW();

        String pwN = this.menuPlataformas.getPW();
        t.setPW(pwN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Password modificada com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuPlataformas.leString();


      } else if (this.menuPlataformas.getOpcao() == 2) { // Alterar raio
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarRaio();

        double raioN = this.menuPlataformas.getRaio();
        t.setRaio(raioN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Raio modificado, para " + raioN + ", com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuPlataformas.getOpcao() == 3) { // Alterar capacidade da empresa
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarCapacidade();

        int capacidadeN = this.menuPlataformas.getCapacidade();
        t.setCapacidade(capacidadeN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Capacidade modificada, para " + capacidadeN + ", com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuPlataformas.getOpcao() == 4) { // Alterar preço por KM
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarPrecoPorKM();

        double precoPorKmN = this.menuPlataformas.getPrecoPorKM();
        t.setPrecoPorKM(precoPorKmN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Preço por KM modificado, para " + precoPorKmN + ", com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuPlataformas.getOpcao() == 5) { // Validar transporte de medicamentos
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuValidarTM();

        boolean tmN = this.menuPlataformas.getTM();
        t.setTransporteMedicamentos(tmN);

        this.menuPlataformas.clearScreen();
        if (tmN)
          logger.log(Level.WARNING, "Transporte de medicamentos ativado.\n");
        else
          logger.log(Level.WARNING, "Transporte de medicamentos desativado.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuPlataformas.leString();


      } else if (this.menuPlataformas.getOpcao() == 6) {
      } else {
        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "A opção que escolheu é inválida.\n");

      }
    }

    if (flagB) {
      this.menuPlataformas.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu principal...\n");
      startTransportadora();
    }
  }

   // Método que lê a opção pretendida do menu de definições de transportadora podendo verificar/alterar
  public void startDefinicoesV() {
    boolean flagB = true;
    PlataformaEntrega pe = ta.getPlataformaEntrega(codigoU);
    Voluntarios v = (Voluntarios) pe;
    this.menuPlataformas.setOpcao(0);

    while (this.menuPlataformas.getOpcao() != 4) {
      this.menuPlataformas.clearScreen();
      this.menuPlataformas.menuVoluntariosDefinicoes();

      // Voltar atrás
      if (this.menuPlataformas.getOpcao() == 1) { // Alterar password
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarPW();

        String pwN = this.menuPlataformas.getPW();
        v.setPW(pwN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Password modificada com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuPlataformas.leString();


      } else if (this.menuPlataformas.getOpcao() == 2) { // Alterar raio
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuAlterarRaio();

        double raioN = this.menuPlataformas.getRaio();
        v.setRaio(raioN);

        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "Raio modificado, para " + raioN + ", com sucesso.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      } else if (this.menuPlataformas.getOpcao() == 3) { // Validar o transporte de medicamentos
        this.menuPlataformas.clearScreen();
        this.menuPlataformas.menuValidarTM();

        boolean tmN = this.menuPlataformas.getTM();
        v.setTransporteMedicamentos(tmN);

        this.menuPlataformas.clearScreen();
        if (tmN)
          logger.log(Level.WARNING, "Transporte de medicamentos ativado.\n");
        else
          logger.log(Level.WARNING, "Transporte de medicamentos desativado.\n");
        logger.log(Level.WARNING, "\nInsira um caracter para voltar ao menu das definições.\n");
        this.menuCliente.leString();


      }
      else {
        this.menuPlataformas.clearScreen();
        logger.log(Level.WARNING, "A opção que escolheu é inválida.\n");

      }
    }

    if (flagB) {
      this.menuPlataformas.clearScreen();
      logger.log(Level.WARNING,"A redireciona-lo para o menu principal...\n");
      startVoluntarios();
    }
  }
}
