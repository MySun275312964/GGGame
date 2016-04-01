package com.gg.gate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.gg.core.harbor.HarborFutureTask;

/**
 * @author guofeng.qin
 */
@Component
public class TestClass {

	private static final Logger logger = LoggerFactory.getLogger(TestClass.class);

	@Autowired
	private ApplicationContext context;

	public void test() {
		logger.info("test......" + (context == null));
	}

	public static void main(String[] args) {
		Executor exe = Executors.newSingleThreadExecutor();
		HarborFutureTask hft = HarborFutureTask.buildTask(String.class, false);
		exe.execute(new Runnable() {
			@Override
			public void run() {
				try {
					hft.get();
					System.out.println("end...");
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		exe.execute(new Runnable() {
			@Override
			public void run() {
				List<String> list = new ArrayList<String>();
				list.add("");
				hft.finish(list);
				System.out.println("finished...");
			}
		});

		
	}
}
