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
    public static final int row =30000;
    public static final int column =7;
        
    //turn csv file to raw data    
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
                
                while (((line = br.readLine()) != null) && (!",,,,,,".equals(line))) {
                    // use comma as separator
                    String[] value = line.split(cvsSplitBy);
                    for (int y=0;y<7;y++)
                    {
                        rawForexData[i][y] = value[y];
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
        
    public static void printMatrix(String[][] matrix)
    {
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < 3; y++) {
                if(matrix[x][y]!=null)
                    System.out.print("matriks ke-" +x+y+ "= " +matrix[x][y] + " ");
            }
            if(matrix[x][0]!=null)
                System.out.print("\n");
        }
    }
    
    //to take close price from the raw matrix data and turn the type from string to double
    public static double[] stringToDoubleClose (String[][] rawForexPrice)
    {
       double[] close = new double[row];
 
       for (int x = 0; x < row; x++) {
           //close price located on column 5
            if(rawForexPrice[x][5]!=null){
                close[x] = Double.parseDouble(rawForexPrice[x][5]);
            }
        }
       return close;
    }
}
