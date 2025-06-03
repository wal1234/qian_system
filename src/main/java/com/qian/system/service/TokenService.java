package com.qian.system.service;

import com.qian.system.domain.LoginUser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * token验证处理
 */
public interface TokenService {
    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    LoginUser getLoginUser();

    /**
     * 设置用户身份信息
     */
    void setLoginUser(LoginUser loginUser);

    /**
     * 删除用户身份信息
     */
    void delLoginUser();

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    String createToken(LoginUser loginUser);

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 用户信息
     */
    void verifyToken(LoginUser loginUser);

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    void refreshToken(LoginUser loginUser);

    /**
     * 设置用户身份信息
     */
    void setLoginUser(LoginUser loginUser, String token);

    /**
     * 获取用户身份信息
     *
     * @param token 令牌
     * @return 用户信息
     */
    LoginUser getLoginUser(String token);

    /**
     * 删除用户身份信息
     *
     * @param token 令牌
     */
    void delLoginUser(String token);

    /**
     * 获取用户身份信息
     *
     * @param request 请求信息
     * @return 用户信息
     */
    LoginUser getLoginUser(HttpServletRequest request);
} 