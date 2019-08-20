package org.igetwell.system.configure;

import org.igetwell.system.entity.SystemMenu;
import org.igetwell.system.entity.SystemRole;
import org.igetwell.system.service.ISystemMenuService;
import org.igetwell.system.service.ISystemRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 该类是资源的访问权限的定义，实现资源和访问权限的对应关系
 * 该类的主要作用是在Spring Security的整个过滤链启动后，
 * 在容器启动的时候，程序就会进入到该类中的init()方法，init调用了loadResourceDefine()方法，
 * 该方法的主要目的是将数据库中的所有资源与权限读取到本地缓存中保存起来！
 * 类中的resourceMap就是保存的所有资源和权限的集合，URL为Key，权限作为Value！
 *
 * @author HeYixuan
 * @create 2017-04-27 22:05
 */
@Component
public class MyInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ISystemRoleService iSystemRoleService;
    @Autowired
    private ISystemMenuService iSystemMenuService;

    private ConcurrentMap<String, Collection<ConfigAttribute>> resourceMap = null;

    /**
     * 程序启动的时候就加载所有资源信息
     * 初始化资源与权限的映射关系
     */
    public void init() {
        // 在Web服务器启动时，提取系统中的所有权限
        Collection<SystemRole> roles = iSystemRoleService.getList();
        //应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
        resourceMap = new ConcurrentHashMap<>();
        roles.forEach(role -> {
            ConfigAttribute ca = new SecurityConfig(role.getRoleAlias());
            Collection<SystemMenu> resources = iSystemMenuService.loadByRole(role.getId());
            //这里是循环所有角色,如果角色等于匿名用户,将数据库中未绑定的资源放在匿名角色里面,这样就会拦截
            //如果你一旦给匿名用户配置了这个URL 那么这个URL不会被拦截了,所以有一定的缺陷
            if ("ROLE_ANON".equals(role.getRoleAlias())) {
                Collection<SystemMenu> unbindResources = iSystemMenuService.loadUnbound();
                unbindResources.forEach(e -> {
                    Collection<ConfigAttribute> attrs = new ArrayList<>();
                    attrs.add(ca);
                    resourceMap.put(e.getPath(), attrs);
                });
            } else {
                resources.forEach(r -> {
                    String url = r.getPath();
                    //判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中
                    if (resourceMap.containsKey(url)) { //如果已存在url 加入权限
                        Collection<ConfigAttribute> value = resourceMap.get(url);
                        value.add(ca);
                        resourceMap.put(url, value);
                    } else {//如果不存存在url 加入url和权限
                        Collection<ConfigAttribute> attrs = new ArrayList<ConfigAttribute>();
                        attrs.add(ca);
                        resourceMap.put(url, attrs);
                    }
                });
            }
        });
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //TODO: object 是一个URL，用户请求的url。
        FilterInvocation filterInvocation = (FilterInvocation) object;
        if (resourceMap == null) {
            init();
        }
        //object 中包含用户请求的request 信息
        Iterator it = resourceMap.keySet().iterator();
        while (it.hasNext()) {
            String url = it.next().toString();
            RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
            //这里做权限验证匹配如果匹配到角色对应列表那么执行MyAccessDecisionManager进行更细致的权限验证(重点！！！！)
            if (requestMatcher.matches(filterInvocation.getHttpRequest())) {
                return resourceMap.get(url);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
