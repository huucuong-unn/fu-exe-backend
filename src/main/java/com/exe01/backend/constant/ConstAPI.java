package com.exe01.backend.constant;

public class ConstAPI {

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
        public static final String CREATE_ACCOUNT_ADMIN = "api/v1/account/create-admin";
        public static final String CREATE_ACCOUNT_COMPANY = "api/v1/account/create-company";
        public static final String CREATE_ACCOUNT_MENTOR = "api/v1/account/create-mentor";
        public static final String UPDATE_ACCOUNT = "api/v1/account/update/";
        public static final String DELETE_ACCOUNT = "api/v1/account/delete/";
        public static final String CHANGE_STATUS_ACCOUNT = "api/v1/account/change-status/";
        public static final String UPLOAD_IMAGE_ACCOUNT = "api/v1/account/upload-image/";
        public static final String DOWNLOAD_IMAGE_ACCOUNT = "api/v1/account/download-image/";
        public static final String GET_ACCOUNT_MENTEE_INFO = "api/v1/account/account-mentee-info/";

        public static final String GET_ACCOUNT_FOR_ADMIN = "api/v1/account/account-for-admin";

        public static final String APPROVE_ACCOUNT = "api/v1/account/approve/";

        public static final String REJECT_ACCOUNT = "api/v1/account/reject/";

