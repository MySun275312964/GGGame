package com.gg.core.harbor;

import com.gg.core.harbor.protocol.HarborOuterClass.HarborMessage;

/**
 * @author guofeng.qin
 */
public interface IHarborHandler {
    void onMessage(HarborMessage msg, ResponseCallback response);
}
