package csb.gui;

import csb.data.Course;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgressDialog  implements Runnable{
    ProgressBar bar;
    ProgressIndicator indicator;
    Label processLabel;
    ReentrantLock progressLock;
    Stage stage;
    int index;
    double increment;
    double total;
    int numTasks;
    Course course; // course whose pages we are interested in.
    
    // Constructor requires a course to read in the number of pages and page names.
    // Currently doesn't actually use the course.
    public ProgressDialog(Course course){    
        this.course = course;
        index = 0;
        progressLock = new ReentrantLock();
        numTasks = course.getPages().size();
        increment = (double)100/numTasks/100;
        total = 0;       
    }
    
   //Builds the GUI for Progress Dialog
    public void buildGUI(){
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        VBox box = new VBox();
        HBox toolbar = new HBox();
        
        bar = new ProgressBar(0);      
        indicator = new ProgressIndicator(0);
        
        toolbar.getChildren().add(bar);
        toolbar.getChildren().add(indicator);
        
        processLabel = new Label();
        processLabel.setFont(new Font("Serif", 20));
        
        box.getChildren().add(toolbar);
        box.getChildren().add(processLabel);
        
        Scene scene = new Scene(box, 250, 100);
        stage.setScene(scene);
        stage.show();
    }
    
    // Our run method. Loads up the gui and increments the progress bar.
    @Override
    public void run(){ 
        Task<Void> task = new Task<Void>() {
            @Override  
            protected Void call() throws Exception {
                try {
                    progressLock.lock();
                    //Our GUI is built
                    Platform.runLater(new Runnable(){
                        @Override
                        public void run(){
                            buildGUI();
                            processLabel.setText("Preparing to load");
                        }
                    });
                    
                    Thread.sleep(2112);
                    
                    //Loops a simulation of a progress bar updating
                    for(int i = 0; i< numTasks; ++i){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run(){
                                total += increment;
                                indicator.setProgress(total);
                                bar.setProgress(total);
                                   
                                processLabel.setText(course.getPages().get(index++).name() + " exported");
                            }
                        });
                        try{
                            Thread.sleep(2112);
                            if (total==1){
                                
                                 Platform.runLater(new Runnable(){
                                @Override
                                public void run(){
                                       processLabel.setText("Complete!");
                                }
                                 });
                            }
                            //Seriously, this is a great song
                            
                        }
                        catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
                finally{
                   progressLock.unlock();
                }
                return null;  
            }
        };
        Thread t = new Thread(task);
        t.start();
        try {
            t.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProgressDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }           
}
