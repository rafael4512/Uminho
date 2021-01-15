package Agents;

import Classes.InformAE;
import Classes.InformAU;
import Classes.Position;
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

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.*;

public class AE extends Agent {
    public static final double price = 2;
    private static final int CAP_MAX = 20;
    private static final int CAP_MIN = 10;
    private static final double DISP_PERCENTAGEM_MAXIMA = 0.8;
    private static final double DISP_PERCENTAGEM_MINIMA = 0.4;
    public static final double RAIO_MAX = 5;
    private static final double RAIO_MIN = 2;
    private InformAE estacao;


    private static final int[] cap = {14,11,18,10,15,19,15,18,17,19,17,13,20,15,10,16,13,15,13,12};
    private static final double[] raio = {3.95,3.5,4.7,3.5,4.25,4.4,4.1,5.1,5.4,5.85,5.4,3.95,4.85,4.25,3.5,4.4,3.95,3.65,3.5,3.8};
    private static final double[] pos = {3.833,3.235,19.564,18.784,17.456,3.114,13.888,2.791,13.849,16.91,13.571,5.922,15.687,9.719,7.682,10.34,7.077,19.918,13.432,8.881,19.255,15.204,8.016,5.46,7.893,17.398,7.193,4.581,3.893,5.224,1.345,17.231,0.181,12.729,7.587,13.429,2.015,7.55,12.316,17.709};
    private static final int[] disp = {9,7,10,6,8,8,11,9,13,15,9,7,11,9,6,10,7,8,8,9};


