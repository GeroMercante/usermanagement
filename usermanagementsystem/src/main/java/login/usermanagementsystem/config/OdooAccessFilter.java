package login.usermanagementsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import login.usermanagementsystem.integration.OdooAccessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class OdooAccessFilter extends OncePerRequestFilter {

    @Autowired
    private OdooAccessInterceptor odooAccessInterceptor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String path = request.getRequestURI();

        if (path.startsWith("/odoo")) {
            String model = request.getParameter("model");
            String operation = request.getParameter("operation");

            if (model != null && operation != null && !odooAccessInterceptor.checkAccessRights(model, operation)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
