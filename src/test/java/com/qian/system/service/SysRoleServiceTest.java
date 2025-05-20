package com.qian.system.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.qian.system.domain.entity.SysRole;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SysRoleServiceTest {
    @Autowired
    private ISysRoleService roleService;

    @Test
    public void testSelectRoleList() {
        SysRole role = new SysRole();
        role.setRoleName("管理员");
        assertNotNull(roleService.selectRoleList(role));
    }

    @Test
    public void testSelectRolesByUserId() {
        assertNotNull(roleService.selectRolesByUserId(1L));
    }

    @Test
    public void testSelectRoleById() {
        SysRole role = roleService.selectRoleById(1L);
        assertNotNull(role);
        assertEquals("超级管理员", role.getRoleName());
    }

    @Test
    public void testCheckRoleNameUnique() {
        SysRole role = new SysRole();
        role.setRoleName("超级管理员");
        assertFalse(roleService.checkRoleNameUnique(role));
    }

    @Test
    public void testCheckRoleKeyUnique() {
        SysRole role = new SysRole();
        role.setRoleKey("admin");
        assertFalse(roleService.checkRoleKeyUnique(role));
    }

    @Test
    public void testCountUserRoleByRoleId() {
        assertTrue(roleService.countUserRoleByRoleId(1L) > 0);
    }
} 