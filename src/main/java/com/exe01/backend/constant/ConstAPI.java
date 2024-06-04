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
        public static final String DELETE_ROLE = "api/v1/role/delete/";
        public static final String CHANGE_STATUS_ROLE = "api/v1/role/change-status/";
    }

    public static class AccountAPI {
        public static final String GET_ACCOUNT = "api/v1/account";
        public static final String GET_ACCOUNT_STATUS_TRUE = "api/v1/account/account-status-true";
        public static final String GET_ACCOUNT_BY_ID = "api/v1/account/";
        public static final String CREATE_ACCOUNT = "api/v1/account/create";
        public static final String UPDATE_ACCOUNT = "api/v1/account/update/";
        public static final String DELETE_ACCOUNT = "api/v1/account/delete/";
        public static final String CHANGE_STATUS_ACCOUNT = "api/v1/account/change-status/";
    }

    public static class MajorAPI {
        public static final String GET_MAJOR = "api/v1/major";
        public static final String GET_MAJOR_STATUS_TRUE = "api/v1/major/major-status-true";
        public static final String GET_MAJOR_BY_ID = "api/v1/major/";
        public static final String CREATE_MAJOR = "api/v1/major/create";
        public static final String UPDATE_MAJOR = "api/v1/major/update/";
        public static final String DELETE_MAJOR = "api/v1/major/delete/";
        public static final String CHANGE_STATUS_MAJOR = "api/v1/major/change-status/";
    }

    public static class MentorProfileAPI {
        public static final String GET_MENTOR_PROFILE = "api/v1/mentor-profile";
        public static final String GET_MENTOR_PROFILE_STATUS_TRUE = "api/v1/mentor-profile/mentor-profile-status-true";
        public static final String GET_MENTOR_PROFILE_BY_ID = "api/v1/mentor-profile/";
        public static final String CREATE_MENTOR_PROFILE = "api/v1/mentor-profile/create";
        public static final String UPDATE_MENTOR_PROFILE = "api/v1/mentor-profile/update/";
        public static final String DELETE_MENTOR_PROFILE = "api/v1/mentor-profile/delete/";
    }

    public static class MentorAPI {
        public static final String GET_MENTOR = "api/v1/mentor";
        public static final String GET_MENTOR_STATUS_TRUE = "api/v1/mentor/mentor-status-true";
        public static final String GET_MENTOR_BY_ID = "api/v1/mentor/";
        public static final String CREATE_MENTOR = "api/v1/mentor/create";
        public static final String UPDATE_MENTOR = "api/v1/mentor/update/";
        public static final String CHANGE_STATUS_MENTOR = "api/v1/mentor/change-status/";
    }

    public static class StudentAPI {
        public static final String GET_STUDENT = "api/v1/student";
        public static final String GET_STUDENT_STATUS_TRUE = "api/v1/student/student-status-true";
        public static final String GET_STUDENT_BY_ID = "api/v1/student/";
        public static final String CREATE_STUDENT = "api/v1/student/create";
        public static final String UPDATE_STUDENT = "api/v1/student/update/";
        public static final String DELETE_STUDENT = "api/v1/student/delete/";
        public static final String CHANGE_STATUS_STUDENT = "api/v1/student/change-status/";
    }

    public static class UniversityAPI {
        public static final String GET_UNIVERSITY = "api/v1/university";
        public static final String GET_UNIVERSITY_STATUS_TRUE = "api/v1/university/university-status-true";
        public static final String GET_UNIVERSITY_BY_ID = "api/v1/university/";
        public static final String CREATE_UNIVERSITY = "api/v1/university/create";
        public static final String UPDATE_UNIVERSITY = "api/v1/university/update/";
        public static final String CHANGE_STATUS_UNIVERSITY = "api/v1/university/change-status/";

    }

    public static class CampaignAPI {
        public static final String GET_CAMPAIGN = "api/v1/campaign";
        public static final String GET_CAMPAIGN_STATUS_TRUE = "api/v1/campaign/campaign-status-true";
        public static final String GET_CAMPAIGN_BY_ID = "api/v1/campaign/";
        public static final String CREATE_CAMPAIGN = "api/v1/campaign/create";
        public static final String UPDATE_CAMPAIGN = "api/v1/campaign/update/";
        public static final String CHANGE_STATUS_CAMPAIGN = "api/v1/campaign/change-status/";
    }

    public static class CampaignMentorProfileAPI {
        public static final String GET_CAMPAIGN_MENTOR_PROFILE_BY_ID = "api/v1/campaign-mentor-profile/";
        public static final String CREATE_CAMPAIGN_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/create";
        public static final String UPDATE_CAMPAIGN_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/update/";
        public static final String CHANGE_STATUS_CAMPAIGN_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/change-status/";
    }

    public static class MenteeAPI {

        public static final String GET_MENTEE = "api/v1/mentee";
        public static final String GET_MENTEE_STATUS_TRUE = "api/v1/mentee/mentee-status-true";
        public static final String GET_MENTEE_BY_ID = "api/v1/mentee/";
        public static final String CREATE_MENTEE = "api/v1/mentee/create";
        public static final String UPDATE_MENTEE = "api/v1/mentee/update/";
        public static final String CHANGE_STATUS_MENTEE = "api/v1/mentee/change-status/";

    }


    public static class SkillAPI {
        public static final String GET_SKILL = "api/v1/skill";
        public static final String GET_SKILL_STATUS_TRUE = "api/v1/skill/skill-status-true";
        public static final String GET_SKILL_BY_ID = "api/v1/skill/";
        public static final String CREATE_SKILL = "api/v1/skill/create";
        public static final String UPDATE_SKILL = "api/v1/skill/update/";
        public static final String DELETE_SKILL = "api/v1/skill/delete/";
        public static final String CHANGE_STATUS_SKILL = "api/v1/skill/change-status/";
    }

    public static class CompanyAPI {
        public static final String GET_COMPANY = "api/v1/company";
        public static final String GET_COMPANY_STATUS_TRUE = "api/v1/company/company-status-true";
        public static final String GET_COMPANY_BY_ID = "api/v1/company/";
        public static final String CREATE_COMPANY = "api/v1/company/create";
        public static final String UPDATE_COMPANY = "api/v1/company/update/";
        public static final String CHANGE_STATUS_COMPANY = "api/v1/company/change-status/";
    }

    public static class TransactionAPI {
        public static final String GET_TRANSACTION = "api/v1/transaction";
        public static final String GET_TRANSACTION_STATUS_TRUE = "api/v1/transaction/transaction-status-true";
        public static final String GET_TRANSACTION_BY_ID = "api/v1/transaction/";
        public static final String CREATE_TRANSACTION = "api/v1/transaction/create";
    }

}
