package sigo.fe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String ZUL_FILES = "/zkau/web/**/*.zul";
    public static final String[] ZK_RESOURCES = {"/zkau/web/**/js/**", "/zkau/web/**/zul/css/**", "/zkau/web/**/img/**"};
    // allow desktop cleanup after logout or when reloading login page
    public static final String REMOVE_DESKTOP_REGEX = "/zkau\\?dtid=.*&cmd_0=rmDesktop&.*";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(ZUL_FILES).denyAll() // block direct access to zul files
                .antMatchers(HttpMethod.GET, ZK_RESOURCES).permitAll() // allow zk resources
                .regexMatchers(HttpMethod.GET, REMOVE_DESKTOP_REGEX).permitAll() // allow desktop cleanup
                .requestMatchers(req -> "rmDesktop".equals(req.getParameter("cmd_0"))).permitAll() // allow desktop cleanup from ZATS
                .mvcMatchers("/","/login","/logout").permitAll()
                .mvcMatchers("/secure/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").defaultSuccessUrl("/secure/main")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}