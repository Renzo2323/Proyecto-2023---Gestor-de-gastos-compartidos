package com.splitit.splitit.backend.Security;

import java.util.Arrays;
import java.util.Collections;

import com.splitit.splitit.backend.Repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
public class SecurityConfig{
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private JWTFilter filter;
	@Autowired
	private MyUserDetailsService userDetailsService;

	
	@Bean
    public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
    }
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
	        .cors(Customizer.withDefaults())
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(ahr -> ahr
	                
	                //Todos los usuarios
	                .requestMatchers(
	                        HttpMethod.POST,
	                        "/api/usuarios/all/**",
	                        "/api/pagos/all/**",
	                        "/api/grupos/all/**",
	                        "/api/gastosCompartidos/all/**",
	                        "/api/categorias/all/**",
	                        "/api/auditoria/all/**",
	                        "/notificaciones/**",
	                        "/swagger-ui/**",
	                        "/swagger-resources/*",
	                        "/api-docs/**",
	                        "/swagger-ui/index.html#/"
	                ).permitAll()
	                
	                .requestMatchers(
	                        HttpMethod.GET,
	                        "/api/usuarios/all/**",
	                        "/api/pagos/all/**",
	                        "/api/grupos/all/**",
	                        "/api/gastosCompartidos/all/**",
	                        "/api/categorias/all/**",
	                        "/api/auditoria/all/**",
	                        "/notificaciones/**",
	                        "/swagger-ui/**",
	                        "/swagger-resources/*",
	                        "/api-docs/**"
	                ).permitAll()
	                
	                .requestMatchers(
	                        HttpMethod.PUT,
	                        "/api/usuarios/all/**",
	                        "/api/pagos/all/**",
	                        "/api/grupos/all/**",
	                        "/api/gastosCompartidos/all/**",
	                        "/api/categorias/all/**",
	                        "/api/auditoria/all/**",
	                        "/notificaciones/**",
	                        "/swagger-ui/**",
	                        "/swagger-resources/*",
	                        "/api-docs/**"
	                ).permitAll()
	                
	                .requestMatchers(
	                        HttpMethod.DELETE,
	                        "/api/usuarios/all/**",
	                        "/api/pagos/all/**",
	                        "/api/grupos/all/**",
	                        "/api/gastosCompartidos/all/**",
	                        "/api/categorias/all/**",
	                        "/api/auditoria/all/**",
	                        "/notificaciones/**",
	                        "/swagger-ui/**",
	                        "/swagger-resources/*",
	                        "/api-docs/**"
	                ).permitAll()
	                
	                //Usuarios finales
	    			.requestMatchers(
	    	                HttpMethod.POST,
	                        "/api/usuarios/usf/**",
	                        "/api/pagos/usf/**",
	                        "/api/grupos/usf/**",
	                        "/api/gastosCompartidos/usf/**",
	                        "/api/categorias/usf/**",
	                        "/api/auditoria/usf/**"
	    	        ).hasRole("USUARIO")
	    			
	    			.requestMatchers(
	    					HttpMethod.GET,
	    					"/api/usuarios/usf/**",
	                        "/api/pagos/usf/**",
	                        "/api/grupos/usf/**",
	                        "/api/gastosCompartidos/usf/**",
	                        "/api/categorias/usf/**",
	                        "/api/auditoria/usf/**",
	                        "/api/reportes/usf/**"
	    	        ).hasRole("USUARIO")
	    			
	    			.requestMatchers(
	    	                HttpMethod.DELETE,
	    	                "/api/usuarios/usf/**",
	                        "/api/pagos/usf/**",
	                        "/api/grupos/usf/**",
	                        "/api/gastosCompartidos/usf/**",
	                        "/api/categorias/usf/**",
	                        "/api/auditoria/usf/**"
	    	        ).hasRole("USUARIO")
	    			
	    			.requestMatchers(
	    					HttpMethod.PUT,
	    					"/api/usuarios/usf/**",
	                        "/api/pagos/usf/**",
	                        "/api/grupos/usf/**",
	                        "/api/gastosCompartidos/usf/**",
	                        "/api/categorias/usf/**",
	                        "/api/auditoria/usf/**"
	    	        ).hasRole("USUARIO")
	    			
	    			//ADMINISTRADORES
	    			.requestMatchers(
	    					HttpMethod.POST,
	                        "/api/usuarios/adm/**",
	                        "/api/pagos/adm/**",
	                        "/api/grupos/adm/**",
	                        "/api/gastosCompartidos/adm/**",
	                        "/api/categorias/adm/**",
	                        "/api/auditoria/adm/**",
	                        "/api/parametros/adm/**"
	    	        ).hasRole("ADMINISTRADOR")
	    			
	    			.requestMatchers(
	    	                HttpMethod.GET,
	    	                "/api/usuarios/adm/**",
	                        "/api/pagos/adm/**",
	                        "/api/grupos/adm/**",
	                        "/api/gastosCompartidos/adm/**",
	                        "/api/categorias/adm/**",
	                        "/api/auditoria/adm/**",
	                        "/api/parametros/adm/**",
	                        "api/reportes/adm/**"
	    	        ).hasRole("ADMINISTRADOR")
	    			
	    			.requestMatchers(
	    	                HttpMethod.DELETE,
	    	                "/api/usuarios/adm/**",
	                        "/api/pagos/adm/**",
	                        "/api/grupos/adm/**",
	                        "/api/gastosCompartidos/adm/**",
	                        "/api/categorias/adm/**",
	                        "/api/auditoria/adm/**",
	                        "/api/parametros/adm/**"
	    	        ).hasRole("ADMINISTRADOR")
	    			
	    			.requestMatchers(
	    	                HttpMethod.PUT,
	    	                "/api/usuarios/adm/**",
	                        "/api/pagos/adm/**",
	                        "/api/grupos/adm/**",
	                        "/api/gastosCompartidos/adm/**",
	                        "/api/categorias/adm/**",
	                        "/api/auditoria/adm/**"
	    	        ).hasRole("ADMINISTRADOR")
	    			
	    			//USUARIOS FINALES + ADMINISTRADORES
	    			.requestMatchers(
	    					HttpMethod.POST,
	                        "/api/usuarios/usr/**",
	                        "/api/pagos/usr/**",
	                        "/api/grupos/usr/**",
	                        "/api/gastosCompartidos/usr/**",
	                        "/api/categorias/usr/**",
	                        "/api/auditoria/usr/**"
	    	        ).hasAnyRole("ADMINISTRADOR","USUARIO")
	    			
	    			.requestMatchers(
	    	                HttpMethod.GET,
	    	                "/api/usuarios/usr/**",
	                        "/api/pagos/usr/**",
	                        "/api/grupos/usr/**",
	                        "/api/gastosCompartidos/usr/**",
	                        "/api/categorias/usr/**",
	                        "/api/auditoria/usr/**"
	    	        ).hasAnyRole("ADMINISTRADOR","USUARIO")
	    			
	    			.requestMatchers(
	    	                HttpMethod.DELETE,
	    	                "/api/usuarios/usr/**",
	                        "/api/pagos/usr/**",
	                        "/api/grupos/usr/**",
	                        "/api/gastosCompartidos/usr/**",
	                        "/api/categorias/usr/**",
	                        "/api/auditoria/usr/**"
	    	        ).hasAnyRole("ADMINISTRADOR","USUARIO")
	    			
	    			.requestMatchers(
	    	                HttpMethod.PUT,
	    	                "/api/usuarios/usr/**",
	                        "/api/pagos/usr/**",
	                        "/api/grupos/usr/**",
	                        "/api/gastosCompartidos/usr/**",
	                        "/api/categorias/usr/**",
	                        "/api/auditoria/usr/**"
	    	        ).hasAnyRole("ADMINISTRADOR","USUARIO")
	    			
	    			.anyRequest()
	    				.authenticated()
	                )
            .sessionManagement(sh -> sh.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        	.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
		
        return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Collections.singletonList("*"));
	    configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
	    configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
		
	 @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	        return authConfig.getAuthenticationManager();
	    }

}
