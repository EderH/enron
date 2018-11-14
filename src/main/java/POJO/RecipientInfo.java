package POJO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table
public class RecipientInfo {
    @Id
    @GeneratedValue
    private int rid;
    private int mid;
    private String rtype;
    private String rvalue;
    private java.sql.Timestamp dater;

    public RecipientInfo() {

    }

    public RecipientInfo(int rid, int mid, String rtype, String rvalue, java.sql.Timestamp dater) {
        this.rid = rid;
        this.mid = mid;
        this.rtype = rtype;
        this.rvalue = rvalue;
        this.dater = dater;
    }

    public int getRid() {
        return rid;
    }

    public int getMid() {
        return mid;
    }

    public String getRtype() {
        return rtype;
    }

    public String getRvalue() {
        return rvalue;
    }

    public Timestamp getDater() {
        return dater;
    }

}
