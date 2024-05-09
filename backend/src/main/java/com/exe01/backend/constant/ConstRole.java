package com.exe01.backend.constant;

public enum ConstRole {
    ADMIN(1L, "Admin"),
    USER(2L, "User");

    private Long roleKey;
    private String roleTypeName;

    public Long getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(Long roleKey) {
        this.roleKey = roleKey;
    }

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    ConstRole(Long roleKey, String roleTypeName) {
        this.roleKey = roleKey;
        this.roleTypeName = roleTypeName;
    }

    public static ConstRole fromRoleTypeName(String roleTypeName) {
        for (ConstRole constRole : ConstRole.values()) {
            if (constRole.roleTypeName.equalsIgnoreCase(roleTypeName)) {
                return constRole;
            }
        }
        return null;
    }

    public static ConstRole fromRoleKey(Long roleKey) {
        for (ConstRole constRole : ConstRole.values()) {
            if (constRole.roleKey == roleKey) {
                return constRole;
            }
        }
        return null;
    }
}
