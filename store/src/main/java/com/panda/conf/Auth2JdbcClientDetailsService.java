package com.panda.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author Panda.HuangWei.
 * @since 2017-07-16 17:39.
 */
@Component
public class Auth2JdbcClientDetailsService extends JdbcClientDetailsService {
    @Autowired
    public Auth2JdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }
}