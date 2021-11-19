/**
* Finding interval-based constant best approximation.
* With interactive options to input custom points or take random point values. 
* Assumption that each x-value is unique. Implemented both recursive and iterative version for time and space complexity analysis.
* Time Complexity: O(n^2) where n is the number of points -> (x,y).
* 
* @author  Vyom Pathak
* @version 1.0
* @since   2021-11-18 
*/
package ICA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

class Point {

    private Integer x;
    private Double y;

    // Constructor
    public Point(Integer x, double y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + String.valueOf(x) + "," + String.valueOf(y) + ")";
    }

}

class SortbyX implements Comparator<Point> {
    @Override
    public int compare(Point o1, Point o2) {
        return o1.getX().compareTo(o2.getX());
    }
}

public class ICA {
    private static int max = 4700;
    private Double Errors[][] = new Double[max + 1][max + 1];
    private int num_points;
    private double delta = -1.0;
    private ArrayList<Point> points = new ArrayList<Point>();
    private Double sol[] = new Double[max + 1];
    private int opt_seg[] = new int[max + 1];
    private Double prefix_sum_y[] = new Double[max + 1];
    private Double prefix_sum_sqry[] = new Double[max + 1];
    private HashMap<Integer, Double> memo = new HashMap<Integer, Double>();
    private int opt_seg_rec[] = new int[max + 1];

    // Constructor
    public ICA(int num_points, double delta, boolean manual) {
        this.num_points = num_points;
        this.delta = delta;
        for (int i = 0; i <= num_points; i++) {

            if (!manual)
                points.add(new Point(i, (Math.random() * (50.0 - 0.0)) + 0.0));
            opt_seg[i] = 0;
            sol[i] = 0.0;
            prefix_sum_y[i] = 0.0;
            prefix_sum_sqry[i] = 0.0;
            prefix_sum_y[i] = 0.0;
            prefix_sum_sqry[i] = 0.0;
        }

        for (int i = 1; i <= num_points; i++) {
            for (int j = 1; j <= num_points; j++) {
                this.Errors[i][j] = 0.0;
            }
        }
    }

    public void sortByX() {
        Collections.sort(points, new SortbyX());
        System.out.println("The Points are :");
        Iterator<Point> itr = points.iterator();
        while (itr.hasNext())
            System.out.print(itr.next().toString() + " ");
        System.out.println();
    }

    public void calculateErrors() {
        for (int j = 1; j <= num_points; j++) {
            double pt = points.get(j - 1).getY();
            prefix_sum_y[j] = prefix_sum_y[j - 1] + pt;
            prefix_sum_sqry[j] = prefix_sum_sqry[j - 1] + (pt * pt);
            for (int i = 1; i <= j; i++) {
                Double interval = Double.valueOf(j - i + 1);
                Double x_sum_ij = prefix_sum_y[j] - prefix_sum_y[i - 1];
                Double sqrx_sum_ij = prefix_sum_sqry[j] - prefix_sum_sqry[i - 1];
                Errors[i][j] = (sqrx_sum_ij / interval) - ((x_sum_ij / interval) * (x_sum_ij / interval));
                if (i == j)
                    Errors[i][j] = 0.0;
            }
        }
    }

    public void findICA() {
        System.out.println("\nThe penalty is : " + delta);
        for (int j = 1; j <= num_points; j++) {
            Double temp = Double.POSITIVE_INFINITY;
            int temp_ind = 0;
            for (int i = 1; i <= j; i++) {
                Double bell_eq = Errors[i][j] + sol[i - 1];
                if (bell_eq < temp) {
                    temp = bell_eq;
                    temp_ind = i;
                }
            }
            sol[j] = temp + delta;
            opt_seg[j] = temp_ind;
        }
        int i = num_points;
        int j = opt_seg[num_points];
        Stack<Integer> point_indices = new Stack<Integer>();
        while (i > 0) {
            point_indices.add(i);
            point_indices.add(j);
            i = j - 1;
            j = opt_seg[i];
        }

        System.out.println("Cost of the optimal solution is : " + sol[num_points]);
        System.out.println("Total number of segments are : " + point_indices.size() / 2);
        while (!point_indices.empty()) {
            int i_x = point_indices.pop();
            int j_y = point_indices.pop();
            System.out.println(
                    "   Segment between indices: " + i_x + "  " + j_y + " with error value : " + Errors[i_x][j_y]);
            while (i_x <= j_y) {
                System.out.print(points.get(i_x - 1).toString() + " ");
                i_x++;
            }
            System.out.println();
        }
    }

    public double findCostRecurse(int n) {
        if (n == 0)
            return 0;
        if (memo.containsKey(n))
            return memo.get(n);
        double temp = Double.POSITIVE_INFINITY, cost;
        int temp_ind = 0;
        for (int i = 1; i <= n; i++) {
            cost = Errors[i][n] + delta + findCostRecurse(i - 1);
            if (cost < temp) {
                temp = cost;
                temp_ind = i;
            }
        }
        memo.put(n, temp);
        opt_seg_rec[n] = temp_ind;
        return temp;
    }

    private void printRecursionSegments() {
        int i = num_points;
        int j = opt_seg_rec[num_points];
        Stack<Integer> point_indices = new Stack<Integer>();
        while (i > 0) {
            point_indices.add(i);
            point_indices.add(j);
            i = j - 1;
            j = opt_seg_rec[i];
        }
        System.out.println("Total number of segments are : " + point_indices.size() / 2);
        while (!point_indices.empty()) {
            int i_x = point_indices.pop();
            int j_y = point_indices.pop();
            System.out.println(
                    "   Segment between indices: " + i_x + "  " + j_y + " with error value : " + Errors[i_x][j_y]);
            while (i_x <= j_y) {
                System.out.print(points.get(i_x - 1).toString() + " ");
                i_x++;
            }
            System.out.println();
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        ICA test = null;
        int num_points = 12;
        double delta = 10.0;
        int manual;
        System.out.println("Enter the number of points");
        num_points = sc.nextInt();
        System.out.println("Enter the penalty value");
        delta = sc.nextFloat();
        System.out.println("To manually enter the points, press 1 else press anyother number for random points: ");
        manual = sc.nextInt();
        if (manual == 1) {
            System.out.println("Enter " + num_points
                    + " points as x and y coordinates separated by space and each point on a new line");

            test = new ICA(num_points, delta, true);
            for (int i = 1; i <= num_points; i++) {
                test.points.add(new Point(sc.nextInt(), sc.nextDouble()));
            }
        } else {
            test = new ICA(num_points, delta, false);
        }
        test.sortByX();
        System.out.println(
                "To run the recursive function press 1, otherwise press anyother number for iterative function: ");
        int recursive = sc.nextInt();

        test.calculateErrors();
        long start1 = System.nanoTime();
        if (recursive != 1)
            test.findICA();
        else {
            System.out.println("The Optimal cost using Recursion is: " + test.findCostRecurse(num_points));
            test.printRecursionSegments();
        }
        long end1 = System.nanoTime();

        System.out.println("Running time: " + (double) (end1 - start1) / 1_000_000_000 + " seconds");
        sc.close();
    }

}
