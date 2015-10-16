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

    public ProgrammaticDriver() throws Exception {
        drone = new DroneController("192.168.2.1", 44444,
                new HandshakeRequest("JumpingSumo-b152",
                        "_arsdk-0902._udp")) {
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
        drone.left(180);
        System.exit(0);
    }
}
