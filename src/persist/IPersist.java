/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persist;

import CFProject.CFComment;
import CFProject.CFProject;
import CFProject.CFStates;
import java.time.LocalDate;
import javafx.collections.ObservableList;

/**
 *
 * @author curt
 */

public interface IPersist {

    public ObservableList<CFProject>getProjects(String searchString, CFStates states);
    public int addProject(LocalDate date, String description, String state, String goal);
    public int addProject(CFProject project);
    public void updateState(int id, String newState);
    public void deleteItem(int id);
    public void updateComment(int id, String newComment);
    public void addComment(int id, String newComment);
    public ObservableList<CFComment> getComments(int id);
    public void updateGoal(int id, String newGoal);
    //public ObservableList<CFProject> searchProjects(String searchString);
    //public ObservableList<CFProject> searchProjects(String searchString, CFStates states);

}
