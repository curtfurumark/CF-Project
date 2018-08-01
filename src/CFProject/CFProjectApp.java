/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
//import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
//import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persist.IPersist;
import persist.SQLitePersist;

/**
 *
 * @author curt
 */
public class CFProjectApp extends Application{
    private final Button buttonAddProject = new Button("add project");
    //private final Button buttonDeleteItem = new Button("delete item");
    private final Button buttonGo2AddProject = new Button("go to add project");
    private final Button buttonGo2ShowProjects = new Button("view projects");
    private final TextArea textAreaGoal = new TextArea();
    private final TextArea textAreaCurrentWorkingDirectory = new TextArea();
    private TableView<CFProject> tableProjects;
    private TableView<CFComment> tableComment;
    private final VBox layout = new VBox();
    private final HBox topLayoutSceneShow = new HBox();
    private final HBox topLayoutSceneAdd  = new HBox();
    private Scene scene = null;
    private Scene sceneNewProject = null;
    private Stage primaryStage = null;
    private final IPersist persist = new SQLitePersist();
    private final TextField textFieldDescription = new TextField();
    private final TextField textFieldSearch = new TextField();
    private final DatePicker datePicker = new DatePicker();
    private final CheckBox checkBoxDone = new CheckBox("done");
    private final CheckBox checkBoxNotDone = new CheckBox("not done");
    private final CheckBox checkBoxWip = new CheckBox("wip");
    private final CheckBox checkBoxFailed = new CheckBox("failed");
    private final CheckBox  checkBoxResting = new CheckBox("resting");
    private final CheckBox  checkBoxInfinite = new CheckBox("infinite");
    private final CheckBox  checkBoxToDo = new CheckBox("to do");
    private final boolean   DEBUG = true;
    private final int       HEIGHT = 700;
    private final int       WIDTH = 1200;
    private final String title = "CF Project 20180801 10:23";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setTitle(title);
        scene = new Scene(layout, WIDTH, HEIGHT);
        buttonAddProject.setOnAction(ae->addProject());
        //buttonDeleteItem.setOnAction(ae->deleteItem());
        checkBoxDone.setOnAction(ae->getProjects());
        checkBoxNotDone.setOnAction(ae->getProjects());
        checkBoxWip.setOnAction(ae->getProjects());
        textFieldSearch.setOnAction(ae->searchProjects());
        checkBoxFailed.setOnAction(ae->getProjects());
        checkBoxResting.setOnAction(ae->getProjects());
        checkBoxInfinite.setOnAction(ae->getProjects());
        checkBoxToDo.setOnAction(ae->getProjects());
        datePicker.setValue(LocalDate.now());
        
        /*
        datePicker.setOnAction(new CFDateHandler());
        */
        initTable();
        initCommentTable();
        topLayoutSceneShow.getChildren().addAll(buttonGo2AddProject, checkBoxDone,checkBoxNotDone, checkBoxWip, checkBoxFailed, checkBoxResting, checkBoxInfinite, checkBoxToDo, textFieldSearch);
        topLayoutSceneShow.setPadding(new Insets(10, 10, 10, 10));
        topLayoutSceneAdd.getChildren().addAll(datePicker, textFieldDescription, buttonAddProject, buttonGo2ShowProjects);
        buttonGo2AddProject.setOnAction(e->primaryStage.setScene(sceneNewProject));
        layout.getChildren().addAll(topLayoutSceneShow, tableProjects, tableComment);
        initAddProjectScene();
        /*choiceBoxStates = new ChoiceBox<>();
        choiceBoxStates.getItems().addAll("done", "not done", "wip", "failed");
        choiceBoxStates.setValue("not done");*/
        textFieldDescription.setPromptText("description");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void initAddProjectScene(){
        VBox vBoxLayout = new VBox();
        textAreaGoal.setPromptText("state your goal here!");
        textAreaCurrentWorkingDirectory.setText(CFUtil.getCurrentDir());
        buttonGo2ShowProjects.setOnAction(e->primaryStage.setScene(scene));
        vBoxLayout.getChildren().addAll(topLayoutSceneAdd, textAreaGoal, textFieldDescription, textAreaCurrentWorkingDirectory);
        sceneNewProject = new Scene(vBoxLayout, WIDTH, HEIGHT);
    }
    private void initTable(){
        TableColumn<CFProject, Integer> columnId = new TableColumn<>("id");
        columnId.setCellValueFactory(new PropertyValueFactory("id"));
        columnId.setMinWidth(50);
        
        TableColumn<CFProject, String> columnDescription = new TableColumn<>("description");
        columnDescription.setCellValueFactory(new PropertyValueFactory("description"));
        columnDescription.setMinWidth(200);
        
        TableColumn<CFProject, LocalDate> columnLastUpdate = new TableColumn<>("last update");
        columnLastUpdate.setCellValueFactory(new PropertyValueFactory("lastUpdate"));
        columnLastUpdate.setMinWidth(200);
        
        TableColumn<CFProject, String> columnGoal = new TableColumn<>("goal");
        columnGoal.setCellValueFactory(new PropertyValueFactory("goal"));
        columnGoal.setEditable(true);
        columnGoal.setMinWidth(250);
        columnGoal.setOnEditCommit(e->handleEditGoal(e));
        
        TableColumn<CFProject, LocalDate> columnTargetDate = new TableColumn<>("target date");
        columnTargetDate.setCellValueFactory(new PropertyValueFactory("targetDate"));
        columnTargetDate.setMinWidth(100);
        
        TableColumn<CFProject, String> columnState = new TableColumn<>("state");
        columnState.setCellValueFactory(new PropertyValueFactory("state"));
        columnState.setMinWidth(100);
        
        TableColumn<CFProject, String> columnComment = new TableColumn<>("comment/status");
        columnComment.setCellValueFactory(new PropertyValueFactory("comment"));
        columnComment.setMinWidth(300);
        
        ObservableList<String> states = FXCollections.observableArrayList();
        states.add("done");
        states.add("wip");
        states.add("not done");
        states.add("failed");
        states.add("resting");
        states.add("infinite");
        states.add("to do");
        columnState.setCellFactory(ComboBoxTableCell.forTableColumn(states));
        columnGoal.setCellFactory(TextFieldTableCell.forTableColumn());
        columnComment.setCellFactory(TextFieldTableCell.forTableColumn());
        //columnTargetDate.setCellFactory(DateEditingCell.forTableColumn());
        columnState.setOnEditCommit(e->handleEditState(e));
        columnComment.setOnEditCommit(e->handleEditComment(e));
        datePicker.setOnMouseClicked(new CFOnMouseClicked());
        tableProjects = new TableView<>();
        tableProjects.setEditable(true);
       
        tableProjects.setItems(persist.getProjects(textFieldSearch.getText(), new CFStates(this)));
        tableProjects.getColumns().addAll(columnId, columnDescription, columnGoal, columnTargetDate,columnLastUpdate, columnComment, columnState);
        tableProjects.setOnMouseClicked(e->handleGetComments(e));
    }

