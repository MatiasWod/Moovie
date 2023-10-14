package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.CustomAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.MoovieUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@ComponentScan("ar.edu.itba.paw.webapp.auth")
@EnableWebSecurity
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MoovieUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler() {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            String contextPath = request.getContextPath();
            if (exception instanceof DisabledException) {
                // Account wasnt verified
                response.sendRedirect(contextPath + "/login?error=disabled");
            } else if (exception instanceof LockedException) {
                // Account was banned
                response.sendRedirect(contextPath + "/login?error=locked");
            }else if(exception instanceof UsernameNotFoundException) {
                // User not found
                response.sendRedirect(contextPath + "/login?error=unknown_user");
            } else if (exception instanceof BadCredentialsException) {
                // Wrong password
                response.sendRedirect(contextPath + "/login?error=bad_credentials");
            }else {
                response.sendRedirect(contextPath + "/login?error=unknown_error");
            }
        }
    };



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
                    //.successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
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
                    .antMatchers( "/createreview", "/uploadProfilePicture","/createrating","/insertMediaToList","/like", "/createlist", "/profile/**" ).hasRole( "USER")
                    .antMatchers( "/deleteReview/**", "/deleteList/**","/banUser/**" ).hasRole("MODERATOR")
                    .antMatchers("/**").permitAll()
                .and().exceptionHandling()
                    .accessDeniedPage("/403")
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
