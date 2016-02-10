package de.bischinger.parrot.network.commands;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public enum ChannelType {
    JUMPINGSUMO_CONTROLLER_TO_DEVICE_NONACK_ID(10),
    JUMPINGSUMO_CONTROLLER_TO_DEVICE_ACK_ID(11),
    JUMPINGSUMO_CONTROLLER_TO_DEVICE_VIDEO_ACK_ID(13),
    JUMPINGSUMO_DEVICE_TO_CONTROLLER_NAVDATA_ID((256 / 2)- 1),
    JUMPINGSUMO_DEVICE_TO_CONTROLLER_EVENT_ID((256 / 2) - 2),
    JUMPINGSUMO_DEVICE_TO_CONTROLLER_VIDEO_DATA_ID((256 / 2) - 3);

    private final byte id;

    ChannelType(int id) {
        this.id = (byte) id;
    }

    public byte getId() {
        return id;
    }
}
