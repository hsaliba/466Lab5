import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class EvaluateCFList {
   private static int method = 0;
   private static List<List<Integer>> data = new ArrayList<List<Integer>> ();
   
   public static void main(String[] args) {
      if(args.length != 2) {
         System.out.println("usage: EvaluateCFList {0,1,2} Filename");
         System.exit(-1);
      }
      try {
         method = Integer.parseInt(args[0]);
      } catch (Exception e) {
         System.out.println("usage: EvaluateCFList {0,1,2} Filename");
         System.exit(-1);
      }
      parseCSV(args[1]);
      evaluate();
   }
   
   public static void evaluate() {
      Filter filter = new Filter();
      int size = filter.getSize();
      int index = 0, joke = 0;
      List<Double> user = null;
      int totalRecordsUsed = 0;
      double predicted = 0, actual = 0, mae = 0;
      for(List<Integer> i : data) {
         if(i.size() == 0) {
            break;
         }
         index = i.get(0);
         joke = i.get(1);
         if(index >= size) {
            System.out.println("User ID not found");
            System.exit(-4);
         }
         user = filter.getUser(index);
         actual = user.get(joke);
         if(Double.compare(actual, 99.0) == 0) {
            System.out.println("UserID: " + index); 
            System.out.println("itemID: " + joke);
            System.out.println("Actual_Rating: N/A\nTest case is Invalid\n");
         } else {
            switch(method) {
               case 0:
                  predicted = filter.meanUtility(index, joke);
               break;
               case 1:
                  predicted = filter.weightedSum(index, joke);
               break;
               case 2:
                  predicted = filter.adjWeightedSum(index, joke);
               break;
               default:
                  System.exit(-3);
            }
            System.out.println("UserID: " + index); 
            System.out.println("itemID: " + joke);
            System.out.println("Actual_Rating: " + actual);
            System.out.println("Predicted_Rating: " + predicted);
            System.out.println("Delta_Rating: " + (actual - predicted) + "\n");                   
            mae += Math.abs(predicted - actual);
            totalRecordsUsed++;
         }
      }
      mae /= totalRecordsUsed;
      System.out.println("MAE: " + mae);   
   }
   
   public static void parseCSV(String file) {
      Scanner sc = null;

      try {
         sc = new Scanner(new File(file));
      }
      catch (Exception e) {
         System.out.println("Caught: "+e);
      }
      String delims = "[,]";
     
      String[] line = null;
      while (sc.hasNextLine()) {
         line = sc.nextLine().split(delims);
         List<Integer> temp = new ArrayList<Integer>();
         int d = 0;
         for (String s : line) {
            s = s.trim();
            if(s.length() != 0) {
               try { 
                  d = Integer.parseInt(s);
               }
               catch (NumberFormatException e) {
                  e.printStackTrace();
                  System.exit(-2);
               }
               temp.add(d);
            }
         }
         data.add(temp);
      } 
   }
   
}
