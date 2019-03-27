// FibonacciNumbersController.java
// Using a Task to perform a long calculation 
// outside the JavaFX application thread.
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FibonacciNumbersController {
   @FXML private TextField numberTextField;
   @FXML private Button goButton;
   @FXML private Button cancelButton;
   @FXML private Label messageLabel;
   @FXML private Label fibonacciLabel;
   @FXML private ProgressBar progressBar;

   private FibonacciTask task;

   // starts FibonacciTask to calculate in background
   @FXML
   void goButtonPressed(ActionEvent event) {
      // get Fibonacci number to calculate
      try {
         int input = Integer.parseInt(numberTextField.getText());

         // Create FibonacciTask
         task = new FibonacciTask(input);
         
         // Create FibonacciRunnable
         FibonacciRunnable runnable = new FibonacciRunnable(input, fibonacciLabel);

         // Display FibonacciTask's messages in messageLabel
         messageLabel.textProperty().bind(task.messageProperty());

         // Display FibonacciTask's progress in progressBar
         progressBar.progressProperty().bind(task.progressProperty());
         
         // Display FibonacciTask's intermediate values in fibonacciLabel
         task.valueProperty().addListener(
             (observable, oldvalue, newvalue) -> {
                 fibonacciLabel.setText(newvalue.toString());
             } 
         );

         // Do When task starts.
         task.setOnRunning((workerStateEvent) -> {
            goButton.setDisable(true);
            //fibonacciLabel.setText("");
            cancelButton.setDisable(false);
         });
         
         // Do when task completes successfully
         task.setOnSucceeded((workerStateEvent) -> {
            //fibonacciLabel.setText(task.getValue().toString());
            goButton.setDisable(false);
            cancelButton.setDisable(true);
         });
         
         // Do when task is cancelled.
         task.setOnCancelled((succeededEvent) -> {
            goButton.setDisable(false);
            cancelButton.setDisable(true);
         });

         // create ExecutorService to manage threads
         ExecutorService executorService = 
            Executors.newFixedThreadPool(1);
         
         // Start the task (start either this or the runnable below)
         executorService.submit(task);
         
         // Start the runnable (start either this or the task above)
         //executorService.submit(runnable);
         
         // Shut down the thread pool when task or runnable stops
         executorService.shutdown();
      }      
      catch (NumberFormatException e) {
         numberTextField.setText("Enter an integer");
         numberTextField.selectAll();
         numberTextField.requestFocus();
      }
   }
   
   // Cancels FibonacciTask
   @FXML
   void cancelButtonPressed(ActionEvent event) {
       if (task!=null){
           messageLabel.textProperty().unbind();
           messageLabel.setText("Cancelled.");
           task.cancel();
       }
   }


}
