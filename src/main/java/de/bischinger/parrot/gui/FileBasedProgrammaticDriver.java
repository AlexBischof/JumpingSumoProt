package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;
import de.bischinger.parrot.network.HandshakeRequest;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Created by Alexander Bischof on 10.10.15.
 */
public class FileBasedProgrammaticDriver {

    public final static String FILENAME = "programm.txt";

    private DroneController drone;

    public FileBasedProgrammaticDriver(String ip, int port, String sumoWlan) throws Exception {
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
                    //System.out.println(changed + " " + changed.equals(FILENAME) + " " + FILENAME);
                    if (changed.endsWith(FILENAME)) {
                        System.out.println("programm.txt has changed");
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
        commands.forEach(new CommandInputConsumer(drone));
    }
}
