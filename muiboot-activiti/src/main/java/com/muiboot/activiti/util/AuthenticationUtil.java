package com.muiboot.activiti.util;

import com.muiboot.activiti.active.group.User;
import org.activiti.engine.impl.identity.Authentication;

/**
 * Created by 75631 on 2018/12/15.
 */
public class AuthenticationUtil extends Authentication {
    static ThreadLocal<User> authenticatedUser = new ThreadLocal();
    public AuthenticationUtil() {
    }

    public static void setAuthenticatedUser(User user) {
        authenticatedUser.set(user);
        setAuthenticatedUserId(user.getUserId());
    }

    public static User getAuthenticatedUser() {
        return authenticatedUser.get();
    }
}
