package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;
import de.bischinger.parrot.network.HandshakeRequest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Alexander Bischof on 10.10.15.
 */
public class FileBasedProgrammaticDriver {

    public final static String FILENAME = "programm.txt";

    private DroneController drone;

    public FileBasedProgrammaticDriver() throws Exception {
        drone = new DroneController("192.168.2.1", 44444,
                new HandshakeRequest("JumpingSumo-b152298",
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
        drone.addBatteryListener(b -> System.out.println("BatteryState: " + b));
        drone.pcmd(0, 0);

        new Thread(this::startFileWatcher).start();

        readCommands();
    }

    private void startFileWatcher() {
        try (final WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(new File("").toURI());
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            System.out.println("Programm gestartet: Schaue auf " + Paths.get(FILENAME));
            while (true) {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    final Path changed = (Path) event.context();
                      System.out.println(changed + " " +changed.equals(FILENAME) + " " + FILENAME);
                    if (changed.endsWith(FILENAME)) {
                            System.out.println("My file has changed");
                        readCommands();
                    }
                }

                boolean valid = wk.reset();
                if (!valid) {
                    System.out.println("Key has been unregisterede");
                }
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void readCommands() throws URISyntaxException, IOException {
        List<String> commands = Files.readAllLines(Paths.get(FILENAME));
        commands.forEach(command -> {
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
                            drone.left(-90*degrees/25);
                        } else if (lowercaseCommand.startsWith("rechts")) {
                            int degrees = parseInt(lowercaseCommand.split(" ")[1]);
                            drone.right(90 * degrees/25);
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
