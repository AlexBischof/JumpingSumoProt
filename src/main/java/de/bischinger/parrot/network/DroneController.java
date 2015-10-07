package de.bischinger.parrot.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

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
        System.out.println("Sending command: " + Arrays.toString(packetAsBytes));

        DatagramPacket packet = new DatagramPacket(packetAsBytes, packetAsBytes.length,
                                                   getByName(deviceIp), controller2DevicePort);

        controller2DeviceSocket.send(packet);
    }

    public DroneController pcmd(int speed, int turn) throws IOException {
        this.sendCommand(
            new byte[] { 0x02, 0x0a, nonackCounter++, 0x0e, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00, 0x01,
                (byte) speed,
                (byte) turn });
        return this;
    }

    public DroneController jump() throws IOException {
        this.sendCommand(
            new byte[] { 0x04, 0x0b, ackCounter++, 0x0f, 0x00, 0x00, 0x00, 0x03, 0x02, 0x03, 0x00, 0x01, 0x00, 0x00,
                0x00 });
        return this;
    }
}
