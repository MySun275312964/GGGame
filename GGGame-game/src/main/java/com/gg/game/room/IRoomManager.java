package com.gg.game.room;

import com.gg.game.proto.Room;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/8/31.
 */
public interface IRoomManager {
    /**
     * 创建房间
     *
     * @param member 房间人数
     * @param roleId 角色ID
     */
    CompletableFuture<CreateRoomResult> createRoom(String roleId, int member);

    /**
     * 加入房间
     * 
     * @param roleId 角色ID
     * @param roomId 房间ID
     */
    CompletableFuture<IGameRoom> joinRoom(String roleId, int roomId);

    /**
     * 获取房间列表
     */
    CompletableFuture<List<Room.RoomInfo>> getRoomList();
}
