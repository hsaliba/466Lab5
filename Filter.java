import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

public class Filter {

   //note: elemement 0 = # of jokes rated NOT a rating
   private static ArrayList<ArrayList<Double>> data; 
   private static double k = 0.000000009;
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
            double d = 0;
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
      for(int i = 0; i < 100; i++)
      System.out.println(meanUtility(5000, i));
      /*
      System.out.println(data.size() + " " + data.get(0).size());
      for (int i = 0; i < 50; i++) {
         ArrayList<Double> temp = data.get(i);
         for (int j = 0; j <10; j++) {
            System.out.print(temp.get(j) + " ");
         }
         System.out.println("\n");
      }*/
   }
   
   public static double meanUtility(int c, int s) {
      double rating = 0;
      int count = 0;
      ArrayList<Double> temp;
      for (int i = 0; i < data.size(); i++) {
         if (i != c) {
            temp = data.get(i);
            if (Double.compare(temp.get(s + 1), 99.0) != 0) {
               rating += temp.get(s + 1);
            }
            count++;
         }
      }
      return rating / count;
      
   }
   
   public static double adjWeightedSum(int c, int s) {
      double rating = 0, temp = 0, total = 0;
      rating += avgRating(c);
      for(int i = 0; c < data.size(); c++) {
         if(i != c) {
            temp = data.get(i).get(s+ 1);
            if(Double.compare(temp, 99.0) == 0) {
               temp = 0;
            }
            total += (cosineSim(data.get(c), data.get(i))) * (temp - avgRating(i));
         }
      }
      rating += k*total;
      return rating;
   }
   
   private static double avgRating(int c) {
      double rating = 0;
      ArrayList<Double> temp = data.get(c);
      int count = 0;
      for(double i : temp) {
         if(Double.compare(i, 99.0) != 0) {
            rating += i;
         }
         count++;
      }
      return rating / count;
   }
}