import java.util.*;
import java.lang.*;
import java.io.*;

public class Filter {

   //note: elemement 0 = # of jokes rated NOT a rating
   private static ArrayList<ArrayList<Double>> data; 

   public static void parseCSV(String file) {
      Scanner sc = null;

      try {
         sc = new Scanner(new File(file));
      }
      catch (Exception e) {
         System.out.println("Caught: "+e);
      }

      data = new ArrayList<ArrayList<Double>>();
      String delims = "[,]";
     
      String[] line = null;
      while (sc.hasNextLine()) {
         line = sc.nextLine().split(delims);
         ArrayList<Double> temp = new ArrayList<Double>();

         for (String s : line) {
            double d = 0.0;
            try { 
               d = Double.parseDouble(s);
            }
            catch (NumberFormatException e) {
               System.out.println("Caught: "+e);
            }
            temp.add(Double.parseDouble(s));
         }
         data.add(temp);
      }
   }

   public static double cosineSim(ArrayList<Double> u1, ArrayList<Double> u2) {
      double top = 0.0;
      double bot1 = 0.0;
      double bot2 = 0.0;


      for (int i = 1; i < u1.size(); i++) { //skip 0 b/c not a rating
         if (!(Double.compare(u1.get(i), 99.0) == 0 || Double.compare(u2.get(i), 99.0) == 0)) {
            top += u1.get(i) * u2.get(i);
            bot1 += u1.get(i) * u1.get(i);
            bot2 += u2.get(i) * u2.get(i); 
         }
      }
      return top / (Math.sqrt(bot1*bot2));
   } 

   public static void main(String[] args) {

      if (args.length != 1) {
         System.out.println("usage: java Filter <csv file>");
         System.exit(1);
      } 


      parseCSV(args[0]);
      for (int i = 0; i < 10; i++) 
         for (int j = i+1; j < 10; j++) 
            System.out.println(cosineSim(data.get(i), data.get(j)));

   }
}
