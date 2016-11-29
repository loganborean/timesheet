package api;

import java.util.HashSet;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/restful")
public class TimesheetApplication extends Application {
    private Set<Class<?>> classes = new HashSet<Class<?>>();

    public TimesheetApplication()
    {  
        classes.add(EmployeeResource.class);
    }
    
    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
