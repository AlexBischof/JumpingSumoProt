package de.bischinger.parrot.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.TimeUnit;

import static de.bischinger.parrot.gui.Command.JUMP;
import static de.bischinger.parrot.gui.Command.PCMD;
import static de.bischinger.parrot.gui.Command.SimpleAnimation;
import static java.net.InetAddress.getByName;

/**
 * Created by Alexander Bischof on 04.10.15.
 */
public class DroneController implements AutoCloseable {

    private DatagramSocket controller2DeviceSocket;
    private String deviceIp;
    private int controller2DevicePort;

    private byte nonackCounter = 0;
    private byte ackCounter = 0;

    public DroneController(String deviceIp, int tcpPort, HandshakeRequest handshakeRequest) throws Exception {

        this.deviceIp = deviceIp;
        HandshakeAnswer handshakeAnswer = new DoTcpHandshake(deviceIp, tcpPort).shake(handshakeRequest);
        controller2DevicePort = handshakeAnswer.getC2d_port();

        controller2DeviceSocket = new DatagramSocket();
    }

    @Override
    public void close() throws Exception {
        controller2DeviceSocket.close();
    }

    public void sendCommand(byte[] packetAsBytes)
        throws IOException {
        // System.out.println("Sending command: " + Arrays.toString(packetAsBytes));

        DatagramPacket packet = new DatagramPacket(packetAsBytes, packetAsBytes.length,
                                                   getByName(deviceIp), controller2DevicePort);

        controller2DeviceSocket.send(packet);

        postSend();
    }

    protected void postSend() {
    }

    public DroneController forward() throws IOException, InterruptedException {
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

    public DroneController turn180() throws IOException {
        this.sendCommand(PCMD.getCommand(++nonackCounter, 0, 50));
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
}
