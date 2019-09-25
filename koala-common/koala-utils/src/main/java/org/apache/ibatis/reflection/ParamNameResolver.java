package org.apache.ibatis.reflection;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.igetwell.common.uitls.Pagination;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 重写mybatis isSpecialParameter方法
 * @see org.apache.ibatis.reflection.ParamNameResolver
 */
public class ParamNameResolver {
    private static final String GENERIC_NAME_PREFIX = "param";
    private final SortedMap<Integer, String> names;
    private boolean hasParamAnnotation;

    public ParamNameResolver(Configuration config, Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        SortedMap<Integer, String> map = new TreeMap();
        int paramCount = paramAnnotations.length;

        for(int paramIndex = 0; paramIndex < paramCount; ++paramIndex) {
            if (!isSpecialParameter(paramTypes[paramIndex])) {
                String name = null;
                Annotation[] var9 = paramAnnotations[paramIndex];
                int var10 = var9.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    Annotation annotation = var9[var11];
                    if (annotation instanceof Param) {
                        this.hasParamAnnotation = true;
                        name = ((Param)annotation).value();
                        break;
                    }
                }

                if (name == null) {
                    if (config.isUseActualParamName()) {
                        name = this.getActualParamName(method, paramIndex);
                    }

                    if (name == null) {
                        name = String.valueOf(map.size());
                    }
                }

                map.put(paramIndex, name);
            }
        }

        this.names = Collections.unmodifiableSortedMap(map);
    }

    private String getActualParamName(Method method, int paramIndex) {
        return (String)ParamNameUtil.getParamNames(method).get(paramIndex);
    }

    /**
     * 重写isSpecialParameter方法
     * @param clazz
     * @return
     */
    private static boolean isSpecialParameter(Class<?> clazz) {
        return Pagination.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
    }

    public String[] getNames() {
        return (String[])this.names.values().toArray(new String[0]);
    }

    public Object getNamedParams(Object[] args) {
        int paramCount = this.names.size();
        if (args != null && paramCount != 0) {
            if (!this.hasParamAnnotation && paramCount == 1) {
                return args[(Integer)this.names.firstKey()];
            } else {
                Map<String, Object> param = new MapperMethod.ParamMap();
                int i = 0;

                for(Iterator var5 = this.names.entrySet().iterator(); var5.hasNext(); ++i) {
                    Map.Entry<Integer, String> entry = (Map.Entry)var5.next();
                    param.put((String)entry.getValue(), args[(Integer)entry.getKey()]);
                    String genericParamName = "param" + String.valueOf(i + 1);
                    if (!this.names.containsValue(genericParamName)) {
                        param.put(genericParamName, args[(Integer)entry.getKey()]);
                    }
                }

                return param;
            }
        } else {
            return null;
        }
    }
}
