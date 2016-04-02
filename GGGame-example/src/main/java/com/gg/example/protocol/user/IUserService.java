package com.gg.example.protocol.user;

import com.gg.core.Async;
import com.gg.core.harbor.HarborFutureTask;

public interface IUserService {
	User getUserById(String id);

	@Async(clazz = User.class)
	HarborFutureTask getUserByAge(int age);
}
