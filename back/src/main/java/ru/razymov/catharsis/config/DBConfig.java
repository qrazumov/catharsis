package ru.razymov.catharsis.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DBConfig {

    @Value("${spring.datasource.hikari.jdbc-url}")
    private String JDBC_URL;
    @Value("${spring.datasource.hikari.driver-class-name}")
    private String JDBC_DRIVER;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private int CONNECTION_TIMEOUT;
    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int MAXIMUM_POOL_SIZE;
    @Value("${spring.datasource.hikari.username}")
    private String USERNAME;
    @Value("${spring.datasource.hikari.password}")
    private String PWD;

    @Bean
    @Primary
    public HikariDataSource initHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(JDBC_URL);
        hikariConfig.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        hikariConfig.setDriverClassName(JDBC_DRIVER);
        hikariConfig.setConnectionInitSql("select 1");
        hikariConfig.setConnectionTestQuery("select 1");
        hikariConfig.setAllowPoolSuspension(false);
        hikariConfig.setConnectionTimeout(CONNECTION_TIMEOUT);
        hikariConfig.setPoolName("HikariPool");
        hikariConfig.setUsername(USERNAME);
        hikariConfig.setPassword(PWD);
        return new HikariDataSource(hikariConfig);
    }

    @Bean
    @Autowired
    public JdbcTemplate igniteJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


}
