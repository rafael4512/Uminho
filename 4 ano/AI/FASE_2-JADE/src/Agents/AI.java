package Agents;

import Classes.InformAE;
import Classes.InformAE_Array;
import Classes.InformAU;
import Classes.Position;
import GUI.Graficos;
import GUI.Mapa;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AI extends Agent {
    public static final long period = 50L;
    public static final double X_MIN = 0;
    public static final double Y_MIN = 0;
    public static final double X_MAX = 20;
    public static final double Y_MAX = 20;
    private static final double DIST_MIN = 0.5;

    public static AtomicInteger system_speed = new AtomicInteger(20);
    public static AtomicInteger user_new_dest = new AtomicInteger(0);
    public static AtomicInteger station_refused_empty = new AtomicInteger(0);
    public static AtomicInteger station_refused_full = new AtomicInteger(0);
    public static AtomicInteger alugueres_feitos = new AtomicInteger(0);
    private final ConcurrentMap<AID, InformAU> utilizadores = new ConcurrentHashMap<>();
    private final ConcurrentMap<AID, InformAE> estacoes = new ConcurrentHashMap<>();
    private final Mapa mapa = new Mapa();
    private final Graficos graficos = new Graficos();

    //private Mapa mapa;

    protected void setup() {
        super.setup();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Central");
        sd.setName("Interface");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);

            System.out.println(getLocalName() + ": Agente Interface Registado");
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        this.addBehaviour(new Receiver());
        this.addBehaviour(new Interface(this, period));

        /*Iniciar a interface
         * --------------------- */
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("S.P.B");
            frame.setPreferredSize(new Dimension(800,830));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JTabbedPane tabbedPane1 = new JTabbedPane();
            tabbedPane1.setForeground(Color.BLACK);
            tabbedPane1.setFont(new Font("Dialog", Font.BOLD, 20));
            tabbedPane1.add("Mapa", mapa);
            tabbedPane1.add("Estatísticas", graficos);
            constructUI(frame);
            frame.add(tabbedPane1);
            // frame.add(tabbedPane1,  BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
        /*---------------------*/
    }

    private void constructUI(JFrame frame) {
        JSlider speed;
        frame.setLayout(new BorderLayout());
        JPanel controls = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        speed = new JSlider(20, 3000, 20);

        controls.add(new JLabel("System Speed:"), gbc);
        //gbc.gridy++;
        //controls.add(new JLabel("Quanity:"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        controls.add(speed, gbc);
        frame.add(controls, BorderLayout.SOUTH);

        speed.addChangeListener(e -> {
            //atualizar o system speed
            system_speed.set(speed.getValue());
        });
    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        super.takeDown();
    }

    private class Receiver extends CyclicBehaviour {
        private boolean aceitar_Estacao(InformAE nova_estacao) {
            Position p = nova_estacao.getLocalizacao();

            for (InformAE ae : estacoes.values()) {
                if (ae.getLocalizacao().distancia(p) < DIST_MIN) {
                    return false;
                }
            }

            estacoes.put(nova_estacao.getAgent(), nova_estacao);
            return true;
        }

        private InformAE_Array getEstacaoAPE(InformAU user) {
            Position p = user.getPa();
            Position pf = user.getPf();

            return new InformAE_Array(estacoes.values()
                    .stream()
                    .filter(map -> !map.getLocalizacao().equals(pf))
                    .filter(informAE -> informAE.getLocalizacao().distancia(p) <= informAE.getRaio() && !pf.equals(informAE.getLocalizacao()))
                    .collect(Collectors.toList()));
        }

        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                try {
                    if (msg.getPerformative() == ACLMessage.SUBSCRIBE) {
                        // Esta��o quer subscrever-se
                        InformAE content = (InformAE) msg.getContentObject();

                        ACLMessage reply = msg.createReply();
                        if (aceitar_Estacao(content)) {
                            reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                        }
                        else {
                            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                        }

                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.REQUEST && !utilizadores.containsKey(msg.getSender())) {
                        // Quer todas as esta��es
                        InformAE estacao_inicial = (InformAE) msg.getContentObject();

                        ArrayList<InformAE> result = (ArrayList<InformAE>) estacoes.values()
                                .stream()
                                .filter(map -> !(map.getNum() == (estacao_inicial.getNum())))
                                .collect(Collectors.toList());

                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.addReceiver(msg.getSender());
                        reply.setContentObject(new InformAE_Array(result));
                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.INFORM && estacoes.containsKey(msg.getSender())) {
                        // Informar sobre estado da estacao
                        InformAE estacao = (InformAE) msg.getContentObject();
                        estacoes.replace(msg.getSender(), estacao);
                    }
                    else if (msg.getPerformative() == ACLMessage.INFORM) {
                        // Formalizar o aluguer/Informar sobre posi��o atual
                        InformAU user = (InformAU) msg.getContentObject();

                        try {
                            utilizadores.put(user.getAgent(), user);

                            if (user.getPergentagem() > 0.75 && !user.isDesconto_aceite()) {
                                ACLMessage reply = msg.createReply();
                                InformAE_Array result = getEstacaoAPE(user);
                                reply.setContentObject(result);
                                reply.setPerformative(ACLMessage.INFORM);
                                myAgent.send(reply);
                            }
                        }
                        catch (NullPointerException e) {
                            System.out.println(msg.getSender().getLocalName());
                            // e.printStackTrace();
                        }
                    }
                    else if (msg.getPerformative() == ACLMessage.CONFIRM && utilizadores.containsKey(msg.getSender())) {
                        // Fim do aluguer
                        utilizadores.remove(msg.getSender());

                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.CONFIRM);
                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() != ACLMessage.UNKNOWN) {
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.UNKNOWN);
                        myAgent.send(reply);
                    }
                } catch (UnreadableException | IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                block();
            }
        }
    }

    private class Interface extends TickerBehaviour {

        public Interface(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            mapa.newMapa(estacoes, utilizadores, true);
            graficos.desenhaGraficos(estacoes);
        }
    }
}
