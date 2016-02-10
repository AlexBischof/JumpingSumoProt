package de.bischinger.parrot.network.commands.js;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class Jump implements Command {
    private CommandKey commandKey = CommandKey.commandKey(3, 2, 3);

    public enum Type {
        Long, High;
    }

    private Type type;

    public Jump(Type type) {
        this.type = type;
    }

    public static Jump jump(Type type) {
        return new Jump(type);
    }

    @Override
    public byte[] getBytes(int counter) {
        return new byte[]{
                (byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(),
                (byte) counter,
                15, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                (byte) type.ordinal(), 0, 0, 0};
    }
}
