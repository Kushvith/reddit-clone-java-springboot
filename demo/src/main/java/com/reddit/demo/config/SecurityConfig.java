package com.reddit.demo.config;
import java.util.List;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.reddit.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
      private final UserDetailsService userDetailsService;
      private final JwtAuthenticationFilter JwtAuthenticationFilter;
      @Bean
      CorsFilter corsFilter() {
          CorsFilter filter = new CorsFilter();
          return filter;
      }
      @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,HandlerMappingIntrospector introspector) throws Exception {
       MvcRequestMatcher auth = new MvcRequestMatcher(introspector, "/**");
       auth.setServletPath("/api/auth/**");
           http.csrf(c -> c.disable())
          
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
               
                .authorizeHttpRequests((authorize) -> authorize.requestMatchers(auth).permitAll()
                .requestMatchers("/api-docs",
                "/swagger-ui/**",
                "/webjars/**").permitAll()
                .anyRequest().authenticated()
                )
                 
          //        .cors(c->c.configurationSource(r->{
          //   var cor = new CorsConfiguration();
          //   cor.setAllowedOrigins(List.of("http://localhost:4200"));
          //   cor.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
          //   cor.setAllowedHeaders(List.of("*"));
          //   // cor.setAllowCredentials(true);
          //   return cor;
          //  }))
                ;
            http.addFilterBefore(JwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
          
          return http.build();

    }
    // @Autowired
    // public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
    //   authenticationManagerBuilder.userDetailsService(userDetailsService)
    //   .passwordEncoder(passwordEncoder());
    // }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
      var cor = new CorsConfiguration();
            cor.addAllowedOrigin("http://localhost:4200");
            cor.addAllowedMethod("*");
            cor.addAllowedHeader("*");
            cor.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", cor);
            return source;
    }
    @Bean 
    public AuthenticationProvider authenticationProvider(){
      DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
      authenticationProvider.setUserDetailsService(userDetailsService);
      authenticationProvider.setPasswordEncoder(passwordEncoder());
      return authenticationProvider;
    }
     @Bean
     PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
     }
     @Bean
     public AuthenticationManager AuthenticationManager(AuthenticationConfiguration config) throws Exception{
      return config.getAuthenticationManager();
     }
}
