package POJO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue
    private int mid;
    private String sender;
    private java.sql.Timestamp date;
    private String message_id;
    private String subject;
    private String body;
    private String folder;

    public Message() {

    }

    public Message(int mid, String sender, java.sql.Timestamp date, String message_id, String subject, String body, String folder) {
        this.mid = mid;
        this.sender = sender;
        this.date = date;
        this.message_id = message_id;
        this.subject = subject;
        this.body = body.substring(1,250);
        this.folder = folder;
    }

    public int getmid() {
        return mid;
    }

    public String getSender() {
        return sender;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public String getMessagemid() {
        return message_id;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public String getFolder() {
        return folder;
    }

    public void setmid(int mid) {
        this.mid = mid;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }

    public void setMessagemid(String message_id) {
        this.message_id = message_id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

}