    private void closeApplication() {
        System.out.println("closeApplication()");
        primaryStage.close();
    }
    
   /**
    * adds a new project to the databas
    */
    private void addProject(){
        if( DEBUG) System.out.println("CFProject.addProject()");
        LocalDate date = datePicker.getValue();
        String description = textFieldDescription.getText();
        String goal = textAreaGoal.getText();
        String state = "not done";
        
        int id = persist.addProject(date, description, state, goal);
        //CFStates states = new CFStates(this);
        CFProject project = new CFProject(date, description, state,  goal);
        id = persist.addProject(project);
        if (DEBUG) System.out.println("\t" + project.toString());
        project.setId(id);
        updateProjectTable();
        textFieldDescription.clear();    
    }
    /**
     * the one to call whenever you want to update the table
     */
    private void updateProjectTable(){
        System.out.println("CFProjectApp.updateProjectTable()");
        ObservableList<CFProject> projects = persist.getProjects(textFieldSearch.getText(),new CFStates(this));
        System.out.println("\tnumber of projects found: " + projects.size());
        tableProjects.setItems(projects);
    
    }

    /**
     * deletes a project, but this one is not in use right now
     * not sure about ROWID... when deleting a project...find out please
     * /
    private void deleteItem() {
        if (DEBUG) System.out.println("CFProjects.deleteItem()");
        ObservableList<CFProject> allItems, deleteItems;
        allItems = tableProjects.getItems();
        for( CFProject item: allItems){
            System.out.println( item.toString());
        }
        if (DEBUG )System.out.println("items to delete: ");
        deleteItems  = tableProjects.getSelectionModel().getSelectedItems();
        for ( CFProject item: deleteItems){
            System.out.println(item.toString());
            persist.deleteItem(item.getId());
        }
        deleteItems.forEach(allItems::remove);
    }
    /**
     * 
     */
    private void getProjects() {
        System.out.println("CFProject.getProjects");
        ObservableList<CFProject> projects =  persist.getProjects(textFieldSearch.getText(), new CFStates(this));      
        tableProjects.getItems().setAll(projects);
    }

    private void initCommentTable() {
        tableComment = new TableView<>();
        TableColumn<CFComment, Integer> columnProjectId = new TableColumn<>("project id");
        columnProjectId.setCellValueFactory(new PropertyValueFactory("project_id"));
        columnProjectId.setMinWidth(100);
        
        TableColumn<CFComment, String> columnComment = new TableColumn<>("comment");
        columnComment.setCellValueFactory(new PropertyValueFactory("comment"));
        columnComment.setMinWidth(200);
        
        TableColumn<CFComment, LocalDate> columnTimeStamp = new TableColumn<>("timestamp");
        columnTimeStamp.setCellValueFactory(new PropertyValueFactory("date_time"));
        columnTimeStamp.setMinWidth(200);
        tableComment.getColumns().addAll(columnProjectId, columnComment, columnTimeStamp);
    }


