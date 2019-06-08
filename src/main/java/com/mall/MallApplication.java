package com.mall;

import com.mall.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableTransactionManagement
@MapperScan("com.mall.mapper") //扫描的mapper
@EnableScheduling
@EnableCaching  //开启缓存功能，为redis服务。
@SpringBootApplication
public class MallApplication {

    public static void main(String[] args) {
        //SpringApplication.run(MallApplication.class, args);
        ApplicationContext applicationContext =
                SpringApplication.run(MallApplication.class, args);
        SpringUtil.setApplicationContext(applicationContext);
        //SpringUtil.printBean();

    }

}
