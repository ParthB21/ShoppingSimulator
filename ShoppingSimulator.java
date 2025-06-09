/* Purpose: Shopping Simulation incorporates reading from and writing to file, formatted the invoice
 * checked for invalid values, added a feature for returning the change if the customer chooses to pay in cash
 */

import java.io.*;
import java.util.Scanner;

public class ShoppingSimulator {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // the main method calls the method which calls all other methods to execute the
        // code

        callMethods();
    } // main method ends here

    public static void callMethods() throws IOException {
        // this method is used to call all other methods

        String[] items = readInventoryNameFromFile();
        String[] stringPrices = readInventoryPricesFromFile();
        double[] prices = convertStrArrToDoubleArr(stringPrices);
        printInventory(items, prices);
        int n1 = getNumItems();
        String itemsNamesBasket[] = getNames(n1, items);
        int[] quantities = getQuantity(n1, itemsNamesBasket);
        double finalcost1 = totalCost(n1, itemsNamesBasket, prices, items, quantities);
        double roundedTotal = roundsTotal(finalcost1);
        double tax = calculateTax(roundedTotal);
        double grandTotal = roundedTotal + tax;
        String paymentMethod = checkCash(grandTotal);
        double change = yesCash(paymentMethod, grandTotal);
        noCash(paymentMethod);
        printInvoice(n1, itemsNamesBasket, quantities, prices, roundedTotal, String.valueOf(change), tax, grandTotal);
    } // method ends here

    public static String[] readInventoryNameFromFile() throws IOException {
        // this method reads the names of items from a .txt file

        String path = ".\\inventoryItems.txt";
        String[] inventoryNames = Library.fileRead(path, 10);
        return inventoryNames;
    } // method ends here

    public static String[] readInventoryPricesFromFile() throws IOException {
        // this method reads the prices of items from a .txt file

        String path = ".\\inventoryPrices.txt";
        String[] inventoryPrices = Library.fileRead(path, 10);
        return inventoryPrices;
    } // method ends here

    public static double[] convertStrArrToDoubleArr(String[] StrArray) {

        // this method converts the string array received as a parameter to a double
        // array
        double[] doubleArray = new double[StrArray.length];
        for (int index = 0; index < StrArray.length; index++) {
            doubleArray[index] = Double.valueOf(StrArray[index]);
        } // for loop ends here
        return doubleArray;
    } // method ends here

    public static void printInventory(String itemsprint[], double pricesprint[]) {

        // this method prints the welcome greeting and inventory (item names and prices)
        System.out.println("Welcome to the Shopping Centre");
        System.out.println("Item name:" + "\t" + "\t" + "Price: ");
        System.out.println("_____________________");

        for (int index = 0; index < 10; index++) {
            System.out.println(itemsprint[index] + "\t\t" + pricesprint[index]);
        } // for loop ends here
    } // method ends here

    public static int getNumItems() {

        // asks the user for the number of distinct items they would like to buy
        System.out.println("Number of distinct items: ");
        String numberOfItems = input.next(); // temporarily accepting the user input as string in order to validate for
                                             // positive integers, converted type later

        if (checkInteger(numberOfItems)) { // validate for only positive integer values
            return (Integer.valueOf(numberOfItems));
        } else {
            System.out.println("Not a positive Integer. Exiting code");
            System.exit(0);
            return 0; // does not matter what I return since this line does not execute because the
                      // code terminates
        }
    } // method ends here

    public static boolean checkInteger(String str) {

        // receives a string as the parameter and checks if the string is an integer
        if (str.isEmpty())
            return false; // empty input is invalid

        for (int i = 0; i < str.length(); i++) { // for loop to check at every value of the string, and not just the
                                                 // first value
            if (!Character.isDigit(str.charAt(i))) { // if a non-digit is found, return false
                return false;
            }
        }
        return true; // if all characters are digits, return true
    }

    public static String[] getNames(int n, String[] itemsArr) {

        // asks the user to input the items they require, stores them in a string array
        // and returns the array
        String[] basket = new String[n];

        for (int index = 0; index < n; index++) {
            boolean validItem = false;

            while (!validItem) { // validating if the item entered by user is in the inventory
                System.out.println("Enter item name:");
                String item = input.next();

                for (int i = 0; i < itemsArr.length; i++) {
                    if (itemsArr[i].equalsIgnoreCase(item)) {
                        basket[index] = item;
                        validItem = true;
                        break; // Exit the loop if a match is found
                    }
                } // for loop ends here
                if (!validItem) {
                    System.out.println("Invalid item. Please enter a valid item from the inventory.");
                }
            } // while loop ends here
        } // for loop ends here
        return basket;
    } // method ends here

    public static int[] getQuantity(int n, String[] itemsNames) {
        // asks the user for the quantity of each item they requested

        String quantity[] = new String[n]; // temporarily accepting the user input as string in order to validate for
                                           // positive integers, converted type later
        for (int index = 0; index < n; index++) {
            System.out.println("How many " + itemsNames[index] + "s do you want?");
            quantity[index] = input.next();
            if (checkInteger(quantity[index])) {
            } else {
                System.out.println("Not a positive Integer. Exiting code");
                System.exit(0); //
            }
        } // for loop ends here
        int quantityInt[] = Library.convertStrArrToIntArr(quantity);
        return quantityInt;
    } // method ends here

    public static double totalCost(int n, String[] itemsNames, double[] prices, String[] items, int[] quantities) {

        // calculating the total cost by multiplying each individual item's price with
        // its quantity and adding it
        double cost = 0.0;
        for (int index1 = 0; index1 < itemsNames.length; index1++) {
            for (int index2 = 0; index2 < items.length; index2++) {
                if (items[index2].equalsIgnoreCase(itemsNames[index1])) {
                    cost += (prices[index1] * quantities[index1]);
                }
            } // second for loop ends here
        } // first for loop ends here
        return cost;
    } // method ends here

    public static double roundsTotal(double finalcost) {
        // rounds the total

        double total;
        finalcost = Math.round(finalcost / 0.01) * 0.01;
        total = finalcost;
        return total;
    } // method ends here

    public static double calculateTax(double cost) {
        // calculates the tax and rounds it

        double tax = 0.13 * cost;
        tax = Math.round(tax / 0.01) * 0.01;
        return tax;
    } // method ends here

    public static String checkCash(double roundedTotal) {
        // asks the user if they want to pay using cash

        String paymentMethod;
        do { // validating and asking the user again if their response is anything other than
             // yes or no
            System.out.println("Do you want to pay using cash? (Yes/No)");
            input.nextLine(); // to ensure the reader reads the next line and does not continue on the same
                              // line
            paymentMethod = input.nextLine();

            if (!paymentMethod.equalsIgnoreCase("Yes") && !paymentMethod.equalsIgnoreCase("No")) {
                System.out.println("Invalid input. Please enter 'Yes' or 'No'.");
            }
        } while (!paymentMethod.equalsIgnoreCase("Yes") && !paymentMethod.equalsIgnoreCase("No"));
        return paymentMethod;
    } // method ends here

    public static double yesCash(String paymentMethod, double roundedTotal) {
        // calculates the change to be returned to the customer if they choose to pay
        // using cash

        double change = 0;
        if (paymentMethod.equalsIgnoreCase("Yes")) {
            double bill;
            do { // validation to ensure that the bill entered is greater than the cost
                System.out.println("Enter the bill: ");
                bill = input.nextInt();
            } while (bill < roundedTotal); // do while loop ends here

            change = bill - roundedTotal;
        }
        return change;
    } // method ends here

    public static void noCash(String paymentMethod) {
        // prints a message for the user if they choose not to pay with cash
        if (paymentMethod.equalsIgnoreCase("no")) {
            System.out.println("Pay using credit/debit card");
        }
    } // method ends here

    public static void printInvoice(int numItems, String[] basket, int[] quantities, double[] prices,
            double finalcost, String change, double tax, double grandTotal) throws IOException {
        // method to print the invoice in a .txt file

        String path = ".\\invoice.txt";
        String[] finalStringArray = new String[numItems + 2]; // adding 8 to the array length because of the headings
                                                              // and extra stuff I am printing
        finalStringArray[0] = ("\t" + "************Invoice************");
        finalStringArray[1] = ("Quantities" + "\t" + "Price" + "\t" + "Quantities" + "\t" + "Sub-Total");

        for (int index = 2; index < numItems + 2; index++) { // filling the array with the item, price, quantity and
                                                             // sub-total
            finalStringArray[index] = basket[index - 2] + "\t" + prices[index - 2] + "\t" + quantities[index - 2]
                    + "\t\t" + (prices[index - 2] * quantities[index - 2]);
        } // for loop ends here

        Library.fileWrite(path, finalStringArray);
        Library.writeLine("***************************************************", path);
        Library.writeLine(" ", path); // adding a blank to the array to leave a line
        Library.writeLine("Sub-Total" + "\t\t\t\t" + String.valueOf(finalcost), path);
        Library.writeLine("Tax" + "\t\t\t\t\t" + String.valueOf(tax), path);
        Library.writeLine("Total" + "\t\t\t\t\t" + String.valueOf(grandTotal), path);
        Library.writeLine("Your change is " + "\t\t\t\t" + change, path);
        Library.writeLine("Thank you for shopping with us today!", path);
        System.out.println("Please find your invoice in the folder");
    }
}