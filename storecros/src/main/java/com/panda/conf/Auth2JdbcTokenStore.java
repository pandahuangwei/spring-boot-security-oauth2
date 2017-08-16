package com.panda.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author panda.
 * @since 2017-07-16 17:39.
 */
@Component
public class Auth2JdbcTokenStore extends JdbcTokenStore {
    @Autowired//构造类注入
    public Auth2JdbcTokenStore(DataSource dataSource) {
        super(dataSource);
    }
}