    /**
     * searches for projects
     * according to state and search string
     * @param no params
     * 
     */
    private void searchProjects() {
        boolean DEBUG_THIS = false;
       System.out.println("CFProjects.searchProjects: " + textFieldSearch.getText());
       String searchString = textFieldSearch.getText();
       if ( searchString == null){
           System.err.println("searchstring is null...");
           return;
       }
       CFStates states = new CFStates(this);
       if( DEBUG_THIS) System.out.println("\tstates: " + states.toString());
       ObservableList<CFProject> projects = persist.getProjects(searchString, states);
       if (DEBUG_THIS) System.out.println("\tnumber of projects found: " + projects.size());
       //tableProjects.
       tableProjects.setItems(projects);
    }

    boolean getInfinite() {
        return checkBoxInfinite.isSelected(); //To change body of generated methods, choose Tools | Templates.
    }

    boolean getToDo() {
        return checkBoxToDo.isSelected(); //To change body of generated methods, choose Tools | Templates.
    }

    boolean getWip() {
        return checkBoxWip.isSelected(); //To change body of generated methods, choose Tools | Templates.
    }

    boolean getDone() {
        return checkBoxDone.isSelected();    
    }

    boolean getFailed() {
        return checkBoxFailed.isSelected();
    }

    boolean getResting() {
        return checkBoxResting.isSelected();
    }

    boolean getNotDone() {
        return checkBoxNotDone.isSelected();
    }
    /**
     * gets comments belonging to....whatever... project
     * @param event 
     */
    public void handleGetComments(MouseEvent event) {
        boolean DEBUG_THIS = false;
        System.out.println("CFProject.handleGetComments(MouseEvent event)");
        if (DEBUG_THIS ) CFUtil.debug(event);
        if(event.getButton().equals(MouseButton.PRIMARY)){
        CFProject project =  tableProjects.getSelectionModel().getSelectedItem();
        if (project != null){
            ObservableList<CFComment> comments = persist.getComments(project.getId());
            if ( comments != null){
                tableComment.setItems(comments);
            }else{
                System.out.println("no comments....");
            }
        }else{
                System.err.println("\tproject is null...");
            }
        }
    }
    
    public void handleEditTargetDate(MouseEvent event){
        boolean DEBUG_THIS = true;
        System.out.println("CFProject.handleEditTargetDate(Event event)");
        LocalDate date = ((DatePicker) event.getSource()).getValue();
        if ( DEBUG_THIS ) System.err.println("\tnew target date: " + date);
    }
    
    public void handleEditGoal(CellEditEvent<CFProject, String> event) {
        boolean DEBUG_THIS = false;
        System.out.println("CFProject.handleEditGoal");
        String newGoal = event.getNewValue();
        if (DEBUG_THIS) System.out.println("\tnew goal: " + newGoal);
        CFProject cf = event.getTableView().getItems().get(event.getTablePosition().getRow());
        cf.setGoal(newGoal);
        persist.updateGoal(cf.getId(), newGoal);
        updateProjectTable();

    }
    /**
     * handle editing of comments from projecttable
     * @param event (event fired by <enter>
     */
    public void handleEditComment(CellEditEvent<CFProject, String> event){
        boolean DEBUG_THIS = true;
        System.out.println("CFProjectApp.handleEditComment");
        String newComment = event.getNewValue();
        if( DEBUG_THIS) System.out.println("\tnew comment: " + newComment);
        CFProject cf = event.getTableView().getItems().get(event.getTablePosition().getRow());
        cf.setComment(newComment);
        persist.updateComment(cf.getId(), newComment);
        persist.addComment(cf.getId(), newComment);
        ((CFProject) event.getTableView().getItems().get(
                event.getTablePosition().getRow())
                ).setState(event.getNewValue());
 
        tableProjects.setItems(persist.getProjects(textFieldSearch.getText(), new CFStates(this)));
        tableComment.setItems(persist.getComments(cf.getId()));
        updateProjectTable();
    }
   
    /**
     * not so much handling edit of comments as adding  a new one, but there you go
     * if it makes you happy
     * @param event 
     */
    public void handleEditState(CellEditEvent<CFProject, String> event){
        boolean DEBUG_THIS = true;
        if ( DEBUG_THIS) System.out.println("CFProjectApp.handleEditState");
        String newState = event.getNewValue();
        if (DEBUG_THIS) System.out.println("\tnew state: " + newState);
        CFProject cf = event.getTableView().getItems().get(event.getTablePosition().getRow());
        cf.setState(newState);
        cf.setLastUpdate(LocalDateTime.now());
        persist.updateState(cf.getId(), newState);
        if ( DEBUG_THIS) System.out.println("\t" + cf.toString());
        ((CFProject) event.getTableView().getItems().get(event.getTablePosition().getRow())).setState(event.getNewValue());
        this.updateProjectTable();
    }
}
