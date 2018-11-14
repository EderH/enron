package Entities;

import POJO.Message;

import java.util.List;

public class Mail {

    private int mid;
    private String sender;
    private java.sql.Timestamp date;
    private String message_id;
    private String subject;
    private String body;
    private String folder;
    private List<String> to;

    public Mail() {

    }

    public Mail(Message message) {
        this.mid = message.getmid();
        this.sender = message.getSender();
        this.date = message.getDate();
        this.message_id = message.getMessagemid();
        this.subject = message.getSubject();
        this.body = message.getBody();
        this.folder = message.getFolder();
    }

    public Mail(int mid, String sender, java.sql.Timestamp date, String message_id, String subject, String body, String folder) {
        this.mid = mid;
        this.sender = sender;
        this.date = date;
        this.message_id = message_id;
        this.subject = subject;
        this.body = body.substring(1,250);
        this.folder = folder;
    }

    public int getMid() {
        return mid;
    }

    public String getSender() {
        return sender;
    }

    public java.sql.Timestamp getDate() {
        return date;
    }

    public String getMessageID() {
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

    public List<String> getTo() {
        return to;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }

    public void setMessagemID(String message_id) {
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

    public void setTo(List<String> to) {
        this.to = to;
    }
}
