package com.panda.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @author Panda.HuangWei.
 * @since 2017-07-16 11:23.
 */
@Configuration
public class OAuth2Configuration {

    private static final String RESOURCE_ID = "restResource";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) {
            resources.resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/hello").hasAnyRole("ANONYMOUS, USER")
                    .antMatchers("/hello").permitAll()
                    .antMatchers("/greeting").authenticated();
        }

    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        private TokenStore tokenStore = new InMemoryTokenStore();

        @Autowired
        @Qualifier("authenticationManagerBean")
        @SuppressWarnings("SpringJavaAutowiringInspection")
        private AuthenticationManager authenticationManager;


        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.tokenStore(this.tokenStore).authenticationManager(this.authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            /*clients.inMemory()
                    .withClient("memoryclient")
                    .authorizedGrantTypes("password", "refresh_token")
                    .authorities("USER").scopes("read", "write")
                    .resourceIds(RESOURCE_ID).secret("123456");
*/
            clients.inMemory()
                    .withClient("memoryclient").secret("123456")
                    .scopes("read", "write")
                    .autoApprove(true)
                    .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code")
                    .accessTokenValiditySeconds(600)
                    .refreshTokenValiditySeconds(1800);
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setTokenStore(this.tokenStore);
            return tokenServices;
        }
    }
}
