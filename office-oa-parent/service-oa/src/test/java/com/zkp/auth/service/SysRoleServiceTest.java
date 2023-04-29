package com.zkp.auth.service;

import com.zkp.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SysRoleServiceTest {

    @Autowired
    private SysRoleService service;

    @Test
    void testSelectAll(){
        List<SysRole> sysRoleList = service.list();
        sysRoleList.forEach(System.out::println);
    }
}