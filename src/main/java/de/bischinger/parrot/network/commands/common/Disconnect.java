package de.bischinger.parrot.network.commands.common;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class Disconnect implements Command {

    private CommandKey commandKey = CommandKey.commandKey(0, 0, 0);

    public static Disconnect disconnect() {
        return new Disconnect();
    }

    @Override
    public byte[] getBytes(int counter) {

        return new byte[]{(byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(), (byte) counter, 15, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0};

    }
}
