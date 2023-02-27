package com.menuit.menuitreplica.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SpringSecurity{

//    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http
                .authorizeRequests()
                    .antMatchers("/user/**").authenticated()
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll()
                .and()
                    .formLogin()
                        .loginPage("/sign-in")
                        .defaultSuccessUrl("/")
                .and()
                    .logout()
                        .logoutSuccessUrl("/sign-in")
                        .invalidateHttpSession(true);
//        http
//                .rememberMe()
//                .rememberMeParameter("remember-me")
//                .tokenValiditySeconds(1209600)
//                .alwaysRemember(true)
//                .userDetailsService(userDetailsService);
        return http.build();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder authManager) throws Exception {
//        // This is the code you usually have to configure your authentication manager.
//        // This configuration will be used by authenticationManagerBean() below.
//    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}