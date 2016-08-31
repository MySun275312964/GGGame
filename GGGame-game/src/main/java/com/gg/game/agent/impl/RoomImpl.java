package com.gg.game.agent.impl;

import com.gg.common.GGLogger;
import com.gg.game.proto.Room;
import com.gg.game.room.CreateRoomResult;
import com.gg.game.room.IGameRoom;
import com.gg.game.room.IRoomManager;
import com.gg.game.room.impl.RoomManager;
import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created by guofeng.qin on 2016/8/31.
 */
public class RoomImpl extends Room.IRoom {
    private static final GGLogger logger = GGLogger.getLogger(RoomImpl.class);

    private IRoomManager roomManager = RoomManager.getInstance();
    /** 当前所在游戏房间 */
    private IGameRoom currentRoom;

    private String roleId;

    public RoomImpl(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public void create(RpcController controller, Room.CreateRoomRequest request,
            RpcCallback<Room.CreateRoomResponse> done) {
        logger.info("create room");
        CompletableFuture<CreateRoomResult> resultFuture = roomManager.createRoom(roleId, request.getMemberCount());
        resultFuture.whenComplete((result, err) -> {
            Room.CreateRoomResponse.Builder builder = Room.CreateRoomResponse.newBuilder();
            builder.setCode(err == null ? 1 : 0);
            if (err == null && result != null && result.roomInfo != null && result.gameRoom != null) {
                Room.RoomInfo roomInfo = result.roomInfo;
                builder.setCode(1).setRoomInfo(roomInfo);
                currentRoom = result.gameRoom;
            } else {
                builder.setCode(0);
            }
            done.run(builder.build());
        });
    }

    @Override
    public void join(RpcController controller, Room.JoinRoomRequest request, RpcCallback<Room.JoinRoomResponse> done) {
        CompletableFuture<IGameRoom> joinFuture = roomManager.joinRoom(roleId, request.getRoomId());
        joinFuture.whenComplete((result, err) -> {
            Room.JoinRoomResponse.Builder builder = Room.JoinRoomResponse.newBuilder();
            if (err == null && result != null) {
                builder.setCode(1);
            } else {
                builder.setCode(0);
                if (err != null) {
                    builder.setMsg(err.getMessage());
                }
            }
            done.run(builder.build());
        });
    }

    @Override
    public void getRoomList(RpcController controller, Room.GetRoomListRequest request,
            RpcCallback<Room.GetRoomListResponse> done) {
        logger.info("getRoomList...");
        CompletableFuture<List<Room.RoomInfo>> getRoomListFuture = roomManager.getRoomList();
        getRoomListFuture.whenComplete((result, err) -> {
            Room.GetRoomListResponse.Builder builder = Room.GetRoomListResponse.newBuilder();
            if (err == null && result != null) {
                builder.addAllRoomList(result);
            }
            done.run(builder.build());
        });
    }
}
