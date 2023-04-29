package com.zkp.auth.mapper;


import com.zkp.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SysRoleMapperTest {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Test
    public void testSelectAll(){
        List<SysRole> sysRoleList = sysRoleMapper.selectList(null);
//        System.out.println(sysRoleList);
        sysRoleList.forEach(System.out::println);
    }

}