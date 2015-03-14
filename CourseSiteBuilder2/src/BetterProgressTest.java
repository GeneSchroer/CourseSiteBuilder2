
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class BetterProgressTest extends Application {
    ProgressBar bar;
    ProgressIndicator indicator;
    Button button;
    Label processLabel;
    int numTasks = 0;
    ReentrantLock progressLock;

    @Override
    public void start(Stage primaryStage) throws Exception {
        progressLock = new ReentrantLock();
        VBox box = new VBox();

        HBox toolbar = new HBox();
        bar = new ProgressBar(0);      
        indicator = new ProgressIndicator(0);
        toolbar.getChildren().add(bar);
        toolbar.getChildren().add(indicator);
        
        button = new Button("Restart");
        processLabel = new Label();
        processLabel.setFont(new Font("Serif", 36));
        box.getChildren().add(toolbar);
        box.getChildren().add(button);
        box.getChildren().add(processLabel);
        
        Scene scene = new Scene(box);
        primaryStage.setScene(scene);

        button.setOnAction(e -> {
                Task<Void> task = new Task<Void>() {
                    int task = numTasks++;
                    double max = 200;
                    double perc;
                    @Override
                    protected Void call() throws Exception {
                        try {
                            progressLock.lock();
                        for (int i = 0; i < 200; i++) {
                            System.out.println(i);
                            perc = i/max;
                            
                            // THIS WILL BE DONE ASYNCHRONOUSLY VIA MULTITHREADING
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    bar.setProgress(perc);
                                    indicator.setProgress(perc);
                                    processLabel.setText("Task #" + task);
                                }
                            });

                            // SLEEP EACH FRAME
                            //try {
                                Thread.sleep(10);
                           // } catch (InterruptedException ie) {
                          //      ie.printStackTrace();
                          //  }
                        }}
                        finally {
                                progressLock.unlock();
                                }
                        return null;
                    }
                };
                // THIS GETS THE THREAD ROLLING
                Thread thread = new Thread(task);
                thread.start();            
        });        
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}