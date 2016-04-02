package com.gg.gate;

import java.lang.reflect.Method;
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
import org.springframework.util.ReflectionUtils;

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
		Method m = ReflectionUtils.findMethod(TestClass.class, "test");
		java.lang.reflect.Type clz = m.getGenericReturnType();
		System.out.println(clz.getClass());
	}
}
