package com.shendehai.com.datasources;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceDruid {


    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost/seckill?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
        dataSource.setUsername("root");// 用户名
        dataSource.setPassword("6112118");// 密码
        return dataSource;
    }

}
