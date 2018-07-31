/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import persist.SQLiteDB;
import persist.SQLitePersist;

/**
 *
 * @author curt
 */
public class Testing {
    public static void main(String[] args){
        //LocalDateTime ldt = LocalDateTime.now();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //System.out.println("ldt:" + ldt.format(formatter));
        //boolean happy = false;
        //System.out.println(String.format("happy? %b", happy));
        //SQLiteDB db = new SQLiteDB();
        //SQLitePersist persist = new SQLitePersist();
        //persist.getComments(31);
        new Testing().runTests();
        
    }
    public void runTests(){
        System.out.println("runTests()");
        //this.testComposeWhereClause();
        this.testGetProjects();
        //this.testSQL();
        //this.testCFProject();
    }
    public void testCFProject(){
        System.out.println("testCFProject");
        CFProject project = new CFProject(LocalDate.now(), "description", "not done" ,"do it" );
        System.out.println(project.toString());
    }
    
    public void testGetProjects(){
        System.out.println("testGetProjects()");
        SQLitePersist persist = new SQLitePersist();
        CFStates states = new CFStates();
        states.setWip(true);
        states.setTodo(true);
        states.setInfinite(true);
        persist.getProjects("", states);
    }
    public void testSQL(){
        try {
            System.out.println("testSQL");
            String sqlQuery = "select *, ROWID from cf_todo where description like('%bass%')";
            String clause =" and (state = 'wip' OR state = 'done');";
            sqlQuery += clause;
            SQLiteDB db = new SQLiteDB();
            db.init();
            ResultSet rs = db.executeQuery(sqlQuery);
            CFUtil.print(rs);
        } catch (Exception ex) {
            Logger.getLogger(Testing.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void testComposeWhereClause(){
        System.out.println("testComposeWhereClause()");
        CFStates states = new CFStates();
        states.setWip(true);
        states.setTodo(true);
        states.setResting(true);
        String fragment = CFUtil.composeWhereClause(states);
        System.out.println("\tfragment: " + fragment);
        
    
    }
}
