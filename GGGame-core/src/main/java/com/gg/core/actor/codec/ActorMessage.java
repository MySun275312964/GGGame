package com.gg.core.actor.codec;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorMessage {
    private int sid;

    private int sender;
    private int receiver;

    private int type;

    private Message msg;

    public ActorMessage() {}

    public ActorMessage(int sid, int receiver, int sender, int type, Message msg) {
        this.receiver = receiver;
        this.sender = sender;
        this.sid = sid;
        this.type = type;
        this.msg = msg;
    }

    public int getReceiver() {
        return receiver;
    }

    public int getSender() {
        return sender;
    }

    public int getSid() {
        return sid;
    }

    public int getType() {
        return type;
    }

    public Message getMsg() {
        return msg;
    }
}
