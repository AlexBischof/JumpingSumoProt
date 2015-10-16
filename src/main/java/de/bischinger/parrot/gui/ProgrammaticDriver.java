package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;
import de.bischinger.parrot.network.HandshakeRequest;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Alexander Bischof on 10.10.15.
 */
public class ProgrammaticDriver {
    private DroneController drone;

    public ProgrammaticDriver(String ip, int port, String sumoWlan) throws Exception {
        drone = new DroneController(ip, port, new HandshakeRequest(sumoWlan,"_arsdk-0902._udp")) {
            @Override
            protected void postSend() {
                try {
                    MILLISECONDS.sleep(580);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        drone.pcmd(0, 0);
    }

    public void drive() throws IOException, InterruptedException {
        drone.forward().forward().left(90);
        System.exit(0);
    }
}
