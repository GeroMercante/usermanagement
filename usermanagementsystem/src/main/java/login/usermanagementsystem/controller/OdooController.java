package login.usermanagementsystem.controller;
import login.usermanagementsystem.integration.dto.*;
import login.usermanagementsystem.service.OdooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = odooService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/task")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectId(@RequestParam Integer projectId) {
        List<TaskDTO> tasks = odooService.getTasksByProjectId(projectId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/all-employees")
    public ResponseEntity<List<PeopleDTO>> getAllEmployees() {
        List<PeopleDTO> employees = odooService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/timesheets")
    public ResponseEntity<String> createTimesheet(@RequestBody TimesheetDTO timesheetDTO) {
        try {
            Integer employeeId = odooService.findEmployeeIdByName(timesheetDTO.getEmployeeName());
            if (employeeId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found");
            }

            Integer projectId = odooService.findProjectIdByName(timesheetDTO.getProjectName());
            if (projectId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found");
            }

            Integer taskId = odooService.findTaskIdByName(timesheetDTO.getTaskName());
            if (taskId == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            }

            boolean success = odooService.createTimesheet(
                    employeeId,
                    projectId,
                    taskId,
                    timesheetDTO.getDate(),
                    timesheetDTO.getDescription(),
                    timesheetDTO.getHours()
            );

            if (success) {
                return ResponseEntity.ok("Timesheet created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create timesheet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @PostMapping("/timesheets/v2")
    public ResponseEntity<String> createTimesheetV2(@RequestBody TimesheetDTO timesheetDTO) {
        try {
            boolean success = odooService.createTimesheet(
                    timesheetDTO.getIdEmployee(),
                    timesheetDTO.getIdProject(),
                    timesheetDTO.getIdTask(),
                    timesheetDTO.getDate(),
                    timesheetDTO.getDescription(),
                    timesheetDTO.getHours()
            );

            if (success) {
                return ResponseEntity.ok("Timesheet created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create timesheet");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

}