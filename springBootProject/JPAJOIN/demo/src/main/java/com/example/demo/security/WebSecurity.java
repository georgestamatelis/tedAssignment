package com.example.demo.security;

import com.example.demo.UserPrincipalDetailsService;
import com.example.demo.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    UserPrincipalDetailsService userPrincipalDetailsService;
   UserRepository userRepository;
    public WebSecurity(UserPrincipalDetailsService userPrincipalDetailsService,UserRepository userRepository) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.userRepository=userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(new JWTAuthorizationFilter(authenticationManager(),  this.userRepository))
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
             .antMatchers("/img/byId").permitAll()
                //Apartment Methods
                .antMatchers(HttpMethod.GET,"/demo/Apartments/**").permitAll()
                .antMatchers(HttpMethod.POST,"/demo/Apartments/**").hasRole("HOST")
                .antMatchers(HttpMethod.PUT,"/demo/Apartments/**").hasRole("HOST")
                .antMatchers(HttpMethod.DELETE,"/demo/Apartments/**").hasRole("HOST")
                //user Methods
                .antMatchers(HttpMethod.POST ,"/demo/user/**").permitAll()
                //EVERYONE CAN LOGIN BUT ONLY AUTHENTICATED GUYS CAN DO THE FOLLOWING
                .antMatchers("/demo/user/usernames").permitAll()
                .antMatchers("/api/Colab").authenticated()
                .antMatchers(HttpMethod.PUT, "/demo/user/**").authenticated()
                .antMatchers(HttpMethod.GET,"/demo/user/**").authenticated()
                //ADMIN METHODS
                .antMatchers("/demo/admin/**").hasRole("ADMIN")
                //REVIEWS
                .antMatchers("/demo/user/usernames").permitAll()
             .antMatchers("/accesories/Reviews").permitAll()
                .antMatchers("/accesories/Apartment/**").permitAll()
            .anyRequest()
            .authenticated()
            .and().
            httpBasic();
    }
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());//userDetailsService(userPrincipalDetailsService);//.passwordEncoder(this.passwordEncoder());
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoauthenticationProvider=new DaoAuthenticationProvider();
        daoauthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        daoauthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoauthenticationProvider;
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(4);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        System.out.println("fuck cors");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration conf=new CorsConfiguration().applyPermitDefaultValues();
        conf.setAllowCredentials(true);
        conf.addAllowedOrigin("**");
        conf.addAllowedHeader("**");
        conf.addAllowedMethod("**");
        conf.setAllowedMethods(Arrays.asList("GET","PUT","POST","DELETE"));
        source.registerCorsConfiguration("/**",conf);
        return source;
    }
}
