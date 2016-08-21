package com.gg.game.agent;

import com.gg.common.StringUtils;
import com.gg.core.actor.ActorBase;
import com.gg.core.actor.ActorSystem;
import com.gg.core.net.IMsgDispatch;
import com.gg.core.net.codec.Net;
import com.google.protobuf.RpcCallback;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by hunter on 2016/8/21.
 */
public class UserAgent extends ActorBase implements IMsgDispatch {

    private static List<Consumer<UserAgent>> registryRunners = new ArrayList<>();

    private Map<String, Object> funcMap = new HashMap<>();

    private Map<String, FuncEntry> funcEntryCache = new HashMap<>();

    public UserAgent(ActorSystem system) {
        super(system);
        init();
    }

    private void init() {
        for (Consumer<UserAgent> runner : registryRunners) {
            runner.accept(this);
        }
    }

    // 業務類註冊自己的初始化進來
    public static void addRegistryRunners (Consumer<UserAgent> runner) {
        registryRunners.add(runner);
    }

    public void addFunc(String key, Object func) {
        funcMap.put(key, func);
    }

    private FuncEntry getFuncEntry(String key, String method) {
        String fullKey = StringUtils.join(".", key, method);

        if (funcEntryCache.containsKey(fullKey)) {
            return funcEntryCache.get(fullKey);
        }

        Object obj = funcMap.get(key);
        Method objMethod = null;
        if (obj != null) {
            Method [] methods = obj.getClass().getMethods();
            if (methods != null) {
                for (Method m : methods) {
                    if (m.getName().equals(method)) {
                        objMethod = m;
                        break;
                    }
                }
            }
        }

        if (objMethod != null) {
            FuncEntry entry = new FuncEntry(obj, objMethod);
            funcEntryCache.put(fullKey, entry);
            return entry;
        }

        return null;
    }

    @Override
    public void process(ChannelHandlerContext ctx, Net.Request request, RpcCallback<?> callback) {
        String instance = request.getInstance();
        String method = request.getMethod();
        FuncEntry funcEntry = getFuncEntry(instance, method);
        if (funcEntry == null) {
            throw new RuntimeException("Method not found.");
        }
        // TODO ... dispatch method...
    }

    private static final class FuncEntry {
        private Object instance;
        private Method method;

        public FuncEntry(Object instance, Method method) {
            this.instance = instance;
            this.method = method;
        }
    }
}