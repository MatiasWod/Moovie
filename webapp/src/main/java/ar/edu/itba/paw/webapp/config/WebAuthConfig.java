package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.exceptions.UserIsBannedException;
import ar.edu.itba.paw.exceptions.UserNotVerifiedException;
import ar.edu.itba.paw.webapp.auth.MoovieUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.util.concurrent.TimeUnit;

@ComponentScan("ar.edu.itba.paw.webapp.auth")
@EnableWebSecurity
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MoovieUserDetailsService userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement()
                .invalidSessionUrl("/")
                .and().formLogin()
                .defaultSuccessUrl("/", false)
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .userDetailsService(userDetailsService)
                .rememberMeParameter("rememberme")
                .key("ultrasecretkey")
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")
                .deleteCookies("remember-me")
                .and().authorizeRequests()
                .antMatchers("/login", "/register").anonymous()
                .antMatchers("/createList", "/createreview", "/profile/**", "/uploadProfilePicture" ).hasRole("USER")
                .antMatchers("/**").permitAll()
                .and().exceptionHandling()
                .accessDeniedPage("/404")//deberia ser 403
                .and().csrf().disable();
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**",	"/js/**",	"/img/**");
    }

    /*
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("/baobao");
            if (exception instanceof UsernameNotFoundException) {
                response.sendRedirect("/login?error=usernameNotFound");
            } else if (exception instanceof UserNotVerifiedException) {
                response.sendRedirect("/login?error=userNotVerified");
            } else if (exception instanceof UserIsBannedException) {
                response.sendRedirect("/login?error=banned");
            } else {
                response.sendRedirect("/login?error=generic");
            }
        };
    }*/


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
