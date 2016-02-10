package de.bischinger.parrot.network.commands.js;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class JumpMotorProblemChanged implements Command {
    private CommandKey commandKey = CommandKey.commandKey(3, 3, 2);

    public static JumpMotorProblemChanged jumpMotorProblemChanged() {
        return new JumpMotorProblemChanged();
    }

    @Override
    public byte[] getBytes(int counter) {
        return new byte[]{
                (byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(),
                (byte) counter,
                15, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                1, 0, 0, 0};
    }
}
