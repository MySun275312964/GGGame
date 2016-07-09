package com.gg.example.user;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gg.core.harbor.HarborFutureTask;
import com.gg.example.protocol.user.IUserService;
import com.gg.example.protocol.user.User;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static Executor pool = Executors.newFixedThreadPool(2);
    private static AtomicLong COUNT = new AtomicLong(0L);

    @Override
    public User getUserById(String id) {
        return new User(id, "testname", "testicon", 20, null);
    }

    @Override
    public HarborFutureTask getUserByAge(int age) {
        HarborFutureTask task = HarborFutureTask.buildTask();
        /*
         * new Thread() {
         * 
         * @Override public void run() { // try { // Thread.sleep(15*1000L); // } catch
         * (InterruptedException e) { // e.printStackTrace(); // } // ITaskService taskService =
         * HarborRPC.getHarbor(ExampleConst.TaskService, ITaskService.class); // List<Task> tl =
         * taskService.getTaskList(); // User u = new User("testid", "testname", "testicon", age,
         * tl);
         * 
         * // tl = JsonHelper.reparse(tl, new TypeToken<List<Task>>() {}.getType()); // if (tl !=
         * null) { // for (Task t:tl) { // logger.info(t.getName()); // } // }
         * 
         * User u = new User("testid", "testname", "testicon", age, null); task.finish(u); }
         * }.start();
         */
        pool.execute(() -> {
            // ITaskService taskService = HarborRPC.getHarbor(ExampleConst.TaskService,
            // ITaskService.class);
            // List<Task> tl = taskService.getTaskList();
            // User u = new User("testid", "testname", "testicon", age, tl);

            // tl = JsonHelper.reparse(tl, new TypeToken<List<Task>>() {}.getType());
            // if (tl != null) {
            // for (Task t:tl) {
            // logger.info(t.getName());
            // }
            // }

            // Thread.currentThread().setName("LogicPool");
            User u = new User("testid", "testname", "testicon", age, null);
            task.finish(u);
        });
        // long l = COUNT.incrementAndGet();
        // logger.info("Count: " + l);
        return task;
    }

    @Override
    public String test() {
        return "abcdefg";
    }

    @Override
    public HarborFutureTask test2() {
        HarborFutureTask future = HarborFutureTask.buildTask();
        future.finish("123456");
        return future;
    }
}
