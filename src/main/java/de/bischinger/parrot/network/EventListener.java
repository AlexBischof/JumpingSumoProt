package de.bischinger.parrot.network;

import java.io.IOException;

/**
 * Created by Alexander Bischof on 11.10.15.
 */
@FunctionalInterface
public interface EventListener {
    void eventFired(byte[] data);
}
