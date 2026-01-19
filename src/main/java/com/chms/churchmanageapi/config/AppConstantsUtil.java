package com.chms.churchmanageapi.config;

public final class AppConstantsUtil {

    private AppConstantsUtil() {}

    public static final int MEMBER_ROLE_ID = 2;
    public static final long APPL_STS_SUBMITTED = 1L;
    public static final long APPL_STS_IN_PROGRESS = 2L;
    public static final long APPL_STS_READY = 3L;
    public static final long APPL_STS_APPROVED = 4L;
    public static final long APPL_STS_REJECTED = 5L;
    public static final String APPLICATION_TYPE = "New Registration";
    public static final String APPLICATION_COMMENT = "New Application Submitted";
    public static final String IPROGRESS_APPL_COMMENT = "Application is In Progress";
    public static final String READY_APPL_COMMENT = "Application is ready for Approval";
    public static final String APPROVED_APPL_COMMENT = "Application has been Approved";
    public static final String REJECTED_APPL_COMMENT = "Application has been Rejected";

}
