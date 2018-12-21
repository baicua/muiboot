package com.muiboot.activiti.active.param.operation;

import com.muiboot.activiti.active.group.User;

import java.util.Map;

/**
 * Created by 75631 on 2018/12/16.
 */
public abstract class Param {
    protected Map variable;
    protected User user;
    protected String opinion="";
    protected String nextLine="";

    public Map getVariable() {
        return variable;
    }

    public void setVariable(Map variable) {
        this.variable = variable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getNextLine() {
        return nextLine;
    }

    public void setNextLine(String nextLine) {
        this.nextLine = nextLine;
    }
}
