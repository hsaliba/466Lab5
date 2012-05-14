import java.util.*;
import java.lang.*;
import java.io.*;

public class Filter {

   //calculated with the getk function below
   private static final double k = .000000022370102912772287;
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

   public static double getk() {
      double sum = 0;

      for (int i = 0; i < data.size(); i++) {
         for (int j = i+1; j < data.size(); j++) {
            sum += cosineSim(data.get(i), data.get(j));
         }
      }
      return 1/sum;
   }

   //currently uses itself in the calculation....shouldn't matter though
   public static double adjustedWeightedSum(ArrayList<Double> u, int ndx) {
      double uavg = 0.0;
      double ans = 0.0;
      double sum = 0.0;

      for (int i = 1; i < u.size(); i++)
         if (Double.compare(u.get(i), 99.0) != 0) 
            uavg += u.get(i);
      uavg = uavg / (u.size()-1);

      for (int i = 0; i < data.size(); i++) {
         double val = data.get(i).get(ndx);
         sum += cosineSim(u, data.get(i)) * ((Double.compare(val, 99.0) == 0 ? 0 : val) - uavg);
      } 
      ans = uavg + (k * sum);
      
      return ans;      
   }

   public static void main(String[] args) {

      if (args.length != 1) {
         System.out.println("usage: java Filter <csv file>");
         System.exit(1);
      } 


      parseCSV(args[0]);
      /*for (int i = 0; i < 10; i++) 
         for (int j = i+1; j < 10; j++) 
            System.out.println(cosineSim(data.get(i), data.get(j)));
*/
      //some example tests
      System.out.println(adjustedWeightedSum(data.get(11), 1));
      System.out.println(adjustedWeightedSum(data.get(11), 2));
      System.out.println(adjustedWeightedSum(data.get(11), 3));
      System.out.println(adjustedWeightedSum(data.get(11), 4));
   }
}
