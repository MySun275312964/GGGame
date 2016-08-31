package com.gg.game.room;

import com.gg.game.room.impl.RoomManager;

/**
 * Created by guofeng.qin on 2016/8/31.
 */
public interface IGameRoom {
    void start();

    void setRoomEntry(RoomManager.RoomEntry roomEntry);

    void input(String roleId, int keyCode);
}
