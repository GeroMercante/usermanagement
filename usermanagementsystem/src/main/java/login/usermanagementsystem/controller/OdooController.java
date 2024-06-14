package login.usermanagementsystem.controller;
import login.usermanagementsystem.integration.dto.EmployeeDTO;
import login.usermanagementsystem.service.OdooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/odoo")
public class OdooController {

    @Autowired
    private OdooService odooService;

    @GetMapping("/check")
    public String checkAccess(@RequestParam String model, @RequestParam String operation) {
        boolean hasAccess = odooService.checkAccessRights(model, operation);
        return "Access check for model: " + model + " and operation: " + operation + " is " + (hasAccess ? "granted" : "denied");
    }

    @PostMapping("/employees")
    public String createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        boolean isCreated = odooService.createEmployee(employeeDTO);
        return isCreated ? "Employee created successfully" : "Failed to create employee";
    }
}