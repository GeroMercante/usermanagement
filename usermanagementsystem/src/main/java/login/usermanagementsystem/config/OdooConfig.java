package login.usermanagementsystem.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class OdooConfig {
    @Value("${integration.odoo.api-key}")
    private String apiKey;

    @Value("${integration.odoo.base-path}")
    private String basePath;

    @Value("${integration.odoo.database}")
    private String database;

    @Value("${integration.odoo.username}")
    private String username;

    private Integer userUid;

    public void setUserUid(Integer userUid) {
        this.userUid = userUid;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("API Key: " + apiKey);
        System.out.println("Base Path: " + basePath);
        System.out.println("Database: " + database);
        System.out.println("Username: " + username);
    }
}