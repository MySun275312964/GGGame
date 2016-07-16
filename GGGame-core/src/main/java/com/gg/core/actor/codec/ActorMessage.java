package com.gg.core.actor.codec;

/**
 * Created by guofeng.qin on 2016/7/9.
 */
public class ActorMessage {
    private int sid;
    private String senderSystem;
    private int sender;
    private String receiverSystem;
    private String receiverName;
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

    public ActorMessage(int sid, String recvSystem, String receiverName, int receiver, int sender, int type, Message msg) {
        this.receiverSystem = recvSystem;
        this.receiverName = receiverName;
        this.sender = sender;
        this.receiver = receiver;
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

    public String getSenderSystem() {
        return senderSystem;
    }

    public void setSenderSystem(String senderSystem) {
        this.senderSystem = senderSystem;
    }

    public String getReceiverSystem() {
        return receiverSystem;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setReceiverSystem(String receiverSystem) {
        this.receiverSystem = receiverSystem;
    }
}
