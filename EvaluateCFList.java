import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class EvaluateCFList {
   private static int method = 0;
   private static List<List<Double>> data = new ArrayList<List<Double>> ();
   public static void main(String[] args) {
      if(args.length != 2) {
         System.out.println("usage: EvaluateCFList Method Filename");
         System.exit(-1);
      }
      try {
         method = Integer.parseInt(args[0]);
      } catch (Exception e) {
         System.out.println("usage: EvaluateCFList Method Filename");
         System.exit(-1);
      }
      parseCSV(args[1]);
      for(int i = 0; i < data.size(); i++) {
         for (int j = 0; j < data.get(i).size(); j++) 
            System.out.print(data.get(i).get(j) + " ");
         System.out.println();
         }
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
         List<Double> temp = new ArrayList<Double>();

         for (String s : line) {
            double d = 0.0;
            if(s.trim().length() != 0) {
              try { 
                d = Double.parseDouble(s);
              }
              catch (NumberFormatException e) {
                System.out.println("Caught: "+e);
              }
            
             temp.add(Double.parseDouble(s));
            }
         }
         data.add(temp);
      } 
   }
   
}
