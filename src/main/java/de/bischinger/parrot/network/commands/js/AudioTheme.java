package de.bischinger.parrot.network.commands.js;

import de.bischinger.parrot.network.commands.ChannelType;
import de.bischinger.parrot.network.commands.Command;
import de.bischinger.parrot.network.commands.CommandKey;
import de.bischinger.parrot.network.commands.FrameType;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class AudioTheme implements Command {
    private CommandKey commandKey = CommandKey.commandKey(3, 12, 1);


    private byte themeNr;

    public AudioTheme(int themeNr) {
        this.themeNr = (byte) themeNr;
    }

    public static AudioTheme audioTheme(int themeNr) {
        return new AudioTheme(themeNr);
    }

    @Override
    public byte[] getBytes(int counter) {
        return new byte[]{
                (byte) FrameType.ARNETWORKAL_FRAME_TYPE_DATA_WITH_ACK.ordinal(),
                ChannelType.JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID.getId(),
                (byte) counter,
                15, 0, 0, 0,
                commandKey.getProjectId(), commandKey.getClazzId(), commandKey.getCommandId(), 0,
                themeNr, 0, 0, 0};
    }
}
