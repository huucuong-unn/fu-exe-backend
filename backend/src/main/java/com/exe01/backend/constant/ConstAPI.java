package com.exe01.backend.constant;

public class ConstAPI {
    public static class AuthenticationAPI {
        public static final String LOGIN_WITH_PASSWORD_USERNAME = "api/v1/auth/login";
        public static final String TEST_AWS_DEPLOY = "api/v1/test-aws-deploy";
        public static final String TEST_AWS_DEPLOY2 = "api/v1/test-aws-deploy2";

    }

    public static class RoleAPI {
        public static final String GET_ROLE = "api/v1/role";
        public static final String GET_ROLE_STATUS_TRUE = "api/v1/role/role-status-true";
        public static final String GET_ROLE_BY_ID = "api/v1/role/";
        public static final String CREATE_ROLE = "api/v1/role/create";
        public static final String UPDATE_ROLE = "api/v1/role/update/";
    }

    public static class AccountAPI {
        public static final String GET_ACCOUNT = "api/v1/account";
        public static final String GET_ACCOUNT_STATUS_TRUE = "api/v1/account/account-status-true";
        public static final String GET_ACCOUNT_BY_ID = "api/v1/account/";
        public static final String CREATE_ACCOUNT = "api/v1/account/create";
        public static final String UPDATE_ACCOUNT = "api/v1/account/update/";
        public static final String DELETE_ACCOUNT = "api/v1/account/delete/";
    }

}
