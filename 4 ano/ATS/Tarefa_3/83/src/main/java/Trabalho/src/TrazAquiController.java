import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TrazAquiController implements Serializable {
    private BDGeral bd;
    private TrazAquiView view;

    /**
     * Método que define a view que o controlador irá conhecer.
     * @param view
     */

    public void setView(TrazAquiView view){this.view = view;}

    /**
     * Método que define o model, que , neste caso, é a BDGeral, que contém todos os users do sistema.
     * @param bd
     */

    public void setBd(BDGeral bd){
        this.bd = bd;
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Método que define se é para carregar os ficheiros a partir de um ficheiro txt ou de um ficheiro binário.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */

    public BDGeral readFlow() throws IOException, ClassNotFoundException {
        int op;
        Input input = new Input();
        clearScreen();
        this.view.loadMenu();
        do {
            op = input.lerInt();
            switch (op) {
                case 1:
                    Parse ler = new Parse();
                    ler.parse();
                    BDGeral base = new BDGeral(ler.getBaseGeral());
                    System.out.println("DONE!");
                    return base;
                case 2:
                    System.out.println("Insira o ficheiro a ler");
                    String aux = Input.lerString();
                    this.bd = new BDGeral();
                    try {
                        BDGeral base2 = this.bd.lerFicheiro(aux);
                        System.out.println("DONE!");
                        return base2;
                    } catch (IOException | ClassNotFoundException e){
                        System.out.println("\nFicheiro inválido\n");
                        this.view.loadMenu();
                    }
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        } while (true);
    }

    /**
     * Método que controla a execucção de cada uma das opções do menu inicial.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */

    public void mainFlow() throws IOException, ClassNotFoundException, InterruptedException{
        int op;
        Input input = new Input();
        this.view.printHeader();
        this.view.showMenuInicial();
        do{
            op = input.lerInt();
            switch(op){
                case 0:
                    break;
                case 1:
                    clearScreen();
                    registosFlow();
                    break;
                case 2:
                    clearScreen();
                    loginFlow();
                    break;
                case 3:
                    clearScreen();
                    System.out.println("Insira o nome com que quer gravar");
                    String l = Input.lerString();
                    this.bd.gravarFicheiro(l);
                    System.out.println("DONE");
                    System.out.println("Insira 5 para voltar a imprimir o menu");
                    break;
                case 4:
                    System.out.println("Insira o ficheiro a ler");
                    String aux = Input.lerString();
                    this.bd = new BDGeral();
                    try {
                        BDGeral base2 = this.bd.lerFicheiro(aux);
                        this.bd = new BDGeral(base2);
                        System.out.println("DONE!");
                    } catch (IOException | ClassNotFoundException e){
                        System.out.println("\nFicheiro inválido\n");
                        this.view.loadMenu();
                    }
                    System.out.println("Insira 5 para voltar a imprimir o menu");
                    break;
                case 5:
                    clearScreen();
                    this.view.showMenuInicial();
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (op != 0);
        System.out.println("\nPRETENDE GRAVAR O ÚLTIMO ESTADO DA APLICAÇÃO?");
        System.out.println("NÃO -> 0 || SIM -> 1");
        op = input.lerInt();
        if(op == 1){
            this.bd.gravarFicheiro("update");
            System.out.println("O estado foi gravado no ficheiro update");
        }
    }

    /** Método utilizado para realizar os registos de novos users do sistema. */

    private void registosFlow(){
        int op;
        Random random = new Random();
        Input input = new Input();
        clearScreen();
        String password = "", nome = "", email = "", local = "";
        Double latitude = 0.0, longitude = 0.0, raio = 0.0 ,tempo_espera = 0.0, custo = 0.0;
        String codigo = "";
        int nif = 0, nrMinimo = 0, velocidade = 0;
        boolean medico = false;
        this.view.showMenuRegisto();
        do {
            op = input.lerInt();
            switch (op) {
                case 0:
                    clearScreen();
                    this.view.showMenuInicial();
                    return;
                case 1:
                    try {
                        clearScreen();
                        this.view.headRegistoUser();
                        this.view.preenchimentoObrg();
                        this.view.email();
                        email = input.lerString();
                        while (this.bd.getUtilizadores().existeEmail(email)){
                            System.out.println("Já existe esse email, selecione um email diferente");
                            email = input.lerString();
                        }
                        this.view.password();
                        password = input.lerString();
                        this.view.nome();
                        nome = input.lerString();
                        this.view.latitude();
                        latitude = input.lerDouble();
                        this.view.longitude();
                        longitude = input.lerDouble();
                        do {
							codigo = "u" + random.nextInt(99);
						} while (this.bd.getUtilizadores().existeCodigo(codigo));
                    } catch (InputMismatchException e) {
                        input.lerString();
                        System.out.println("Input inválido");
                    }
                    bd.addUser(new Utilizador(email, password, codigo, nome, latitude, longitude, new ArrayList<>()).clone());
                    System.out.println("REGISTO EFETUADO COM SUCESSO");
                    System.out.println("PRIMA 0 PARA VOLTAR AO MENU INICIAL");
                    break;
                case 2:
                    try {
                        clearScreen();
                        this.view.headRegistoVoluntario();
                        this.view.preenchimentoObrg();
                        this.view.email();
                        email = input.lerString();
                        while (this.bd.getVoluntarios().existeEmail(email)){
                            System.out.println("Já existe esse email, selecione um email diferente");
                            email = input.lerString();
                        }
                        this.view.password();
                        password = input.lerString();
                        this.view.nome();
                        nome = input.lerString();
                        this.view.latitude();
                        latitude = input.lerDouble();
                        this.view.longitude();
                        longitude = input.lerDouble();
                        this.view.raio_acao();
                        raio = input.lerDouble();
                        codigo = "v" + random.nextInt(99);
                        this.view.encomendas_medicas();
                        medico = input.lerBoolean();
                        while (bd.getVoluntarios().existeCodigo(codigo)) {
							codigo = "v" + random.nextInt(99);
						}
                        System.out.println("Insira a velocidade a que se costuma deslocar");
                        velocidade = input.lerInt();
                    } catch (InputMismatchException e) {
                        input.lerString();
                        System.out.println("Input inválido");
                    }
                    bd.addVoluntario(new Voluntario(email, password, nome, codigo, true, latitude, longitude, LocalDate.now(), raio, new ArrayList<>(), 0, 0, medico, 0,0).clone());
                    System.out.println("REGISTO EFETUADO COM SUCESSO");
                    System.out.println("PRIMA 0 PARA VOLTAR AO MENU INICIAL");
                    break;
                case 3:
                    try {
                        clearScreen();
                        this.view.headRegistoLoja();
                        this.view.preenchimentoObrg();
                        this.view.email();
                        email = input.lerString();
                        while (this.bd.getLojas().existeEmail(email)){
                            System.out.println("Já existe esse email, selecione um email diferente");
                            email = input.lerString();
                        }
                        this.view.password();
                        password = input.lerString();
                        this.view.nome();
                        nome = input.lerString();
                        this.view.latitude();
                        latitude = input.lerDouble();
                        this.view.longitude();
                        longitude = input.lerDouble();
                        this.view.tempo_espera();
                        tempo_espera = input.lerDouble();
                        do {
							codigo = "l" + random.nextInt(99);
						} while (bd.getLojas().existeCodigo(codigo));
                    } catch (InputMismatchException e) {
                        input.lerString();
                        System.out.println("Input inválido");
                    }
                    bd.addLoja(new Loja(email, password, codigo, nome, tempo_espera, latitude, longitude, new ArrayList<>(), 0).clone());
                    System.out.println("REGISTO EFETUADO COM SUCESSO");
                    System.out.println("PRIMA 0 PARA VOLTAR AO MENU INICIAL");
                    break;
                case 4:
                    try {
                        clearScreen();
                        this.view.headRegistoEmpresa();
                        this.view.preenchimentoObrg();
                        this.view.email();
                        email = input.lerString();
                        while (this.bd.getTransportes().existeEmail(email)){
                            System.out.println("Já existe esse email, selecione um email diferente");
                            email = input.lerString();
                        }
                        this.view.password();
                        password = input.lerString();
                        this.view.nome();
                        nome = input.lerString();
                        this.view.custo_km();
                        custo = input.lerDouble();
                        this.view.latitude();
                        latitude = input.lerDouble();
                        this.view.longitude();
                        longitude = input.lerDouble();
                        this.view.local();
                        local = input.lerString();
                        this.view.raio_acao();
                        raio = input.lerDouble();
                        this.view.nif();
                        nif = input.lerInt();
                        this.view.encomendas_medicas();
                        medico = input.lerBoolean();
                        System.out.println("Insira a velocidade a que se costuma deslocar");
                        velocidade = input.lerInt();
                        do {
							codigo = "t" + random.nextInt(99);
						} while (bd.getTransportes().existeCodigo(codigo));
                    }
                    catch(InputMismatchException e){
                        input.lerString();
                        System.out.println("Input inválido");
                    }
                    bd.addTransporte(new EmpresaTransportes(email, password, codigo, nome, nif,custo,local,latitude,longitude,raio, new ArrayList<>(), medico, 0, 0, false,0 , velocidade).clone());
                    System.out.println("REGISTO EFETUADO COM SUCESSO");
                    System.out.println("PRIMA 0 PARA VOLTAR AO MENU INICIAL");
                    break;
                default:
                    System.out.println("Opcao inválida");
                    break;
            }
        }
        while (op != 0);
    }

    /**
     * Método que controla todas as ações do user, tendo este já efetuado o login no sistema.
     * @throws InterruptedException
     */

    private void loginFlow() throws InterruptedException{
        int op;
        Input input = new Input();
        clearScreen();
        this.view.showMenuLogin();
        do {
            op = input.lerInt();
            switch (op) {
                case 0:
                    clearScreen();
                    this.view.showMenuInicial();
                    return;
                case 1:
                    userFlow();
                    break;
                case 2:
                    voluntarioFlow();
                    break;
                case 3:
                    lojaFlow();
                    break;
                case 4:
                    empresaFlow();
                    break;
                default:
                    System.out.println("Opcao inválida");
                    break;
            }
        }
        while (op != 0);
    }

    /**
     * Método que controla as opções de um utilizador do tipo Empresa de Trnasportes.
     */

    private void empresaFlow(){
        int op;
        Input input = new Input();
        clearScreen();
        this.view.headLoginEmpresa();
        this.view.email();
        String email = input.lerString();
        this.view.password();
        String password = input.lerString();
        try{
            EmpresaTransportes et = bd.loginEmpresa(email, password).clone();
            clearScreen();
            this.view.showMenuTransportes();
            System.out.println("\n-> Tem " + et.porLevantar() + " encomendas por levantar.");
            System.out.println("\n-> Tem " + et.porEntregar() + " encomendas prontas a entregar\n");
            System.out.print("\nInsira uma opção --> ");
            do{
                op = input.lerInt();
                switch (op){
                    case 0:
                        clearScreen();
                        this.view.showMenuLogin();
                        return;
                    case 1:
                        EmpresaTransportes aux = this.bd.getTransportes().getTransportes().get(et.getEmail()).clone();
                        aux.setDisponivel(true);
                        this.bd.addEmpresaDisponivel(aux);
                        System.out.println("Está disponível para levantar encomendas.");
                        System.out.println("Prima 11 para voltar ao menu");
                        break;
                    case 2:
                        EmpresaTransportes aux2 = this.bd.getTransportes().getTransportes().get(et.getEmail()).clone();
                        aux2.setDisponivel(false);
                        this.bd.addEmpresaDisponivel(aux2);
                        System.out.println("Está indisponível para levantar encomendas.");
                        System.out.println("Prima 9 para voltar ao menu");
                        break;
                    case 3:
                        EmpresaTransportes aux3 = this.bd.getTransportes().getTransportes().get(et.getEmail()).clone();
                        aux3.aceitaMedicamentos(true);
                        this.bd.addEmpresaDisponivel(aux3);
                        System.out.println("Está disponível para transportar encomendas médicas.");
                        System.out.println("Prima 11 para voltar ao menu");
                    case 4:
                        EmpresaTransportes aux4 = this.bd.getTransportes().getTransportes().get(et.getEmail()).clone();
                        aux4.aceitaMedicamentos(true);
                        this.bd.addEmpresaDisponivel(aux4);
                        System.out.println("Está indisponível para transportar encomendas médicas.");
                        System.out.println("Prima 11 para voltar ao menu");
                    case 5:
                        System.out.println(et.getRegistos());
                        System.out.println("Insira o código da encomenda para o qual quer calcular o custo");
                        String codCust = input.lerString();
                        try{
                            Encomenda encomendaCust = this.bd.getTransportes().getTransportes().get(et.getEmail()).getEncomenda(codCust);
                            Loja ljCust = this.bd.getLojas().getLojas().get(this.bd.getLojas().getEmail(encomendaCust.getCodigo_loja())).clone();
                            Utilizador uCust = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(encomendaCust.getCodigo_user())).clone();
                            int distCust1 = (int) DistanceCalculator.distance(et.getLatitude(), ljCust.getLatitude(), et.getLongitude(), ljCust.getLongitude());
                            int distCust2 = (int) DistanceCalculator.distance(uCust .getLatitude(), ljCust.getLatitude(), uCust.getLongitude(), ljCust.getLongitude());
                            int custoTotal = (int) ((distCust1 + distCust2) * et.getCusto_km() + encomendaCust.getPeso() * 0.2);
                            System.out.println("\nO lucro da encomenda " + codCust + " será de " + custoTotal + " u.m.");
                        } catch (LojaNotFoundException e) {
                            System.out.println("Loja inválida");
                        } catch (EncomendaNotFoundException e) {
                            System.out.println("Encomenda Inválida");
                        } catch (UserNotFoundException e){
                        }
                        System.out.println("Prima 11 para voltar ao menu");
                        break;
                    case 6:
                        EmpresaTransportes et2 = this.bd.getTransportes().getTransportes().get(et.getEmail()).clone();
                        et2.setDisponivel(false);
                        this.bd.addEmpresaDisponivel(et2);
                        String preparadas = this.bd.getTransportes().getTransportes().get(et.getEmail()).getPreparadas();
                        if("0".equals(preparadas)) {
							System.out.println("Ainda não existem encomendas preparadas");
						} else {
                            System.out.println(preparadas);
                            System.out.println("Indique o código da encomenda que levantou");
                            String cod = Input.lerString();
                            try {
                                Encomenda encomenda = this.bd.getTransportes().getTransportes().get(et.getEmail()).getEncomenda(cod);
                                Loja lj = this.bd.getLojas().getLojas().get(this.bd.getLojas().getEmail(encomenda.getCodigo_loja())).clone();
                                Utilizador u = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(encomenda.getCodigo_user())).clone();
                                int minutos = (int) (lj.getTempo_espera() + lj.getNrPessoasEmFila() * lj.getTempo_espera());
                                et.setMinutosDeEspera(minutos);
                                u.updateEncomendaLoja(encomenda);
                                this.bd.updateUser2(u);
                                Encomenda e = lj.getEnc(cod);
                                this.bd.updateLoja2(e, lj);
                                et.updateEncomendaLoja(encomenda);
                                this.bd.updateTransportes2(et);
                                System.out.println("Realizado com sucesso");
                            } catch (LojaNotFoundException e) {
                                System.out.println("Loja inválida");
                            } catch (EncomendaNotFoundException e) {
                                System.out.println("Encomenda Inválida");
                            } catch (UserNotFoundException e){
                            }
                        }
                        System.out.println("Prima 11 para voltar ao menu");
                        break;
                    case 7:
                        String naoEntregue = this.bd.getTransportes().getTransportes().get(et.getEmail()).getNaoEntregue();
                        if("0".equals(naoEntregue)){System.out.println("Não existem encomendas por entregar");}
                        else if("1".equals(naoEntregue)) {
							System.out.println("Existem mais de uma encomenda por entregar.\nPrima 8 para realizar todas as entregas");
						} else {
                            System.out.println(naoEntregue);
                            System.out.println("Indique a encomenda que acabou de ser entregue");
                            String cod2 = Input.lerString();
                            try {
                                Encomenda encomenda2 = this.bd.getTransportes().getTransportes().get(et.getEmail()).getEncomenda(cod2);
                                Loja lj = this.bd.getLojas().getLojas().get(this.bd.getLojas().getEmail(encomenda2.getCodigo_loja())).clone();
                                int totalTime = 0;
                                int distancia1 = (int) DistanceCalculator.distance(et.getLatitude(),lj.getLatitude(), et.getLongitude(), lj.getLongitude());
                                LocalDateTime entrega = LocalDateTime.now();
                                LocalDateTime emissao = encomenda2.getData();
                                Duration duration = Duration.between(entrega, emissao);
                                long diff = Math.abs(duration.toMinutes());
                                et.updateEncomenda(encomenda2);
                                String codUser = encomenda2.getCodigo_user();
                                try {
                                    String emailUser = this.bd.getUtilizadores().getEmail(codUser);
                                    Utilizador u = this.bd.getUtilizadores().getUsers().get(emailUser).clone().clone();
                                    int distancia2 = (int) DistanceCalculator.distance(u.getLatitude(),lj.getLatitude(), u.getLongitude(), lj.getLongitude());
                                    totalTime += ((distancia1 + distancia2) * 60) / et.getVelocidade();
                                    totalTime += et.getMinutosDeEspera();
                                    diff += totalTime;
                                    diff += et.calculaAtrasos();
                                    et.setMinutosDeEspera(0);
                                    et.updateEncomenda(encomenda2);
                                    u.updateEncomenda(encomenda2);
                                    this.bd.updateUser2(u);
                                    this.bd.updateTransportes2(et);
                                    System.out.println("A encomenda " + encomenda2.getCodigo() + " foi entregue ao utilizador " + encomenda2.getCodigo_user());
                                    System.out.println("Demorou " + diff + " minutos a ser entregue");
                                    System.out.println("Prima 11 para voltar ao menu");
                                } catch (UserNotFoundException e) {
                                    System.out.println("Utilizador não foi encontrado");
                                }
                            } catch (EncomendaNotFoundException e){
                                System.out.println("Código que encomenda inválido");
                            } catch (LojaNotFoundException e){
                                System.out.println("Loja inválida");
                            }
                        }
                        break;
                    case 8:
                        ArrayList<Encomenda> rota = et.getRota();
                        Encomenda inicial = rota.get(0);
                        int totalTime = 0;
                        int totalKms = 0;
                        if(rota.size() <= 1){
                            System.out.println("Não existem mais de 1 encomenda levantadas");
                        }
                        else {
                            try {
                                System.out.println("Serão entregues todas as encomendas que se encontram levantadas");
                                Utilizador u = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(inicial.getCodigo_user())).clone();
                                Loja lj = this.bd.getLojas().getLojas().get(this.bd.getLojas().getEmail(inicial.getCodigo_loja())).clone();
                                int distancia1 = (int) DistanceCalculator.distance(et.getLatitude(), lj.getLatitude(), et.getLongitude(), lj.getLongitude());
                                int distancia2 = (int) DistanceCalculator.distance(u.getLatitude(), lj.getLatitude(), u.getLongitude(), lj.getLongitude());
                                int distancia3 = et.distanciaEntreLojas(rota, this.bd);
                                LocalDateTime entrega = LocalDateTime.now();
                                LocalDateTime emissao = inicial.getData();
                                Duration duration = Duration.between(entrega, emissao);
                                long diff = Math.abs(duration.toMinutes());
                                totalTime += ((distancia1 + distancia2 + distancia3) * 60) / et.getVelocidade();
                                int atraso = et.calculaAtrasos();
                                totalTime += et.getMinutosDeEspera();
                                diff += totalTime;
                                diff += atraso;
                                totalKms += distancia1 + distancia2 + distancia3;
                                et.setMinutosDeEspera(0);
                                et.updateEncomenda(inicial);
                                u.updateEncomenda(inicial);
                                this.bd.updateUser2(u);
                                this.bd.updateTransportes2(et);
                                System.out.println("\nDemorou " + diff + " minutos a ser entregue ao user " + u.getCodigo());
                                System.out.println("Distância percorrida " + totalKms + " kms");
                                for (int i = 1; i < rota.size(); i++) {
                                    Encomenda e1 = rota.get(i-1);
                                    Utilizador u1 = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(e1.getCodigo_user())).clone();
                                    Encomenda e2 = rota.get(i);
                                    Utilizador u2 = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(e2.getCodigo_user())).clone();
                                    int distancia = (int) DistanceCalculator.distance(u1.getLatitude(), u2.getLatitude(), u1.getLongitude(), u2.getLongitude());
                                    totalKms += distancia;
                                    totalTime += (distancia * 60) / et.getVelocidade();
                                    totalTime += atraso;
                                    et.updateEncomenda(e2);
                                    u2.updateEncomenda(e2);
                                    this.bd.updateUser2(u2);
                                    this.bd.updateTransportes2(et);
                                    diff += totalTime;
                                    System.out.println("\nDemorou " + diff + " minutos a ser entregue ao user " + u2.getCodigo());
                                    System.out.println("Distância percorrida " + totalKms + " kms\n");
                                }
                            } catch (UserNotFoundException e) {
                                System.out.println("Utilizador inválido");
                            } catch (LojaNotFoundException e) {
                                System.out.println("Loja inválida");
                            }
                        }
                        System.out.println("Prima 11 para voltar ao menu");
                        break;
                    case 9:
                        int size3 = this.bd.getTransportes().getTransportes().get(et.getEmail()).getRegistos().size();
                        if (size3 == 0) {
							System.out.println("Não tem pedidos de encomendas");
						} else {
                            System.out.println("Histórico de encomendas: ");
                            System.out.println(this.bd.getTransportes().getTransportes().get(et.getEmail()).getRegistos());
                        }
                        System.out.println("Prima 11 para voltar ao menu");
                        break;
                    case 10:
                        try {
                            System.out.println("Insira o intervalo temporal que pretende analisar (DD/MM/YYYY)");
                            System.out.println("Inicio: ");
                            Scanner sc = new Scanner(System.in);
                            String dataRecebida = sc.nextLine();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Date dateTime1 = df.parse(dataRecebida);
                            System.out.println("Fim: ");
                            String dataRecebida2 = sc.nextLine();
                            Date dateTime2 = df.parse(dataRecebida2);
                            LocalDateTime d1 = dateTime1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            LocalDateTime d2 = dateTime2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                            System.out.println(et.getFaturacao(d1, d2, this.bd.clone()));
                            System.out.println("Prima 11 para voltar ao menu");
                        }  catch (ParseException e){
                            System.out.println("Data inválida");
                            System.out.println("Insira 11 para retroceder");
                        }
                        break;
                    case 11:
                        clearScreen();
                        this.view.showMenuTransportes();
                        System.out.println("\n-> Tem " + et.porLevantar() + " encomendas por levantar.");
                        System.out.println("\n-> Tem " + et.porEntregar() + " encomendas prontas a entregar\n");
                        System.out.print("\nInsira uma opção --> ");
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } while (op != 0);
        } catch (TransporteNotFoundException e){
            clearScreen();
            System.out.println("Email ou password inválidos\n\n");
            System.out.println("Selecione nova opção de login\n");
            this.view.showMenuLogin();
        }
    }

    /** Método que controla as opções de um utilizador do tipo Voluntário. */

    private void voluntarioFlow() {
        int op;
        Input input = new Input();
        clearScreen();
        this.view.headLoginVoluntario();
        this.view.email();
        String email = input.lerString();
        this.view.password();
        String password = input.lerString();
        try {
            Voluntario v = bd.loginVoluntario(email, password).clone();
            clearScreen();
            this.view.showMenuVoluntario();
            System.out.println("\n-> Tem " + v.porLevantar() + " encomendas por levantar");
            System.out.println("\n-> Tem " + v.porEntregar() + " encomendas prontas a entregar\n");
            System.out.print("\nInsira uma opção --> ");
            do {
                op = input.lerInt();
                switch (op) {
                    case 0:
                        clearScreen();
                        this.view.showMenuLogin();
                        return;
                    case 1:
                        Voluntario aux = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).clone();
                        aux.setDisponibilidade(true);
                        this.bd.addVoluntarioDisponivel(aux);
                        System.out.println("Está disponível para levantar encomendas.");
                        System.out.println("Prima 8 para voltar ao menu");
                        break;
                    case 2:
                        Voluntario aux3 = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).clone();
                        aux3.setDisponibilidade(false);
                        this.bd.addVoluntarioDisponivel(aux3);
                        System.out.println("Está indisponível para levantar encomendas.");
                        System.out.println("Prima 8 para voltar ao menu");
                        break;
                    case 3:
                        Voluntario aux4 = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).clone();
                        aux4.aceitaMedicamentos(true);
                        this.bd.addVoluntarioDisponivel(aux4);
                        System.out.println("Está disponível para transportar encomendas médicas.");
                        System.out.println("Prima 8 para voltar ao menu");
                        break;
                    case 4:
                        Voluntario aux5 = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).clone();
                        aux5.aceitaMedicamentos(false);
                        this.bd.addVoluntarioDisponivel(aux5);
                        System.out.println("Está indisponível para transportar encomendas médicas.");
                        System.out.println("Prima 8 para voltar ao menu");
                        break;
                    case 5:
                        int size3 = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).getHistorico().size();
                        if (size3 == 0) {
							System.out.println("Não tem pedidos de encomendas");
						} else {
                           System.out.println("Histórico de encomendas: ");
                           System.out.println(this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).getHistorico());
                        }
                        System.out.println("Prima 8 para voltar ao menu");
                        break;
                    case 6:
                        Voluntario aux2 = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).clone();
                        aux2.setDisponibilidade(false);
                        this.bd.addVoluntarioDisponivel(aux2);
                        String preparadas = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).getPreparadas();
                        if("0".equals(preparadas)) {
							System.out.println("Ainda não existem encomendas preparadas");
						} else {
                            System.out.println(preparadas);
                            System.out.println("Indique o código da encomenda que levantou");
                            String cod = Input.lerString();
                            try {
                                Encomenda encomenda = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).getEncomenda(cod);
                                Utilizador u = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(encomenda.getCodigo_user())).clone();
                                Loja lj = this.bd.getLojas().getLojas().get(this.bd.getLojas().getEmail(encomenda.getCodigo_loja())).clone();
                                Encomenda e = lj.getEnc(cod);
                                int minutos = (int) (lj.getTempo_espera() + lj.getTempo_espera() * lj.getNrPessoasEmFila());
                                this.bd.updateLoja2(e, lj);
                                u.updateEncomendaLoja(encomenda);
                                v.updateEncomendaLoja(encomenda);
                                v.setMinutosDeEspera(minutos);
                                this.bd.updateVoluntario2(v);
                                this.bd.updateUser2(u);
                                System.out.println("Realizado com sucesso");
                            } catch (LojaNotFoundException e) {
                                System.out.println("Loja inválida");
                                System.out.println("Prima 8 para voltar ao menu");
                            } catch (EncomendaNotFoundException e) {
                                System.out.println("Encomenda Inválida");
                                System.out.println("Prima 8 para voltar ao menu");
                            } catch (UserNotFoundException e){
                            }
                        }
                        System.out.println("Prima 8 para voltar ao menu");
                        break;
                    case 7:
                        String naoEntregue = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).getNaoEntregue();
                        if("0".equals(naoEntregue)){System.out.println("Não existem encomendas por entregar");}
                        else {
                            System.out.println(naoEntregue);
                            System.out.println("Indique a encomenda que acabou de ser entregue");
                            String cod2 = Input.lerString();
                            try {
                                Encomenda encomenda2 = this.bd.getVoluntarios().getVoluntarios().get(v.getEmail()).getEncomenda(cod2).clone();
                                Loja lj = this.bd.getLojas().getLojas().get(this.bd.getLojas().getEmail(encomenda2.getCodigo_loja())).clone();
                                int totalTime = 0;
                                int distancia1 = (int) DistanceCalculator.distance(v.getLatitude(),lj.getLatitude(), v.getLongitude(), lj.getLongitude());
                                LocalDateTime entrega = LocalDateTime.now();
                                LocalDateTime emissao = encomenda2.getData();
                                Duration duration = Duration.between(entrega, emissao);
                                long diff = Math.abs(duration.toMinutes());
                                v.updateEncomenda(encomenda2);
                                String codUser = encomenda2.getCodigo_user();
                                try {
                                    String emailUser = this.bd.getUtilizadores().getEmail(codUser);
                                    Utilizador u = this.bd.getUtilizadores().getUsers().get(emailUser).clone();
                                    int distancia2 = (int) DistanceCalculator.distance(v.getLatitude(),lj.getLatitude(), v.getLongitude(), lj.getLongitude());
                                    totalTime += ((distancia1 + distancia2) * 60) / v.getVelocidade();
                                    totalTime += v.getMinutosDeEspera();
                                    diff += totalTime;
                                    diff += v.calculaAtrasos();
                                    v.setMinutosDeEspera(0);
                                    v.updateEncomenda(encomenda2);
                                    u.updateEncomenda(encomenda2);
                                    this.bd.updateUser2(u);
                                    this.bd.updateVoluntario2(v);
                                    System.out.println("A encomenda " + encomenda2.getCodigo() + " foi entregue ao utilizador " + encomenda2.getCodigo_user());
                                    System.out.println("Demorou " + diff + " minutos a ser entregue");
                                    System.out.println("Prima 8 para voltar ao menu");
                                } catch (UserNotFoundException e) {
                                    System.out.println("Utilizador não foi encontrado");
                                }
                            } catch (EncomendaNotFoundException e){
                                System.out.println("Código de encomenda inválido");
                                System.out.println("Insira 6 para retroceder");
                            } catch (LojaNotFoundException E){
                                System.out.println("Loja inválida");
                            }
                        }
                        break;
                    case 8:
                        clearScreen();
                        this.view.showMenuVoluntario();
                        System.out.println("\n-> Tem " + v.porLevantar() + " encomendas por levantar");
                        System.out.println("\n-> Tem " + v.porEntregar() + " encomendas prontas a entregar\n");
                        System.out.print("\nInsira uma opção --> ");
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } while (op != 0);
        } catch (VoluntarioNotFoundException e) {
            clearScreen();
            System.out.println("Email ou password inválidos\n\n");
            System.out.println("Selecione nova opção de login\n");
            this.view.showMenuLogin();
        }
    }

    /** Método que controla as opções de um utilizador do tipo Loja. */

    private void lojaFlow(){
        int op;
        Input input = new Input();
        clearScreen();
        this.view.headLoginLoja();
        this.view.email();
        String email = input.lerString();
        this.view.password();
        String password = input.lerString();
        try {
            Loja lj = bd.loginLoja(email, password).clone();
            clearScreen();
            this.view.showMenuLoja();
            System.out.print("\nInsira uma opção --> ");
            do {
                op = input.lerInt();
                switch (op) {
                    case 0:
                        clearScreen();
                        this.view.showMenuLogin();
                        return;
                    case 1:
                        String notReady = this.bd.getLojas().getLojas().get(lj.getEmail()).getEncNotReady();
                        if("0".equals(notReady)){
                            System.out.println("Não existem encomendas por preparar");
                        }
                        else {
                            System.out.println("Lista de encomendas por preparar: ");
                            System.out.println(notReady);
                            System.out.println("Indique o código da encomenda que está pronta a ser levantada");
                            String cod = input.lerString();
                            try {
                                Encomenda enc = this.bd.getLojas().getLojas().get(lj.getEmail()).getEnc(cod).clone();
                                Utilizador u = this.bd.getUtilizadores().getUsers().get(this.bd.getUtilizadores().getEmail(enc.getCodigo_user())).clone();
                                lj.updateEncomenda(enc);
                                if(this.bd.getVoluntarios().existeEnc(enc.getCodigo())) {
                                    this.bd.updateEncomendaVoluntario(enc);
                                } else {
                                    this.bd.updateEncomendaTransportes(enc);
                                }
								u.updateEncomendaPronta(enc);
								this.bd.updateUser2(u);
								this.bd.updateLoja3(lj);
                            } catch (EncomendaNotFoundException e) {
                                System.out.println("Código de encomenda inválido");
                            } catch (UserNotFoundException e){
                            }
                        }
                        System.out.println("Prima 4 para imprimir o menu");
                        break;
                    case 2:
                        if(this.bd.getLojas().getLojas().get(lj.getEmail()).getEncomendas_recebidas().isEmpty()){
                            System.out.println("Não existem encomendas por levantar");
                        } else {
							System.out.println(this.bd.getLojas().getLojas().get(lj.getEmail()).getEncomendas_recebidas());
						}
                        System.out.println("Prima 4 para imprimir o menu");
                        break;
                    case 3:
                        System.out.println("Insira quantas pessoas se encontram na fila");
                        int fila = input.lerInt();
                        lj.setNrPessoasEmFila(fila);
                        this.bd.updateLoja3(lj);
                        System.out.println("Prima 4 para retroceder");
                        break;
                    case 4:
                        clearScreen();
                        this.view.showMenuLoja();
                        System.out.print("\nInsira uma opção --> ");
                        break;
                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            } while (op != 0);
        } catch (LojaNotFoundException e) {
            clearScreen();
            System.out.println("Email ou password inválidos\n\n");
            System.out.println("Selecione nova opção de login\n");
            this.view.showMenuLogin();
        }
    }

    /** Método que controla as opções de um utilizador do Utilizador doméstico. */

    private void userFlow() throws InterruptedException{
        int op;
        Input input = new Input();
        String email, password, produto = "";

        clearScreen();
        this.view.headLoginUser();
        this.view.email();
        email = input.lerString();
        this.view.password();
        password = input.lerString();
        try {
            Utilizador u = bd.loginUser(email, password).clone();
            clearScreen();
            this.view.showMenuUser();
            System.out.print("\nInsira uma opção --> ");
                do {
                    op = input.lerInt();
                    switch (op) {
                        case 0:
                            clearScreen();
                            this.view.showMenuLogin();
                            return;
                        case 1:
                            int quantidade = 0;
                            double quantidadeTot = 0.0;
                            double custoTotal = 0.0;
                            produto = "";
                            String loja;
                            List<String> produtosSel = new ArrayList<>();
                            Map<String, LinhaEncomenda> produtos = new TreeMap<>();
                            this.view.headNovaEncomenda();
                            this.view.preenchimentoObrg();
                            System.out.println(this.bd.getLojas().listLojasUser(u));
                            System.out.println("Selecione o código de uma loja");
                            loja = input.lerString();
                            try {
                                String emailLoja = this.bd.getLojas().getEmail(loja);
                                System.out.println(bd.getProdutos().listProdutosNormais());
                                while (!"0".equals(produto)) {
                                    System.out.println("Insira 0 para concluir a seleção de produtos");
                                    this.view.produto();
                                    produto = input.lerString();
                                    produtosSel.add(produto);
                                    System.out.println("\n");
                                }
                                produtosSel.remove("0");
                                try {
                                    for (String s : produtosSel) {
                                        this.bd.getProdutos().existeProd(s);
                                        clearScreen();
                                        System.out.println("Insira a quantidade de " + s + " || Preço por unidade = " + bd.getProdutos().getProdutos().get(s).getPreco());
                                        quantidade = input.lerInt();
                                        LinhaEncomenda enc = new LinhaEncomenda(bd.getProdutos().getProdutos().get(s).getCodigo(), s, quantidade, bd.getProdutos().getProdutos().get(s).getPreco()).clone();
                                        produtos.put(s, enc);
                                        quantidadeTot += quantidade;
                                        custoTotal += bd.getProdutos().getProdutos().get(s).getPreco() * quantidade;
                                    }
                                    System.out.println("\nCusto total da encomenda: " + custoTotal);
                                    Loja j = this.bd.getLojas().getLojas().get(emailLoja).clone();
                                    Random random = new Random();
                                    int low = 1000;
                                    int high = 9999;
                                    int result = random.nextInt(high-low) + low;
                                    String cod = "e" + result;
                                    while(this.bd.getEncomendasAceites().getAceites().contains(cod)){
                                        result = random.nextInt(high-low) + low;
                                        cod = "e" + result;
                                    }
                                    Encomenda novaEnc = new Encomenda(cod, u.getCodigo(), loja, quantidadeTot, u.getNome(), this.bd.getLojas().getLojas().get(emailLoja).getNome(), produtos, false, LocalDateTime.now(), false, false, false);
                                    List<Voluntario> disponiveis = this.bd.getVoluntarios().voluntariosDisponíveis(j, u);

                                    if(disponiveis.isEmpty()){
                                        System.out.println("\nNão existem voluntários disponíveis perto da loja selecionada.");
                                        System.out.println("\nSerá necessário selecionar uma empresa transportadora, pagando pelos seus serviços.");
                                        String r = this.bd.getTransportes().printEmpresas(u, j, novaEnc.getPeso());
                                        if("0".equals(r)){
                                            System.out.println("\nNão existem empresas disponíveis...\n");
                                            System.out.println("A sua encomenda foi cancelada...\n");
                                            System.out.println("Prima 12 para voltar ao menu");
                                            r = "";
                                            break;
                                        }
                                        System.out.println(this.bd.getTransportes().printEmpresas(u, j, novaEnc.getPeso()));
                                        System.out.println("Selecione o código de uma empresa ou 0 para cancelar a encomenda");
                                        String opEmpresa = input.lerString();
                                        if("0".equals(opEmpresa)){
                                            System.out.println("\nA sua encomenda foi cancelada");
                                            System.out.println("\nPrima 12 para voltar ao menu");
                                            break;
                                        }
                                        else {
                                            try {
                                                u.addEncomenda(novaEnc);
                                                String emailEmpresa = this.bd.getTransportes().getEmail(opEmpresa);
                                                EmpresaTransportes et = this.bd.getTransportes().getTransportes().get(emailEmpresa).clone();
                                                et.addEncomenda(novaEnc);
                                                this.bd.updateTransportes2(et);
                                                this.bd.updateUser2(u);
                                                this.bd.updateLoja(novaEnc, this.bd.getLojas().getLojas().get(emailLoja));
                                                this.bd.updateAceites(novaEnc.getCodigo());
                                                System.out.println("ENCOMENDA FEITA COM SUCESSO. PRIMA 12 PARA SAIR");
                                            } catch (TransporteNotFoundException e){
                                                System.out.println("Empresa transportadora inválida");
                                            }
                                        }
                                    } else {
										if(disponiveis.size() == 1){
										    u.addEncomenda(novaEnc);
										    Voluntario v = disponiveis.get(0).clone();
										    System.out.println("A encomenda será entregue pelo voluntário --> " + v.getNome() + " --> " + v.getCodigo());
										    v.addEncomenda(novaEnc);
										    this.bd.updateVoluntario2(v);
										    this.bd.updateLoja(novaEnc, this.bd.getLojas().getLojas().get(emailLoja));
										    this.bd.updateAceites(novaEnc.getCodigo());
										    this.bd.updateUser2(u);
										}
										else {
										    u.addEncomenda(novaEnc);
										    int choice = random.nextInt(disponiveis.size() - 1);
										    Voluntario v = disponiveis.get(choice).clone();
										    System.out.println("A encomenda será entregue pelo voluntário --> " + v.getNome() + " --> " + v.getCodigo());
										    v.addEncomenda(novaEnc);
										    this.bd.updateVoluntario2(v);
										    this.bd.updateUser2(u);
										    this.bd.updateLoja(novaEnc, this.bd.getLojas().getLojas().get(emailLoja));
										    this.bd.updateAceites(novaEnc.getCodigo());
										}
										System.out.println("ENCOMENDA FEITA COM SUCESSO. PRIMA 12 PARA SAIR");
									}
                                } catch (ProductNotFoundException e) {
                                    System.out.println("Produto não existe");
                                    System.out.println("Prima 12 para retroceder");
                                }
                            } catch (LojaNotFoundException e){
                                System.out.println("Loja inválida");
                                System.out.println("Prima 12 para retroceder");
                            }
                            break;

                        case 2:
                            int quantidade2 = 0;
                            double quantidadeTot2 = 0.0;
                            double custoTotal2 = 0.0;
                            String loja2;
                            List<String> produtosSel2 = new ArrayList<>();
                            Map<String, LinhaEncomenda> produtos2 = new TreeMap<>();
                            String produto2 = "";
                            this.view.headNovaEncomenda();
                            this.view.preenchimentoObrg();
                            System.out.println(this.bd.getLojas().listLojasUser(u));
                            System.out.println("Selecione o código de uma loja");
                            loja2 = input.lerString();
                            try {
                                String emailLoja = this.bd.getLojas().getEmail(loja2);
                                System.out.println(bd.getProdutos().listProdutosMedicos());
                                while (!"0".equals(produto2)) {
                                    this.view.produto();
                                    System.out.println("Insira 0 para concluir a seleção de produtos");
                                    produto2 = input.lerString();
                                    produtosSel2.add(produto2);
                                    System.out.println("\n");
                                }
                                clearScreen();
                                produtosSel2.remove("0");
                                produto = "";
                                System.out.println(bd.getProdutos().listProdutosNormais());
                                System.out.println("Pretende encomendar mais produtos não-médicos? (Prima 0 se não pretender encomendar nenhum)");
                                while (!"0".equals(produto)) {
                                    System.out.println("Insira 0 para concluir a seleção de produtos");
                                    this.view.produto();
                                    produto = input.lerString();
                                    produtosSel2.add(produto);
                                    System.out.println("\n");
                                }
                                produtosSel2.remove("0");
                                try {
                                    for (String s : produtosSel2) {
                                        this.bd.getProdutos().existeProd(s);
                                        clearScreen();
                                        System.out.println("Insira a quantidade de " + s + " || Preço por unidade = " + bd.getProdutos().getProdutos().get(s).getPreco());
                                        quantidade2 = input.lerInt();
                                        LinhaEncomenda enc = new LinhaEncomenda(bd.getProdutos().getProdutos().get(s).getCodigo(), s, quantidade2, bd.getProdutos().getProdutos().get(s).getPreco());
                                        produtos2.put(s, enc);
                                        quantidadeTot2 += quantidade2;
                                        custoTotal2 += bd.getProdutos().getProdutos().get(s).getPreco() * quantidade2;
                                    }
                                    System.out.println("\nCusto total da encomenda: " + custoTotal2);
                                    Loja j = this.bd.getLojas().getLojas().get(emailLoja).clone();
                                    Random random = new Random();
                                    int low = 1000;
                                    int high = 9999;
                                    int result = random.nextInt(high-low) + low;
                                    String cod = "e" + result;
                                    while(this.bd.getEncomendasAceites().getAceites().contains(cod)){
                                        result = random.nextInt(high-low) + low;
                                        cod = "e" + result;
                                    }
                                    Encomenda novaEnc = new Encomenda(cod, u.getCodigo(), loja2, quantidadeTot2, u.getNome(), this.bd.getLojas().getLojas().get(emailLoja).getNome(), produtos2, true, LocalDateTime.now(), false, false, false);
                                    List<Voluntario> disponiveis = this.bd.getVoluntarios().voluntariosDisponíveisMed(j, u);

                                    if(disponiveis.isEmpty()){
                                        System.out.println("\nNão existem voluntários disponíveis perto da loja selecionada.");
                                        System.out.println("\nSerá necessário selecionar uma empresa transportadora, pagando pelos seus serviços.");
                                        String ret = this.bd.getTransportes().printEmpresasMed(u, j, novaEnc.getPeso());
                                        if("0".equals(ret)){
                                            System.out.println("\nNão existem empresas disponíveis\n");
                                            System.out.println("A sua encomenda foi cancelada...\n");
                                            System.out.println("Prima 12 para voltar ao menu");
                                            ret = "";
                                            break;
                                        }
                                        System.out.println(this.bd.getTransportes().printEmpresasMed(u, j, novaEnc.getPeso()));
                                        System.out.println("Selecione o código de uma empresa ou 0 para cancelar a encomenda");
                                        String opEmpresa = input.lerString();
                                        if("0".equals(opEmpresa)){
                                            System.out.println("\nA sua encomenda foi cancelada");
                                            System.out.println("\nPrima 12 para voltar ao menu");
                                            break;
                                        }
                                        else {
                                            try {
                                                u.addEncomenda(novaEnc);
                                                String emailEmpresa = this.bd.getTransportes().getEmail(opEmpresa);
                                                EmpresaTransportes et = this.bd.getTransportes().getTransportes().get(emailEmpresa).clone();
                                                et.addEncomenda(novaEnc);
                                                this.bd.updateUser2(u);
                                                this.bd.updateTransportes2(et);
                                                this.bd.updateLoja(novaEnc, this.bd.getLojas().getLojas().get(emailLoja));
                                                this.bd.updateAceites(novaEnc.getCodigo());
                                                System.out.println("ENCOMENDA FEITA COM SUCESSO. PRIMA 12 PARA SAIR");
                                            } catch (TransporteNotFoundException e){
                                                System.out.println("Empresa transportadora inválida");
                                            }
                                        }
                                    } else {
										if(disponiveis.size() == 1){
										    u.addEncomenda(novaEnc);
										    Voluntario v = disponiveis.get(0).clone();
										    System.out.println("A encomenda será entregue pelo voluntário --> " + v.getNome() + " --> " + v.getCodigo());
										    v.addEncomenda(novaEnc);
										    this.bd.updateVoluntario2(v);
										}
										else {
										    u.addEncomenda(novaEnc);
										    int choice = random.nextInt(disponiveis.size() - 1);
										    Voluntario v = disponiveis.get(choice).clone();
										    System.out.println("A encomenda será entregue pelo voluntário --> " + v.getNome() + " --> " + v.getCodigo());
										    v.addEncomenda(novaEnc);
										    this.bd.updateVoluntario2(v);
										}
										this.bd.updateUser2(u);
										this.bd.updateLoja(novaEnc, this.bd.getLojas().getLojas().get(emailLoja));
										this.bd.updateAceites(novaEnc.getCodigo());
										System.out.println("ENCOMENDA FEITA COM SUCESSO. PRIMA 12 PARA SAIR");
									}
                                } catch (ProductNotFoundException e) {
                                    System.out.println("Produto não existe");
                                    System.out.println("Prima 12 para retroceder");
                                }
                            } catch (LojaNotFoundException e){
                                System.out.println("Loja inválida");
                                System.out.println("Prima 12 para retroceder");
                            }
                            break;
                        case 3:
                            System.out.println(u.printEncomendasRecebidas());
                            System.out.println("Insira 12 para imprimir de novo o menu");
                            break;
                        case 4:
                            System.out.println(u.printEncomendasPorEntregar());
                            System.out.println("Insira 12 para imprimir de novo o menu");
                            break;
                        case 5:
                            if(u.getEncomendas().isEmpty()){
                                System.out.println("Não tem encomendas efetuadas");
                                System.out.println("Insira 12 para retroceder");
                            }
                            else {
                                System.out.println(u.getEncomendas());
                                System.out.println("Insira o código da encomenda que pretende analisar");
                                String cod1 = input.lerString();
                                try {
                                    Encomenda enc = u.devolveEncomenda(cod1);
                                    String one, two, three, four;
                                    one = "NÃO";
                                    two = "NÃO";
                                    three = "NÃO";
                                    four = "NÃO";

                                    if (enc.isEncomendaMedica()) {
                                        one = "SIM";
                                    }

                                    if (enc.isPreparada()) {
                                        two = "SIM";
                                    }

                                    if (enc.isLevantada()) {
                                        three = "SIM";
                                    }

                                    if (enc.isEntregue()) {
                                        four = "SIM";
                                    }
                                    clearScreen();
                                    System.out.println("\n\nENCOMENDA COM O CÓDIGO " + enc.getCodigo());
                                    System.out.println("\nESTADOS DA ENCOMENDA: ");
                                    System.out.println("\nENCOMENDA MÉDICA ---> " + one);
                                    System.out.println("\nENCOMENDA PRONTA A SER LEVANTADA DA LOJA ---> " + two);
                                    System.out.println("\nENCOMENDA LEVANTADA E A CAMINHO ---> " + three);
                                    System.out.println("\nENCOMENDA ENTREGUE ---> " + four);
                                    System.out.println("\nInsira 12 para retroceder");
                                } catch (EncomendaNotFoundException e) {
                                    System.out.println("Código inválido");
                                    System.out.println("Insira 12 para retroceder");
                                }
                            }
                            break;
                        case 6:
                            System.out.println(this.bd.getVoluntarios().printVoluntario());
                            System.out.println("Escolha o código do voluntário a avaliar");
                            String cod = Input.lerString();
                            try {
                                String emailVol = this.bd.getVoluntarios().getEmail(cod);
                                System.out.println("Insira a classificação de 1 a 10:");
                                Double classificacao = Input.lerDouble();
                                if(classificacao >= 0 && classificacao <= 10) {
                                    Voluntario v = this.bd.getVoluntarios().getVoluntarios().get(emailVol).clone();
                                    this.bd.updateVoluntario(classificacao, v);
                                    System.out.println("DONE!");
                                    System.out.println("Prima 12 para retroceder");
                                } else {
									System.out.println("Classificação inválida!");
								}
                            } catch (VoluntarioNotFoundException e){
                                System.out.println("Voluntário inválido");
                                System.out.println("Prima 12 para retroceder");
                            }
                            break;
                        case 7:
                            System.out.println(this.bd.getTransportes().printTransportes());
                            System.out.println("Escolha o código da empresa de transportes a avaliar");
                            String cod2 = Input.lerString();
                            try {
                                String emailTrans = this.bd.getTransportes().getEmail(cod2);
                                System.out.println("Insira a classificação de 1 a 10:");
                                Double classificacao = Input.lerDouble();
                                if(classificacao >= 0 && classificacao <= 10) {
                                    EmpresaTransportes e = this.bd.getTransportes().getTransportes().get(emailTrans).clone();
                                    this.bd.updateTransportes(classificacao, e);
                                    System.out.println("DONE!");
                                    System.out.println("Prima 12 para retroceder");
                                } else {
									System.out.println("Classificação inválida!");
								}
                            } catch (TransporteNotFoundException e){
                                System.out.println("Empresa inválida");
                                System.out.println("Prima 12 para retroceder");
                            }
                            break;
                        case 8:
                             System.out.println(this.bd.getVoluntarios().printVoluntario());
                             System.out.println("Insira o código do voluntário que pretende analisar");
                             String cod7 = Input.lerString();
                            try {
                                String emailVol = this.bd.getVoluntarios().getEmail(cod7);
                                Voluntario v = this.bd.getVoluntarios().getVoluntarios().get(emailVol).clone();
                                System.out.println("Insira o intervalo temporal que pretende analisar (DD/MM/YYYY)");
                                System.out.println("Inicio: ");
                                Scanner sc = new Scanner(System.in);
                                String dataRecebida = sc.nextLine();
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                Date dateTime1 = df.parse(dataRecebida);
                                System.out.println("Fim: ");
                                String dataRecebida2 = sc.nextLine();
                                Date dateTime2 = df.parse(dataRecebida2);
                                LocalDateTime d1 = dateTime1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                LocalDateTime d2 = dateTime2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                System.out.println(v.getInfoEncomendas(d1, d2));
                                System.out.println("Prima 12 para retroceder");
                            } catch (VoluntarioNotFoundException e){
                                System.out.println("Código inválido");
                                System.out.println("Insira 12 para retroceder");
                            } catch (ParseException e) {
                                System.out.println("Data inválida");
                                System.out.println("Insira 12 para retroceder");
                            }
                            break;
                        case 9:
                            System.out.println(this.bd.getTransportes().printTransportes());
                            System.out.println("Insira o código da empresa de transportes que pretende analisar");
                            String cod8 = Input.lerString();
                            try {
                                String emailTrans = this.bd.getTransportes().getEmail(cod8);
                                EmpresaTransportes et = this.bd.getTransportes().getTransportes().get(emailTrans).clone();
                                System.out.println("Insira o intervalo temporal que pretende analisar (DD/MM/YYYY)");
                                System.out.println("Inicio: ");
                                Scanner sc = new Scanner(System.in);
                                String dataRecebida = sc.nextLine();
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                Date dateTime1 = df.parse(dataRecebida);
                                System.out.println("Fim: ");
                                String dataRecebida2 = sc.nextLine();
                                Date dateTime2 = df.parse(dataRecebida2);
                                LocalDateTime d1 = dateTime1.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                LocalDateTime d2 = dateTime2.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                                System.out.println(et.getInfoEncomendas(d1, d2));
                                System.out.println("Prima 12 para retroceder");
                            } catch (TransporteNotFoundException e){
                                System.out.println("Código inválido");
                                System.out.println("Insira 12 para retroceder");
                            } catch (ParseException e) {
                                System.out.println("Data inválida");
                                System.out.println("Insira 12 para retroceder");
                            }
                            break;
                        case 10:
                            System.out.println("TOP10 voluntário e empresas que mais encomendas realizaram: \n");
                            Set<Pair> result = this.bd.top10Encomendas();
                            result.stream().limit(10).forEach(e -> System.out.println(e + " encomendas realizadas"));
                            System.out.println("\nInsira 12 para retroceder");
                            break;
                        case 11:
                            System.out.println("TOP10 empresas que mais kms percorreram: \n");
                            Set<Pair> result2 = this.bd.top10KmsPercorridos();
                            result2.stream().limit(10).forEach(e -> System.out.println(e + " kms percorridos"));
                            System.out.println("\nInsira 12 para retroceder");
                            break;
                        case 12:
                            clearScreen();
                            this.view.showMenuUser();
                            System.out.print("\nInsira uma opção --> ");
                            break;
                        default:
                            System.out.println("Opcao inválida");
                            break;
                    }
                }
                while (op != 0);
            } catch (UserNotFoundException e){
            clearScreen();
            System.out.println("Email ou password inválidos\n\n");
            System.out.println("Selecione nova opção de login\n");
            this.view.showMenuLogin();
        }
    }
}
