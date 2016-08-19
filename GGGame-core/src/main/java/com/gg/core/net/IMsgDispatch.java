package com.gg.core.net;

import com.gg.core.net.codec.Net;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public interface IMsgDispatch {
    void process(Net.Request request);
}
