/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author curtr
 */
public class CFUtil {

    private static boolean DEBUG = true;
    
  public static String composeWhereClause(boolean done, boolean notDone, boolean wip, boolean failed, boolean resting, boolean infinite, boolean toDo){
      String strDone = done ? "done" : "";
      String strNotDone = notDone ? "not done": "";
      String strWip = wip ? "wip": "";
      String strInfinite = infinite ? "infinite": "";
      String strFailed = failed ? "failed": "";
      String strResting = resting ? "resting": "";
      String strToDo = toDo ? "to do": "";
      if( DEBUG){
          System.out.println(String.format("done: %b,  notDone %b, wip: %b,  failed: %b, resting; %b, infinite: %b, todo:%b", 
          done, notDone, wip, failed, resting, infinite, toDo));
      
      }
      return composeWhereClause(strDone, strNotDone, strWip, strFailed, strResting, strInfinite, strToDo);
  
  }
  public static String composeWhereClause(String done, String notDone, String wip, String failed, String resting, String infinite, String toDo) {
        System.out.println("composeWhereClause()");
        if (DEBUG){
            System.out.println(done + notDone + wip + failed + resting + infinite);
        }
        String whereClause = "";
        List<String> conditions = new ArrayList<>();
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
        if (!resting.isEmpty()){
            conditions.add(resting);
        }
        if (!toDo.isEmpty()){
            conditions.add(toDo);
        }
                
        System.out.println(conditions);
        if ( conditions.isEmpty()){
            System.out.println("no where conditions");
            return "";
        }
        whereClause = " where state = ";
        for ( int i = 0; i < conditions.size(); i++){
            whereClause +=  "'" + conditions.get(i) + "'";
            if ( i < conditions.size() - 1) {
                whereClause += " or state = ";
            }
        }
        System.out.println("and the winner is: " + whereClause);
        return whereClause;
    }  
    public static String getCurrentDir(){
        File cwd = new File("").getAbsoluteFile();
        return cwd.toString();
    }
}
