import java.util.*;
import java.lang.*;
import java.io.*;

public class EvaluateCFRandom {

   public static void test(int size, int method) {
      Filter filter = new Filter();
      Random rand = new Random();
      int len = filter.getSize(); 
      int ndx = 0;
      int user = 0;
      double predicted = 0.0;
      double mae = 0.0;

      switch (method) {
         case 0: 
            break;
         case 1 :
            break;
         case 2 :
            for (int i = 0; i < size; i++) {
               user = Math.abs(rand.nextInt()%len);
               ArrayList<Double> u = filter.getUser(user); 
               ndx = Math.abs(rand.nextInt()%u.size()); 

               while (ndx == 0 || Double.compare(99.0, u.get(ndx)) == 0) 
                  ndx = Math.abs(rand.nextInt()%u.size());

               predicted = filter.adjustedWeightedSum(user, ndx);
               mae += Math.abs(predicted - u.get(ndx));
               System.out.println("User: "+user+" Joke: "+ndx); 
               System.out.println("Actual: "+u.get(ndx)+" Predicted: "+predicted); 
            }
            mae = mae / size;
            System.out.println("MAE: "+mae);
            break;
         default :
            System.out.println("Entered invalid method number");
            break;
      }
   } 

   public static void main(String[] args) {

      if (args.length != 2) {
         System.out.println("usage: java EvaluateCFRandom <int-method> <size>\n");

         System.out.println("Methods: \n"+
               "0 - Mean Utility\n"+
               "1 - Weighted Sum\n"+
               "2 - Adjusted Weighted Sum");
         System.exit(0);
      }

//      test(20, 2);
      Filter filter = new Filter();
      System.out.println("k: "+filter.getk());
   }

}
