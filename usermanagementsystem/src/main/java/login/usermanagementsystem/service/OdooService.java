package login.usermanagementsystem.service;
import login.usermanagementsystem.config.OdooConfig;
import login.usermanagementsystem.integration.dto.EmployeeDTO;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;

@Service
public class OdooService {

    @Autowired
    private OdooConfig odooConfig;

    public int authenticate() {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/common"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] versionParams = new Object[]{};
            Object versionResult = client.execute(config, "version", versionParams);
            System.out.println("Server Version: " + versionResult);

            Object[] authParams = new Object[]{
                    odooConfig.getDatabase(),
                    odooConfig.getUsername(),
                    odooConfig.getApiKey(),
                    Collections.emptyMap()
            };
            int uid = (int) client.execute(config, "authenticate", authParams);
            System.out.println("UID: " + uid);
            return uid;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean checkAccessRights(String model, String operation) {
        try {
            Integer userUid = odooConfig.getUserUid();
            if (userUid == null) {
                userUid = authenticate();
                odooConfig.setUserUid(userUid);
            }

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/object"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            List<Object> params = Arrays.asList(
                    odooConfig.getDatabase(),
                    userUid,
                    odooConfig.getApiKey(),
                    model,
                    "check_access_rights",
                    Collections.singletonList(operation),
                    new HashMap<String, Object>() {{
                        put("raise_exception", false);
                    }}
            );
            return (Boolean) client.execute("execute_kw", params);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createEmployee(EmployeeDTO employeeDTO) {
        try {
            Integer userUid = odooConfig.getUserUid();
            if (userUid == null) {
                userUid = authenticate();
                odooConfig.setUserUid(userUid);
            }

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/object"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Map<String, Object> employeeData = new HashMap<>();
            employeeData.put("active", employeeDTO.getActive());
            employeeData.put("email", employeeDTO.getEmail());
            employeeData.put("gender", employeeDTO.getGender());
            employeeData.put("name", employeeDTO.getName());
            employeeData.put("phone", employeeDTO.getPhone());

            Object[] params = new Object[]{
                    odooConfig.getDatabase(),
                    userUid,
                    odooConfig.getApiKey(),
                    "hr.employee",
                    "create",
                    Collections.singletonList(employeeData)
            };
            System.out.println(Arrays.deepToString(params));
            Integer employeeId = (Integer) client.execute("execute_kw", params);
            System.out.println("Created Employee ID: " + employeeId);
            return employeeId != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // get de proyectos

    // get de tareas por id de proyecto

    // get de empleados

    public Integer findEmployeeIdByName(String employeeName) {
        try {
            Integer userUid = odooConfig.getUserUid();
            if (userUid == null) {
                userUid = authenticate();
                odooConfig.setUserUid(userUid);
            }

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/object"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            List<Object> criteria = Arrays.asList(
                    Arrays.asList("name", "=", employeeName)
            );

            Object[] searchParams = new Object[]{
                    odooConfig.getDatabase(),
                    userUid,
                    odooConfig.getApiKey(),
                    "hr.employee",
                    "search",
                    Arrays.asList(criteria)
            };

            Object[] ids = (Object[]) client.execute("execute_kw", searchParams);
            if (ids.length > 0) {
                return (Integer) ids[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer findProjectIdByName(String projectName) {
        try {
            Integer userUid = odooConfig.getUserUid();
            if (userUid == null) {
                userUid = authenticate();
                odooConfig.setUserUid(userUid);
            }

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/object"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            List<Object> criteria = Arrays.asList(
                    Arrays.asList("name", "=", projectName)
            );

            Object[] searchParams = new Object[]{
                    odooConfig.getDatabase(),
                    userUid,
                    odooConfig.getApiKey(),
                    "project.project",
                    "search",
                    Arrays.asList(criteria)
            };

            Object[] ids = (Object[]) client.execute("execute_kw", searchParams);
            if (ids.length > 0) {
                return (Integer) ids[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer findTaskIdByName(String taskName) {
        try {
            Integer userUid = odooConfig.getUserUid();
            if (userUid == null) {
                userUid = authenticate();
                odooConfig.setUserUid(userUid);
            }

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/object"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            List<Object> criteria = Arrays.asList(
                    Arrays.asList("name", "=", taskName)
            );

            Object[] searchParams = new Object[]{
                    odooConfig.getDatabase(),
                    userUid,
                    odooConfig.getApiKey(),
                    "project.task",
                    "search",
                    Arrays.asList(criteria)
            };

            Object[] ids = (Object[]) client.execute("execute_kw", searchParams);
            if (ids.length > 0) {
                return (Integer) ids[0];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createTimesheet(Integer employeeId, Integer projectId, Integer taskId, String date, String description, double hours) {
        try {
            Integer userUid = odooConfig.getUserUid();
            if (userUid == null) {
                userUid = authenticate();
                odooConfig.setUserUid(userUid);
            }

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(odooConfig.getBasePath() + "/xmlrpc/2/object"));
            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Map<String, Object> timesheetData = new HashMap<>();
            timesheetData.put("employee_id", employeeId);
            timesheetData.put("project_id", projectId);
            timesheetData.put("task_id", taskId);
            timesheetData.put("date", date);
            timesheetData.put("name", description);
            timesheetData.put("unit_amount", hours);

            Object[] params = new Object[]{
                    odooConfig.getDatabase(),
                    userUid,
                    odooConfig.getApiKey(),
                    "account.analytic.line",
                    "create",
                    Collections.singletonList(timesheetData)
            };

            Integer timesheetId = (Integer) client.execute("execute_kw", params);
            System.out.println("Created Timesheet ID: " + timesheetId);
            return timesheetId != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}