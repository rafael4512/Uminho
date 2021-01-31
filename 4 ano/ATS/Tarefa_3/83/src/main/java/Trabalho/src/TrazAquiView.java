import java.io.Serializable;

public class TrazAquiView implements Serializable {
    /** Método que imprime o menu inicial. */

    public void showMenuInicial(){
        System.out.println("«««««««««««««««««««««TRAZ-AQUI»»»»»»»»»»»»»»»»»»»»»»»»");
        System.out.println("1.Registar");
        System.out.println("2.Login");
        System.out.println("3.Gravar estado");
        System.out.println("4.Carregar estado");
        System.out.println("0.Sair da aplicação");
        System.out.print("\nInsira uma opção --> ");
    }

    public void loadMenu(){
        System.out.println("ESCOLHA O TIPO DE LOAD QUE PRETENDE REALIZAR");
        System.out.println("1. LER FICHEIRO LOGS.TXT");
        System.out.println("2. LER OUTRO FICHEIRO .DAT");
        System.out.print("\nInsira uma opção --> ");
    }

    public void printHeader(){
        System.out.println("Bem vindo à nossa aplicação Traz-Aqui");
    }

    public void showMenuRegisto(){
        System.out.println("----------------REGISTO----------------");
        System.out.println("   Tipo de utilizador: ");
        System.out.println("1. Utilizador doméstico");
        System.out.println("2. Voluntário");
        System.out.println("3. Loja");
        System.out.println("4. Empresa de Transportes");
        System.out.println("0. Retroceder");
        System.out.print("\nInsira uma opção --> ");
    }

    public void showMenuLogin(){
        System.out.println("----------------LOGIN----------------");
        System.out.println("1. Utilizador doméstico");
        System.out.println("2. Voluntário");
        System.out.println("3. Loja");
        System.out.println("4. Empresa de Transportes");
        System.out.println("0. Retroceder");
        System.out.print("\nInsira uma opção --> ");
    }

    public void headRegistoUser() {
        System.out.println("----------------REGISTO USER----------------");
    }

    public void headRegistoVoluntario() {System.out.println("----------------REGISTO VOLUNTÁRIO----------------");}

    public void headRegistoLoja() { System.out.println("----------------REGISTO LOJA----------------");}

    public void headRegistoEmpresa() { System.out.println("----------------REGISTO EMPRESA TRANSPORTES----------------");}

    public void headLoginUser() { System.out.println("----------------LOGIN USER----------------");}

    public void headLoginVoluntario() { System.out.println("----------------LOGIN VOLUNTÁRIO----------------");}

    public void headLoginLoja() { System.out.println("----------------LOGIN LOJA----------------");}

    public void headLoginEmpresa() { System.out.println("----------------LOGIN EMPRESA TRANSPORTADORA---------------");}

    public void headNovaEncomenda() {  System.out.println("----------------NOVA ENCOMENDA----------------");}

    public static void showMenuUser(){
        System.out.println("----------------USER----------------");
        System.out.println("1. Nova encomenda");
        System.out.println("2. Nova encomenda Médica");
        System.out.println("3. Histórico de encomendas recebidas");
        System.out.println("4. Histórico de encomendas por entregar");
        System.out.println("5. Ver estado de encomenda");
        System.out.println("6. Avaliar um voluntário");
        System.out.println("7. Avaliar uma transportadora");
        System.out.println("8. Informação sobre entregas de voluntário");
        System.out.println("9. Informação sobre entregas de Empresa Transportadora");
        System.out.println("10. TOP10 mais encomendas efetuadas");
        System.out.println("11.TOP10 mais kms percorridos por transportadoras");
        System.out.println("0. Retroceder");
    }

    public static void showMenuVoluntario(){
        System.out.println("----------------VOLUNTÁRIO----------------");
        System.out.println("1. Declarar-se disponível para ir buscar encomenda");
        System.out.println("2. Declarar-se indisponível para ir buscar encomenda");
        System.out.println("3. Declarar-se disponível para transportar encomendas médicas");
        System.out.println("4. Declarar-se indisponível para transportar encomendas médicas");
        System.out.println("5. Histórico total de encomendas.");
        System.out.println("6. Sinalizar levantamento de encomenda em loja");
        System.out.println("7. Sinalizar entrega de encomenda a utilizador");
        System.out.println("8. Imprimir menu.");
        System.out.println("0. Retroceder");
    }

    public static void showMenuLoja(){
        System.out.println("----------------LOJA----------------");
        System.out.println("1. Marcar encomenda como preparada");
        System.out.println("2. Ver encomendas em stock");
        System.out.println("3. Atualizar número de pessoas na fila");
        System.out.println("0. Retroceder");
    }

    public static void showMenuTransportes(){
        System.out.println("----------------EMPRESA TRANSPORTADORA----------------");
        System.out.println("1. Declarar-se disponível para transporte de encomenda");
        System.out.println("2. Declarar-se indisponível para transporte de encomenda");
        System.out.println("3. Declarar-se disponível para transportar encomendas médicas");
        System.out.println("4. Declarar-se indisponível para transportar encomendas médicas");
        System.out.println("5. Apresentar preço de transporte de encomenda");
        System.out.println("6. Sinalizar levantamento de encomenda na loja");
        System.out.println("7. Anotar tempo e entrega de uma encomenda");
        System.out.println("8. Entregar todas as encomendas");
        System.out.println("9. Histórico de encomendas");
        System.out.println("10. Total faturado entre 2 datas");
        System.out.println("11. Imprimir menu");
        System.out.println("0. Retroceder");
    }

    public void preenchimentoObrg() {
        System.out.println("Os seguintes campos são de preenchimento obrigatório: ");
    }

    public void email() {
        System.out.println("Insira o seu email: ");
    }

    public void password() {
        System.out.println("Insira a sua password: ");
    }

    public void nome() {
        System.out.println("Insira o seu nome: ");
    }

    public void latitude() {
        System.out.println("Insira a sua latitude: ");
    }

    public void longitude() {
        System.out.println("Insira a sua longitude: ");
    }

    public void raio_acao() {
        System.out.println("Insira o seu raio de ação: ");
    }

    public void tempo_espera() {
        System.out.println("Insira o tempo de espera em fila: ");
    }

    public void custo_km() {
        System.out.println("Insira o custo por km: ");
    }

    public void local() {
        System.out.println("Insira o local onde se situa a empresa: ");
    }

    public void nif() {
        System.out.println("Insira o seu NIF: ");
    }

    public void nr_min_encomendas() {
        System.out.println("Insira o número mínimo de encomendas: ");
    }

    public void encomendas_medicas() {
        System.out.println("Aceita transportar encomendas médicas? (True/False) ");
    }

    public void produto() {
        System.out.println("Insira o produto a comprar: ");
    }
}
