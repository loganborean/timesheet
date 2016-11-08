package singleAction;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ca.bcit.infosys.employee.Credentials;
import services.UserService;

@Named("login")
@RequestScoped
public class Login extends Credentials implements Serializable {

    @Inject private UserService user;

    public String loginAction() {
        return user.loginAction(this);
    }


}
