package com.ankush.cleancity.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration          //MARK AS CONFIGURATION FILE
@EnableWebSecurity      //ENABLE SECURITY
@EnableJdbcHttpSession  //ENABLE JDBC SESSIONS
public class SecurityConfig {
    @Bean
    public PasswordEncoder password() {
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsManager manager(@Autowired DataSource source) {
        return new JdbcUserDetailsManager(source);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity security, UserDetailsManager users, PasswordEncoder encoder) throws Exception {
        AuthenticationManagerBuilder builder = security.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(users)
                .passwordEncoder(encoder)
        ;
        return builder.build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http, @Autowired UserDetailsManager userDetailsManager, @Autowired AuthenticationManager authenticationManager, @Autowired PasswordEncoder passwordEncoder) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(x -> {
                    x.loginPage("/login");
//                    x.loginPage("http://localhost:3000/login");
                    x.loginProcessingUrl("/java/api/auth/login_page");
                    x.successHandler((a,b,c)->{});
                    x.failureHandler((a,b,c)->{});
                })
                .logout(x -> {
                    x.logoutUrl("/java/api/auth/logout").permitAll();
                    x.logoutSuccessHandler((a,b,c)->{});
                })

//                .formLogin(x->x.loginPage("/login").permitAll())
//                .formLogin(x ->
//                        x
//                                .loginPage("/login.html").permitAll()
////                                .loginProcessingUrl("/java/api/auth/perform_login")
////                                .defaultSuccessUrl("/java/api/home/hello")
////                                .failureUrl("/error.html")
//                )
//                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsManager)
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests(x -> x
                                .requestMatchers("/java/api/test/**").permitAll()
                                .requestMatchers("/java/api/auth/**").permitAll()
                                .requestMatchers("/java/api/home/**").hasAnyAuthority("USER", "ADMIN")
//                        .requestMatchers("/java/api/waste/**").hasAnyAuthority("USER")
                                .requestMatchers("/java/api/waste/**").permitAll()
//                        .requestMatchers("/java/api/user/**").hasAnyAuthority("USER","ADMIN")
                                .requestMatchers("/java/api/userspace/**").hasAnyAuthority("USER")
                                .requestMatchers("/java/api/adminspace/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/java/api/analytics/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers("/java/api/mail/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/java/api/auth/login_page").permitAll()
                                .requestMatchers("/login").permitAll()
//                        .requestMatchers("/login*").permitAll()
                ).build();
    }
}
