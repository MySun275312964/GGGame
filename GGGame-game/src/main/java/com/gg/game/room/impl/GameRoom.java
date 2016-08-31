package com.gg.game.room.impl;

import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.game.room.IGameRoom;

/**
 * 游戏房间
 * 
 * Created by guofeng.qin on 2016/8/31.
 */
public class GameRoom extends ActorBase implements IGameRoom {

    public GameRoom(ActorSystem system) {
        super(system);
    }

    @Override
    public void start() {

    }
}
