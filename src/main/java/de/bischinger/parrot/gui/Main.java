package de.bischinger.parrot.gui;

import static java.lang.Integer.valueOf;

public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "keyboard":
                    int speedConfig = args.length > 1 ? valueOf(args[1]) : 100;
                    int turnConfig = args.length > 2 ? valueOf(args[2]) : 30;
                    new KeyboardDriver(speedConfig, turnConfig);
                    break;
                case "program":
                    new ProgrammaticDriver().drive();
                    break;
                case "file":
                    new FileBasedProgrammaticDriver();
                    break;
                default:
                    System.out.println("Argument unbekannt: keyboard | program | file");
            }
        } else {
            new KeyboardDriver(100,30);
        }
    }
}