package com.gg.core.net;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by guofeng.qin on 2016/8/19.
 */
public class GameNetConfig {
    private GameNetConfig() {

    }

    private static class Holder {
        private static final GameNetConfig Instance = new GameNetConfig();
    }

    public static GameNetConfig getInstance() {
        return Holder.Instance;
    }

    /**
     * parent 事件循环线程, 负责处理端口的监听及注册等
     */
    private EventLoopGroup parentGroup;
    /**
     * child 事件循环线程, 负责处理连接的可读可写等事件
     */
    private EventLoopGroup childGroup;
    /**
     * Server执行事件循环对应的实现类
     */
    private Class<? extends ServerChannel> evtLoopClass;
    /**
     * Client执行事件循环对应的实现类
     */
    private Class<? extends Channel> clientEvtLoopClass;

    /**
     * 初始化Netty事件循环体系
     *
     * @param bossNum parent事件循环的线程数，参考 {@link EventLoopGroup}
     * @param childNum child事件循环的线程数，参考 {@link EventLoopGroup}
     * @param useNativeEpoll 是否是用Epoll扩展，参考 {@link EpollEventLoopGroup}
     */
    public void init(int bossNum, int childNum, boolean useNativeEpoll) {
        if (useNativeEpoll && Epoll.isAvailable()) {
            parentGroup = new EpollEventLoopGroup(bossNum);
            childGroup = new EpollEventLoopGroup(childNum);
            evtLoopClass = EpollServerSocketChannel.class;
            clientEvtLoopClass = EpollSocketChannel.class;
        } else {
            parentGroup = new NioEventLoopGroup(bossNum);
            childGroup = new NioEventLoopGroup(childNum);
            evtLoopClass = NioServerSocketChannel.class;
            clientEvtLoopClass = NioSocketChannel.class;
        }
    }

    public EventLoopGroup getParentGroup() {
        return parentGroup;
    }

    public EventLoopGroup getChildGroup() {
        return childGroup;
    }

    public Class<? extends ServerChannel> getEvtLoopClass() {
        return evtLoopClass;
    }

    public Class<? extends Channel> getClientEvtLoopClass() {
        return clientEvtLoopClass;
    }
}
