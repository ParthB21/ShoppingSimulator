/* Name: Parth Bhasin
 * Date: February 28 2025
 * Purpose: This library creates frequently used methods so I can call them from this class*/


import java.io.*;
import java.awt.*;
public class Library {

    public static String[] fileRead(String filePath, int lines) throws IOException {
        // reads from a .txt file whose path and number of lines to be read are received as a parameter

        BufferedReader readFile;
        readFile = new BufferedReader(new FileReader(filePath));
        String[] dataFrFile = new String[lines];
        for (int index = 0; index < lines; index++) {
            dataFrFile[index] = readFile.readLine();
        }
        readFile.close();
        return dataFrFile;
    } // method ends here

    public static void fileWrite(String filePath, String[] textToBeWritten) throws IOException {
        // writes a string array received as a parameter to a .txt file whose path is received as a parameter 

        BufferedWriter writeFile;
        writeFile = new BufferedWriter(new FileWriter(filePath));
        for (int index = 0; index < textToBeWritten.length; index++) {
            writeFile.write(textToBeWritten[index] + "\n");
        }
        writeFile.close();
    } // method ends here

    public static void fileWriteInt(String filePath, int[] intArrToBeWritten) throws IOException {
        // writes a string array received as a parameter to a .txt file whose path is received as a parameter 

        BufferedWriter writeFile;
        writeFile = new BufferedWriter(new FileWriter(filePath));
        for (int index = 0; index < intArrToBeWritten.length; index++) {
            writeFile.write(Integer.toString(intArrToBeWritten[index])+"\n");
        }
        writeFile.close();
    } // method ends here

    public static void writeLine(String text, String filePath) throws IOException {
        // writes a single line to the .txt file whose path is received as parameter, it appends the line and does not overwrite 

        BufferedWriter writeFile;
        writeFile = new BufferedWriter(new FileWriter(filePath, true)); // true is added in order to append
        writeFile.write("\n"+text + "\n");
        writeFile.close();
    } // method ends here

    public static double[] convertStrArrToDoubleArr(String[] StrArray) {
        // converts the string array received as parameter to a double array and returns it

        double[] doubleArray = new double[StrArray.length];
        for(int index = 0; index<StrArray.length; index++){
            doubleArray[index] = Double.valueOf(StrArray[index]);
        }
        return doubleArray;
    } // method ends here

    public static int[] convertStrArrToIntArr(String[] StrArray) {
        // converts the string array received as parameter to an integer array and returns it


        int[] intArray = new int[StrArray.length];
        for(int index = 0; index<StrArray.length; index++){
            intArray[index] = Integer.valueOf(StrArray[index]);
        }
        return intArray;
    } // method ends here
    
    public static boolean isInteger(String str) {
        // checks if the string received as a parameter is an integer

        if (str.isEmpty())
            return false; // invalidates empty input

        for (int i = 0; i < str.length(); i++) { //for loop to check at every value of the string, and not just the first value
            if (!Character.isDigit(str.charAt(i))) { // if a non-digit is found, return false
                return false;
            }
        }
        return true; // if all characters are digits, return true
    }

    public static void automaticFileOpening (String fileName) throws IOException{ // inspired by a conversation with Ragison Jegathaasan
        // automatically opens the .txt file whose path is received as a parameter

        File userFile = new File (fileName);
        if (userFile.exists()){
             // Desktop is an inbuilt class in java.awt package
            Desktop desktop = Desktop.getDesktop(); // getDesktop is a method in the Desktop class
            desktop.open(userFile); // open (File userFile) is a method in Desktop class to open the file
        }
    } // method ends here


}