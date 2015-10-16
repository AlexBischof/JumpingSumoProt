package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;

import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

/**
 * Created by alexanderbischof on 16.10.15.
 */
public class CommandInputConsumer implements Consumer<String> {

    private DroneController drone;

    public CommandInputConsumer(DroneController drone) {
        this.drone = drone;
    }

    @Override
    public void accept(String command) {
        try {
            String lowercaseCommand = command.toLowerCase().trim();
            switch (lowercaseCommand) {
                case "vor":
                    drone.forward();
                    break;
                case "rechts":
                    drone.right();
                    break;
                case "links":
                    drone.left();
                    break;
                case "zurueck":
                    drone.backward();
                    break;
                case "springe hoch":
                    drone.jump(true);
                    break;
                case "springe weit":
                    drone.jump(false);
                    break;
                default:
                    //Handling for links/rechts mit winkel
                    if (lowercaseCommand.startsWith("links")) {
                        int degrees = parseInt(lowercaseCommand.split(" ")[1]);
                        drone.left(-90 * degrees / 25);
                    } else if (lowercaseCommand.startsWith("rechts")) {
                        int degrees = parseInt(lowercaseCommand.split(" ")[1]);
                        drone.right(90 * degrees / 25);
                    }else {
                        System.out.println("Kommando nicht implementiert: " + command);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
