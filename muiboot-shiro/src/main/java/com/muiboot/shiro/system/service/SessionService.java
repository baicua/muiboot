package com.muiboot.shiro.system.service;

import java.util.List;

import com.muiboot.shiro.system.entity.UserOnline;

public interface SessionService {

	List<UserOnline> list();

	boolean forceLogout(String sessionId);
}
