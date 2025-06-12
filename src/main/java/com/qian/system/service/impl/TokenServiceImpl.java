package com.qian.system.service.impl;

import java.util.concurrent.TimeUnit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.qian.common.constant.Constants;
import com.qian.common.utils.ServletUtils;
import com.qian.common.utils.StringUtils;
import com.qian.common.utils.ip.AddressUtils;
import com.qian.common.utils.ip.IpUtils;
import com.qian.common.utils.uuid.IdUtils;
import com.qian.system.domain.entity.LoginUser;
import com.qian.system.service.TokenService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * token验证处理
 */
@Component
public class TokenServiceImpl implements TokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    
    /**
     * 令牌自定义标识
     */
    @Value("${token.header}")
    private String header;

    /**
     * 令牌密钥
     */
    @Value("${token.secret}")
    private String secret;

    /**
     * 令牌有效期（默认30分钟）
     */
    @Value("${token.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    /**
     * 构造函数注入RedisTemplate
     */
    public TokenServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    @Override
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 设置用户身份信息
     */
    @Override
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    @Override
    public void delLoginUser() {
        String token = ServletUtils.getRequest().getHeader(header);
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisTemplate.delete(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    @Override
    public String createToken(LoginUser loginUser) {
        String token = IdUtils.fastUUID();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        return token;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser 用户信息
     */
    @Override
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    @Override
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisTemplate.opsForValue().set(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    /**
     * 设置用户身份信息
     */
    @Override
    public void setLoginUser(LoginUser loginUser, String token) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(token)) {
            loginUser.setToken(token);
            refreshToken(loginUser);
        }
    }

    /**
     * 获取用户身份信息
     *
     * @param token 令牌
     * @return 用户信息
     */
    @Override
    public LoginUser getLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            return (LoginUser) redisTemplate.opsForValue().get(userKey);
        }
        return null;
    }

    /**
     * 获取用户身份信息
     *
     * @param request 请求信息
     * @return 用户信息
     */
    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            try {
                return getLoginUser(token);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 删除用户身份信息
     *
     * @param token 令牌
     */
    @Override
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisTemplate.delete(userKey);
        }
    }

    /**
     * 获取请求token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    private String getTokenKey(String token) {
        return Constants.LOGIN_TOKEN_KEY + token;
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    private void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }
} 