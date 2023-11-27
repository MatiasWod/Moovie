package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.CustomAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.MoovieUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@ComponentScan("ar.edu.itba.paw.webapp.auth")
@EnableWebSecurity
@Configuration
public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private MoovieUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Value("classpath:supersecrete.key")
    private Resource supersecrete;

    private AuthenticationFailureHandler authenticationFailureHandler = new AuthenticationFailureHandler() {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            String contextPath = request.getContextPath();
            if (exception instanceof DisabledException) {
                // Account wasnt verified
                response.sendRedirect(contextPath + "/login?error=disabled");
            } else if (exception instanceof LockedException) {
                // Account wasnt verified
                int uid = userService.findUserByUsername(request.getParameter("username")).getUserId();
                response.sendRedirect(contextPath + "/bannedMessage/" + uid);
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
                    .key(FileCopyUtils.copyToString(new InputStreamReader(supersecrete.getInputStream())))
                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .deleteCookies("JSESSIONID")
                    .deleteCookies("remember-me")
                .and().authorizeRequests()
                    .antMatchers("/login", "/register").anonymous()
                    .antMatchers( "/createreview", "/uploadProfilePicture","/createrating","/insertMediaToList","/like", "/createlist", "/profile/**"
                            ,"/createListAction","/deleteMediaFromList","/likeReview","/unlikeReview","/editList/**", "/updateMoovieListOrder/**", "/followList","/likeMoovieListReview","/unlikeMoovieListReview","/MoovieListReview",
                            "/likeComment","/dislikeComment","/createcomment","/reports/new","/deleteUserReview/**", "/deleteMoovieList/**").hasRole( "USER")
                    .antMatchers(  "/deleteList/**","/deleteReview/**","/banUser/**","/unbanUser/**","/makeUserMod/**","/deleteUserMoovieListReviewMod/**","/reports/review/**","/reports/resolve/**").hasRole("MODERATOR")
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

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
