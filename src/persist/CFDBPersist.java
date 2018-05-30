/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import CFProject.CFComment;
import CFProject.CFProject;
import java.time.LocalDate;
import javafx.collections.ObservableList;

/**
 *
 * @author curtr
 */
public class CFDBPersist implements IPersist {

    @Override
    public ObservableList<CFProject> getProjects(boolean isDone, boolean notDone, boolean wip, boolean failed, boolean resting, boolean infinite, boolean toDo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int addProject(LocalDate date, String description, String state, String goal) {
           System.out.println(String.format("addProject(%s,%s, %s,%s)", date.toString(), description, state, goal ));
           
           return 0;
    }

    @Override
    public void update(int id, String newState) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    @Override
    public ObservableList<CFProject> searchProjects(String searchString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void setURL(String httpcurtfurumark) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
