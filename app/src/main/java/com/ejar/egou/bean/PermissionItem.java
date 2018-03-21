package com.ejar.egou.bean;

/**
 * Created by Administrator on 2018\3\6 0006.
 */

public class PermissionItem {
    public String PermissionName;
    public String Permission;
    public int PermissionIconRes;


    public PermissionItem(String permission, String permissionName, int permissionIconRes) {
        Permission = permission;
        PermissionName = permissionName;
        PermissionIconRes = permissionIconRes;
    }

    public PermissionItem(String permission) {
        Permission = permission;
    }
}
