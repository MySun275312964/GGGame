package com.gg.gate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
}
