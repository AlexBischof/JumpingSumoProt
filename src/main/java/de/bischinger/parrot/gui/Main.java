package de.bischinger.parrot.gui;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import static java.lang.Integer.valueOf;

public class Main {

    public static void main(String[] args) throws Exception {

        String ip = "192.168.2.1";
        int port = 44444;
        String wlan = "JumpingSumo-b169798";

        File configFile = new File("config.properties");
        if (configFile.exists()){
            Properties properties = new Properties();
            properties.load(new FileReader(configFile));
            ip = properties.getProperty("ip");
            port = Integer.valueOf(properties.getProperty("port"));
            wlan = properties.getProperty("wlan");
        }

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "keyboard":
                    int speedConfig = args.length > 1 ? valueOf(args[1]) : 100;
                    int turnConfig = args.length > 2 ? valueOf(args[2]) : 30;
                    new KeyboardDriver(ip, port, wlan, speedConfig, turnConfig);
                    break;
                case "program":
                    new ProgrammaticDriver(ip, port, wlan).drive();
                    break;
                case "file":
                    new FileBasedProgrammaticDriver(ip, port, wlan);
                    break;
                case "swing":
                    new SwingBasedProgrammaticDriver(ip, port, wlan);
                    break;
                default:
                    System.out.println("Argument unbekannt: keyboard | program | file | swing");
            }
        } else {
            new KeyboardDriver(ip, port, wlan, 100, 30);
        }
    }
}