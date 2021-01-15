package GUI;

import Classes.InformAE;
import Classes.InformAU;
import jade.core.AID;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

public class Mapa extends JPanel {

    private ConcurrentMap<AID, InformAE> estacoes;
    private ConcurrentMap<AID, InformAU> utilizadores;
    private boolean desenha_Estacoes;

    private BufferedImage img_Bvermelha, img_Bverde, img_Bazul;


    /*Load das imagens*/
    public Mapa() {
        try {
            img_Bvermelha = ImageIO.read(new File("src/images/bike.png"));//bike vermelha
            img_Bverde = ImageIO.read(new File("src/images/bike2.png"));//bike verde
            img_Bazul = ImageIO.read(new File("src/images/bike3.png"));//bike azul
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERRO AO DAR LOAD DAS IMAGENS DAS BIKES");
        }

    }

    public void newMapa(ConcurrentMap<AID, InformAE> est, ConcurrentMap<AID, InformAU> uti, boolean desenha_Est) {

        this.estacoes = est;
        this.utilizadores = uti;
        this.desenha_Estacoes = desenha_Est;

        this.repaint();
    }


    /**
     * Define o tamanho da janela
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 700);
    }

    /**
     * Desenha as estacoes e/ou utilizadores.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        if (desenha_Estacoes) {
            for (InformAE e1 : estacoes.values())
                e1.paint(g2d, getWidth(), getHeight());
        }
        for (InformAU u1 : utilizadores.values()) {
            if (!u1.getPa().equals(u1.getPf())) {
                int x, y;
                x = u1.getPa().drawX(getWidth());
                y = u1.getPa().drawY(getHeight());

                if (u1.getPergentagem() <= 0.75 && !u1.isDesconto_aceite()) {
                    // 25 é o raio da imagem
                    // x é a largura maxima , y a altura maxima. logo para desenhar no centro qtem que se tirar 25.
                    g2d.drawImage(img_Bazul, x - 25, y - 25, null);
                }
                else if (u1.isDesconto_aceite()) {
                    g2d.drawImage(img_Bverde, x - 25, y - 25, null);
                }
                else {
                    g2d.drawImage(img_Bvermelha, x - 25, y - 25, null);
                }
                g2d.setColor(new Color(0, 0, 0));
                g2d.setFont(new Font("default", Font.BOLD, 16));
                g2d.drawString(u1.getAgent().getLocalName(), x-35, y - 15);
            }
        }
        g2d.dispose();
    }

}
