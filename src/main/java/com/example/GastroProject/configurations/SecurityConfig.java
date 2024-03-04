package com.example.GastroProject.configurations;



import com.example.GastroProject.service.impl.UserServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

		http.csrf(AbstractHttpConfigurer::disable)

				.authorizeHttpRequests(request -> request
						.requestMatchers("/admin-page").hasAuthority("ROLE_ADMIN")
						.requestMatchers("/user-page").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
						.requestMatchers("/registration", "/css/**")
						.permitAll()
						.anyRequest().authenticated())

				.formLogin(form -> form
						.usernameParameter("email")
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.defaultSuccessUrl("/user-page",true)
						.permitAll())

				.logout(form -> form
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout").permitAll());
		return http.build();

	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public UserDetailsService userDetailsService() {
        return new UserServiceImpl();
    }



}
