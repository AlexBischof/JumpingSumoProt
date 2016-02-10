package de.bischinger.parrot.network.commands.common;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class Pong implements Command {

    public static Pong pong() {
        return new Pong();
    }

    @Override
    public byte[] getBytes(int counter) {
        return new byte[]{1, (byte) 0xfe, (byte) counter,8,0,0,0, (byte) counter};
    }
}
