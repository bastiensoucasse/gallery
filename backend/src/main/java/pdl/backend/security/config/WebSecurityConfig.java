package pdl.backend.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import pdl.backend.security.jwt.AuthTokenFilter;
import pdl.backend.security.jwt.JwtAuthenticationEntryPoint;
import pdl.backend.security.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity // Allows spring to find and apply this class
@EnableGlobalMethodSecurity( // provides AOP security on methods, GET, POST, DELETE etc...
    //securedEnabled = true,
    //jsr250Enabled = true,
    prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService customUserDetailsService; // load user details to perform authentification and authorization

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public AuthTokenFilter authTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }


    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();// encoder to be sure to not use plain text
    }

    /**
     * This methods tell spring how to configure CORS and CSRF
     * when we want to require all user to be authentifacted or not
     * Also which filter to use and when to use it
     * And which exception handler is use
     *
     * CORS: https://developer.mozilla.org/fr/docs/Web/HTTP/CORS
     *
     */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
            .cors()
                .and()
            .csrf()
                .disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authorizeRequests()
                .antMatchers("/", "/images/**", "/auth/**", "/static/**", "/api/test/**", "/user/**")
                    .permitAll()
                .anyRequest()
                    .authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class); // use filter authToken

			/*.authorizeRequests()
				.antMatchers("/", "/images/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.permitAll();*/
	}


}