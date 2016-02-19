/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.*;

/**
 *
 * @author Melvin
 */
public class ForexFileReader {
    private int j;
    private int i;
    private final int row =30000;
    private final int column =7;
        
        
    public String[][] forexPrice(String csvFile) {
        String[][] rawForexData = new String[row][column];
        String[][] forexData = new String[row][column];
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ",";
        i=0;
        String Str = new String(":00");
        
        //read from csv file then put in to matrix
        try {
                br = new BufferedReader(new FileReader(csvFile));
                while ((line = br.readLine()) != null && i<30000) {
                    // use comma as separator
                    String[] value = line.split(cvsSplitBy);
                    System.arraycopy(value, 0, rawForexData[i], 0, 7);
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
        
        int x=0;
        int y=0;
        //to take price per hour time
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if(rawForexData[i][j]!=null&&rawForexData[i][1].endsWith(":00"))
                {
                    forexData[x][j] = rawForexData[i][j];
                }
            }
            if(rawForexData[i][j]!=null&&rawForexData[i][1].endsWith(":00"))
                x++;
        }
        
        
        return forexData;
    }
        
    public void printMatrix(String[][] matrix)
    {
        System.out.println("tescacat : " + matrix[1][1]);
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
