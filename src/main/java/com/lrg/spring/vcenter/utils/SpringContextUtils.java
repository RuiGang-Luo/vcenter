package com.lrg.spring.vcenter.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
/**
 * @author ：denghao
 * @date ：Created in 2019/11/21  11:04
 * @description：Spring Context 工具类
 * @modified By：
 * @version: v1.0$
 */
    @Component
    public class SpringContextUtils implements ApplicationContextAware {
        public static ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext)
                throws BeansException {
            SpringContextUtils.applicationContext = applicationContext;
        }

        public static Object getBean(String name) {
            return applicationContext.getBean(name);
        }
        public static Object getBean(Class classType) {
            return applicationContext.getBean(classType);
        }

        public static <T> T getBean(String name, Class<T> requiredType) {
            return applicationContext.getBean(name, requiredType);
        }

        public static boolean containsBean(String name) {
            return applicationContext.containsBean(name);
        }

        public static boolean isSingleton(String name) {
            return applicationContext.isSingleton(name);
        }

        public static Class<? extends Object> getType(String name) {
            return applicationContext.getType(name);
        }

}
