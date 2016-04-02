package com.gg.example.user;

import org.springframework.stereotype.Service;

import com.gg.example.protocol.user.IUserService;
import com.gg.example.protocol.user.User;

@Service
public class UserServiceImpl implements IUserService {
	@Override
	public User getUserById(String id) {
		return new User(id, "testname", "testicon", 20);
	}
}
