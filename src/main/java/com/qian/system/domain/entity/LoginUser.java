package com.qian.system.domain.entity;

import java.util.Collection;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 登录用户身份权限
 */
@Data
@Schema(description = "登录用户身份权限")
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户唯一标识
     */
    @NotBlank(message = "用户唯一标识不能为空")
    @Schema(description = "用户唯一标识")
    private String token;

    /**
     * 登录时间
     */
    @NotNull(message = "登录时间不能为空")
    @Schema(description = "登录时间")
    private Long loginTime;

    /**
     * 过期时间
     */
    @NotNull(message = "过期时间不能为空")
    @Schema(description = "过期时间")
    private Long expireTime;

    /**
     * 登录IP地址
     */
    @NotBlank(message = "登录IP地址不能为空")
    @Schema(description = "登录IP地址")
    private String ipaddr;

    /**
     * 登录地点
     */
    @Schema(description = "登录地点")
    private String loginLocation;

    /**
     * 浏览器类型
     */
    @Schema(description = "浏览器类型")
    private String browser;

    /**
     * 操作系统
     */
    @Schema(description = "操作系统")
    private String os;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private Set<String> permissions;

    /**
     * 用户信息
     */
    @NotNull(message = "用户信息不能为空")
    @Schema(description = "用户信息")
    private SysUser user;

    public LoginUser() {
    }

    public LoginUser(SysUser user) {
        this.user = user;
    }

    public LoginUser(Long userId, SysUser user) {
        this.userId = userId;
        this.user = user;
    }

    @JSONField(serialize = false)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JSONField(serialize = false)
    @Override
    public boolean isEnabled() {
        return true;
    }
} 