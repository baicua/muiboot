package com.muiboot.activiti.active.group;

/**
 * <p>Description: </p>
 *
 * @version 1.0 2018/12/10
 */
public class UserEntity implements User {
    private String userId;
    private String OrganId;
    private String deptId;

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getOrganId() {
        return OrganId;
    }

    public void setOrganId(String organId) {
        OrganId = organId;
    }

    @Override
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
