/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author curt
 */
public class CFProject {
    private LocalDate       targetDate;
    private LocalDateTime   lastUpdate;
    private String          description;
    private String          comment;
    private String          goal;

    public String getComment() {
        return comment;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    private String state = "not done";
    private int id = -1;
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getLastUpdate() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public CFProject(LocalDate targetDate, String description, String state, String goal) {
        System.out.println("CFProject ctor...4 args");
        this.targetDate = targetDate;
        this.description = description;
        this.state = state;
        this.goal = goal;
        this.comment = "<no comment yet>";
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.lastUpdate = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        //DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //this.lastUpdate = LocalDateTime.now().format(formatter);
        
        //lastUpdate = lastUpdate.format(formatter);
    }

    public static CFProject fromString(String str){
        System.out.println("CFProject.fromString()");
        boolean DEBUG_THIS = true;
        String arr[] = str.split("::");
        Map<String, String> map = CFUtil.stringArr2HashMap(arr, ">");
        String tmp = map.get("<target_date");
        LocalDate targetDate = LocalDate.parse(tmp);
        String str2 = map.get("<last_update");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime lastUpdate = LocalDateTime.parse(str2, formatter);
        String description = map.get("<description");
        String goal = map.get("<goal");
        String state = map.get("<state");
        String id = map.get("<id");
        if ( DEBUG_THIS) System.out.println("\tid: " + id);
        return new CFProject(targetDate, description, state,Integer.parseInt(id), "no comment", goal, lastUpdate);
    }
     
    public CFProject(LocalDate targetDate, String description, String state, int id, String comment, String goal, LocalDateTime lastUpdate) {
        this.targetDate = targetDate;
        this.description = description;
        this.id = id;
        this.state = state;
        this.comment = comment;
        this.goal = goal;
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        if (lastUpdate == null){
            System.err.println("lastUpdate is null");
        }
        return String.format("CFTodoItem(%s, %s, %s, %d, %s, %s, %s)", description, targetDate, state, id, comment, goal, lastUpdate.toString());
    }
}
