package com.zkp.security.filter;

import com.alibaba.fastjson.JSON;
import com.zkp.common.jwt.JwtHelper;
import com.zkp.common.result.Result;
import com.zkp.common.result.ResultCodeEnum;
import com.zkp.common.utils.ResponseUtil;
import com.zkp.security.custom.LoginUserInfoHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 认证解析token过滤器
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("uri:"+request.getRequestURI());
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        // 查看请求头里是否有token信息，如果没有，那就需要登录，如果有就放到上下文信息里
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        logger.info("token:"+token);
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            logger.info("username:"+username);
            if (!StringUtils.isEmpty(username)) {
                //通过ThreadLocal记录当前登录人信息
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(username);
                //通过username从Redis中获取权限数据
                String authoritiesString = (String) redisTemplate.opsForValue().get(username);
                //如果不为空，把Redis获取到的数据json转换成集合类型List<SimpleGrantedAuthority>
                if(!StringUtils.isEmpty(authoritiesString)){
                    List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                    System.out.println("--------------------");
                    System.out.println(mapList);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (Map map : mapList) {
                        authorities.add(new SimpleGrantedAuthority((String)map.get("authority")));
                    }
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                } else {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }
            }
        }
        return null;
    }

/**
 * [{authority=bnt.menu.add}, {authority=bnt.menu.list}, {authority=bnt.menu.remove},
 * {authority=bnt.menu.removeMenu}, {authority=bnt.menu.syncMenu}, {authority=bnt.menu.update},
 * {authority=bnt.process.list}, {authority=bnt.processTemplate.list}, {authority=bnt.processTemplate.onlineProcessSet},
 * {authority=bnt.processTemplate.publish}, {authority=bnt.processTemplate.remove}, {authority=bnt.processTemplate.templateSet},
 * {authority=bnt.processType.add}, {authority=bnt.processType.list}, {authority=bnt.processType.remove},
 * {authority=bnt.processType.update}, {authority=bnt.sysDept.add}, {authority=bnt.sysDept.list},
 * {authority=bnt.sysDept.remove}, {authority=bnt.sysDept.update}, {authority=bnt.sysLoginLog.list},
 * {authority=bnt.sysMenu.add}, {authority=bnt.sysMenu.list}, {authority=bnt.sysMenu.remove},
 * {authority=bnt.sysMenu.update}, {authority=bnt.sysOperLog.list}, {authority=bnt.sysPost.add},
 * {authority=bnt.sysPost.list}, {authority=bnt.sysPost.remove}, {authority=bnt.sysPost.update},
 * {authority=bnt.sysRole.add}, {authority=bnt.sysRole.assignAuth}, {authority=bnt.sysRole.list},
 * {authority=bnt.sysRole.remove}, {authority=bnt.sysRole.update}, {authority=bnt.sysUser.add}, {authority=bnt.sysUser.assignRole},
 * {authority=bnt.sysUser.list}, {authority=bnt.sysUser.remove}, {authority=bnt.sysUser.update}]
 */
}
