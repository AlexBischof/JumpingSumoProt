package de.bischinger.parrot.gui;

import static java.lang.Integer.valueOf;

public class Main {

    public static void main(String[] args) throws Exception {

        String ip = "192.168.2.1";
        int port = 44444;
        String sumoWlan = "JumpingSumo-b169798";

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "keyboard":
                    int speedConfig = args.length > 1 ? valueOf(args[1]) : 100;
                    int turnConfig = args.length > 2 ? valueOf(args[2]) : 30;
                    new KeyboardDriver(ip, port, sumoWlan, speedConfig, turnConfig);
                    break;
                case "program":
                    new ProgrammaticDriver(ip, port, sumoWlan).drive();
                    break;
                case "file":
                    new FileBasedProgrammaticDriver(ip, port, sumoWlan);
                    break;
                case "swing":
                    new SwingBasedProgrammaticDriver(ip, port, sumoWlan);
                    break;
                default:
                    System.out.println("Argument unbekannt: keyboard | program | file | swing");
            }
        } else {
            new KeyboardDriver(ip, port, sumoWlan, 100, 30);
        }
    }
}