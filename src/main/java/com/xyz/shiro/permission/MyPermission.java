package com.xyz.shiro.permission;


import org.apache.shiro.authz.Permission;

public class MyPermission implements Permission {
    private String resourceId;
    private String operator;
    private String instanceId;

    public MyPermission(String permissionString) {
        String[] permSplArr = permissionString.split("|");
        if(permSplArr.length == 1) {

        }
    }

    @Override
    public boolean implies(Permission p) {

        return false;
    }
}
