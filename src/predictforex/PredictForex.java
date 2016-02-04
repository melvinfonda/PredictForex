/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package predictforex;

import java.io.FileNotFoundException;

/**
 *
 * @author Melvin
 */
public class PredictForex {
    private static String[][] forexPairData;

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        ForexFileReader tes = new ForexFileReader();
        forexPairData = tes.forexPrice("tes.csv");
        tes.printMatrix(forexPairData);
    }
    
}
