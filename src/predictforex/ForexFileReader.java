/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.*;
import java.util.*;

/**
 *
 * @author Melvin
 */
public class ForexFileReader {
    private int j;
    private int i;
    private int row =30000;
    private int column =7;
        
        
    public String[][] forexPrice(String csvFile) {
        String[][] forexData = new String[row][column];
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        i=0;
        try {
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null && i<30000) {
                    // use comma as separator
                    String[] value = line.split(cvsSplitBy);   
                    for (int j = 0; j < 7; j++) { // For each token in the line that we've read:
                        forexData[i][j] = value[j]; // Place the token into the 'i'th "column"
                    }
                    i++;
                }
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return forexData;
    }
        
    public void printMatrix(String[][] matrix)
    {
        System.out.println("tes : " + matrix[1][0]);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if(matrix[i][j]!=null)
                    System.out.print(matrix[i][j] + " ");
            }
            if(matrix[i][j]!=null)
                System.out.print("\n");
        }
    }
}
