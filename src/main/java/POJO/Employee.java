package POJO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Employee {

    @Id
    @GeneratedValue
    private int eid;
    private String firstName;
    private String lastName;
    private String email_id;

    public Employee() {

    }

    public Employee(int eid, String firstName, String lastName, String email_id) {
        this.eid = eid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email_id = email_id;
    }

    public int getEid() {
        return eid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}
