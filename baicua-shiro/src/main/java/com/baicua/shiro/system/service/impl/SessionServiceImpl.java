package com.baicua.shiro.system.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baicua.shiro.system.domain.User;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.baicua.shiro.common.util.AddressUtils;
import com.baicua.shiro.system.domain.UserOnline;
import com.baicua.shiro.system.service.SessionService;

/**
 * Shiro Session 对象管理
 * @link https://mrbird.cc/Spring-Boot-Shiro%20session.html
 * @author MrBird
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService {

	@Autowired
	private SessionDAO sessionDAO;

	@Autowired
	ObjectMapper mapper;

	@Override
	public List<UserOnline> list() {
		List<UserOnline> list = new ArrayList<>();
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for (Session session : sessions) {
			UserOnline userOnline = new UserOnline();
			User user = new User();
			SimplePrincipalCollection principalCollection = new SimplePrincipalCollection();
			if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
				continue;
			} else {
				principalCollection = (SimplePrincipalCollection) session
						.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				user = (User) principalCollection.getPrimaryPrincipal();
				userOnline.setUsername(user.getUsername());
				userOnline.setUserId(user.getUserId().toString());
			}
			userOnline.setId((String) session.getId());
			userOnline.setHost(session.getHost());
			userOnline.setStartTimestamp(session.getStartTimestamp());
			userOnline.setLastAccessTime(session.getLastAccessTime());
			Long timeout = session.getTimeout();
			if (timeout == 0L) {
				userOnline.setStatus("0");
			} else {
				userOnline.setStatus("1");
			}
			String address = AddressUtils.getRealAddressByIP(userOnline.getHost(), mapper);
			userOnline.setLocation(address);
			userOnline.setTimeout(timeout);
			list.add(userOnline);
		}
		return list;
	}

	@Override
	public boolean forceLogout(String sessionId) {
		Session session = sessionDAO.readSession(sessionId);
		session.setTimeout(0);
		return true;
	}

}
