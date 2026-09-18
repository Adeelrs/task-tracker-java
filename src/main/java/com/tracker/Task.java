package main.java.com.tracker;

public class Task {
    int id;
    String Description;
    String Status;
    public Task(int id, String description) {
        this.id = id;
        Description = description;
        Status = "pending";
    }
    
    public Task(int id, String description, String status) {
        this.id = id;
        Description = description;
        Status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return Description; }
    public void setDescription(String description) { Description = description; }

    public String getStatus() { return Status; }
    public void setStatus(String status) { Status = status; }   

}
