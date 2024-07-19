package com.exe01.backend.constant;

public class ConstStatus {

    public static final String ACTIVE_STATUS = "ACTIVE";
    public static final String INACTIVE_STATUS = "INACTIVE";
    public static final String PENDING = "PENDING";

    public static class CampaignStatus {
        public static final String COMPANY_APPLY = "COMPANY_APPLY";
        public static final String STUDENT_APPLY = "STUDENT_APPLY";
        public static final String TRAINING = "TRAINING";
        public static final String CLOSED = "CLOSED";
        public static final String CANCEL = "CANCEL";

    }

    public static class ApplicationStatus {
        public static final String PROCESSING = "IN PROCESS";
        public static final String APPROVED = "APPROVED";
        public static final String REJECTED = "REJECTED";
    }
    public static class  TransactionStatus {
        public static final String SUCCESS_STATUS = "SUCCESS";
        public static final String FAILED_STATUS = "FAILED";
    }

    public static class MentorProfileStatus {
        public static final String USING = "USING";
    }

    public static class MentorApplyStatus {
        public static final String IN_PROCESS = "TRAINING";
        public static final String DONE = "DONE";
    }

}
