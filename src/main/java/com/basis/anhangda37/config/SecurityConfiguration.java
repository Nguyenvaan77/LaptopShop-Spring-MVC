package com.basis.anhangda37.config;

import org.apache.tomcat.util.net.DispatchType;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpMessageConverterAuthenticationSuccessHandler.AuthenticationSuccess;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

import com.basis.anhangda37.service.CustomUserDetailsService;
import com.basis.anhangda37.service.UserService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new CustomUserDetailsService(userService);
    }

    @Bean
    public DaoAuthenticationProvider authProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);
        // authProvider.setHideUserNotFoundExceptions(false);
        return authProvider;
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.FORWARD,
                                jakarta.servlet.DispatcherType.INCLUDE)
                        .permitAll()

                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/client/**", "/product/**", "/product")
                        .permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .anyRequest().permitAll()
                )

                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl(("/logout?expired"))
                        .maximumSessions(1)//Giới hạn số tài khoản có thể đặng nhập đồng thời vào hệ thống 
                        .maxSessionsPreventsLogin(false))//false cho phép người thứ 2 đăng nhập thì người thứ nhất sẽ out

                .logout(logout -> logout.deleteCookies("JSESSIONID").invalidateHttpSession(true))

                .rememberMe(r -> r.rememberMeServices(rememberMeServices()))

                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .successHandler(getAuthenticationSuccessHandler())
                        .permitAll())
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-deny"))
                        ;

        return http.build();
    }

    /*
     * Bean cho phép có // trong url
     */
    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }

    @Bean
    public AuthenticationSuccessHandler getAuthenticationSuccessHandler() {
        return new CustomSuccessHandler();
    }
}