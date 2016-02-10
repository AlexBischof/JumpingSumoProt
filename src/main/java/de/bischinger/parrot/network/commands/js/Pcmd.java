package de.bischinger.parrot.network.commands.js;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class Pcmd implements Command {
    private CommandKey commandKey = CommandKey.commandKey(3, 0, 0);
    private byte speed;
    private byte turn;

    public Pcmd(int speed, int turn) {
        this.speed = (byte) speed;
        this.turn = (byte) turn;
    }

    public static Pcmd pcmd(int speed, int turn) {
        return new Pcmd(speed, turn);
    }

    @Override
    public byte[] getBytes(int counter) {
        byte touchscreen = 1;
        return new byte[]{(byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_NONACK_ID.getId(),
                (byte) counter,
                14, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                touchscreen, speed, turn};
    }
}
