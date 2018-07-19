package com.baicua.shiro.system.service;

import java.util.List;

import com.baicua.shiro.system.domain.UserOnline;

public interface SessionService {

	List<UserOnline> list();

	boolean forceLogout(String sessionId);
}
