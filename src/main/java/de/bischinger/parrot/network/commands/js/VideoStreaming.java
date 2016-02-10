package de.bischinger.parrot.network.commands.js;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class VideoStreaming implements Command {
    private CommandKey commandKey = CommandKey.commandKey(3, 18, 0);

    private byte enable;

    public VideoStreaming(byte enable) {
        this.enable = enable;
    }

    public static VideoStreaming videoStreamingEnable() {
        return new VideoStreaming((byte) 1);
    }

    public static VideoStreaming videoStreamingDisable() {
        return new VideoStreaming((byte) 0);
    }

    @Override
    public byte[] getBytes(int counter) {
        return new byte[]{
                (byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(),
                (byte) counter,
                12, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                enable, 0};
    }
}
