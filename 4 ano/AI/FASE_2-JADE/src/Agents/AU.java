package Agents;

import Classes.InformAE;
import Classes.InformAE_Array;
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
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AU extends Agent {
    private static final double MAX_SPEED = 30;
    private static final double MIN_SPEED = 10;
    private static final double nano_to_hour = 3.6e+12;

    private InformAU utilizador;
    private AID estacao_final;
    private double ganancia;
    // 0 -> 1 == Pouco ganancioso -> Muito Ganancioso
    private double indice_amigavel;
    // 0 -> 1 == Rejeita desloca��es grandes -> Aceita desloca��es maiores


    public double getActionPrice(InformAU utilizador, Position alt_p, double preco_proposto) {
        double preco_atual = utilizador.getPreco();
        if (preco_atual != 0 && preco_atual < preco_proposto) return -1;

        Position pf = utilizador.getPf();
        Position pa = utilizador.getPa();

        double d_diff = alt_p.distancia(pf);
        double remaining_d = pf.distancia(pa);
        double new_remaining_d = alt_p.distancia(pa);
        double desconto_maximo = 0.6;

        //ALTERAR VALORES DESTA VARIAVEL PARA TRATAR DOS RECUSADOS
        double aceita_preco = 1 - Math.pow(ganancia, 2) - preco_proposto / preco_atual + desconto_maximo;
        //double aceita_deslocacao = ((indice_amigavel+100) * remaining_d * utilizador.getVelocidade() / MAX_SPEED) - (d_diff * new_remaining_d / remaining_d);
        double aceita_deslocacao = ((indice_amigavel+1) * remaining_d * utilizador.getVelocidade() / MAX_SPEED) - (d_diff * new_remaining_d / Math.pow(remaining_d, 2));

        if (preco_atual == 0) {
			/*
			Primeiro aluguer
			Oferece Max, 20, s� rejeita se ganancia > 0.9
			Oferece Min, 12, s� rejeita se ganancia > 0.98
			=> 0.95 - 0.85 = 0.1
			=> 20 - 12 = 8
			*/
            double percentagem_rejeitar_preco_maximo = 0.9;
            double percentagem_rejeitar_preco_minimo = 0.99;
            double diff_percentagem = percentagem_rejeitar_preco_minimo - percentagem_rejeitar_preco_maximo;
            double diff_desconto = 1 - desconto_maximo;

            Random r = new Random();
            double prob_g = r.nextDouble();
            double preco_MAX = AE.price * remaining_d;

            if (0.7 + 0.3 * ganancia * prob_g <= percentagem_rejeitar_preco_minimo - (preco_proposto - desconto_maximo * preco_MAX) / (diff_desconto * preco_MAX) * diff_percentagem) {
                return preco_proposto;
            }
            else {
                return -1;
            }
        }
        //System.out.println("Preco Aceite "+aceita_preco+"; Aceita Deslocação "+aceita_deslocacao);

        if (aceita_deslocacao > 0 && aceita_preco > 0) {
            return preco_proposto;
        }
        else if (aceita_deslocacao > 0 || aceita_preco > 0) {
            // Tentar maximo 10
            // ganancia 1 => p' = 0.85 * p
            // ganancia 0 => p' = 0.98 * p
            // 0.13 = 0.98 - 0.85
            return preco_proposto * (0.98 - (Math.pow(ganancia, 2) * 0.13));
        }
        else return -1;
    }

    protected void setup() {
        super.setup();

        Object[] args = getArguments();
        int num_estacaoes = (int) args[0];

        this.addBehaviour(new Request(num_estacaoes));
    }

    protected void takeDown() {super.takeDown();}

    private class Request extends CyclicBehaviour {
        private final int num_estacoes;
        private DFAgentDescription[] servico_estacao_inicial = null;
        private DFAgentDescription[] servico_estacao_final = null;
        private DFAgentDescription[] result_ai = null;
        private InformAE ae_final;


        public Request(int n) {
            this.num_estacoes = n;
        }

        public void action() {
            try {
                // Caso ainda não exista e não tenha estacao escolhida
                if (utilizador == null) {
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    Random r = new Random();
                    int estacao_pretendida = r.nextInt(num_estacoes);
                    sd.setName("Estacao" + estacao_pretendida);
                    template.addServices(sd);

                    servico_estacao_inicial = DFService.search(myAgent, template);
                    if (servico_estacao_inicial.length > 0) {
                        AID id = myAgent.getAID();
                        r = new Random();
                        double speed = r.nextFloat() * (MAX_SPEED - MIN_SPEED) + MIN_SPEED;
                        ganancia = r.nextFloat();
                        indice_amigavel = r.nextFloat();
                        utilizador = new InformAU(id, speed);

                        ACLMessage mensagem = createAclMessage(ACLMessage.REQUEST, utilizador, servico_estacao_inicial);
                        myAgent.send(mensagem);
                    }
                    else {
                        myAgent.doDelete();
                    }
                }
                // Processo de aluguer em curso
                else {
                    ACLMessage msg = receive();
                    if (msg != null) {
                        // Caso ainda não tenha estação final e ainda não falou com o AI e recebeu mensagem INFORM do AE -> obter lista do AI
                        if (result_ai == null && msg.getPerformative() == ACLMessage.INFORM) {
                            InformAE ae_inicial = (InformAE) msg.getContentObject();
                            utilizador.setEstacaoInicial(ae_inicial.getLocalizacao());

                            // Passar a falar com AI
                            DFAgentDescription template = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setName("Interface");
                            template.addServices(sd);
                            result_ai = DFService.search(myAgent, template);

                            if (result_ai.length > 0) {
                                ACLMessage mensagem = createAclMessage(ACLMessage.REQUEST, ae_inicial, result_ai);
                                myAgent.send(mensagem);
                            }
                            else {
                                cancelar_estacao(servico_estacao_inicial);
                                myAgent.doDelete();
                            }
                        }
                        // Caso não tenha estação final escolhida e tenha recebido uma mensagem INFORM do AI
                        else if (servico_estacao_final == null && msg.getPerformative() == ACLMessage.INFORM) {
                            InformAE_Array tmp = (InformAE_Array) msg.getContentObject();
                            ArrayList<InformAE> estacoes_list = tmp.getList();

                            Random r = new Random();
                            int ind = r.nextInt(estacoes_list.size());
                            ae_final = estacoes_list.get(ind);

                            DFAgentDescription template = new DFAgentDescription();
                            ServiceDescription sd = new ServiceDescription();
                            sd.setName("Estacao" + ae_final.getNum());
                            template.addServices(sd);

                            servico_estacao_final = DFService.search(myAgent, template);

                            if (servico_estacao_final.length > 0) {
                                // Falar com esta��o final
                                ACLMessage mensagem = createAclMessage(ACLMessage.REQUEST, utilizador, servico_estacao_final);

                                estacao_final = ae_final.getAgent();

                                myAgent.send(mensagem);
                            }
                            else {
                                cancelar_estacao(servico_estacao_inicial);
                                myAgent.doDelete();
                            }
                        }
                        else if (servico_estacao_final != null && msg.getPerformative() == ACLMessage.INFORM) {
                            utilizador.setEstacaoFinal(ae_final.getLocalizacao());
                            ACLMessage mensagem = createAclMessage(ACLMessage.REQUEST, utilizador, servico_estacao_final);

                            myAgent.send(mensagem);
                        }
                        else if (msg.getPerformative() == ACLMessage.PROPOSE) {
                            double preco_oferecido_estacao = (double) msg.getContentObject();
                            double preco_calculado_user = getActionPrice(utilizador, utilizador.getPf(), preco_oferecido_estacao);

                            if (preco_calculado_user >= preco_oferecido_estacao) {
                                utilizador.setPreco(preco_oferecido_estacao);
                                utilizador.setEm_viagem(true);

                                ACLMessage reply = createAclMessage(ACLMessage.CONFIRM, null, servico_estacao_inicial);
                                myAgent.send(reply);

                                // Confirmar aluguer ao AI
                                ACLMessage reply_ai = createAclMessage(ACLMessage.INFORM, utilizador, result_ai);
                                myAgent.send(reply_ai);

                                myAgent.addBehaviour(new Reply());
                                myAgent.addBehaviour(new InformPosition(myAgent, AI.period));
                                myAgent.removeBehaviour(this);
                            }
                            else {
                                DFAgentDescription[] array = new DFAgentDescription[servico_estacao_inicial.length + servico_estacao_final.length];
                                System.arraycopy(servico_estacao_inicial, 0, array, 0, servico_estacao_inicial.length);
                                System.arraycopy(servico_estacao_final, 0, array, servico_estacao_inicial.length, servico_estacao_final.length);
                                cancelar_estacao(array);
                                myAgent.doDelete();
                            }
                        }
                        else if (msg.getPerformative() == ACLMessage.FAILURE) {
                            if (servico_estacao_inicial == null) {
                                servico_estacao_inicial = new DFAgentDescription[0];
                            }
                            if (servico_estacao_final == null) {
                                servico_estacao_final = new DFAgentDescription[0];
                            }

                            DFAgentDescription[] array = new DFAgentDescription[servico_estacao_inicial.length + servico_estacao_final.length];
                            System.arraycopy(servico_estacao_inicial, 0, array, 0, servico_estacao_inicial.length);
                            System.arraycopy(servico_estacao_final, 0, array, servico_estacao_inicial.length, servico_estacao_final.length);
                            cancelar_estacao(array);

                            myAgent.doDelete();
                        }
                        else if (msg.getPerformative() != ACLMessage.UNKNOWN) {
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.UNKNOWN);
                            myAgent.send(reply);
                        }
                    }
                    else {
                        block();
                    }
                }
            } catch (FIPAException | IOException | UnreadableException e) {
                e.printStackTrace();
            }
        }

        private ACLMessage createAclMessage(int perf, Serializable ae_inicial, DFAgentDescription[] addresses) throws IOException {
            ACLMessage mensagem;
            mensagem = new ACLMessage(perf);
            mensagem.setContentObject(ae_inicial);

            for (DFAgentDescription df : addresses) {
                mensagem.addReceiver(df.getName());
            }

            return mensagem;
        }

        private void cancelar_estacao(DFAgentDescription[] servico_estacao) {
            ACLMessage mensagem;
            int i;
            mensagem = new ACLMessage(ACLMessage.CANCEL);
            for (i = 0; i < servico_estacao.length; ++i) {
                mensagem.addReceiver(servico_estacao[i].getName());
            }

            myAgent.send(mensagem);
        }
    }


    private class Reply extends CyclicBehaviour {
        private final HashMap<AID, InformAE> estacoes_lista_ape = new HashMap<>();
        private List<AID> estacoes_negociacao = new ArrayList<>();

        public void action() {

            if (utilizador.getPergentagem() > 0.75) {
                ACLMessage msg = receive();
                if (msg != null) {
                    try {
                        ACLMessage reply = msg.createReply();

                        if (msg.getPerformative() == ACLMessage.INFORM && utilizador.isDesconto_aceite()) {
                            reply.setPerformative(ACLMessage.CANCEL);
                            myAgent.send(reply);
                        }
                        else if (msg.getPerformative() == ACLMessage.INFORM && estacoes_negociacao.contains(msg.getSender())) {
                            // Saber se estacao aceitou proposta
                            utilizador.setAceitarDesconto(true);
                            ACLMessage mensagem = new ACLMessage(ACLMessage.CANCEL);
                            mensagem.addReceiver(estacao_final);
                            myAgent.send(mensagem);

                            estacao_final = msg.getSender();
                            utilizador.setEstacaoFinal(estacoes_lista_ape.get(msg.getSender()).getLocalizacao());

                            estacoes_negociacao = new ArrayList<>();

                            AI.user_new_dest.getAndIncrement();

                            // System.out.println(getLocalName() + ": Estacao " + estacao_final.getName() + " novo fim");
                        }
                        else if (msg.getPerformative() == ACLMessage.FAILURE) {
                            // Saber se estacao rejeitou proposta
                            estacoes_negociacao.remove(msg.getSender());
                        }
                        else if (msg.getPerformative() == ACLMessage.INFORM) {
                            // Receber lista de esta��es na APE
                            InformAE_Array temp = (InformAE_Array) msg.getContentObject();
                            try {
                                reply.setPerformative(ACLMessage.INFORM);
                                reply.setContentObject(utilizador);

                                for (InformAE e : temp.getList()) {
                                    estacoes_lista_ape.put(e.getAgent(), e);
                                }
                                for (AID a : estacoes_lista_ape.keySet()) {
                                    reply.addReceiver(a);
                                }
                            } catch (Exception e) {
                                return;
                            }
                        }
                        else if (msg.getPerformative() == ACLMessage.PROPOSE && !utilizador.isDesconto_aceite()) {
                            // Preco proposto
                            if (estacoes_lista_ape.containsKey(msg.getSender())) {
                                InformAE estacao_prop = estacoes_lista_ape.get(msg.getSender());

                                double preco = (double) msg.getContentObject();
                                double preco_calculado_user = getActionPrice(utilizador, estacao_prop.getLocalizacao(), preco);

                                if(!estacoes_negociacao.contains(estacao_prop.getAgent()))
                                    estacoes_negociacao.add(estacao_prop.getAgent());

                                try {
                                    if (preco_calculado_user >= preco) {
                                        reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                                        //System.out.println(getLocalName() + ": Estacao " + estacao_prop.getNum() + " oferece desconto e aceita");
                                    }
                                    else if (preco_calculado_user > 0) {
                                        reply.setPerformative(ACLMessage.PROPOSE);
                                        reply.setContentObject(preco_calculado_user);
                                        estacoes_negociacao.add(estacao_prop.getAgent());
                                        // System.out.println(getLocalName() + ": Estacao " + estacao_prop.getNum() + " oferece desconto e contra-proposta");
                                    }
                                    else {
                                        reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                                        // System.out.println(getLocalName() + ": Estacao " + estacao_prop.getNum() + " oferece desconto e rejeito");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        else if (msg.getPerformative() != ACLMessage.UNKNOWN) {
                            reply.setPerformative(ACLMessage.UNKNOWN);
                        }
                        myAgent.send(reply);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else {
                    block();
                }
            }

            if (utilizador.getPergentagem() >= 1) {
                // Confirmar a viagem

                // Confirmar � AE
                utilizador.setEm_viagem(false);
                ACLMessage msg = new ACLMessage(ACLMessage.CONFIRM);
                msg.addReceiver(estacao_final);

                try {
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setName("Interface");
                    template.addServices(sd);

                    DFAgentDescription[] result = DFService.search(myAgent, template);

                    // Confirmar ao AI
                    if (result.length > 0) {
                        for (DFAgentDescription dfAgentDescription : result) {
                            msg.addReceiver(dfAgentDescription.getName());
                        }
                    }
                    myAgent.send(msg);

                    AI.alugueres_feitos.getAndIncrement();
                    myAgent.doDelete();

                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class InformPosition extends TickerBehaviour {
        private Instant ant;

        public InformPosition(Agent a, long period) {
            super(a, period);
            ant = Instant.now();
        }

        @Override
        protected void onTick() {
            Instant now = Instant.now();

            if (utilizador.isEm_viagem() && utilizador.getPergentagem() < 1) {

                try {
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setName("Interface");
                    template.addServices(sd);
                    DFAgentDescription[] result = DFService.search(myAgent, template);


                    utilizador.incrementarDistancia((double) Duration.between(ant, now).toNanos() / (nano_to_hour / AI.system_speed.get()));

                    if (result.length > 0) {
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.setContentObject(utilizador);

                        for (DFAgentDescription dfAgentDescription : result) {
                            msg.addReceiver(dfAgentDescription.getName());
                        }
                        myAgent.send(msg);
                    }
                }
                catch (FIPAException | IOException e) {
                    e.printStackTrace();
                }
            }
            ant = now;
        }
    }
}
