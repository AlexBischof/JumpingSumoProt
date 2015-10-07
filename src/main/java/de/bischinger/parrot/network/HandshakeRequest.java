package de.bischinger.parrot.network;

/**
 * Created by Alexander Bischof on 21.09.15.
 */
public class HandshakeRequest {
    private String controller_name;
    private String controller_type;
    private int d2c_port = 54321;

    public HandshakeRequest(String controller_name, String controller_type) {
        this.controller_name = controller_name;
        this.controller_type = controller_type;
    }

    public String getController_name() {
        return controller_name;
    }

    public void setController_name(String controller_name) {
        this.controller_name = controller_name;
    }

    public String getController_type() {
        return controller_type;
    }

    public void setController_type(String controller_type) {
        this.controller_type = controller_type;
    }

    public int getD2c_port() {
        return d2c_port;
    }

    public void setD2c_port(int d2c_port) {
        this.d2c_port = d2c_port;
    }

    @Override
    public String toString() {
        return "DeviceInit{" +
               "controller_name='" + controller_name + '\'' +
               ", controller_type='" + controller_type + '\'' +
               ", d2c_port=" + d2c_port +
               '}';
    }
}
