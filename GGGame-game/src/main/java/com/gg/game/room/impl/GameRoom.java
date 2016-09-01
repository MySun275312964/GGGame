package com.gg.game.room.impl;

import com.gg.common.GGLogger;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.game.proto.Battle;
import com.gg.game.proto.Room;
import com.gg.game.room.IGameRoom;
import com.gg.game.session.ISessionManager;
import com.gg.game.session.SessionManager;
import com.google.protobuf.MessageOrBuilder;

import java.util.List;

/**
 * 游戏房间
 * 
 * Created by guofeng.qin on 2016/8/31.
 */
public class GameRoom extends ActorBase implements IGameRoom {
    private static final GGLogger logger = GGLogger.getLogger(GameRoom.class);

    private List<String> roleList;

    private ISessionManager sessionManager = SessionManager.getInstance();

    public GameRoom(ActorSystem system) {
        super(system);
    }

    @Override
    public void start() {
        Battle.StartBattle startBattle = Battle.StartBattle.newBuilder().addAllOthers(roleList).build();
        broadcast(startBattle);
    }

    @Override
    public void setRoomEntry(RoomManager.RoomEntry roomEntry) {
        roleList = roomEntry.memberList;
        start();
    }

    @Override
    public void input(String roleId, int keyCode) {
        Battle.ControlInfo controlInfo = Battle.ControlInfo.newBuilder().setRid(roleId).setKeyCode(keyCode).build();
        broadcast(controlInfo);
    }

    @Override
    public void position(String roleId, Room.PositionRequest posReq) {
        Battle.PositionInfo posInfo = Battle.PositionInfo.newBuilder().setPosition(posReq.getPosition())
                .setRotation(posReq.getRotation()).setRid(roleId).build();
        broadcast(posInfo);
    }

    private void broadcast(MessageOrBuilder msg) {
        for (String role : roleList) {
            sessionManager.push(role, msg);
        }
    }
}
