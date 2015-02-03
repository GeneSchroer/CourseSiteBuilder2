package randomstudentpicker;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This is a simple little program useful for randomly picking
 * students registered in our class to be called on to
 * answer a question during lecture.
 * 
 * @author Richard McKenna
 */
public class RandomStudentPicker extends Application {

    // HERE ARE THE STUDENTS WE'LL PICK FROM
    String[] students = {"Nahin Alam", "Layla Baroudi", "Christopher Camenares", "Arthur Chan", "Michelle Chan", "Kenny Chen", "Michael Chen", "Stanley Chen", "Yongbin Chen", "Sean Cheng", "Victor Cheng", "Awaes Choudhary", "Brandon Fieger", "Evan Glover", "Alan Guan", "Tyler Hadley", "Yinquan Hao", "Sean Healey", "Kenji Japra", "Thomas Karnati", "Wan Jong Kim", "Andreas Konstantinou", "Jonathan Leifer", "Zachary Lerman", "Liyun Li", "Vincent Li", "Zeqing Li", "Huahai Liang", "Patrick Liao", "Joon Hyung Lim", "Seonghyeon Lim", "Hangting Lin", "Ziang Lin", "Qianling Lu", "James Lynn", "Celeste Ma", "Terrell Mack", "Devon Maguire", "Usman Malik", "Ariel Manalo", "Steven Massaro", "Chun Maung", "Samuel McKay", "Peter Mei", "Halaa Menasy", "Kat Mendoza", "Louis Mezzich", "Tianran Mo", "Roman Nersesyan", "Gleb Nikonorov", "Mukai Nong", "Jason Park", "Sushal Penugonda", "Maksim Perel", "Jay Polanco", "Marc Rejndrup", "Yael Romero", "Gene Schroer", "Daniel Sha", "Purav Shah", "Marlene Shankar", "Shaan Sheikh", "Rafik Soughou", "Ashley Taylor", "Tristen Terracciano", "Tyler Thompson", "Jonathan Valentin", "Christopher Vaneron", "George Vergara", "Edward Wang", "Nicolas Wein", "Marcin Wolski", "Sik Ho Wong", "Kenneth Wu", "Xuhao Yang", "Kevin Young", "Zexun Yu", "Xujin Zhang", "Wendy Zheng", "Victor Zhong"};

    // THESE ARE OUR ONLY CONTROLS, A BUTTON AND A DISPLAY LABEL
    Button pickButton;
    final Label studentNameLabel = new Label();
    
    @Override
    public void start(Stage primaryStage) {
        // MAKE OUR TWO CONTROLS
        pickButton = new Button("Pick Random Student");
        pickButton.setFont(new Font("Cursive", 36));
        studentNameLabel.setFont(new Font("Serif", 48));

        // PUT THEM IN A CONTAINER
        BorderPane root = new BorderPane();
        FlowPane topPane = new FlowPane();
        topPane.getChildren().add(pickButton);
        topPane.setAlignment(Pos.CENTER);
        root.setTop(topPane);
        root.setCenter(studentNameLabel);

        // AND PUT THE CONTAINER IN THE WINDOW (i.e. the "stage")
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);

        // PROVIDE A RESPONSE TO BUTTON CLICKS
        pickButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task<Void> task = new Task<Void>() {
                    @Override protected Void call() throws Exception {
                        for (int i = 0; i < 20; i++) {
                            // RANDOMLY SELECT A STUDENT
                            int randomIndex = (int) (Math.random() * students.length);
                            
                            /* Gene Schroer: The first bug was that student was set to
                            only the first student in the students array.
                            I changed the index to randomIndex, so it cycles through each name
                            randomly
                            */
                            String student = students[randomIndex];
                            
                            // THIS WILL BE DONE ASYNCHRONOUSLY VIA MULTITHREADING
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    studentNameLabel.setText(students[randomIndex]);
                                }
                            });
                            
                            // SLEEP EACH FRAME
                            /* Gene Schroer: The second bug was that the parameter for 
                            sleep was too low. I increased it to 2500, or about 2.5 seconds,
                            so it is possible to view and remember each name.                     
                            */
                            try { Thread.sleep(2500); }
                            catch(InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                        return null;
                    }
                };
                // THIS GETS THE THREAD ROLLING
                
                Thread thread = new Thread(task);
                if(!thread.isAlive())
                thread.start();
                
                
                
            }
        });
        // OPEN THE WINDOW
        primaryStage.show();
    }

    /**
     * This starts our JavaFX application rolling.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
