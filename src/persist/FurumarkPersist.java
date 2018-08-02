/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import CFProject.CFComment;
import CFProject.CFProject;
import CFProject.CFStates;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;

/**
 *
 * @author curtr
 */
public class FurumarkPersist implements IPersist {



    @Override
    public int addProject(LocalDate date, String description, String state, String goal) {
        int id = 0;
            System.out.println(String.format("FurumarkPersist.addProject(%s,%s, %s,%s)", date.toString(), description, state, goal ));
            String params = String.format("?target_date=%s&description=%s&state=%s&goal=%s", date.toString(), description, state, goal);
            params = params.replace(" ", "+");
            //params = URLEncoder.encode(params, "UTF-8");
            CFRemoteDB remoteDB = new CFRemoteDB("http://localhost/hello.php" + params);
            id = remoteDB.doStuff();

        return id;
    }

    
    @Override
    public void updateState(int id, String newState) {
        System.out.println("FurumarkPersist.updateState(): " + id + " " + newState);
        String query = String.format("update cf_project set state=%s where id=%d", newState, id);
        String url = "http://localhost/update.php";
        CFRemoteDB remoteDB = new CFRemoteDB(url);
        remoteDB.doStuff();
    }

    @Override
    public void deleteItem(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateComment(int id, String newComment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addComment(int id, String newComment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ObservableList<CFComment> getComments(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGoal(int id, String newGoal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
/*
    @Override
    public ObservableList<CFProject> searchProjects(String searchString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/
    public void setURL(String httpcurtfurumark) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int addProject(CFProject project) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }




    @Override
    public ObservableList<CFProject> getProjects(String searchString, CFStates states) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
