package domain;

import java.sql.Timestamp;

import ca.bcit.infosys.employee.Employee;

public class Token {
    
    private int id;
    private Employee emp;
    private String token;
    private Timestamp expires_at;
    
    public Token(int id, Employee emp, String token, Timestamp expires_at) {
        this.id = id;
        this.emp = emp;
        this.token = token;
        this.expires_at = expires_at;
    }

    public Employee getEmp() {
        return emp;
    }

    public void setEmp(Employee emp) {
        this.emp = emp;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public Timestamp getExpires_at() {
        return expires_at;
    }
    public void setExpires_at(Timestamp expires_at) {
        this.expires_at = expires_at;
    }

}
