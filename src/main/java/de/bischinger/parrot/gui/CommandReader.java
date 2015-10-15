package de.bischinger.parrot.gui;

/**
 * Created by Alexander Bischof on 11.10.15.
 */
public class CommandReader {
    private byte[] data;

    public CommandReader(byte[] data) {
        this.data = data;
    }

    public static CommandReader commandReader(byte[] data) {
        return new CommandReader(data);
    }

    public boolean isPing() {
        return data[0] == 2 && data[1] == 0;
    }
}