    public static double getActionPrice(InformAE estacao, InformAU utilizador, double preco_proposto, HashMap<Position, InformAE> estacoes_redor) {
        try {
            final double OCUPACAO_BAIXA = 0.35;
            final double OCUPACAO_MEDIA = 0.6;

            Position p_estacao = estacao.getLocalizacao();
            Position pa = utilizador.getPa();
            double new_total_dist = pa.distancia(p_estacao) + utilizador.getDist();

            double percentagem = estacao.getEstado();
            boolean percentagemAEDestinoAlta = false;

            if (preco_proposto != -1) {
                if (estacoes_redor.containsKey(utilizador.getPf())) {
                    double percentagem_ae_destino = estacoes_redor.get(utilizador.getPf()).getEstado();
                    if (percentagem_ae_destino <= OCUPACAO_BAIXA) {
                        return -1;
                    }
                    percentagemAEDestinoAlta = percentagem_ae_destino >= OCUPACAO_MEDIA;
                }

                for (InformAE e : estacoes_redor.values()) {
                    if (e.getLocalizacao().distancia(utilizador.getPa()) <= e.getRaio() && e.getEstado() <= OCUPACAO_BAIXA && percentagem > e.getEstado()) {
                        return -1;
                    }
                }
            }

            double preco_calculado = 0;
            // 12 -> 18  == 40 % -> 10%
            if (percentagem <= OCUPACAO_BAIXA) {
                // 1 - 10%
                double DESCONTO_MINIMO = 0.9;
                // 1 - 40%
                double DESCONTO_MAXIMO = 0.6;
                // 40% - 10%
                double DIFF_DESCONTO = DESCONTO_MINIMO - DESCONTO_MAXIMO;
                // 0.800  = 0^2 * 0.2 + 0.8
                // 0.8245 = 0.35^2 * 0.2 + 0.8
                // 0.0245  = 0.8245 - 0.8
                double DIFF_VALOR_PERCENTAGEM = Math.pow(OCUPACAO_BAIXA, 2) * 0.2;

                preco_calculado = ((Math.pow(percentagem, 2) * 0.2) * DIFF_DESCONTO / DIFF_VALOR_PERCENTAGEM + DESCONTO_MAXIMO) * price * new_total_dist;
            }
            // 18 -> 19.6 == 10 % -> 2%
            else if (percentagem <= OCUPACAO_MEDIA) {
                // 1 - 2%
                double DESCONTO_MINIMO = 0.98;
                // 1 - 10%
                double DESCONTO_MAXIMO = 0.9;
                // 10% - 2%
                double DIFF_DESCONTO = DESCONTO_MINIMO - DESCONTO_MAXIMO;
                // 0.87 = 0.35 * 0.2 + 0.8
                double VALOR_PERCENTAGEM_MINIMA = 0.8 + OCUPACAO_BAIXA * 0.2;
                // 0.92 = 0.6 * 0.2 + 0.8
                // 0.05 = 0.92 - 0.87
                double DIFF_VALOR_PERCENTAGEM = (OCUPACAO_MEDIA - OCUPACAO_BAIXA) * 0.2;

                preco_calculado = (((percentagem * 0.2 + 0.8) - VALOR_PERCENTAGEM_MINIMA) * DIFF_DESCONTO / DIFF_VALOR_PERCENTAGEM + DESCONTO_MAXIMO) * price * new_total_dist;
            }
            // 20
            else if (preco_proposto > 0) {
                return -1;
            }

            // Preco_proposto == -1 -> Primeira proposta do inicio do aluguer
            if (preco_proposto == -1) {
                new_total_dist = utilizador.getPi().distancia(p_estacao);

                return (preco_calculado <= 0) ? price * new_total_dist : preco_calculado;
            }

            // Preco_proposto == -2 -> Primeira proposta de negociacao
            if (preco_proposto == -2 && utilizador.getPreco() < preco_calculado) {
                if (percentagem <= OCUPACAO_BAIXA) {
                    return utilizador.getPreco() * 0.9;
                }
                else {
                    return -2;
                }
            }
            else if (preco_proposto == -2) {
                return preco_calculado;
            }

            // Negociacao propriamente dita
            if (preco_proposto >= preco_calculado) {
                return preco_proposto;
            }
            else if (preco_proposto >= preco_calculado * 0.8 || percentagemAEDestinoAlta) {
                return preco_proposto * 0.4 + preco_calculado * 0.6;
            }
            else {
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    protected void setup() {
        super.setup();

        Object[] args = getArguments();
        int num_estacao = (int) args[0];

        this.addBehaviour(new Register_Estacao(num_estacao));
    }

    protected void takeDown() {
        super.takeDown();

        try {
            DFService.deregister(this);
        } catch (Exception e) {
            System.out.println("Estação " + this.getLocalName() + " não criada devido a proximidade a outra estação");
        }
    }

    private class Register_Estacao extends CyclicBehaviour {
        private final int no_estacao;

        public Register_Estacao(int n) {
            this.no_estacao = n;
        }

        public void action() {
            if (estacao == null) {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setName("Interface");
                template.addServices(sd);

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);

                    // Se 1 ou + AI existem!
                    if (result.length > 0) {
                        AID id = myAgent.getAID();

                        Random r = new Random();
                        double x = r.nextFloat() * (AI.X_MAX - AI.X_MIN) + AI.X_MIN;
                        double y = r.nextFloat() * (AI.Y_MAX - AI.Y_MIN) + AI.Y_MIN;
                        int cap = r.nextInt(CAP_MAX - CAP_MIN + 1) + CAP_MIN;
                        int disp = (int) (cap * (r.nextFloat() * (DISP_PERCENTAGEM_MAXIMA - DISP_PERCENTAGEM_MINIMA) + DISP_PERCENTAGEM_MINIMA));
                        double raio = (RAIO_MAX - RAIO_MIN) * ((double) cap / (CAP_MAX)) + RAIO_MIN;

                        /*
                        double x = AE.pos[no_estacao * 2];
                        double y = AE.pos[no_estacao * 2 + 1];
                        int cap = AE.cap[no_estacao];
                        int disp = AE.disp[no_estacao];
                        double raio = AE.raio[no_estacao];
                        */

                        Position p = new Position(x, y);

                        estacao = new InformAE(id, p, cap, disp, raio, no_estacao);

                        ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
                        msg.setContentObject(estacao);

                        for (DFAgentDescription dfAgentDescription : result) {
                            msg.addReceiver(dfAgentDescription.getName());
                        }

                        myAgent.send(msg);
                    }
                    else {
                        // N�o h� AI - kill the agent!
                        System.out.println(myAgent.getAID().getLocalName() + ": No AI available. Agent offline");
                        myAgent.doDelete();
                    }
                } catch (FIPAException | IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                ACLMessage reply = myAgent.receive();

                if (reply != null) {
                    if (reply.getPerformative() != ACLMessage.ACCEPT_PROPOSAL) {
                        myAgent.doDelete();
                    }
                    else {
                        DFAgentDescription dfd = new DFAgentDescription();
                        dfd.setName(getAID());
                        ServiceDescription sd = new ServiceDescription();
                        sd.setType("Estacao");
                        sd.setName("Estacao" + no_estacao);
                        dfd.addServices(sd);

                        try {
                            DFService.register(myAgent, dfd);

                            // System.out.println(getLocalName() + ": Agente Estac�o Registado" );
                        } catch (FIPAException e) {
                            e.printStackTrace();
                        }

                        myAgent.addBehaviour(new InformEstado(myAgent, AI.period));
                        myAgent.addBehaviour(new Receiver());
                        myAgent.removeBehaviour(this);
                    }
                }
                else {
                    block();
                }
            }
        }
    }

    private class Receiver extends CyclicBehaviour {
        private final Map<AID, InformAU> utilizadores_APE = new HashMap<>();
        private final List<AID> reservas = new ArrayList<>();
        private final List<AID> chegadas = new ArrayList<>();
        private final List<AID> utilizadores_negociacao = new ArrayList<>();
        private final HashMap<Position, InformAE> estacoes = new HashMap<>();
        // private final Map<AID, Integer> preco_oferecidos = new HashMap<>();
        // private final Map<AID, String> estado_ultima_proposta = new HashMap<>();

        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                try {
                    ACLMessage reply = msg.createReply();
                    if (msg.getPerformative() == ACLMessage.REQUEST) {
                        // Tentativa de aluguer, inicial e final
                        InformAU au = (InformAU) msg.getContentObject();
                        if (au.getPi() == null) {
                            // Inicial
                            if (estacao.reservarLugar()) {
                                reservas.add(msg.getSender());
                                reply.setContentObject(estacao);
                                reply.setPerformative(ACLMessage.INFORM);
                            }
                            else {
                                AI.station_refused_empty.getAndIncrement();
                                reply.setPerformative(ACLMessage.FAILURE);
                            }
                        }
                        else if (au.getPf() == null) {
                            // Final
                            if (estacao.reservarChegada()) {
                                chegadas.add(msg.getSender());
                                reply.setPerformative(ACLMessage.INFORM);
                            }
                            else {
                                AI.station_refused_full.getAndIncrement();
                                reply.setPerformative(ACLMessage.FAILURE);
                            }
                        }
                        else {
                            // Inicial para come�ar aluguer
                            double preco = getActionPrice(estacao, au, -1, this.estacoes);

                            reply.setContentObject(preco);
                            reply.setPerformative(ACLMessage.PROPOSE);
                        }

                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.INFORM) {
                        // Informar sobre a posi��o atual
                        InformAU au = (InformAU) msg.getContentObject();
                        utilizadores_APE.put(msg.getSender(), au);

                        // Oferecer desconto, novo cliente
                        double preco;
                        if (!utilizadores_negociacao.contains(msg.getSender())) {
                            if ((preco = AE.getActionPrice(estacao, au, -2, this.estacoes)) > 0) {
                                utilizadores_negociacao.add(msg.getSender());
                                reply.setPerformative(ACLMessage.PROPOSE);
                                reply.setContentObject(preco);
                                myAgent.send(reply);
                            }
                        }
                    }
                    else if (msg.getPerformative() == ACLMessage.PROPAGATE){
                        InformAE e = (InformAE) msg.getContentObject();

                        this.estacoes.put(e.getLocalizacao(), e);
                    }
                    else if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL && utilizadores_negociacao.contains(msg.getSender())) {
                        // Aceitar desconto oferecido, novo cliente
                        if (estacao.reservarChegada()) {
                            chegadas.add(msg.getSender());
                            reply.setPerformative(ACLMessage.INFORM);
                        }
                        else {
                            reply.setPerformative(ACLMessage.FAILURE);
                        }

                        utilizadores_APE.remove(msg.getSender());
                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.PROPOSE && utilizadores_negociacao.contains(msg.getSender())) {
                        // Negociar desconto oferecido, novo cliente
                        double preco_dado_user = (double) msg.getContentObject();
                        InformAU au = utilizadores_APE.get(msg.getSender());
                        double preco_calculado = AE.getActionPrice(estacao, au, preco_dado_user, this.estacoes);

                        if (preco_calculado <= preco_dado_user && estacao.reservarChegada()) {
                            chegadas.add(msg.getSender());

                            utilizadores_APE.remove(msg.getSender());
                            reply.setPerformative(ACLMessage.INFORM);
                        }
                        else if (preco_calculado > 0 && preco_calculado > preco_dado_user) {
                            reply.setPerformative(ACLMessage.PROPOSE);
                            reply.setContentObject(preco_calculado);
                        }
                        else {
                            utilizadores_APE.remove(msg.getSender());
                            reply.setPerformative(ACLMessage.FAILURE);
                        }
                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.REJECT_PROPOSAL && utilizadores_negociacao.contains(msg.getSender())) {
                        // Rejeitar desconto oferecido, ex-possivel cliente

                        // utilizadores_negociacao.remove(msg.getSender());
                        utilizadores_APE.remove(msg.getSender());
                    }
                    else if (msg.getPerformative() == ACLMessage.CONFIRM && reservas.contains(msg.getSender())) {
                        // Confirmar que se vai iniciar o aluguer
                        estacao.saida();
                        reservas.remove(msg.getSender());
                    }
                    else if (msg.getPerformative() == ACLMessage.CONFIRM && chegadas.contains(msg.getSender())) {
                        // Confirmar que chegou � esta��o
                        estacao.chegada();
                        chegadas.remove(msg.getSender());

                        reply.setPerformative(ACLMessage.CONFIRM);
                        myAgent.send(reply);
                    }
                    else if (msg.getPerformative() == ACLMessage.CANCEL && reservas.contains(msg.getSender())) {
                        // Cancelar reserva
                        reservas.remove(msg.getSender());
                        estacao.cancelarLugar();
                    }
                    else if (msg.getPerformative() == ACLMessage.CANCEL && chegadas.contains(msg.getSender())) {
                        // Cancelar chegada, aceitou outra esta��o
                        chegadas.remove(msg.getSender());
                        estacao.cancelarChegada();
                    }
                    else if (msg.getPerformative() != ACLMessage.UNKNOWN) {
                        reply.setPerformative(ACLMessage.UNKNOWN);
                        myAgent.send(reply);
                    }
                } catch (NullPointerException | BufferUnderflowException | IOException | UnreadableException e) {
                    e.printStackTrace();
                }
            }
            else {
                block();
            }
        }
    }

    private class InformEstado extends TickerBehaviour {

        public InformEstado(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setName("Interface");
            template.addServices(sd);

            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);

                if (result.length > 0) {
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.setContentObject(estacao);
                    for (DFAgentDescription dfAgentDescription : result) {
                        msg.addReceiver(dfAgentDescription.getName());
                    }

                    myAgent.send(msg);
                }

                template = new DFAgentDescription();
                sd = new ServiceDescription();
                sd.setType("Estacao");
                template.addServices(sd);
                result = DFService.search(myAgent, template);
                if (result.length > 0) {
                    ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
                    msg.setContentObject(estacao);
                    for (DFAgentDescription dfAgentDescription : result) {
                        msg.addReceiver(dfAgentDescription.getName());
                    }
                    msg.removeReceiver(myAgent.getAID());
                    myAgent.send(msg);
                }
            } catch (FIPAException | IOException e) {
                e.printStackTrace();
            }

        }
    }
}
