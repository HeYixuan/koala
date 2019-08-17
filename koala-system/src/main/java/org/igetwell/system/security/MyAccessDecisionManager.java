package org.igetwell.system.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * 该类为访问决策器，决定某个用户具有的角色，是否有足够的权限去访问某个资源，实现用户和访问权限的对应关系。
 * 这个类主要是处理用户在访问某个URL的时候，就会通过访问该类的权限与登录用户所拥有的权限做比较，
 * 如果用户拥有权限，那就可以到访问资源，如果没有权限，那不能访问资源，还会抛一个异常。
 * AccessdecisionManager在Spring security中是很重要的。
 * 验证部分简略提过所有的Authentication实现需要保存在一个GrantedAuthority对象数组中。 这就是赋予给主体的权限。
 * GrantedAuthority对象通过AuthenticationManager 保存到 Authentication对象里，然后从AccessDecisionManager读出来，进行授权判断。
 * Spring Security提供了一些拦截器，来控制对安全对象的访问权限，例如方法调用或web请求。
 * 一个是否允许执行调用的预调用决定，是由AccessDecisionManager实现的。
 * 这个 AccessDecisionManager被AbstractSecurityInterceptor调用， 用来作最终访问控制的决定。
 *
 * 这个AccessDecisionManager接口包含三个方法：
 * void decide(Authentication authentication, Object secureObject, List<ConfigAttributeDefinition> config);
 * boolean  supports(ConfigAttribute attribute);  boolean supports(Class clazz);
 * 第一个方法：AccessDecisionManager使用方法参数传递所有信息，认证评估时进行决定。
 * 如果访问被拒绝，实现将抛出一个AccessDeniedException异常。
 * 第二个方法：在启动的时候被 AbstractSecurityInterceptor调用，
 * 来决定AccessDecisionManager 是否可以执行传递ConfigAttribute。
 * 第三个方法：被安全拦截器实现调用， 安全拦截器将显示的AccessDecisionManager支持安全对象的类型。
 *
 * @author HeYixuan
 * @create 2017-05-03 10:05
 */

@Component
public class MyAccessDecisionManager implements AccessDecisionManager {

    /**
     * 该方法：需要比较权限和权限配置
     * object参数是一个 URL, 同一个过滤器该url对应的权限配置被传递过来.
     * 查看authentication是否存在权限在configAttributes中
     * 如果没有匹配的权限, 扔出一个拒绝访问的异常
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null){
            return;
        }
        Iterator<ConfigAttribute> it = configAttributes.iterator();
        while (it.hasNext()) {
            ConfigAttribute ca = it.next();
            String needRole = ca.getAttribute();
            //grant 为用户所被赋予的权限，needRole为访问相应的资源应具有的权限
            for (GrantedAuthority gra : authentication.getAuthorities()) {
                if (needRole.trim().equals(gra.getAuthority().trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("Access Denied");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
