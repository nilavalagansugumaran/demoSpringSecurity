package com.example.demoSpringSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CreateUser {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void setupDatabase(){
        String createUser = "create table users(" +
                "username varchar(50) not null primary key," +
                "password varchar(100) not null," +
                "enabled boolean not null" +
                ")";
        jdbcTemplate.execute(createUser);

        String createAuthorities = "create table authorities (" +
                "username varchar(50) not null," +
                "authority varchar(50) not null," +
                "constraint fk_authorities_users" +
                " foreign key(username) references users(username)" +
                ")";
        jdbcTemplate.execute(createAuthorities);

        String createIndex = "create unique index ix_auth_username on authorities (username,authority)";
        jdbcTemplate.execute(createIndex);

        jdbcTemplate.update("insert into users(username,password,enabled) " +
                "values(?,?,?)", new Object[]{"admin",encodePassword("admin"),true});

        jdbcTemplate.update("insert into authorities(username,authority)" +
                " values(?,?)", new Object[]{"admin", "ROLE_ADMIN"});

        jdbcTemplate.update("insert into users(username,password,enabled) " +
                "values(?,?,?)", new Object[]{"nila",encodePassword("password"),true});

        jdbcTemplate.update("insert into authorities(username,authority)" +
                " values(?,?)", new Object[]{"nila", "ROLE_USER"});
    }

    private String encodePassword(String password){
        return new BCryptPasswordEncoder().encode(password);
    }

}
