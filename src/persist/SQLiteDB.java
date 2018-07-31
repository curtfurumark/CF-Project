/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author curt
 */
public class SQLiteDB {
    private Connection connection;
    private final String driverName = "org.sqlite.JDBC"; 
    private final String dbUrl = "jdbc:sqlite:cfdb.db";
    private static final boolean DEBUG = true;
        
    public void init() throws ClassNotFoundException, SQLException{
        System.out.println("SQLiteDB.init()");
          Class.forName(driverName);
        connection = DriverManager.getConnection(dbUrl);
        String query  = "CREATE TABLE IF NOT EXISTS cf_todo "
                + "(id int, "
                + "description varchar(100), "
                + "date varchar(12), "
                + "comment varchar(100), "
                + "state varchar(10), "
                + "goal varchar(23), " 
                + "lastUpdate varchar(12))";
        Statement statement = connection.createStatement();
        boolean stat  = statement.execute(query);
        if (DEBUG)System.out.println("\tresult of create todo table: " + stat);
        //statement.execute("drop table cf_comment");
        String queryCreateCommentTable  = "CREATE TABLE IF NOT EXISTS cf_comment (project_id int, comment varchar(100), date_time varchar(20))";
        stat  = statement.execute(queryCreateCommentTable);
        if( DEBUG) System.out.println("\tresult of create comment table: " + stat);
    }
    public boolean insert(String query){
        boolean stat = false;
        try {
            if (DEBUG){System.out.println("SQLiteDB.insert " + query);}
            Statement statement = connection.createStatement();
            statement.execute(query);
            stat = true;
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stat;
    }
    public ResultSet executeQuery(String query) throws Exception{
        ResultSet res  = null;
        if (DEBUG){System.out.println("SQLiteDB.executeQuery " + query);}
        if ( connection == null){
            System.out.println("connection is null");
            throw new Exception("connection is null");
        }
        try {
            Statement statement = connection.createStatement();
            res = statement.executeQuery(query);
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    public boolean update(String query){
        boolean stat = false;
        try {
            if (DEBUG){System.out.println("SQLiteDB.update " + query);}
            Statement statement = connection.createStatement();
            int whatever = statement.executeUpdate(query);
            if ( DEBUG)System.out.println("\texecuteUpdate: " + whatever);
            stat = true;
        } catch (SQLException ex) {
            stat = true;
            Logger.getLogger(SQLiteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stat;
    
    }
    
    public SQLiteDB() {
    }
    
    /**
    public void addColumnLastUpdate(){
        System.out.println("addColumnLastUpdate()");
        String query = "alter  table cf_todo add column lastUpdate varchar(15)";
        update(query);
    }
   */
/**
    public static void main(String[] args){
        try {
            SQLiteDB db = new SQLiteDB();
            db.init();
            db.update("alter table cf_todo add column date varchar(10)");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SQLiteDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */
}
