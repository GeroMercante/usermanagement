package login.usermanagementsystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import login.usermanagementsystem.service.OurUsersDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  
  @Autowired
  private OurUsersDetailsService ourUsersDetailsService;

  @Autowired
  private JWTAuthFilter jwtAuthFilter;

  /**
   * Configura la cadena de filtros de seguridad.
   * 
   * @param httpSecurity objeto HttpSecurity para configurar la seguridad HTTP.
   * @return SecurityFilterChain configurado.
   * @throws Exception en caso de error durante la configuración.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable) // Deshabilita la protección CSRF
        .cors(Customizer.withDefaults()) // Habilita CORS con la configuración predeterminada
        .authorizeHttpRequests(requests -> requests
            .requestMatchers("/auth/**", "/public/**").permitAll() // Permite acceso sin autenticación a las URLs que empiecen con /auth/ y /public/
            .requestMatchers("/admin/**").hasAnyAuthority("ADMIN") // Permite acceso a URLs que empiecen con /admin/ solo a usuarios con autoridad "ADMIN"
            .requestMatchers("/user/**").hasAnyAuthority("USER") // Permite acceso a URLs que empiecen con /user/ solo a usuarios con autoridad "USER"
            .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "USER") // Permite acceso a URLs que empiecen con /adminuser/ a usuarios con autoridad "ADMIN" o "USER"
            .anyRequest().authenticated()) // Requiere autenticación para cualquier otra URL
        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Configura el manejo de sesiones para ser sin estado (stateless)
        .authenticationProvider(authenticationProvider()) // Establece el proveedor de autenticación personalizado
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Añade el filtro de autenticación JWT antes del filtro de autenticación de usuario y contraseña
    
    return httpSecurity.build();
  }

  /**
   * Configura el proveedor de autenticación.
   * 
   * @return AuthenticationProvider configurado.
   */
  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(ourUsersDetailsService); // Establece el servicio de detalles de usuario personalizado
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder()); // Establece el codificador de contraseñas
    return daoAuthenticationProvider;
  }

  /**
   * Configura el codificador de contraseñas.
   * 
   * @return PasswordEncoder configurado.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Usa BCrypt para codificar las contraseñas
  }

  /**
   * Configura el administrador de autenticación.
   * 
   * @param authenticationConfiguration configuración de autenticación de Spring.
   * @return AuthenticationManager configurado.
   * @throws Exception en caso de error durante la configuración.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager(); // Obtiene y retorna el administrador de autenticación de la configuración de autenticación
  }
}
