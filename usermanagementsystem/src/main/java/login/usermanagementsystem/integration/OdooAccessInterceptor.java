package login.usermanagementsystem.integration;
import login.usermanagementsystem.service.OdooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class OdooAccessInterceptor {

    @Autowired
    private OdooService odooService;

    public boolean checkAccessRights(String model, String operation) {
        return odooService.checkAccessRights(model, operation);
    }
}
