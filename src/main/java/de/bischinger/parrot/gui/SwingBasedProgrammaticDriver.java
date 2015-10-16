package de.bischinger.parrot.gui;

import de.bischinger.parrot.network.DroneController;
import de.bischinger.parrot.network.HandshakeRequest;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.SOUTH;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Stream.of;

/**
 * Created by Alexander Bischof on 10.10.15.
 */
public class SwingBasedProgrammaticDriver extends JFrame {

    private DroneController drone;
    private JTextArea textArea;

    public SwingBasedProgrammaticDriver(String ip, int port, String sumoWlan) throws Exception {
        drone = new DroneController(ip, port, new HandshakeRequest(sumoWlan,"_arsdk-0902._udp")){
            @Override
            protected void postSend() {
                try {
                    MILLISECONDS.sleep(580);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        setResizable(false);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setMinimumSize(new Dimension(700, 800));

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea("", 5, 10);
        textArea.setPreferredSize(new Dimension(600, 700));
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, CENTER);
        JButton jbStart = new JButton("Start");
        jbStart.addActionListener(e -> {
            String text = textArea.getText();
            of(text.split("\\r?\\n")).map(c -> c.toLowerCase().trim()).forEach(new CommandInputConsumer(drone));
        });
        this.add(jbStart, SOUTH);
        this.pack();
        setVisible(true);

        drone.addBatteryListener(b -> System.out.println("BatteryState: " + b));
        drone.addCriticalBatteryListener(b -> System.out.println("Critical-BatteryState: " + b));
        drone.addPCMDListener(b -> System.out.println("PCMD: " + b));
    }
}
