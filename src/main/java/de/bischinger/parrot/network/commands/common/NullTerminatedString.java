package de.bischinger.parrot.network.commands.common;

import java.io.UnsupportedEncodingException;

/**
 * Created by alexanderbischof on 21.10.15.
 */
public class NullTerminatedString {
    private String string;

    public NullTerminatedString(String string) {
        this.string = string;
    }
    public byte[] getNullTerminatedString() {
        try {
            byte[] stringBytes = string.getBytes("UTF-8");
            byte[] ntBytes = new byte[stringBytes.length + 1];
            System.arraycopy(stringBytes, 0, ntBytes, 0, stringBytes.length);
            return ntBytes;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
