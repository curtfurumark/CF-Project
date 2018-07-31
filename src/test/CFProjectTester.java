/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import CFProject.CFProject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import persist.SQLiteDB;
import persist.SQLitePersist;

/**
 *
 * @author curt
 */
public class CFProjectTester {

    public CFProjectTester() {
        System.out.println("CFProjectTester()");
        //this.searchProjects("playalong");
        this.composeWhereClause("", "not done", "", "", "");
        this.composeWhereClause("done", "", "", "", "");
        this.composeWhereClause("", "", "", "", "failed");
        this.composeWhereClause("done", "", "wip", "infinite", "");
    }
    public static void main(String[] args){
        System.out.println("CFProjectTester.main()");
        new CFProjectTester();
    }
    /*
    public void searchProjects(String searchString){
       
        SQLitePersist persist = new SQLitePersist();
        ObservableList<CFProject> projects = persist.searchProjects(searchString);
        for ( CFProject project : projects){
            System.out.println("\t" + project.toString());
        }
    }
*/
    private String composeWhereClause(String done, String notDone, String wip, String infinite, String failed) {
        System.out.println("composeWhereClause()");
        String whereClause = "";
        List<String> conditions = new ArrayList<String>();
        if ( !done.isEmpty()){
            conditions.add(done);
        }
        if(!notDone.isEmpty()){
            conditions.add(notDone);
        }
        if(!wip.isEmpty()){
            conditions.add(wip);
        }
        if(!infinite.isEmpty()){
            conditions.add(infinite);
        }
        if (!failed.isEmpty()){
            conditions.add(failed);
        }
                
        System.out.println(conditions);
        if ( conditions.isEmpty()){
            return "";
        }
        whereClause = "where status = ";
        for ( int i = 0; i < conditions.size(); i++){
            whereClause += conditions.get(i);
            if ( i < conditions.size() - 1) {
                whereClause += " or status = ";
            }
        }
        System.out.println("\twhereClause: " + whereClause);
        return whereClause;
    }
}
