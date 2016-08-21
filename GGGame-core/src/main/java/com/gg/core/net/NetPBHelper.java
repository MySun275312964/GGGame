package com.gg.core.net;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

/**
 * Created by hunter on 8/21/16.
 */
public class NetPBHelper {
    public static boolean parseJson(String json, Message.Builder builder) {
        JsonFormat.Parser parser = JsonFormat.parser();
        try {
            parser.merge(json, builder);
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return false;
    }
}
