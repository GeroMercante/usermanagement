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

            Integer employeeId = (Integer) client.execute("execute_kw", params);
            System.out.println("Created Employee ID: " + employeeId);
            return employeeId != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}