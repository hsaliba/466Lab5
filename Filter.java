import java.util.*;
import java.lang.*;
import java.io.*;

public class Filter {

   //note: elemement 0 = # of jokes rated NOT a rating
   private ArrayList<ArrayList<Double>> data; 

   public Filter() {
      data = new ArrayList<ArrayList<Double>>();
      parseCSV("jester-data-1.csv");
   }

   private void parseCSV(String file) {
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

   private double cosineSim(ArrayList<Double> u1, ArrayList<Double> u2) {
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

   public double getk(int u, int col) {
      double sum = 0;

      for (int i = 0; i < data.size(); i++) {
         if (Double.compare(data.get(i).get(col), 99.0) != 0) 
            sum += Math.abs(cosineSim(data.get(u), data.get(i)));
      }
      return 1/sum;
   }

   public double weightedSum(int user, int ndx) {
      double ans = 0.0;
      double sum = 0.0;
      double k = getk(user, ndx);
      ArrayList<Double> u = data.get(user);

      for (int i = 0; i < data.size(); i++) {
         double val = data.get(i).get(ndx);
         if (Double.compare(val, 99.0) != 0 && user != i) 
            sum += cosineSim(u, data.get(i)) * val;
      } 
      ans = k * sum;
      
      return ans;      
   }

   public double meanUtility(int c, int s) {
      double rating = 0;
      int count = 0;
      ArrayList<Double> temp;
      for (int i = 0; i < data.size(); i++) {
         if (i != c) {
            temp = data.get(i);
            if (Double.compare(temp.get(s), 99.0) != 0) {
               rating += temp.get(s);
               count++;
            }
         }
      }
      return rating / count;
      
   }
   
   public double adjWeightedSum(int c, int s) {
      double rating = 0, temp = 0, total = 0;
      double k = getk(c, s);
      rating += avgRating(c);
      for(int i = 0; i < data.size(); i++) {
         if(i != c) {
            temp = data.get(i).get(s);
            if(Double.compare(temp, 99.0) != 0) {
               total += (cosineSim(data.get(c), data.get(i))) * (temp - avgRating(i));
            }           
         }
      }
      rating += k*total;
      return rating;
   }
   
   private double avgRating(int c) {
      double rating = 0;
      ArrayList<Double> temp = data.get(c);
      int count = 0;
      for(double i : temp) {
         if(Double.compare(i, 99.0) != 0) {
            rating += i;
            count++;
         }
      }
      return rating / count;
   }

   public int getSize() {
      return data.size();
   }

   public ArrayList<Double> getUser(int ndx) {
      return data.get(ndx);
   }
}

