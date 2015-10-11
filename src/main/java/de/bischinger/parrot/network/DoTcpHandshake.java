package de.bischinger.parrot.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.net.Socket;

/**
 * Created by Alexander Bischof on 05.10.15.
 */
public class DoTcpHandshake implements AutoCloseable {
    private Socket tcpSocket;
    private PrintWriter tcpOut;
    private BufferedReader tcpIn;

    public DoTcpHandshake(String deviceIp, int tcpPort) throws IOException {
        tcpSocket = new Socket(deviceIp, tcpPort);
        tcpOut = new PrintWriter(tcpSocket.getOutputStream(), true);
        tcpIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
    }

    public HandshakeAnswer shake(HandshakeRequest handshakeRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        StringWriter shakeData = new StringWriter();
        objectMapper.writeValue(shakeData, handshakeRequest);
        return tcpHandshakeResult(shakeData.toString());
    }

    private HandshakeAnswer tcpHandshakeResult(String shakeData) throws IOException {
        //Send to device
        tcpOut.println(shakeData);

        //Reads json response
        String responseLine;
        HandshakeAnswer deviceAnswer = null;
        ObjectMapper objectMapper = new ObjectMapper();
        while ((responseLine = tcpIn.readLine()) != null) {
            System.out.println(responseLine);
            responseLine = responseLine.substring(0, responseLine.lastIndexOf("}") + 1);
            deviceAnswer = objectMapper.readValue(responseLine, HandshakeAnswer.class);
        }
        return deviceAnswer;
    }

    public void close() throws Exception {
        tcpOut.close();
        tcpSocket.close();
        tcpIn.close();
    }
}
