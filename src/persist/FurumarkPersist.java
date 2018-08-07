/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import CFProject.CFComment;
import CFProject.CFProject;
import CFProject.CFStates;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * create table session(id int not null auto_increment, project_id int, description varchar(100), date_time varchar(20), primary key(id));
 * 
 * 
 * @author curtr
 */
public class FurumarkPersist implements IPersist {
    private CFRemoteDB db = new CFRemoteDB();


    @Override
    public int addProject(LocalDate date, String description, String state, String goal) {
        int id = 0;
            LocalDateTime latestUpdate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
            System.out.println("\tdate formatted: " + latestUpdate.format(formatter));
            System.out.println(String.format("FurumarkPersist.addProject(%s,%s, %s,%s, %s)", date.toString(), description, state, goal, latestUpdate.format(formatter) ));
            String params = String.format("?target_date=%s&description=%s&state=%s&goal=%s&latest_update=%s", date.toString(), description, state, goal, latestUpdate.format(formatter));
            params = params.replace(" ", "+");
            //params = URLEncoder.encode(params, "UTF-8");
            CFRemoteDB remoteDB = new CFRemoteDB("http://localhost/project/add_project.php" + params);
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
        System.out.println("FurumarkPersist.updateComment()....not really just a dummy");
    }

    @Override
    public void addComment(int id, String newComment) {
        System.out.format("FurumarkPersist.addComment(%d, %s)\n", id, newComment);
        //LocalDateTime now = LocalDateTime.now();
        //DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //String query = String.format("insert into cf_comment(project_id, comment, date_time) values(%d, '%s', '%s')\n", id, newComment, LocalDateTime.now().format(formatter));
        db.addComment(id, newComment, LocalDateTime.now());
        getComments(id);
    }

    @Override
    public ObservableList<CFComment> getComments(int id) {
        System.out.format("FurumarkPersist.getComments(%d)\n", id);
        return null;
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
        System.out.println("FurumarkPersist.addProject()");
        int id = 0;
        LocalDateTime latestUpdate = LocalDateTime.now();
        System.out.println(String.format("FurumarkPersist.addProject(%s,%s, %s,%s, %s)", 
                                                                    project.getTargetDate().toString(),
                                                                    project.getDescription(), 
                                                                    project.getState(),
                                                                    project.getGoal(), 
                                                                    latestUpdate.toString() ));
            String params = String.format("?target_date=%s&description=%s&state=%s&goal=%s&latest_update=%s", 
                                                                    project.getTargetDate().toString(), 
                                                                    project.getDescription(), 
                                                                    project.getState(), 
                                                                    project.getGoal(), 
                                                                    latestUpdate.toString());
            params = params.replace(" ", "+");
            //params = URLEncoder.encode(params, "UTF-8");
            CFRemoteDB remoteDB = new CFRemoteDB("http://localhost/project/add_project.php" + params);
            id = remoteDB.doStuff();

        return id;
    }




    /**
     * getProjects from remote db....
     * @param searchString
     * @param states
     * @return 
     */
    @Override
    public ObservableList<CFProject> getProjects(String searchString, CFStates states) {
            System.out.println("FurumarkPersist.getProjects");
            ObservableList<CFProject> projectList = FXCollections.observableArrayList();
            String url = "http://localhost/testing/getProjects.php";
            CFRemoteDB remoteDB = new CFRemoteDB(url);
            boolean DEBUG_THIS = true;
            URL myURL;
            try {
                myURL = new URL(url);
                if (DEBUG_THIS) System.out.println("\turl: " + myURL.toString());
                URLConnection urlConnection = myURL.openConnection();
                urlConnection.connect();
                if (DEBUG_THIS) System.out.println("connected ....???");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                                    urlConnection.getInputStream()));
                String inputLine;
                while ((inputLine = bufferedReader.readLine()) != null){ 
                    System.out.println("\ta line: " + inputLine);
                    projectList.add(CFProject.fromString(inputLine));
                }
                bufferedReader.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(CFRemoteDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CFRemoteDB.class.getName()).log(Level.SEVERE, null, ex);
        }    
       return projectList;
    }
}
