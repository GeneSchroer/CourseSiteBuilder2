package csb.controller;

import static csb.CSB_PropertyType.REMOVE_ITEM_MESSAGE;
import csb.data.Assignment;
import csb.data.Course;
import csb.data.CourseDataManager;
import csb.data.Lecture;
import csb.data.ScheduleItem;
import csb.gui.AssignmentDialog;
import csb.gui.CSB_GUI;
import csb.gui.LectureDialog;
import csb.gui.MessageDialog;
import csb.gui.ScheduleItemDialog;
import csb.gui.YesNoCancelDialog;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class ScheduleEditController {
    ScheduleItemDialog sid;
    LectureDialog ld;
    AssignmentDialog ad;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    public ScheduleEditController(Stage initPrimaryStage, Course course, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        sid = new ScheduleItemDialog(initPrimaryStage, course, initMessageDialog);
        ld = new LectureDialog(initPrimaryStage, course, initMessageDialog);
        ad = new AssignmentDialog(initPrimaryStage, course, initMessageDialog);
        messageDialog = initMessageDialog;
        yesNoCancelDialog = initYesNoCancelDialog;
    }

    // THESE ARE FOR SCHEDULE ITEMS
    
    public void handleAddScheduleItemRequest(CSB_GUI gui) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        sid.showAddScheduleItemDialog(course.getStartingMonday());
        
        // DID THE USER CONFIRM?
        if (sid.wasCompleteSelected()) {
            // GET THE SCHEDULE ITEM
            ScheduleItem si = sid.getScheduleItem();
            
            // AND ADD IT AS A ROW TO THE TABLE
            course.addScheduleItem(si);
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    public void handleAddLectureRequest(CSB_GUI gui){
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ld.showAddLectureDialog();
        
        // DID THE USER CONFIRM?
        if (ld.wasCompleteSelected()) {
            // GET THE SCHEDULE ITEM
            
            Lecture lect = ld.getLecture();
            
            // AND ADD IT AS A ROW TO THE TABLE
            course.addLecture(lect);
            
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    public void handleAddAssignmentRequest(CSB_GUI gui) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ad.showAddAssignmentDialog(course.getStartingMonday());
        
        //DID THE USER CONFIRM
        if (ad.wasCompleteSelected()){
            Assignment assign = ad.getAssignment();
            
            // AND ADD IT AS A ROW TO THE TABLE
            course.addAssignment(assign);
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    public void handleEditScheduleItemRequest(CSB_GUI gui, ScheduleItem itemToEdit) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        sid.showEditScheduleItemDialog(itemToEdit);
        
        // DID THE USER CONFIRM?
        if (sid.wasCompleteSelected()) {
            // UPDATE THE SCHEDULE ITEM
            ScheduleItem si = sid.getScheduleItem();
            itemToEdit.setDescription(si.getDescription());
            itemToEdit.setDate(si.getDate());
            itemToEdit.setLink(si.getLink());
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }        
    }
    public void handleEditLectureRequest(CSB_GUI gui, Lecture itemToEdit){
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ld.showEditLectureDialog(itemToEdit);
        
        // DID THE USER CONFIRM?
        if (ld.wasCompleteSelected()) {
            // UPDATE THE SCHEDULE ITEM
            Lecture lect = ld.getLecture();
            itemToEdit.setTopic(lect.getTopic());
            itemToEdit.setSessions(lect.getSessions());

            //Update the Toolbar, because this is not saved.\
            gui.updateToolbarControls(false);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }   
    }
    public void handleEditAssignmentRequest(CSB_GUI gui, Assignment itemToEdit) {
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ad.showEditAssignmentDialog(itemToEdit);
        
        // DID THE USER CONFIRM?
        if (ad.wasCompleteSelected()) {
            // UPDATE THE SCHEDULE ITEM
            Assignment assign = ad.getAssignment();
            itemToEdit.setName(assign.getName());
            itemToEdit.setTopics(assign.getTopics());
            itemToEdit.setDate(assign.getDate());
            
            
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }   
    }
    public void handleRemoveScheduleItemRequest(CSB_GUI gui, ScheduleItem itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();
        //Update the Toolbar, because this is not saved.
        gui.updateToolbarControls(false);

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(YesNoCancelDialog.YES)) { 
            gui.getDataManager().getCourse().removeScheduleItem(itemToRemove);
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
    }
    public void handleRemoveLectureRequest(CSB_GUI gui, Lecture itemToRemove){
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
   
        // GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();
        
        // IF USER SAYS YES, SAVE BEFORE MOVING ON
        if(selection.equals(YesNoCancelDialog.YES)){
            gui.getDataManager().getCourse().removeLecture(itemToRemove);
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
            
        }
    
    }
    public void handleRemoveAssignmentRequest(CSB_GUI gui, Assignment itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
   
        // GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();
        
        // IF USER SAYS YES, SAVE BEFORE MOVING ON
        if(selection.equals(YesNoCancelDialog.YES)){
            gui.getDataManager().getCourse().removeAssignment(itemToRemove);
            //Update the Toolbar, because this is not saved.
            gui.updateToolbarControls(false);
        }
    }
    //Handles moving a lecture up
    public void handleMoveUpLectureRequest(CSB_GUI gui, int index) {
        
        ObservableList<Lecture> list = gui.getDataManager().getCourse().getLectures();
        Lecture temp = new Lecture();
        temp.setSessions(list.get(index).getSessions());
        temp.setTopic(list.get(index).getTopic());
        if(index>0){
            list.set(index, list.get(index-1));
            list.set(index-1, temp);
        }
        gui.updateToolbarControls(false);
    }
    // Handles moving a lecture down
    public void handleMoveDownLectureRequest(CSB_GUI gui, int index) {
        ObservableList<Lecture> list = gui.getDataManager().getCourse().getLectures();
                
        Lecture temp = new Lecture();
        temp.setSessions(list.get(index).getSessions());
        temp.setTopic(list.get(index).getTopic());
           
        if(index<list.size()-1){
            list.set(index, list.get(index + 1));
            list.set(index + 1, temp);
        }
        gui.updateToolbarControls(false);
    }
}