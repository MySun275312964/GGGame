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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 游戏房间
 * <p>
 * Created by guofeng.qin on 2016/8/31.
 */
public class GameRoom extends ActorBase implements IGameRoom {
    private static final GGLogger logger = GGLogger.getLogger(GameRoom.class);

    private List<String> roleList;

    private ISessionManager sessionManager = SessionManager.getInstance();

    private int frameIndex = 1;

    private Map<Integer, Map<String, List<Room.InputFrame>>> frameMap = new HashMap<>();

    public GameRoom(ActorSystem system) {
        super(system);
    }

    @Override
    public void start() {
        int seed = (int) (System.currentTimeMillis() / 1000);
        List<Battle.PlayerInfo> list = new ArrayList<>();
        int planeId = 0;
        for (String role : roleList) {
            Battle.PlayerInfo info = Battle.PlayerInfo.newBuilder().setPlaneId(planeId++).setRid(role).build();
            list.add(info);
        }
        Battle.StartBattle startBattle = Battle.StartBattle.newBuilder().addAllOthers(list).setSeed(seed).build();
        broadcast(startBattle);
    }

    @Override
    public void setRoomEntry(RoomManager.RoomEntry roomEntry) {
        roleList = roomEntry.memberList;
        start();
    }

    @Override
    public void input(String roleId, Room.InputFrame inputFrame) {
        logger.info("input {}:{}:{}", roleId, inputFrame.getFrameIndex(), inputFrame.getInput().getKeyCode());
        int frame = inputFrame.getFrameIndex();
        if (frame != frameIndex) {
            logger.info("Frame Index Error. {}:{}:{}.", roleId, frame, frameIndex);
            return;
        }
        Map<String, List<Room.InputFrame>> inputMap = frameMap.get(frame);
        if (inputMap == null) {
            inputMap = new HashMap<>();
            frameMap.put(frame, inputMap);
        }
        List<Room.InputFrame> inputList = inputMap.get(roleId);
        if (inputList == null) {
            inputList = new ArrayList<>();
            inputMap.put(roleId, inputList);
        }
        inputList.add(inputFrame);

        if (inputMap.size() >= roleList.size()) {
            for (Map.Entry<String, List<Room.InputFrame>> entry : inputMap.entrySet()) {
                for (Room.InputFrame input : entry.getValue()) {
                    Battle.ControlInfo controlInfo = Battle.ControlInfo.newBuilder().setRid(entry.getKey())
                            .setKeyCode(input.getInput().getKeyCode())
                            .setMoveHorizontal(input.getInput().getMoveHorizontal())
                            .setMoveVertical(input.getInput().getMoveVertical()).setFrameIndex(input.getFrameIndex())
                            .build();
                    broadcast(controlInfo);
                }
            }
            frameMap.remove(frameIndex);
            frameIndex++;
        }
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
