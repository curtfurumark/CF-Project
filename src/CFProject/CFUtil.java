/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;

/**
 *
 * @author curtr
 */
public class CFUtil {

    private static boolean DEBUG = true;
    
    /**
     * for the state part of the sql query
     * select * from db where ( state = 'todo') order by....
     **/
    public static String composeWhereClause(CFStates states){
        if ( DEBUG) System.out.println("CFUtil.composeWhereClause()");
        String clauseFragment = "";
        List trueStatesList = states.getTrueStates();
        System.out.println(trueStatesList.toString());
        List<String> list = states.getTrueStates();
        if ( list.size() > 0){
            clauseFragment += "and (";
            for( int i = 0; i < list.size(); i++){
                if ( i > 0 && (i <= list.size() - 1)){
                    clauseFragment += "or ";
                }
                clauseFragment += String.format("state = '%s' ", list.get(i));
            }
            clauseFragment += ")";
        }
        return clauseFragment;
    }
  

    public static String getCurrentDir(){
        File cwd = new File("").getAbsoluteFile();
        return cwd.toString();
    }

    static void print(ResultSet rs) {
        System.out.println("CFUtil.print(ResultSet)");
        try {
            while ( rs.next()){
                System.out.println("");
                System.out.print(rs.getString("description"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CFUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    static Map<String, String> stringArr2HashMap(String[] arr, String delim){
        System.out.println("CFUtil.stringArr2HashMap: " + delim);
        boolean DEBUG_THIS = false;
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < arr.length; i++){
            if( DEBUG_THIS) System.out.println("\tsplitting this: " + arr[i]);
            String[] tmp = arr[i].split(delim);
            if ( tmp.length == 2){
                if( DEBUG_THIS) System.out.format("\t:[0] %s [1] %s\n", tmp[0], tmp[1]);
                map.put(tmp[0], tmp[1]);
            }else{
                map.put(tmp[0], "");
            }
        }
        return map;
    }

    static void debug(Event event) {
        System.out.println("CFUtil.debug(Event");
        System.out.println("Source: " + event.getSource());
    }
}
