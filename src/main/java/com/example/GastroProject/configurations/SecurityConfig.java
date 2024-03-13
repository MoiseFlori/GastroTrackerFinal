package com.example.GastroProject.configurations;



import com.example.GastroProject.service.impl.UserServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collection;


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
						.requestMatchers("/doctor-page").hasAuthority("ROLE_DOCTOR")
						.requestMatchers("/user-page").hasAnyAuthority("ROLE_PATIENT","ROLE_ADMIN")
						.requestMatchers("/registration", "/css/**","/registration-doctor","/welcome","/redirect")

						.permitAll()
						.anyRequest().authenticated())

				.formLogin(form -> form
						.usernameParameter("email")
						.loginPage("/login")
						.loginProcessingUrl("/login")
						//.defaultSuccessUrl("/user-page", true)
						.successHandler(myAuthenticationSuccessHandler())
						.permitAll())

				.logout(form -> form
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout").permitAll());
		return http.build();

	}
	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
				String redirectUrl = null;
				Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				for (GrantedAuthority grantedAuthority : authorities) {
					if (grantedAuthority.getAuthority().equals("ROLE_DOCTOR")) {
						redirectUrl = "/doctor-page";
						break;
					} else if (grantedAuthority.getAuthority().equals("ROLE_PATIENT")) {
						redirectUrl = "/user-page";
						break;
					}
				}
				if (redirectUrl == null) {
					throw new IllegalStateException();
				}
				response.sendRedirect(redirectUrl);
			}
		};
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
