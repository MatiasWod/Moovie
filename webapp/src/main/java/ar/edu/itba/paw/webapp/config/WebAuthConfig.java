package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.models.User.UserRoles;
import ar.edu.itba.paw.services.UserService;
import ar.edu.itba.paw.webapp.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.web.cors.CorsConfiguration.ALL;

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

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private BasicAuthenticationFilter basicAuthenticationFilter;

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
            } else if (exception instanceof UsernameNotFoundException) {
                // User not found
                response.sendRedirect(contextPath + "/login?error=unknown_user");
            } else if (exception instanceof BadCredentialsException) {
                // Wrong password
                response.sendRedirect(contextPath + "/login?error=bad_credentials");
            } else {
                response.sendRedirect(contextPath + "/login?error=unknown_error");
            }
        }
    };

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = String.format("%s > %s",
                UserRoles.MODERATOR.name(), UserRoles.USER.name());
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
        return webExpressionVoter;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = Arrays.asList(
                webExpressionVoter(),
                new RoleVoter(),
                new AuthenticatedVoter()
        );
        return new UnanimousBased(decisionVoters);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(@Value("classpath:jwt.key") Resource jwtKeyResource) throws IOException {
        return new JwtTokenProvider(jwtKeyResource);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new UnauthorizedRequestHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement()
//                .invalidSessionUrl("/")
//                .and().formLogin()
//                    .defaultSuccessUrl("/", false)
//                    .loginPage("/login")
//                    .usernameParameter("username")
//                    .passwordParameter("password")
//                    //.successHandler(customAuthenticationSuccessHandler)
//                    .failureHandler(authenticationFailureHandler)
//                .and().rememberMe()
//                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
//                    .userDetailsService(userDetailsService)
//                    .rememberMeParameter("rememberme")
//                    .key(FileCopyUtils.copyToString(new InputStreamReader(supersecrete.getInputStream())))
//                .and().logout()
//                    .logoutUrl("/logout")
//                    .logoutSuccessUrl("/login")
//                    .deleteCookies("JSESSIONID")
//                    .deleteCookies("remember-me")
//                .and().authorizeRequests()
//                    .antMatchers("/login", "/register").anonymous()
//                    .antMatchers( "/createreview", "/uploadProfilePicture","/createrating","/insertMediaToList","/like", "/createlist", "/profile/**"
//                            ,"/createListAction","/deleteMediaFromList","/likeReview","/unlikeReview","/editList/**", "/updateMoovieListOrder/**", "/followList","/likeMoovieListReview","/unlikeMoovieListReview","/MoovieListReview",
//                            "/likeComment","/dislikeComment","/createcomment","/reports/new","/deleteUserReview/**", "/deleteMoovieList/**").hasRole( "USER")
//                    .antMatchers(  "/deleteList/**","/deleteReview/**","/banUser/**","/unbanUser/**","/makeUserMod/**","/deleteUserMoovieListReviewMod/**","/reports/review/**","/reports/resolve/**").hasRole("MODERATOR")
//                    .antMatchers("/**").permitAll()
//                .and().exceptionHandling()
//                    .accessDeniedPage("/403")
//                .and().csrf().disable();
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedRequestHandler())
                .accessDeniedHandler(new ForbiddenRequestHandler())
                .and()
                .headers().cacheControl().disable()
                .and()
                .authorizeRequests()
                .accessDecisionManager(accessDecisionManager())
                .antMatchers(HttpMethod.GET, "/login", "/register").anonymous()

                .antMatchers(HttpMethod.GET, "/users/authtest").hasRole(UserRoles.USER.name())

                .antMatchers("/createreview", "/uploadProfilePicture", "/createrating", "/insertMediaToList", "/like", "/createlist",
                        "/profile/**", "/createListAction", "/deleteMediaFromList", "/likeReview", "/unlikeReview", "/editList/**",
                        "/updateMoovieListOrder/**", "/followList", "/likeMoovieListReview", "/unlikeMoovieListReview",
                        "/MoovieListReview", "/likeComment", "/dislikeComment", "/createcomment", "/reports/new", "/deleteUserReview/**",
                        "/deleteMoovieList/**").hasRole("USER")
                .antMatchers("/deleteList/**", "/deleteReview/**", "/banUser/**", "/unbanUser/**", "/makeUserMod/**",
                        "/deleteUserMoovieListReviewMod/**", "/reports/review/**", "/reports/resolve/**").hasRole("MODERATOR")
                .antMatchers(HttpMethod.GET, "/users/*").permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .and()
                .cors()
                .and()
                .csrf().disable()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(basicAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList(ALL));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.addAllowedHeader(ALL);
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "Location", "ETag", "Total-Elements"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
