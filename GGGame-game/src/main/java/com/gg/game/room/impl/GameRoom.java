package com.gg.game.room.impl;

import com.gg.common.GGLogger;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.game.room.IGameRoom;

import java.util.List;

/**
 * 游戏房间
 * 
 * Created by guofeng.qin on 2016/8/31.
 */
public class GameRoom extends ActorBase implements IGameRoom {
    private static final GGLogger logger = GGLogger.getLogger(GameRoom.class);

    private List<String> roleList;

    public GameRoom(ActorSystem system) {
        super(system);
    }

    @Override
    public void start() {

    }

    @Override
    public void setRoomEntry(RoomManager.RoomEntry roomEntry) {
        roleList = roomEntry.memberList;
    }
}
