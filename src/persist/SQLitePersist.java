/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import CFProject.CFComment;
import CFProject.CFProject;
import CFProject.CFUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author curt
 */
public class SQLitePersist implements IPersist{
    private SQLiteDB  db = new SQLiteDB();
    private static boolean DEBUG = true;

    @Override
    public ObservableList<CFProject> getProjects
        (   boolean isDone, 
            boolean notDone, 
            boolean wip, 
            boolean failed, 
            boolean resting,
            boolean infinite,
            boolean toDo){
        String whereCondition = "";
        int nOfWheres = 0;
        String query = "select *, ROWID from cf_todo";
        /*if ( isDone || wip || failed || notDone){
            whereCondition = " where ";
        }
        if (isDone){
            whereCondition += " state = 'done'";
            nOfWheres++;
        }
        if (notDone){
            nOfWheres++;
            if ( nOfWheres > 1){
                whereCondition += " or ";
            }
            whereCondition += " state = 'not done'";
        }
        if( wip){
            nOfWheres++;
            if ( nOfWheres > 1){
                whereCondition += " or ";
            }
            whereCondition += " state = 'wip'";
        }
        if ( failed){
            nOfWheres++;
          if ( nOfWheres > 1){
                whereCondition += " or ";
            }
            whereCondition += " state = 'failed'";
        }
        if ( resting){
            nOfWheres++;
          if ( nOfWheres > 1){
                whereCondition += " or ";
            }
            whereCondition += " state = 'resting'";
        }
*/
        query += whereCondition;
        query += CFUtil.composeWhereClause(isDone, notDone, wip, failed, resting, infinite, toDo);
        query += " order by lastUpdate desc";
        if(DEBUG) System.out.println("\tquery: " +  query);
        ObservableList<CFProject> todoItems = FXCollections.observableArrayList();
        ResultSet rs = db.executeQuery(query );
        try{
            while(rs.next()){
                    String description = rs.getString("description");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    //LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("lastUpdate"));
                    String tmp = rs.getString("lastUpdate");
                    //if ( DEBUG) System.out.println("\tlast update: " + tmp);
                    LocalDateTime lastUpdate = null;
                    if ( tmp == null){
                        //lastUpdate = LocalDateTime.now();
                    }else{
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        try{
                            lastUpdate = LocalDateTime.parse(tmp, formatter);//, formatter);
                        }catch(DateTimeParseException dte){
                            System.out.println("dte :" + dte.toString());
                        }
                    }
                    String state = rs.getString("state");
                    String comment = rs.getString("comment");
                    String goal = rs.getString("goal");
                    int id = rs.getInt("ROWID");
                    todoItems.add(new CFProject(date, description, state, id, comment, goal, lastUpdate));
            }
        } catch (SQLException ex) {
                Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
            }
        return todoItems;
    }

    public SQLitePersist() {
        try {
            db.init();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int addProject(LocalDate date, String description, String state, String goal) {
        if (DEBUG){System.out.println("addTodoItem");}
        int lastId = -1;
        if ( date == null || description.isEmpty()){
            System.out.println("date or description is missing");
        }
        String query = String.format("INSERT INTO cf_todo(date, description, state, goal) values ('%s', '%s', '%s', '%s')", 
                date.toString(), description, state, goal);
        db.insert(query);
        ResultSet rs = db.executeQuery("select *, ROWID from cf_todo order by ROWID desc LIMIT 1");
        try {
            while (rs.next()){
                lastId = rs.getInt("ROWID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastId;
    }

    @Override
    public void update(int id, String newState) {
        if(DEBUG) System.out.println("update");
        String query  = String.format("update cf_todo set state = '%s' where ROWID = %d", newState,id);
        if (DEBUG)System.out.println("\t" + query);
        db.update(query);
    }

    @Override
    public void deleteItem(int id) {
        String query = String.format("DELETE FROM cf_todo where ROWID = %d", id);
        db.update(query);
    }

    @Override
    public void updateComment(int id, String newComment) {
        if ( DEBUG) System.out.println(String.format("updatComment(%d, %s)", id, newComment));
        //if(DEBUG) System.out.println("update");
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String query  = String.format("update cf_todo set comment = '%s', lastUpdate = '%s' where ROWID = %d", newComment, LocalDateTime.now().format(formatter), id);
        if (DEBUG)System.out.println("\t" + query);
        db.update(query);
    }

    @Override
    public void addComment(int id, String newComment) {
        if (DEBUG) System.out.println(String.format("addComment(%d, %s", id, newComment));
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String query = String.format("insert into cf_comment(project_id, comment, date_time) values(%d, '%s', '%s')", id, newComment, LocalDateTime.now().format(formatter));
        db.insert(query);
        getComments(id);
    }

    @Override
    public ObservableList<CFComment> getComments(int id) {
        String query = String.format("select *, ROWID from cf_comment where project_id = %s order by date_time desc", id);
        ResultSet res = db.executeQuery(query);
        ObservableList<CFComment>  comments= FXCollections.observableArrayList();
        try {
            while( res.next()){
                int projectId =  res.getInt("project_id");
                String comment =  res.getString("comment");
                String  dateTime = res.getString("date_time");
                //String lastUpdate = res.getString("lastUpdate");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                CFComment cfComment = new CFComment(projectId, LocalDateTime.parse(dateTime, formatter), comment);
                System.out.println("\t" + cfComment.toString());
                comments.add(cfComment);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comments;
    }

    @Override
    public void updateGoal(int id, String newGoal) {
        System.out.println(String.format("updateGoal(%d, %s", id, newGoal));
        String query =String.format("update cf_todo set goal='%s' where ROWID = %d", newGoal, id);
        boolean stat = db.update(query);
        System.out.println("db update stat: " + stat);
    }

    @Override
    public ObservableList<CFProject> searchProjects(String searchString) {
        System.out.println(String.format("searchProjects(%s)", searchString));
        String query = String.format("select *, ROWID from cf_todo where description like('%%%s%%') order by lastUpdate desc", searchString);
        System.out.println("\tquery: " + query);
        ObservableList<CFProject> todoItems = FXCollections.observableArrayList();
        ResultSet rs = db.executeQuery(query );
        try{
            while(rs.next()){
                    String description = rs.getString("description");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
                    //LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("lastUpdate"));
                    String tmp = rs.getString("lastUpdate");
                    //if ( DEBUG) System.out.println("\tlast update: " + tmp);
                    LocalDateTime lastUpdate = null;
                    if ( tmp == null){
                        //lastUpdate = LocalDateTime.now();
                    }else{
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        try{
                            lastUpdate = LocalDateTime.parse(tmp, formatter);//, formatter);
                        }catch(DateTimeParseException dte){
                            System.out.println("dte :" + dte.toString());
                        }
                    }
                    String state = rs.getString("state");
                    String comment = rs.getString("comment");
                    String goal = rs.getString("goal");
                    int id = rs.getInt("ROWID");
                    todoItems.add(new CFProject(date, description, state, id, comment, goal, lastUpdate));
            }
        } catch (SQLException ex) {
                Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
            }
        return todoItems;
        //return null;
    }
}
