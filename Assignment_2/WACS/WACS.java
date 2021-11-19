/**
* Finding weighted approximate best common sub-string.
* With interactive options to input custom strings to generate custom strings using SALT characters. 
* Also, implemented taking weight of each chracater as either 1 or taking frequnecy of each letter of the alphabet in the english dictionary.
* Time Complexity: O(n*m) where n is the length of string A and m is the length of string B.
* 
* @author  Vyom Pathak
* @version 1.0
* @since   2021-11-18 
*/
package WACS;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class WACS {
    private static int max = 30000;
    private HashMap<Character, Integer> weights = new HashMap<Character, Integer>() {
        {
            put('A', 43);
            put('B', 10);
            put('C', 23);
            put('D', 17);
            put('E', 56);
            put('F', 9);
            put('G', 12);
            put('H', 15);
            put('I', 38);
            put('J', 1);
            put('K', 5);
            put('L', 27);
            put('M', 15);
            put('N', 33);
            put('O', 36);
            put('P', 16);
            put('Q', 1);
            put('R', 38);
            put('S', 29);
            put('T', 35);
            put('U', 18);
            put('V', 5);
            put('W', 6);
            put('X', 1);
            put('Y', 9);
            put('Z', 1);
        }
    };
    private String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Integer memo[][] = new Integer[max][max];
    private String A, B;
    private int len_A, len_B;
    private int delta = 0;
    private int final_weight = -100_000_000;
    private int end_points[] = { 0, 0 };
    private Boolean equiweight;

    // Constructor
    public WACS(int len_A, int len_B, int delta, Boolean equiweight, int read_string, String strA, String strB) {
        this.equiweight = equiweight;
        this.len_A = len_A + 1;
        this.len_B = len_B + 1;
        if (!this.equiweight && delta == -1)
            this.delta = (int) (new Random().nextFloat() * 56);
        else
            this.delta = delta;
        if (read_string != 1) {
            Random rnd = new Random();
            int i = len_A;
            int j = len_B;
            StringBuilder salt = new StringBuilder();
            while (i > 0) {
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
                i--;
            }
            A = salt.toString();
            salt = new StringBuilder();
            while (j > 0) {
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
                j--;
            }
            B = salt.toString();
        } else {
            A = strA;
            B = strB;
        }
        System.out.print("String A is : " + A + "\n" + "String B is : " + B);
        System.out.println();
        for (int i = 0; i < this.len_A; i++) {
            for (int j = 0; j < this.len_B; j++) {
                memo[i][j] = 0;
            }
        }
    }

    public Boolean findWACS() {
        System.out.println("The penalty is : " + delta);
        for (int i = 1; i < len_A; i++) {
            for (int j = 1; j < len_B; j++) {
                if (A.charAt(i - 1) == B.charAt(j - 1)) {
                    memo[i][j] = memo[i - 1][j - 1];
                    if (equiweight)
                        memo[i][j] += 1;
                    else
                        memo[i][j] += weights.get(A.charAt(i - 1));
                } else
                    memo[i][j] = Math.max(0, memo[i - 1][j - 1] - delta);
                if (memo[i][j] > final_weight) {
                    final_weight = memo[i][j];
                    end_points[0] = i;
                    end_points[1] = j;
                }
            }
        }
        if (final_weight > 0)
            return true;
        return false;
    }

    private void printWACS() {
        System.out.println("The weight of the Common Substring is : " + final_weight);
        String sol = "";
        int i = end_points[0];
        int j = end_points[1];
        while ((i > 0 && j > 0) && (memo[i][j] > 0)) {
            sol = A.charAt(i - 1) + sol;
            i--;
            j--;
        }
        System.out.println("The Common Substring is : " + sol);
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int len_A = 10;
        int len_B = 10;
        int delta = -1;
        int fl = -1;
        String A = "";
        String B = "";
        Boolean equiweight = false;
        int read_string = -1;
        System.out.println(
                "If you want to read strings A and B press 1 else press anyother number to make random strings: ");
        read_string = sc.nextInt();
        if (read_string == 1) {
            System.out.println("Enter String A and String B from capital alphabets separated by newline: ");
            sc.nextLine();
            A = sc.nextLine();
            B = sc.nextLine();
            len_A = A.length();
            len_B = B.length();
        } else {
            System.out.println("Enter the size of string A");
            len_A = sc.nextInt();
            System.out.println("Enter the size of string B");
            len_B = sc.nextInt();
        }
        System.out.println("Enter 1 for making weights of characters equal else enter any number");
        fl = sc.nextInt();
        if (fl == 1) {
            System.out.println("Enter penalty value for equiweight case");
            delta = sc.nextInt();
            equiweight = true;
        }

        WACS test = new WACS(len_A, len_B, delta, equiweight, read_string, A, B);
        long start1 = System.nanoTime();
        Boolean flag = test.findWACS();
        if (flag)
            test.printWACS();
        else
            System.out
                    .println("No Weighted Approximate Common Substring found for strings: \n" + test.A + "\n" + test.B);
        long end1 = System.nanoTime();

        System.out.println("Running time: " + (double) (end1 - start1) / 1_000_000_000 + " seconds");
        sc.close();
    }

}
