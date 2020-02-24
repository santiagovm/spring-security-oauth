package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(createDataSource())

               .withClient("sampleClientId")
               .authorizedGrantTypes("implicit")
               .scopes("read")
               .autoApprove(true)
               .and()

               .withClient("clientIdPassword")
               .secret("secret")
               .authorizedGrantTypes("password", "authorization_code", "refresh_token")
               .scopes("read");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(createTokenStore())
                 .authenticationManager(_authenticationManager);
    }

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager _authenticationManager;

    @Bean
    public TokenStore createTokenStore() {
        return new JdbcTokenStore(createDataSource());
    }

    @Bean
    public DataSourceInitializer createDataSourceInitializer(DataSource dataSource) {

        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(createDatabasePopulator());
        return initializer;
    }

    private DatabasePopulator createDatabasePopulator() {

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(_schemaScript);
        return populator;
    }

    @Value("classpath:schema.sql")
    private Resource _schemaScript;

    @Bean
    public DataSource createDataSource() {

        final DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(_env.getProperty("jdbc.driverClassName"));
        dataSource.setUrl(_env.getProperty("jdbc.url"));
        dataSource.setUsername(_env.getProperty("jdbc.user"));
        dataSource.setPassword(_env.getProperty("jdbc.pass"));

        return dataSource;
    }

    @Autowired
    private Environment _env;
}
