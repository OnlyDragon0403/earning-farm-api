package tech.yeez.investment.utils;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @description: utils
 * @author: xiangbin
 * @create: 2022-04-14 13:41
 **/
public class SpringBeanUtil {

    public static ConfigurableApplicationContext applicationContext;

    
    public static <T> T getBean(Class<T> c){
        if(applicationContext==null) return null;
        return applicationContext.getBean(c);
    }


}
