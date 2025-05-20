package com.qian.system.service;

import com.qian.system.domain.LoginUser;

/**
 * token验证处理
 */
public interface TokenService {
    /**
     * 获取当前登录用户
     * 
     * @return 用户信息
     */
    public LoginUser getLoginUser();
} 