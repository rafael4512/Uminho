package Classes;

import jade.core.AID;

import java.io.Serializable;

public class InformAU implements Serializable {

    private final AID agent;
    private final double velocidade;
    private Position pi;
    private Position pa;
    private Position pf;
    private double preco;
    private boolean desconto_aceite;
    private double distancia_sem_alteracao;
    private boolean em_viagem;
    private final Serializable lock = new Serializable() {};

    public InformAU(AID agent, double velocidade) {
        super();
        this.agent = agent;
        this.velocidade = velocidade;
        this.pi = null;
        this.pa = null;
        this.pf = null;
        this.desconto_aceite = false;
        this.preco = 0;
        this.distancia_sem_alteracao = 0;
        em_viagem = false;
    }

    public AID getAgent() {
        return agent;
    }

    public Position getPi() {
        return pi;
    }

    public Position getPa() {
        return pa;
    }

    public Position getPf() {
        return pf;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public boolean isDesconto_aceite() {
        synchronized (lock) {
            return desconto_aceite;
        }
    }

    public double getVelocidade() {
        return velocidade;
    }

    public double getDist() {
        return this.distancia_sem_alteracao;
    }

    public void setEstacaoInicial(Position pi) {
        this.pi = pi.clone();
        this.pa = pi.clone();
        this.distancia_sem_alteracao = 0;
    }

    public void setEstacaoFinal(Position pf) {
        this.pf = pf.clone();
    }

    public void setAceitarDesconto(boolean desconto_aceite) {
        synchronized (lock) {
            this.desconto_aceite = desconto_aceite;
        }
    }

    public void incrementarDistancia(double time) {
        if (getPergentagem() < 1) {
            this.distancia_sem_alteracao += this.pa.move(this.pf, velocidade * time);
        }
    }

    public double getPergentagem() {
        if (pi == null || pa == null || pf == null) {
            return 0;
        }
        else {
            return distancia_sem_alteracao / (distancia_sem_alteracao + pa.distancia(pf));
        }
    }

    public boolean isEm_viagem() {
        return em_viagem;
    }

    public void setEm_viagem(boolean em_viagem) {
        this.em_viagem = em_viagem;
    }

    @Override
    public String toString() {
        String pa_string = "null";

        if (pa != null) {
            pa_string = pa.toString();
        }

        return "{" +
                agent.getName() +
                ", pa=" + pa_string +
                ", velocidade=" + velocidade +
                ", distancia_sem_alteracao=" + String.format("%.3f", distancia_sem_alteracao).replace(",", ".") +
                '}';
    }
}
