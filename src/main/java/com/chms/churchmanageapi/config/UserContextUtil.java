package com.chms.churchmanageapi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserContextUtil {

    private static final Logger logger = LoggerFactory.getLogger(UserContextUtil.class);

    private static final ThreadLocal<String> userHolder = new ThreadLocal<>();

    public static void setUser(String username) {
        userHolder.set(username);
    }

    public static String getUser() {
        return userHolder.get();
    }

    public static void clear() {
        logger.info("UserContextUtil: userHolder is cleared successfully to prevent memory leaks");
        userHolder.remove();
    }
}
