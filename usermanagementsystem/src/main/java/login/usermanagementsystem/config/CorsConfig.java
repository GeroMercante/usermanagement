package login.usermanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  /**
   * Configura el soporte CORS para la aplicación.
   * 
   * @return WebMvcConfigurer que personaliza el manejo de CORS.
   */
  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurer() {
      /**
       * Configura los mapeos de CORS.
       * 
       * @param registry el registro de configuraciones de CORS.
       */
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite solicitudes CORS a cualquier ruta
          .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite los métodos HTTP especificados
          .allowedOrigins("*"); // Permite solicitudes CORS desde cualquier origen
      }
    };
  }
}