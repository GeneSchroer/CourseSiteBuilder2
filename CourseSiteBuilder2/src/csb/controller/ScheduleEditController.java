package csb.controller;

import static csb.CSB_PropertyType.REMOVE_ITEM_MESSAGE;
import csb.data.Course;
import csb.data.CourseDataManager;
import csb.data.Lecture;
import csb.data.ScheduleItem;
import csb.gui.CSB_GUI;
import csb.gui.LectureDialog;
import csb.gui.MessageDialog;
import csb.gui.ScheduleItemDialog;
import csb.gui.YesNoCancelDialog;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author McKillaGorilla
 */
public class ScheduleEditController {
    ScheduleItemDialog sid;
    LectureDialog ld;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    public ScheduleEditController(Stage initPrimaryStage, Course course, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        sid = new ScheduleItemDialog(initPrimaryStage, course, initMessageDialog);
        ld = new LectureDialog(initPrimaryStage, course, initMessageDialog);
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
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }        
    }
    public void handleEditLecutreRequest(CSB_GUI gui, Lecture itemToEdit){
        CourseDataManager cdm = gui.getDataManager();
        Course course = cdm.getCourse();
        ld.showEditLectureDialog(itemToEdit);
    }
    public void handleRemoveScheduleItemRequest(CSB_GUI gui, ScheduleItem itemToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_ITEM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(YesNoCancelDialog.YES)) { 
            gui.getDataManager().getCourse().removeScheduleItem(itemToRemove);
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
        }
    
    }
    
}