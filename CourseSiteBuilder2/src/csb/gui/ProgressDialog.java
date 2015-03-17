package csb.gui;

import csb.data.Course;
import java.util.concurrent.locks.ReentrantLock;
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
    double increment;
    double total;
    int numTasks;
    Course course; // course whose pages we are interested in.
    
    // Constructor requires a course to read in the number of pages and page names.
    // Currently doesn't actually use the course.
    public ProgressDialog(Course course){    
        this.course = course;
        
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
        processLabel.setFont(new Font("Serif", 26));
        
        box.getChildren().add(toolbar);
        box.getChildren().add(processLabel);
        
        Scene scene = new Scene(box, 150, 100);
        stage.setScene(scene);
        stage.showAndWait();
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
                        }
                    });
                    
                    //Loops a simulation of a progress bar updating
                    for(int i = 0; i< numTasks+1; ++i){
                        Platform.runLater(new Runnable(){
                            @Override
                            public void run(){
                                   indicator.setProgress(total);
                                   bar.setProgress(total);
                                   total += increment;
                                   //processLabel.setText();
                            }
                        });
                        try{
                            if (total==1){
                                 Platform.runLater(new Runnable(){
                                @Override
                                public void run(){
                                       processLabel.setText("Complete!");
                                }
                                 });
                            }
                            //Seriously, this is a great song
                            Thread.sleep(2112);
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
    }           
}
