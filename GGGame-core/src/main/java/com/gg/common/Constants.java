package com.gg.common;

import com.gg.core.net.IMsgDispatch;
import io.netty.util.AttributeKey;

/**
 * @author guofeng.qin
 */
public class Constants {
    public static final String Localhost = "0.0.0.0";
    public static final int MasterPort = 19999; // master服务的端口号
    public static final int GatePort = 19998; // gate 服务的端口号

    public static final String Gate = "gate";
    public static final String Master = "master";


    public static final class Net {
        public static final int RPC_TIMEOUT = 50;
        public static final int DefaultHeadLength = 4;

        public static final AttributeKey<IMsgDispatch> DispatchKey = AttributeKey.valueOf("DispatchKey");
    }
}
