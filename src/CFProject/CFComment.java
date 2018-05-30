/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.time.LocalDateTime;

/**
 *
 * @author curt
 */
public class CFComment {
    private int project_id;
    private int id;
    private  LocalDateTime date_time;
    private String comment;

    public CFComment() {
    }

    public CFComment(int project_id, LocalDateTime date_time, String comment) {
        this.project_id = project_id;
        this.date_time = date_time;
        this.comment = comment;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public LocalDateTime getDate_time() {
        return date_time;
    }

    public void setDate_time(LocalDateTime date_time) {
        this.date_time = date_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "CFComment{" + "project_id=" + project_id + ", date_time=" + date_time + ", comment=" + comment + '}';
    }
    
    
    
    
}
