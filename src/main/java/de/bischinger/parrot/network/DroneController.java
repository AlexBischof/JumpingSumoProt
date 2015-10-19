package de.bischinger.parrot.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static de.bischinger.parrot.network.Command.*;
import static java.lang.String.format;
import static java.net.InetAddress.getByName;

/**
 * Created by Alexander Bischof on 04.10.15.
 */
public class DroneController implements AutoCloseable {

    private Logger logger = Logger.getLogger("DroneController");

    private DatagramSocket controller2DeviceSocket;
    private String deviceIp;
    private int controller2DevicePort;

    private byte nonackCounter = 0;
    private byte ackCounter = 0;
    private List<EventListener> eventListeners = new ArrayList<>();

    private Set<String> loggingSet = new HashSet<>();

    public DroneController(String deviceIp, int tcpPort, HandshakeRequest handshakeRequest) throws Exception {

        logger.info(format("Creating DroneController for %s:%s...", deviceIp, tcpPort));
        this.deviceIp = deviceIp;
        HandshakeAnswer handshakeAnswer = new TcpHandshake(deviceIp, tcpPort).shake(handshakeRequest);
        logger.info(format("Handshake completed with %s", handshakeAnswer));
        controller2DevicePort = handshakeAnswer.getC2d_port();

        controller2DeviceSocket = new DatagramSocket();

        this.sendCommand(Command.AllStates.getCommand(ackCounter++));

        addAnswerSocket();
    }

    private void addAnswerSocket() {

        new Thread(() -> {
            try (DatagramSocket sumoSocket = new DatagramSocket(controller2DevicePort)) {
                logger.info(format("Listing for answers on port %s", controller2DevicePort));
                int pingCounter = 0;
                while (true) {
                    byte[] buf = new byte[512];

                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    sumoSocket.receive(packet);
                    byte[] data = packet.getData();

                    //Sends pong every 4 pings
                    //FIXME
                    CommandReader commandReader = CommandReader.commandReader(data);
                    if (commandReader.isPing() && pingCounter++ % 4 == 0) {
                        pcmd(0, 0);
                    }

                    if (commandReader.isPing()) {
                        continue;
                    }

                    //logIncoming(data);

                    eventListeners.stream().forEach(eventListener -> eventListener.eventFired(data));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void logIncoming(byte[] data) {

        boolean add = loggingSet.add(Arrays.toString(data));
        if (add) {
            for (int i = 0; i < 10; i++) {
                System.out.print(data[i] + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void close() throws Exception {
        controller2DeviceSocket.close();
    }

    public void sendCommand(byte[] packetAsBytes)
            throws IOException {

        DatagramPacket packet = new DatagramPacket(packetAsBytes, packetAsBytes.length,
                getByName(deviceIp), controller2DevicePort);
        logger.fine(format("Sending command: %s", Arrays.toString(packetAsBytes)));
        controller2DeviceSocket.send(packet);

        postSend();
    }

    protected void postSend() {
    }

    private DroneController pong(byte[] data) throws IOException {
        data[1] = 1;
        // data[2] = pongCounter++;
        this.sendCommand(data);
        return this;
    }

    public DroneController forward() throws IOException {
        this.sendCommand(PCMD.getCommand(++nonackCounter, 40, 0));
        return this;
    }

    public DroneController backward() throws IOException {
        this.sendCommand(PCMD.getCommand(++nonackCounter, -40, 0));
        return this;
    }

    public DroneController left() throws IOException {
        this.sendCommand(PCMD.getCommand(++nonackCounter, 0, -25));
        return this;
    }

    public DroneController left(int degrees) throws IOException {
        degrees = degrees % 180;
        this.sendCommand(PCMD.getCommand(++nonackCounter, 0, -25 * degrees / 90));
        return this;
    }

    public DroneController right(int degrees) throws IOException {
        degrees = degrees % 180;
        this.sendCommand(PCMD.getCommand(++nonackCounter, 0, 25 * degrees / 90));
        return this;
    }

    public DroneController right() throws IOException {
        this.sendCommand(PCMD.getCommand(++nonackCounter, 0, 25));
        return this;
    }

    public DroneController pcmd(int speed, int turn) throws IOException {
        this.sendCommand(PCMD.getCommand(++nonackCounter, speed, turn));
        return this;
    }

    public DroneController jump(boolean high) throws IOException {
        this.sendCommand(JUMP.getCommand(++ackCounter, high ? 1 : 0));
        return this;
    }

    public DroneController stopAnnimation() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 0));
        return this;
    }

    public DroneController spin() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 1));
        return this;
    }

    public DroneController tap() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 2));
        return this;
    }

    public DroneController slowshake() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 3));
        return this;
    }

    public DroneController metronome() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 4));
        return this;
    }

    public DroneController ondulation() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 5));
        return this;
    }

    public DroneController spinjump() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 6));
        return this;
    }

    public DroneController spintoposture() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 7));
        return this;
    }

    public DroneController spiral() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 8));
        return this;
    }

    public DroneController slalom() throws IOException {
        this.sendCommand(SimpleAnimation.getCommand(++ackCounter, 9));
        return this;
    }

    public AudioController audio() {
        return new AudioController();
    }


    public DroneController addCriticalBatteryListener(Consumer<BatteryState> consumer) {
        this.eventListeners.add(data -> {
            if (filterProject(data, 3, 1, 1)) {
                consumer.accept(BatteryState.values()[data[11]]);
            }
        });
        return this;
    }

    public DroneController addBatteryListener(Consumer<Byte> consumer) {
        this.eventListeners.add(data -> {
            if (filterProject(data, 0, 5, 1)) {
                consumer.accept(data[11]);
            }
        });
        return this;
    }

    public DroneController addPCMDListener(Consumer<String> consumer) {
        this.eventListeners.add(data -> {
            if (filterProject(data, 3, 1, 0)) {
                consumer.accept("" + data[11]);
            }
        });
        return this;
    }

    private boolean filterChannel(byte[] data, int frametype, int channel) {
        return data[0] == frametype && data[1] == channel;
    }

    private boolean filterProject(byte[] data, int project, int clazz, int cmd) {
        return data[7] == project && data[8] == clazz && data[9] == cmd;
    }

    public DroneController outdoorSpeedMax() throws IOException {
        sendCommand(OutdoorSpeed.getCommand(++ackCounter, 1));
        return this;
    }

    public DroneController outdoorSpeedDefault() throws IOException {
        sendCommand(OutdoorSpeed.getCommand(++ackCounter, 0));
        return this;
    }

    public class AudioController {
        public AudioController robotTheme() throws IOException {
            sendCommand(AudioTheme.getCommand(++ackCounter, 1));
            return this;
        }

        public AudioController insectTheme() throws IOException {
            sendCommand(AudioTheme.getCommand(++ackCounter, 2));
            return this;
        }

        public AudioController monsterTheme() throws IOException {
            sendCommand(AudioTheme.getCommand(++ackCounter, 3));
            return this;
        }

        public AudioController mute() throws IOException {
            sendCommand(Volume.getCommand(++ackCounter, 0));
            return this;
        }

        public AudioController unmute() throws IOException {
            sendCommand(Volume.getCommand(++ackCounter, 100));
            return this;
        }
    }
}
