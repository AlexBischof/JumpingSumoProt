package de.bischinger.parrot.network.commands;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public interface Command {
    byte[] getBytes(int counter);
}
