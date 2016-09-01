package com.gg.game.room.impl;

import com.gg.core.actor.ActorAgent;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorRef;
import com.gg.core.actor.ActorSystem;
import com.gg.game.GGGameApp;
import com.gg.game.proto.Room;
import com.gg.game.room.CreateRoomResult;
import com.gg.game.room.IGameRoom;
import com.gg.game.room.IRoomManager;
import com.gg.game.session.ISessionManager;
import com.gg.game.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 房间管理
 * 
 * Created by guofeng.qin on 2016/8/31.
 */
public class RoomManager extends ActorBase implements IRoomManager {
    private static final class Holder {
        private static final IRoomManager Instance;

        static {
            ActorSystem system = GGGameApp.getActorSystem();
            RoomManager roomManager = new RoomManager(system);
            ActorRef roomManagerRef = system.actor("RoomManager", roomManager);
            Instance = ActorAgent.getAgent(IRoomManager.class, system.getSystemActor(), roomManagerRef);
        }
    }

    public static final IRoomManager getInstance() {
        return Holder.Instance;
    }

    private Map<Integer, RoomEntry> readyRoomMap = new HashMap<>();
    private Map<Integer, RoomEntry> gamingRoomMap = new HashMap<>();
    private int roomIndex = 0;

    private ISessionManager sessionManager = SessionManager.getInstance();

    private RoomManager(ActorSystem system) {
        super(system);
    }

    private int getRoomIndex() {
        return ++roomIndex;
    }

    @Override
    public CompletableFuture<CreateRoomResult> createRoom(String roleId, int member) {
        int roomIndex = getRoomIndex();
        GameRoom gameRoom = new GameRoom(system);
        ActorRef roomRef = system.actor("room-" + roomIndex, gameRoom);
        IGameRoom roomAgent = ActorAgent.getAgent(IGameRoom.class, this, roomRef);
        RoomEntry roomEntry = new RoomEntry(roomIndex, member, Room.RoomStatus.CREATED_VALUE, roomAgent, roleId);
        readyRoomMap.put(roomIndex, roomEntry);
        Room.RoomInfo roomInfo = Room.RoomInfo.newBuilder().setId(roomIndex).setMemberCount(member)
                .setStatus(Room.RoomStatus.CREATED_VALUE).build();

        CreateRoomResult result = new CreateRoomResult();
        result.gameRoom = roomAgent;
        result.roomInfo = roomInfo;

        // 推送所有客户端
        sessionManager.pushAll(roomInfo);

        return CompletableFuture.completedFuture(result);
    }

    @Override
    public CompletableFuture<IGameRoom> joinRoom(String roleId, int roomId) {
        RoomEntry roomEntry = readyRoomMap.get(roomId);
        if (roomEntry != null && roomEntry.status == Room.RoomStatus.CREATED_VALUE
                && roomEntry.memberCount > roomEntry.memberList.size()) {
            for (String memberId : roomEntry.memberList) {
                if (memberId.equals(roleId)) {
                    return CompletableFuture.completedFuture(null);
                }
            }
            // 加入房间
            roomEntry.memberList.add(roleId);
            if (roomEntry.memberList.size() >= roomEntry.memberCount) {
                readyRoomMap.remove(roomId);
                roomEntry.status = Room.RoomStatus.GAMING_VALUE;
                gamingRoomMap.put(roomId, roomEntry);
                roomEntry.gameRoom.setRoomEntry(roomEntry);
            }
            return CompletableFuture.completedFuture(roomEntry.gameRoom);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<List<Room.RoomInfo>> getRoomList() {
        List<Room.RoomInfo> list = new ArrayList<>();
        for (RoomEntry entry : readyRoomMap.values()) {
            Room.RoomInfo roomInfo = Room.RoomInfo.newBuilder().setMemberCount(entry.memberCount)
                    .setStatus(entry.status).setId(entry.id).build();
            list.add(roomInfo);
        }
        return CompletableFuture.completedFuture(list);
    }

    public static class RoomEntry {
        private int id;
        private int status;
        private int memberCount;
        private IGameRoom gameRoom;
        public List<String> memberList = new ArrayList<>();

        public RoomEntry(int id, int memberCount, int status, IGameRoom gameRoom, String roleId) {
            this.id = id;
            this.memberCount = memberCount;
            this.status = status;
            this.gameRoom = gameRoom;
            this.memberList.add(roleId);
        }
    }
}
