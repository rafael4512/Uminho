package Classes;

import Agents.AI;
import jade.core.AID;

import java.awt.*;
import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.util.Objects;

public class InformAE implements Serializable {
    private final AID agent;
    private final Position localizacao;
    private final int capacidade;
    private final double raio;
    private final int num;
    private int bicicletas_disponiveis;
    private int chegadas;
    private int reservas;
    private Color color;

    public InformAE(AID agent, Position p, int capacidade, int bicicletas_disponiveis, double raio, int num) {
        super();
        color = randomColor();
        this.agent = agent;
        this.localizacao = p.clone();
        this.capacidade = capacidade;
        this.bicicletas_disponiveis = bicicletas_disponiveis;
        this.raio = raio;
        this.num = num;

    }

    public AID getAgent() {
        return agent;
    }

    public Position getLocalizacao() {
        return localizacao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getBicicletas_disponiveis() {
        return bicicletas_disponiveis;
    }

    public int getChegadas() {
        return chegadas;
    }

    public int getReservas() {
        return reservas;
    }

    public double getRaio() {
        return raio;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    // Bicicletas na esta��o + as previstas chegarem
    public int getTotalOcupacao() {
        return bicicletas_disponiveis + chegadas + reservas;
    }

    public synchronized boolean reservarLugar() {
        if (bicicletas_disponiveis <= 0) return false;

        bicicletas_disponiveis--;
        reservas++;
        return true;
    }

    public synchronized void cancelarLugar() throws BufferUnderflowException {
        if (reservas <= 0) throw new BufferUnderflowException();

        bicicletas_disponiveis++;
        reservas--;
    }

    public synchronized void saida() throws BufferUnderflowException {
        if (reservas <= 0) throw new BufferUnderflowException();

        reservas--;
    }

    public synchronized boolean reservarChegada() {
        if (bicicletas_disponiveis + chegadas + reservas >= capacidade) return false;

        chegadas++;
        return true;
    }

    public synchronized void cancelarChegada() throws BufferUnderflowException {
        if (chegadas <= 0) throw new BufferUnderflowException();

        chegadas--;
    }

    public synchronized void chegada() throws BufferUnderflowException {
        if (chegadas <= 0) throw new BufferUnderflowException();

        chegadas--;
        bicicletas_disponiveis++;
    }

    // Percentagem
    public double getEstado() {
        return (double) bicicletas_disponiveis / capacidade;
    }

    public int getNum() {
        return num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformAE informAE = (InformAE) o;
        return Double.compare(informAE.raio, raio) == 0 &&
                num == informAE.num &&
                Objects.equals(agent, informAE.agent) &&
                Objects.equals(localizacao, informAE.localizacao);
    }

    @Override
    public String toString() {
        return "{" + agent.getName() +
                ", localizacao=" + localizacao.toString() +
                ", capacidade=" + capacidade +
                ", bicicletas_disponiveis=" + bicicletas_disponiveis +
                ", chegadas=" + chegadas +
                ", reservas=" + reservas +
                '}';
    }

    //Escolhe uma cor aleatoria com transparencia
    protected Color randomColor() {
        int red = (int) Math.abs(Math.random() * 255);
        int green = (int) Math.abs(Math.random() * 255);
        int blue = (int) Math.abs(Math.random() * 255);
        return new Color(red, green, blue, 100);
    }

    /**
     * Pintar e escrever a legenda de uma estação
     *
     * @param g reponsavel por imprimir no ecra
     * @param w largura da janela
     * @param h altura da janela
     */
    public void paint(Graphics g, int w, int h) {
        g.setColor(color);
        //colocar o sistema de coor
        int x, y;
        x = this.localizacao.drawX(w);
        y = this.localizacao.drawY(h);

        int raio_coord = (int) (raio / AI.X_MAX * 0.6 * w);
        int raio_coord_y = (int) (raio / AI.Y_MAX * 0.6 * h);
        g.fillOval(x - raio_coord, y - raio_coord_y, raio_coord * 2, raio_coord_y * 2);

        //g.fillOval(x - raio_coord, y - raio_coord, raio_coord * 2, raio_coord * 2);
        g.setFont(new Font( "default", Font.BOLD, 16));

        g.setColor(new Color(0,0,0));
        g.fillOval(x - 3,y - 3,6,6);

        g.setColor(new Color(color.getRed(),color.getGreen(),color.getBlue(), 255));
        g.drawString(this.agent.getLocalName(), x - 25, y - 5 - raio_coord_y);
    }
}
