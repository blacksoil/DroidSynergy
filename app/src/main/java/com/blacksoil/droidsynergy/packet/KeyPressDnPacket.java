package com.blacksoil.droidsynergy.packet;

import com.blacksoil.droidsynergy.utils.Converter;
import com.blacksoil.droidsynergy.utils.GlobalLogger;

import java.util.List;

/**
 * Created by aharijanto on 9/22/14.
 */
public class KeyPressDnPacket extends ResponselessPacket {
    private final static String mType = "DKDN";
    private final static String mDescription = "KeyPress Down";
    private final static boolean DEBUG = true;

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public Packet getInstance(List<Byte> packets) {
        if (DEBUG) {
            // Ascii code of the pressed key
            int arg1 = Converter.intFrom16bit(packets.get(0), packets.get(1));
            // 0 if shift is off, 1 otherwise
            int arg2 = Converter.intFrom16bit(packets.get(2), packets.get(3));
            // 38?
            int arg3 = Converter.intFrom16bit(packets.get(4), packets.get(5));

            GlobalLogger.getInstance().getLogger().Logd("KeyPress Down. Arg1= " +
                    arg1 + ". Arg2=" + arg2 + ". Arg3=" + arg3);
        }
        return new KeyPressDnPacket();
    }

    @Override
    public String toString() {
        return mDescription;
    }
}
