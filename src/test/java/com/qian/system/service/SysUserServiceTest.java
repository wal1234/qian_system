package com.qian.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.qian.system.domain.entity.SysUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SysUserServiceTest {
    @Autowired
    private ISysUserService userService;

    @Test
    public void testSelectUserList() {
        SysUser user = new SysUser();
        user.setUserName("admin");
        assertNotNull(userService.selectUserList(user));
    }

    @Test
    public void testSelectUserByUserName() {
        SysUser user = userService.selectUserByUserName("admin");
        assertNotNull(user);
        assertEquals("admin", user.getUserName());
    }

    @Test
    public void testCheckUserNameUnique() {
        SysUser user = new SysUser();
        user.setUserName("admin");
        assertFalse(userService.checkUserNameUnique(user));
    }

    @Test
    public void testCheckPhoneUnique() {
        SysUser user = new SysUser();
        user.setPhonenumber("15888888888");
        assertFalse(userService.checkPhoneUnique(user));
    }

    @Test
    public void testCheckEmailUnique() {
        SysUser user = new SysUser();
        user.setEmail("admin@163.com");
        assertFalse(userService.checkEmailUnique(user));
    }
} 