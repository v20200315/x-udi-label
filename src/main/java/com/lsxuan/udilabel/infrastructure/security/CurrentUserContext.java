package com.lsxuan.udilabel.infrastructure.security;

import java.util.UUID;

public interface CurrentUserContext {

    /**
     * 获取当前登录用户的内部用户 ID。
     */
    UUID getCurrentUserId();

    /**
     * 获取当前登录用户的外部身份 Subject。
     */
    String getCurrentSubject();

    /**
     * 判断当前用户是否已认证。
     */
    boolean isAuthenticated();

}
