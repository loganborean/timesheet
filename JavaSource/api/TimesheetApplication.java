package api;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import api.filters.AdminAuthenticationFilter;
import api.filters.AuthenticationFilter;
import api.resources.EmployeeResource;
import api.resources.LoginResource;
import api.resources.TimesheetResource;
import api.resources.TimesheetRowResource;

@ApplicationPath("/api")
public class TimesheetApplication extends Application {
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public TimesheetApplication()
    {  
        classes.add(EmployeeResource.class);
        classes.add(TimesheetResource.class);
        classes.add(TimesheetRowResource.class);
        classes.add(LoginResource.class);
        classes.add(AdminAuthenticationFilter.class);
        classes.add(AuthenticationFilter.class);

    }
    
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
