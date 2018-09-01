package com.muiboot.shiro.common.util;

import com.muiboot.shiro.system.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

/**
 * Created by 75631 on 2018/8/31.
 */
public class ShiroUtil {
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static User getCurrentUser() {
        Subject subject=SecurityUtils.getSubject();
        return subject!=null? (User) subject.getPrincipal() :null;
    }
    public static Long getCurrentUserId() {
        User user=getCurrentUser();
        return user!=null? user.getUserId():null;
    }

    public static Session getSession() {
        return getSubject().getSession();
    }

    public static Session getSession(Boolean flag) {
        return getSubject().getSession(flag);
    }

}
