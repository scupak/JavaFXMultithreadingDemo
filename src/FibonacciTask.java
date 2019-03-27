// FibonacciTask.java
// Task subclass for calculating Fibonacci numbers in the background
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;

public class FibonacciTask extends Task<Long> {
   private final int n; // Fibonacci number to calculate

   // constructor
   public FibonacciTask(int n) {
      this.n = n;
   } 

   @Override
   protected Long call() throws Exception {
      updateMessage("Calculating...");
      long result = fibonacci(n);
      updateMessage("Done calculating."); 
      return result;
   }
   
   // Non-recursive Fibonacci method (is much faster than the recursive
   // implementation, and hence the delay in each iteration)
   private long fibonacci(int number) throws Exception {
                             
      if (number <= 1) {
         return number;
      }
      
      long fibo = 1;
      long fiboPrev = 1;
      
      for(int i = 2; i < number; ++i) { 
          long temp = fibo;
          fibo += fiboPrev;
          fiboPrev = temp;
          updateValue(fibo);
          updateProgress(i+1, number);
          TimeUnit.MILLISECONDS.sleep(100);                  
      }
      
      return fibo;                       
      
   }

} 