        public static final String GET_POINT = "api/v1/account/point/";

    }

    public static class AuthenticationAPI {
        public static final String LOGIN_WITH_PASSWORD_USERNAME = "api/v1/auth/login";
        public static final String LOGIN_WITH_GOOGLE = "api/v1/auth/login-google";
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
        public static final String UPDATE_MENTOR_PROFILE = "api/v1/mentor-profile/update";
        public static final String DELETE_MENTOR_PROFILE = "api/v1/mentor-profile/delete/";

        public static final String GET_ALL_MENTOR_PROFILE_BY_MENTOR_ID = "api/v1/mentor-profile/all/mentor/";
        public static final String GET_MENTOR_PROFILE_BY_MENTOR_ID = "api/v1/mentor-profile/mentor/";
        public static final String CREATE_NEW_MENTOR_PROFILE_SKILLS = "api/v1/mentor-profile/create-new-mentor-profile-skills";
    }

    public static class MentorAPI {
        public static final String GET_MENTOR = "api/v1/mentor";
        public static final String GET_MENTOR_WITH_ALL_INFORMATION = "api/v1/mentor/mentor-with-all-information";
        public static final String GET_MENTOR_STATUS_TRUE = "api/v1/mentor/mentor-status-true";
        public static final String GET_MENTOR_BY_ID = "api/v1/mentor/";
        public static final String GET_MENTOR_BY_MENTOR_PROFILE_ID = "api/v1/mentor/find-by-mentor-profile-id/";
        public static final String GET_MENTORS_BY_COMPANY_ID = "api/v1/mentor/find-by-mentors-company-id/";
        public static final String CREATE_MENTOR = "api/v1/mentor/create";
        public static final String UPDATE_MENTOR = "api/v1/mentor/update/";
        public static final String CHANGE_STATUS_MENTOR = "api/v1/mentor/change-status/";
        public static final String GET_SIMILAR_MENTORS_BY_COMPANY_ID = "api/v1/mentor/similar-mentors/";
        public static final  String GET_MENTORS_BY_STUDENT_ID = "api/v1/mentor/student/";
        public static final String GET_ALL_MENTOR_FOR_ADMIN_SEARCH = "api/v1/mentor/mentor-for-admin-search";

        public static final String GET_MENTOR_FOR_LIST_CHOOSE= "api/v1/mentor/mentor-for-list-choose/";

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

        public static final String GET_UNIVERSITY_DROP_DOWN_LIST = "api/v1/university/drop-down-list";

    }

    public static class CampaignAPI {
        public static final String GET_CAMPAIGN = "api/v1/campaign";
        public static final String GET_CAMPAIGN_STATUS_TRUE = "api/v1/campaign/campaign-status-true";
        public static final String GET_CAMPAIGN_BY_ID = "api/v1/campaign/";
        public static final String CREATE_CAMPAIGN = "api/v1/campaign/create";
        public static final String UPDATE_CAMPAIGN = "api/v1/campaign/update/";
        public static final String CHANGE_STATUS_CAMPAIGN = "api/v1/campaign/change-status/";

        public static final String GET_ALL_CAMPAIGN_WITHOUT_PAGING = "api/v1/campaign/campaign-without-paging";

        public static final String GET_CAMPAIGN_WITH_SEARCH = "api/v1/campaign/campaign-for-admin";

    }

    public static class CampaignMentorProfileAPI {
        public static final String GET_CAMPAIGN_MENTOR_PROFILE_BY_ID = "api/v1/campaign-mentor-profile/";
        public static final String CREATE_CAMPAIGN_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/create";
        public static final String UPDATE_CAMPAIGN_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/update/";
        public static final String CHANGE_STATUS_CAMPAIGN_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/change-status/";
        public static final String SWAP_MENTOR_PROFILE = "api/v1/campaign-mentor-profile/swap-mentor-profile/";
    }

    public static class MenteeAPI {

        public static final String GET_MENTEE = "api/v1/mentee";
        public static final String GET_MENTEE_STATUS_TRUE = "api/v1/mentee/mentee-status-true";
        public static final String GET_MENTEE_BY_ID = "api/v1/mentee/";
        public static final String CREATE_MENTEE = "api/v1/mentee/create";
        public static final String CHANGE_STATUS_MENTEE = "api/v1/mentee/change-status/";
       // public static final String GET_MENTEE_BY_MENTORID_CAMPAIGNID = "api/v1/mentee/mentee-status-true";

        public static final String GET_MENTEE_BY_MENTORID_CAMPAIGNID = "api/v1/mentee/mentor-campaign";

    }


    public static class SkillAPI {
        public static final String GET_SKILL = "api/v1/skill";
        public static final String GET_SKILL_STATUS_TRUE = "api/v1/skill/skill-status-true";
        public static final String GET_SKILL_BY_ID = "api/v1/skill/";
        public static final String GET_ALL_SKILL_BY_COMPANY_ID = "api/v1/skill/skills-by-company-id/";
        public static final String CREATE_SKILL = "api/v1/skill/create";
        public static final String CREATE_SKILL_MENTOR_PROFILE = "api/v1/skill/create-skill-mentor-profile";
        public static final String UPDATE_SKILL = "api/v1/skill/update/";
        public static final String DELETE_SKILL = "api/v1/skill/delete/";
        public static final String CHANGE_STATUS_SKILL = "api/v1/skill/change-status/";
        public static final String GET_ALL_SKILL = "api/v1/skill/all";
    }

    public static class CompanyAPI {
        public static final String GET_COMPANY = "api/v1/company";
        public static final String GET_COMPANY_STATUS_TRUE = "api/v1/company/company-status-true";
        public static final String GET_COMPANY_BY_SEARCH_SORT = "api/v1/company/search-sort";
        public static final String GET_COMPANY_BY_ID = "api/v1/company/";
        public static final String CREATE_COMPANY = "api/v1/company/create";
        public static final String UPDATE_COMPANY = "api/v1/company/update/";
        public static final String CHANGE_STATUS_COMPANY = "api/v1/company/change-status/";

        public static final String GET_COMPANY_STATUS_TRUE_WITHOUT_PAGING = "api/v1/company/company-status-true-without-paging";
    }

    public static class ApplicationAPI {
        public static final String GET_APPLICATION = "api/v1/application";
        public static final String GET_APPLICATION_BY_ID = "api/v1/application/";
        public static final String CREATE_APPLICATION = "api/v1/application/create";
        public static final String UPDATE_APPLICATION = "api/v1/application/update/";
        public static final String CHANGE_STATUS_APPLICATION = "api/v1/application/change-status/";
        public static final String GET_APPLICATION_BY_MENTOR_ID = "api/v1/application/mentor/";
        public static final String GET_APPLICATION_BY_MENTEE_ID = "api/v1/application/mentee/";
        public static final String APPROVE_APPLICATION = "api/v1/application/approve/";
        public static final String GET_APPLICATION_BY_MENTOR_ID_AND_STATUS_AND_SORT_BY_CREATED_DATE = "api/v1/application/mentor-current-campaign/";
        public static final String GET_APPLICATION_BY_STUDENT_ID_AND_STATUS_AND_SORT = "api/v1/application/student/";

        public static final String REJECT_APPLICATION = "api/v1/application/reject/";
    }

    public static class MentorApplyAPI {
        public static final String GET_MENTOR_APPLY = "api/v1/mentor-apply";
        public static final String GET_MENTOR_APPLY_BY_ID = "api/v1/mentor-apply/";
        public static final String CREATE_MENTOR_APPLY = "api/v1/mentor-apply/create";
        public static final String UPDATE_MENTOR_APPLY = "api/v1/mentor-apply/update/";
        public static final String CHANGE_STATUS_MENTOR_APPLY = "api/v1/mentor-apply/change-status/";
        public static final String GET_MENTOR_APPLY_BY_MENTOR_ID = "api/v1/mentor-apply/mentor/";
        public static final String GET_MENTOR_APPLY_BY_MENTEE_ID = "api/v1/mentor-apply/mentee/";
        public static final String GET_MENTOR_APPLY_BY_MENTEE_NAME_AND_MENTOR_FULL_NAME_AND_CAMPAIGN_ID = "api/v1/mentor-apply/search";

        public static final String GET_MENTOR_APPLY_BY_STUDENT_ID = "api/v1/mentor-apply/student/";
    }


    public static class TransactionAPI {
        public static final String GET_TRANSACTION = "api/v1/transaction";
        public static final String GET_TRANSACTION_STATUS_TRUE = "api/v1/transaction/transaction-status-true";
        public static final String GET_TRANSACTION_BY_ID = "api/v1/transaction/";
        public static final String CREATE_TRANSACTION = "api/v1/transaction/create";
        public static final String GET_TRANSACTION_BY_ACCOUNT_ID_AND_SORT_BY_CREATED_DATE = "api/v1/transaction/account/";

        public static final String GET_TRANSACTION_FOR_ADMIN = "api/v1/transaction/admin";
    }

    public static class NotificationAPI
    {
        public static final String SEND_NOTIFITION = "api/v1/send-notification";
        public static final String NOTIFITION_TOKEN = "api/v1/notifcation-token";
    }

    public static class DashboardAPI {
        public static final String GET_DASHBOARD = "api/v1/dashboard";
    }

}
