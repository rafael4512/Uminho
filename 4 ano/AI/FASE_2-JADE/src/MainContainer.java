import Agents.AI;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.util.concurrent.atomic.AtomicInteger;

public class MainContainer {
    Runtime rt;
    ContainerController container;

    public static void main(String[] args) {
        MainContainer a = new MainContainer();
        AtomicInteger no_estacoes = new AtomicInteger();

        try {
            a.initMainContainerInPlatform("localhost", "9888", "MainContainer");

            // First Open Central Agent - Manager
            a.startAgentInPlatform("Central", "Agents.AI", new Object[0]);

            // Provide some time for Agent to register in services
            Thread.sleep(100);

            // Start agents Stations!
            new Thread(() -> {
                for (no_estacoes.set(0); no_estacoes.get() < 20; no_estacoes.getAndIncrement()) {
                    Object[] obj = new Object[1];
                    int no_e = no_estacoes.get();
                    obj[0] = no_e;

                    a.startAgentInPlatform("Estação" + no_e, "Agents.AE", obj);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // Provide some time for Agents to register in services
            Thread.sleep(1000);

            // Start agents Customers!
            new Thread(() -> {
                for (int n = 0; n < 1500; n++) {
                    Object[] obj = new Object[1];
                    obj[0] = no_estacoes.get();

                    a.startAgentInPlatform("Utilizador" + n, "Agents.AU", obj);

                    try {
                        Thread.sleep(60000 / AI.system_speed.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initMainContainerInPlatform(String host, String port, String containerName) {
        // Get the JADE runtime interface (singleton)
        this.rt = Runtime.instance();

        // Create a Profile, where the launch arguments are stored
        Profile prof = new ProfileImpl();
        prof.setParameter(Profile.CONTAINER_NAME, containerName);
        prof.setParameter(Profile.MAIN_HOST, host);
        prof.setParameter(Profile.MAIN_PORT, port);
        prof.setParameter(Profile.MAIN, "true");
        prof.setParameter(Profile.GUI, "true");

        // create a main agent container
        this.container = rt.createMainContainer(prof);
        rt.setCloseVM(true);
    }

    public void startAgentInPlatform(String name, String classpath, Object[] args) {
        try {
            AgentController ac = container.createNewAgent(name, classpath, args);
            ac.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}