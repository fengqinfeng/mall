package com.mall;

import com.mall.util.SpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }


}
