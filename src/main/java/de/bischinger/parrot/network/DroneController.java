package de.bischinger.parrot.network;

import de.bischinger.parrot.network.commands.common.CurrentDate;
import de.bischinger.parrot.network.commands.common.CurrentTime;
import de.bischinger.parrot.network.commands.common.Disconnect;
import de.bischinger.parrot.network.commands.common.Pong;
import de.bischinger.parrot.network.commands.js.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static de.bischinger.parrot.network.commands.js.Jump.Type.High;
import static de.bischinger.parrot.network.commands.js.Jump.Type.Long;
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

        sendCommand(CurrentDate.currentDate().getBytes(ackCounter++));
        sendCommand(CurrentTime.currentTime().getBytes(ackCounter++));

        sendCommand(VideoStreaming.videoStreamingEnable().getBytes(ackCounter++));

        addAnswerSocket();
    }

    private void addAnswerSocket() {

        new Thread(() -> {
            try (DatagramSocket sumoSocket = new DatagramSocket(controller2DevicePort)) {
                logger.info(format("Listing for answers on port %s", controller2DevicePort));
                while (true) {
                    byte[] buf = new byte[65000];

                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    sumoSocket.receive(packet);
                    byte[] data = packet.getData();

                    if (data[1] == 126) {
                        sendCommand(Pong.pong().getBytes(data[3]));
                        continue;
                    }

                    if (data[1] == 125) {
                        // videoStream.write();
                        try (FileOutputStream fos = new FileOutputStream("video.jpeg")) {
                            fos.write(getJpegDate(data));
                        }
                        continue;
                    }

                    //FIXME
                    CommandReader commandReader = CommandReader.commandReader(data);
                    if (commandReader.isPing() || commandReader.isLinkQualityChanged() ||
                            commandReader.isWifiSignalChanged()) {
                        continue;
                    }

                    //System.out.println("---" + Arrays.toString(data));
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
        sendCommand(Disconnect.disconnect().getBytes(ackCounter++));
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

    public DroneController forward() throws IOException {
        this.sendCommand(Pcmd.pcmd(40, 0).getBytes(++nonackCounter));
        return this;
    }

    public DroneController backward() throws IOException {
        this.sendCommand(Pcmd.pcmd(-40, 0).getBytes(++nonackCounter));
        return this;
    }

    public DroneController left() throws IOException {
        this.sendCommand(Pcmd.pcmd(0, -25).getBytes(++nonackCounter));
        return this;
    }

    public DroneController left(int degrees) throws IOException {
        degrees = degrees % 180;
        this.sendCommand(Pcmd.pcmd(0, -25 * degrees / 90).getBytes(++nonackCounter));
        return this;
    }

    public DroneController right(int degrees) throws IOException {
        degrees = degrees % 180;
        this.sendCommand(Pcmd.pcmd(0, 25 * degrees / 90).getBytes(++nonackCounter));
        return this;
    }

    public DroneController right() throws IOException {
        this.sendCommand(Pcmd.pcmd(0, 25).getBytes(++nonackCounter));
        return this;
    }

    public DroneController pcmd(int speed, int turn) throws IOException {
        this.sendCommand(Pcmd.pcmd(speed, turn).getBytes(++nonackCounter));
        return this;
    }

    public DroneController jump(boolean high) throws IOException {
        this.sendCommand(Jump.jump(high ? High : Long).getBytes(++ackCounter));
        return this;
    }

    public DroneController stopAnnimation() throws IOException {
        this.sendCommand(StopAnimation.stopAnimation().getBytes(++ackCounter));
        return this;
    }

    public DroneController spin() throws IOException {
        this.sendCommand(Spin.spin().getBytes(++ackCounter));
        return this;
    }

    public DroneController tap() throws IOException {
        this.sendCommand(Tap.tap().getBytes(++ackCounter));
        return this;
    }

    public DroneController slowshake() throws IOException {
        this.sendCommand(SlowShake.slowShake().getBytes(++ackCounter));
        return this;
    }

    public DroneController metronome() throws IOException {
        this.sendCommand(Metronome.metronome().getBytes(++ackCounter));
        return this;
    }

    public DroneController ondulation() throws IOException {
        this.sendCommand(Ondulation.ondulation().getBytes(++ackCounter));
        return this;
    }

    public DroneController spinjump() throws IOException {
        this.sendCommand(SpinJump.spinJump().getBytes(++ackCounter));
        return this;
    }

    public DroneController spintoposture() throws IOException {
        this.sendCommand(SpinToPosture.spinToPosture().getBytes(++ackCounter));
        return this;
    }

    public DroneController spiral() throws IOException {
        this.sendCommand(Spiral.spiral().getBytes(++ackCounter));
        return this;
    }

    public DroneController slalom() throws IOException {
        this.sendCommand(Slalom.slalom().getBytes(++ackCounter));
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

    public DroneController addOutdoorSpeedListener(Consumer<String> consumer) {
        this.eventListeners.add(data -> {
            if (filterProject(data, 3, 17, 0)) {
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

    private byte[] getJpegDate(byte[] data) {
        byte[] jpegData = new byte[data.length];
        System.arraycopy(data, 12, jpegData, 0, data.length - 12);
        return jpegData;
    }

    public class AudioController {
        public AudioController robotTheme() throws IOException {
            sendCommand(AudioTheme.audioTheme(1).getBytes(++ackCounter));
            return this;
        }

        public AudioController insectTheme() throws IOException {
            sendCommand(AudioTheme.audioTheme(2).getBytes(++ackCounter));
            return this;
        }

        public AudioController monsterTheme() throws IOException {
            sendCommand(AudioTheme.audioTheme(3).getBytes(++ackCounter));
            return this;
        }

        public AudioController mute() throws IOException {
            sendCommand(Volume.volume(0).getBytes(++ackCounter));
            return this;
        }

        public AudioController unmute() throws IOException {
            sendCommand(Volume.volume(100).getBytes(++ackCounter));
            return this;
        }
    }
}
