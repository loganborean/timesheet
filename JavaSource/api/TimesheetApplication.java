package api;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class TimesheetApplication extends Application {
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public TimesheetApplication()
    {  
        classes.add(EmployeeResource.class);
        classes.add(TimesheetResource.class);
        classes.add(LoginResource.class);

    }
    
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
