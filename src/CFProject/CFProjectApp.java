/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFProject;

import java.time.LocalDate;
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

    //private Button buttonGetTodoItems = new Button("get todo items");
    private final Button buttonAddProject = new Button("add project");
    private final Button buttonDeleteItem = new Button("delete item");
    private final Button buttonGo2AddProject = new Button("go to add project");
    private Button buttonGo2ShowProjects = new Button("view projects");
    private TextArea textAreaGoal = new TextArea();
    private TextArea textAreaCurrentWorkingDirectory = new TextArea();
    private TableView<CFProject> tableProjects;
    private TableView<CFComment> tableComment;
    private VBox layout = new VBox();
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
    private final CheckBox checkBoxResting = new CheckBox("resting");
    private final CheckBox  checkBoxInfinite = new CheckBox("infinite");
    private final CheckBox checkBoxToDo = new CheckBox("to do");
    private final boolean DEBUG = true;
    private final int HEIGHT = 800;
    private final int WIDTH = 1300;
    private final String title = "CF Project 20180530 001";
    
    
    
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
        buttonDeleteItem.setOnAction(ae->deleteItem());
        checkBoxDone.setOnAction(ae->showState());
        checkBoxNotDone.setOnAction(ae->showState());
        checkBoxWip.setOnAction(ae->showState());
        textFieldSearch.setOnAction(ae->searchProjects());
        checkBoxWip.setSelected(true);
        checkBoxFailed.setOnAction(ae->showState());
        checkBoxResting.setOnAction(ae->showState());
        checkBoxInfinite.setOnAction(ae->showState());
        checkBoxToDo.setOnAction(ae->showState());
        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(new EventHandler() {
     @Override
     public void handle(Event t) {
         LocalDate date = datePicker.getValue();
         System.err.println("Selected date: " + date);
     }});
        initTable();
        initCommentTable();
        topLayoutSceneShow.getChildren().addAll(buttonGo2AddProject, checkBoxDone,checkBoxNotDone, checkBoxWip, checkBoxFailed, checkBoxResting, checkBoxInfinite, checkBoxToDo, textFieldSearch);
        topLayoutSceneShow.setPadding(new Insets(10, 10, 10, 10));
        //topLayoutSceneShow
         topLayoutSceneAdd.getChildren().addAll(datePicker, textFieldDescription, buttonAddProject, buttonDeleteItem, buttonGo2ShowProjects);
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
        columnGoal.setOnEditCommit(
            new EventHandler<CellEditEvent<CFProject, String>>() 
           {
        @Override
        public void handle(CellEditEvent<CFProject, String> t) {
            System.out.println("will handle goal editing ???");
            String newGoal = t.getNewValue();
            System.out.println("new goal: " + newGoal);
            CFProject cf = t.getTableView().getItems().get(t.getTablePosition().getRow());
            cf.setGoal(newGoal);
            persist.updateGoal(cf.getId(), newGoal);
            System.out.println("\t" + cf.toString());
            ((CFProject) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setState(t.getNewValue());
        }
      
        }
    );
        
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
        //states.add("resting");
        columnState.setCellFactory(ComboBoxTableCell.forTableColumn(states));
        columnGoal.setCellFactory(TextFieldTableCell.forTableColumn());
        columnComment.setCellFactory(TextFieldTableCell.forTableColumn());
        
        columnState.setOnEditCommit(
            new EventHandler<CellEditEvent<CFProject, String>>() 
           {
        @Override
        public void handle(CellEditEvent<CFProject, String> t) {
            System.out.println("will handle editing of states");
            String newState = t.getNewValue();
            System.out.println("new state: " + newState);
            CFProject cf = t.getTableView().getItems().get(t.getTablePosition().getRow());
            cf.setState(newState);
            persist.update(cf.getId(), newState);
            System.out.println("\t" + cf.toString());
            ((CFProject) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setState(t.getNewValue());
        }
        }
    );
        //columnGoal.setOnEditCommit(ae->editGoal());
        columnComment.setOnEditCommit(
            new EventHandler<CellEditEvent<CFProject, String>>() 
           {
        @Override
        public void handle(CellEditEvent<CFProject, String> t) {
            System.out.println("will handle editing comment/status");
            String newComment = t.getNewValue();
            System.out.println("new comment: " + newComment);
            CFProject cf = t.getTableView().getItems().get(t.getTablePosition().getRow());
            cf.setComment(newComment);
            persist.updateComment(cf.getId(), newComment);
            persist.addComment(cf.getId(), newComment);
            ((CFProject) t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setState(t.getNewValue());
            tableProjects.setItems(persist.getProjects( checkBoxNotDone.isSelected(),
                                                        checkBoxDone.isSelected(), 
                                                        checkBoxWip.isSelected(), 
                                                        checkBoxFailed.isSelected(), 
                                                        checkBoxResting.isSelected(),
                                                        checkBoxInfinite.isSelected(),
                                                        checkBoxToDo.isSelected()));
            tableComment.setItems(persist.getComments(cf.getId()));
        }
        }
    );
       
        
        
        tableProjects = new TableView<>();
        tableProjects.setEditable(true);
       
        tableProjects.setItems(persist.getProjects( checkBoxNotDone.isSelected(), 
                                                    checkBoxDone.isSelected(), 
                                                    checkBoxWip.isSelected(), 
                                                    checkBoxFailed.isSelected(), 
                                                    checkBoxResting.isSelected(),
                                                    checkBoxInfinite.isSelected(),
                                                    checkBoxToDo.isSelected()));
        //table.getColumns().addAll(columnDescription, columnTargetDate, columnIsDone);
        tableProjects.getColumns().addAll(columnId, columnDescription, columnGoal, columnTargetDate,columnLastUpdate, columnComment, columnState);
        tableProjects.setOnMouseClicked((MouseEvent event) -> {
        if(event.getButton().equals(MouseButton.PRIMARY)){
            CFProject project =  tableProjects.getSelectionModel().getSelectedItem();
            if (project != null){
                ObservableList<CFComment> comments = persist.getComments(project.getId());
                if ( comments != null){
                    tableComment.setItems(comments);
                }else{
                    System.out.println("no comments....");
                }
                //tableComment.setItems(persist.getComments(todoItem.getId()));
                //System.out.println(tableProjects.getSelectionModel().getSelectedItem());                
            }else{
                System.err.println("project is null...");
            }

        }
    });
    }

    private void closeApplication() {
        System.out.println("closeApplication()");
        primaryStage.close();
    }
    
    /**
    private void buttonGetTodoItems(){
        System.out.println("buttonGetTodoItems");
        boolean isDone = checkBoxDone.isSelected();
        boolean notDone = checkBoxNotDone.isSelected();
        boolean wip = checkBoxWip.isSelected();
        boolean failed = checkBoxFailed.isSelected();
        boolean resting = checkBoxResting.isSelected();
        boolean infinite = checkBoxInfinite.isSelected();
        boolean toDo = checkBoxToDo.isSelected();
        ObservableList<CFProject> items = persist.getProjects(isDone,  notDone, wip, failed, resting, infinite, toDo);
        System.out.println("items size: " + items.size());
        for(CFProject item: items){
            System.out.println("\t" + item.toString());
        }
        tableProjects.setItems(items);
    }
    * */
    private void addProject(){
        LocalDate date = datePicker.getValue();
        String description = textFieldDescription.getText();
        String goal = textAreaGoal.getText();
        String state = "not done";
        int id = persist.addProject(date, description, state, goal);
        CFProject project = new CFProject(date, description, state,  goal);
        if (DEBUG) System.out.println("\t" + project.toString());
        project.setId(id);
        tableProjects.getItems().add(project);
        textFieldDescription.clear();    
    }

    private void deleteItem() {
        System.out.println("deleteItem()  ");
        ObservableList<CFProject> allItems, deleteItems;
        allItems = tableProjects.getItems();
        for( CFProject item: allItems){
            System.out.println( item.toString());
        }
        System.out.println("items to delete: ");
        deleteItems  = tableProjects.getSelectionModel().getSelectedItems();
        for ( CFProject item: deleteItems){
            System.out.println(item.toString());
            persist.deleteItem(item.getId());
        }
        deleteItems.forEach(allItems::remove);
    }
    private void showComments(int id){
        if ( DEBUG) System.out.println("showComments: " + id);
        persist.getComments(id);
    }

    private void showState() {
        System.out.println("show state");
        boolean isDone = checkBoxDone.isSelected();
        boolean notDone = checkBoxNotDone.isSelected();
        boolean wip = checkBoxWip.isSelected();
        boolean failed = checkBoxFailed.isSelected();
        boolean resting = checkBoxResting.isSelected();
        boolean infinite = checkBoxInfinite.isSelected();
        boolean toDo = checkBoxToDo.isSelected();
        if( checkBoxDone.isSelected()){
            System.out.println("done is selected");
        }
        if( checkBoxNotDone.isSelected()){
            System.out.println("not done is selected");
        }
        if( checkBoxWip.isSelected()){
            System.out.println("wip is selected");
        }
        if( checkBoxFailed.isSelected()){
            System.out.println("failed is selected");
        }
        if ( checkBoxInfinite.isSelected()){
            System.out.println("infinite is selected");
        }
        if ( checkBoxToDo.isSelected()){
            System.out.println("to do is selected");
        }
        ObservableList<CFProject> list = persist.getProjects(isDone, notDone, wip, failed, resting, infinite, toDo);
        tableProjects.getItems().setAll(list);
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

    private void editGoal() {
        System.out.println("editGoal()");
    }

    private void searchProjects() {
       System.out.println("searchProjects: " + textFieldSearch.getText());
       String searchString = textFieldSearch.getText();
       if ( searchString == null){
           System.err.println("searchstring is null...");
           return;
       }
       ObservableList<CFProject> projects = persist.searchProjects(searchString);
       System.out.println("\tnumber of projects found: " + projects.size());
       //tableProjects.
       tableProjects.setItems(projects);
    }
    
}
