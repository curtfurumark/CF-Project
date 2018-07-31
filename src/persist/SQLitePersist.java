/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import CFProject.CFComment;
import CFProject.CFProject;
import CFProject.CFStates;
import CFProject.CFUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
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



    public SQLitePersist() {
        try {
            db.init();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * creates a new CFProject and adds it to the database, 
     * @param date  the target date for the project
     * @param description
     * @param state
     * @param goal
     * @return the id of the newly created CFProject (ROWID in the SQLite DB)
     */
    @Override
    public int addProject(LocalDate date, String description, String state, String goal) {
        if (DEBUG){System.out.println("addProject");}
        int lastId = -1;
        if ( date == null || description.isEmpty()){
            System.out.println("date or description is missing");
        }
        //String lastUpdate = LocalDateTime.now().toString();
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String lastUpdate = LocalDateTime.now().format(formatter);
        String query = String.format("INSERT INTO cf_todo(date, description, state, goal, lastUpdate) values ('%s', '%s', '%s', '%s', '%s')", 
                date.toString(), description, state, goal, lastUpdate);
        db.insert(query);
        try {
            ResultSet rs = db.executeQuery("select *, ROWID from cf_todo order by ROWID desc LIMIT 1");
            while (rs.next()){
                lastId = rs.getInt("ROWID");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lastId;
    }

    @Override
    public void updateState(int id, String newState) {
        if(DEBUG) System.out.format("SQLitePersist.update(%d, %s)", id, newState);
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String query  = String.format("update cf_todo set state = '%s' , lastUpdate = '%s' where ROWID = %d", newState, LocalDateTime.now().format(formatter),id);
        
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

    /**
     * adds a comment to the comment database
     * @param id, id of the CFProject to which this comment belongs
     * @param newComment 
     */
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
        
        ObservableList<CFComment>  comments= FXCollections.observableArrayList();
        try {
            ResultSet res = db.executeQuery(query);
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
        } catch (Exception ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comments;
    }
    /**
     * updates goal, but of course and also lastUpdate.
     * @param id
     * @param newGoal 
     */

    @Override
    public void updateGoal(int id, String newGoal) {
        System.out.println(String.format("SQLitePersist.updateGoal(%d, %s", id, newGoal));
        DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String query =String.format("update cf_todo set goal='%s', lastUpdate = '%s' where ROWID = %d", newGoal,LocalDateTime.now().format(formatter), id);
        boolean stat = db.update(query);
        //System.out.println("db update stat: " + stat);
    }
/*
    @Override
    public ObservableList<CFProject> searchProjects(String searchString) {
        System.out.println(String.format("searchProjects(%s)", searchString));
        String query = String.format("select *, ROWID from cf_todo where description like('%%%s%%') order by lastUpdate desc", searchString);
        System.out.println("\tquery: " + query);
        ObservableList<CFProject> todoItems = FXCollections.observableArrayList();
        
        try{
            ResultSet rs = db.executeQuery(query );
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
            } catch (Exception ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        return todoItems;
        //return null;
    }
*/
    @Override
    public int addProject(CFProject project) {
        System.out.println("SQLitePersist.addProject: " + project.toString());
        return 0;
    }
/**
    @Override
    public ObservableList<CFProject> searchProjects(String searchString, CFStates states) {
        System.out.format("searchProjects(%s,%s)\n", searchString, states.toString());
        String whereFragment = CFUtil.composeWhereClause(states);
        System.out.println("\twhereFragment: " + whereFragment);
        if (!whereFragment.isEmpty()){
            whereFragment = String.format("and (%s)", whereFragment);
        }
        String query = String.format("select *, ROWID from cf_todo where description like('%%%s%%') %s order by lastUpdate desc",  searchString, whereFragment);
        System.out.println("\tquery: " + query);
        ObservableList<CFProject> projects = FXCollections.observableArrayList();
        try{
            ResultSet rs = db.executeQuery( query );
            while(rs.next()){
                    String description = rs.getString("description");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
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
                    projects.add(new CFProject(date, description, state, id, comment, goal, lastUpdate));
            }
        } catch (SQLException ex) {
                Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("number of projects found: " + projects.size());
        return projects;
    }
*/
    @Override
    public ObservableList<CFProject> getProjects(String searchString, CFStates states) {
        boolean DEBUG_THIS = true;
        System.out.format("SQLitePersist.getProjects(%s,%s)\n", searchString, states.toString());
        String whereFragment = CFUtil.composeWhereClause(states);
        if( DEBUG_THIS)System.out.println("\twhere: " + whereFragment);
        String query = String.format("select *, ROWID from cf_todo where description like('%%%s%%') %s order by lastUpdate desc",  searchString, whereFragment);
        if(DEBUG_THIS)System.out.println("\tquery: " + query);
        ObservableList<CFProject> projects = FXCollections.observableArrayList();
        try{
            ResultSet rs = db.executeQuery( query );
            while(rs.next()){
                    String description = rs.getString("description");
                    LocalDate date = LocalDate.parse(rs.getString("date"));
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
                    projects.add(new CFProject(date, description, state, id, comment, goal, lastUpdate));
            }
        } catch (SQLException ex) {
                Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
            Logger.getLogger(SQLitePersist.class.getName()).log(Level.SEVERE, null, ex);
        }
        if( DEBUG_THIS) System.out.println("\tnumber of projects found: " + projects.size());
        return projects;
    }
}
