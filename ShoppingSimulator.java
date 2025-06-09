/* Purpose: Shopping Simulator incorporating reading from and writing to file, formatted the invoice
 * checked for invalid values, added a feature for returning the change if the customer chooses to pay in cash and added discount codes
 */

import java.io.*;
import java.util.Scanner;

public class ShoppingSimulator {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        // calls the method which calls all other methods to execute the task
        callMethods();
    } // main method ends here

    public static void callMethods() throws IOException {
        // calls all other methods

        String[] items = readInventoryNameFromFile();
        String[] stringPrices = readInventoryPricesFromFile();
        double[] prices = convertStrArrToDoubleArr(stringPrices);
        writeCombinedInventory(items, stringPrices);
        printInventory(items, prices);
        int numberItems = getNumItems();
        String itemsNamesBasket[] = getNames(numberItems, items);
        int[] quantities = getQuantity(numberItems, itemsNamesBasket);
        double[] userPrices = userPricesArray(numberItems, itemsNamesBasket, prices, items);

        double finalCost = totalCost(numberItems, itemsNamesBasket, userPrices, quantities);
        double grossTotal = roundTotal(finalCost);
        double tax = calculateTax(grossTotal);
        double subTotal = subTotal(grossTotal, tax);

        int discountIndex = checkDiscountCode();
        double discountPercentage = findDiscount(discountIndex);

        double discountedTotal = discountedTotal(subTotal, discountPercentage);
        double finalDiscount = finalDiscountCalculator(discountedTotal, subTotal);

        double netTotal = netTotal(subTotal, finalDiscount);

        System.out.println("Your Net-total is " + netTotal);

        String paymentMethod = checkCash(netTotal);
        noCash(paymentMethod);
        double change = yesCash(paymentMethod, netTotal);

        printInvoice(numberItems, itemsNamesBasket, quantities, userPrices, grossTotal, finalDiscount,
                change, tax,
                netTotal, subTotal);
    } // method ends here

    public static String[] readInventoryNameFromFile() throws IOException {
        // reads the names of items from a .txt file

        String path = ".\\inventoryItems.txt";
        String[] inventoryNames = Library.fileRead(path, 10);
        return inventoryNames;
    } // method ends here

    public static String[] readInventoryPricesFromFile() throws IOException {
        // reads the prices of items from a .txt file

        String path = ".\\inventoryPrices.txt";
        String[] inventoryPrices = Library.fileRead(path, 10);
        return inventoryPrices;
    } // method ends here

    public static double[] convertStrArrToDoubleArr(String[] StrArray) {
        // converts the string array received as a parameter to a double array

        double[] doubleArray = new double[StrArray.length];
        for (int index = 0; index < StrArray.length; index++) {
            doubleArray[index] = Double.valueOf(StrArray[index]);
        } // for loop ends here
        return doubleArray;
    } // method ends here

    public static void writeCombinedInventory(String[] items, String[] prices) throws IOException {
        // combines the 2 string arrays received as parameters, stores their information
        // in a single array and writes that array to file

        String[] finalInventory = new String[items.length];
        String path = ".\\combinedInventory.txt";
        for (int index = 0; index < items.length; index++) {
            finalInventory[index] = items[index] + "\t" + prices[index];
        }
        Library.fileWrite(path, finalInventory);
    } // method ends here

    public static void printInventory(String itemsPrint[], double pricesPrint[]) {
        // prints the welcome greeting and inventory (item names and prices) received as
        // parameter

        System.out.println("Welcome to the Shopping Centre");
        System.out.println("Item name:" + "\t" + "\t" + "Price: ");
        System.out.println("_____________________");

        for (int index = 0; index < 10; index++) {
            System.out.println(itemsPrint[index] + "\t\t" + pricesPrint[index]);
        } // for loop ends here
    } // method ends here

    public static int getNumItems() {
        // asks the user for the number of distinct items they would like to buy

        System.out.println("Number of distinct items: ");
        String numberOfItems = input.next(); // temporarily accepting the user input as string in order to validate for
                                             // positive integers, converted type later

        if (checkInteger(numberOfItems) && Integer.valueOf(numberOfItems) < 10) { // validate for only positive integer
                                                                                  // values
            return (Integer.valueOf(numberOfItems));
        } else {
            System.out.println("Not in range or not an integer. Exiting code");
            System.exit(0);
            return 0; // does not matter what I return since this line does not execute because the
                      // code terminates
        }
    } // method ends here

    public static boolean checkInteger(String str) {
        // receives a string as the parameter and checks if the string is an integer

        if (str.isEmpty())
            return false;

        for (int index = 0; index < str.length(); index++) {
            if (!Character.isDigit(str.charAt(index))) {
                return false;
            }
        }
        return true;
    } // method ends here

    public static String[] getNames(int numItems, String[] itemsArr) {
        // asks the user to input the items they require, stores them in a string array
        // and returns the array

        String[] basket = new String[numItems];

        for (int index = 0; index < numItems; index++) {
            boolean validItem = false;

            while (!validItem) { // validating if the item entered by user is in the inventory
                System.out.println("Enter item name:");
                String item = input.next();

                for (int index2 = 0; index2 < itemsArr.length; index2++) {
                    if (itemsArr[index2].equalsIgnoreCase(item)) {
                        basket[index] = itemsArr[index2];
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

    public static int[] getQuantity(int numItems, String[] itemsNames) {
        // asks the user for the quantity of each item they requested

        String quantityTemp[] = new String[numItems]; // temporarily accepting the user input as string in order to
                                                      // validate
        // for
        // positive integers, converted type later
        for (int index = 0; index < numItems; index++) {
            System.out.println("How many " + itemsNames[index] + "s do you want?");
            quantityTemp[index] = input.next();
            if (checkInteger(quantityTemp[index])) {
            } else {
                System.out.println("Not a positive Integer. Exiting code");
                System.exit(0); //
            }
        } // for loop ends here
        int quantity[] = Library.convertStrArrToIntArr(quantityTemp);
        return quantity;
    } // method ends here

    public static double[] userPricesArray(int numItems, String[] itemsNames, double[] prices, String[] items) {
        // returns a double array with the prices of only the products selected by the
        // user given by the number of items, item names, prices and the selected items
        // array received as parameters

        double[] userPrices = new double[numItems];

        for (int index1 = 0; index1 < items.length; index1++) {
            for (int index2 = 0; index2 < itemsNames.length; index2++) {
                if (itemsNames[index2].equalsIgnoreCase(items[index1])) {
                    userPrices[index2] = prices[index1];
                    // break;
                }
            }
        }
        return userPrices;
    } // method ends here

    public static double totalCost(int n, String[] itemsNames, double[] prices, int[] quantities) {
        // calculates the total cost by multiplying each individual item's price with
        // its quantity and adding it

        double cost = 0.0;
        for (int index1 = 0; index1 < itemsNames.length; index1++) {
            cost += (prices[index1] * quantities[index1]);
        } // for loop ends here
        return cost;
    } // method ends here

    public static double roundTotal(double finalcost) {
        // rounds the total

        double total;
        finalcost = Math.round(finalcost / 0.01) * 0.01;
        total = finalcost;
        return total;
    } // method ends here

    public static int checkDiscountCode() throws IOException {
        // reads the discount codes from a file and validates the input received from
        // user, returns the index of the code

        System.out.println("Enter discount code (Enter none if you don't have any) ");
        String checkCode = input.next();
        String path = ".\\discountCodes.txt";
        String discountCodes[] = Library.fileRead(path, 3);
        for (int index = 0; index < 3; index++) {
            if (checkCode.equalsIgnoreCase(discountCodes[index])) {
                System.out.println("You are eligible for a discount!");
                return index;
            }
        }
        return 3; // 0 discount
    } // method ends here

    public static int findDiscount(int discountIndex) throws IOException {
        // receives the index of the discount code as its parameter and reads the
        // discount value from a file, then returns the discount value as an int

        String path = ".\\discountAmounts.txt";
        String[] strDiscountArray = Library.fileRead(path, 4);
        int[] intDiscountArray = Library.convertStrArrToIntArr(strDiscountArray);
        return intDiscountArray[discountIndex];
    } // method ends here

    public static double calculateTax(double cost) {
        // calculates the tax and rounds it

        double tax = 0.13 * cost;
        return tax;
    } // method ends here

    public static double discountedTotal(double subTotalMethod, double discountAmount) {
        // returns the discounted total by receiving the discount and the total amount
        // as parameters

        double doubleDiscountAmount = Double.valueOf(discountAmount);
        double discountedTotal = (1 - doubleDiscountAmount / 100) * (subTotalMethod);
        return discountedTotal;
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

    public static double netTotal(double subtotal, double discount) {

        double netTotal = subtotal - discount;

        return netTotal;
    }

    public static double yesCash(String paymentMethod, double netTotal) {
        // calculates the change to be returned to the customer if they choose to pay
        // using cash

        double change = 0;
        if (paymentMethod.equalsIgnoreCase("Yes")) {
            String bill1;
            double bill = 0;
            do { // validation to ensure that the bill entered is greater than the cost
                System.out.println("Enter the bill: ");

                bill1 = input.nextLine();
                bill = Double.valueOf(bill1);

            } while (bill < netTotal); // do while loop ends here
            change = bill - netTotal;
        }
        return change;
    } // method ends here

    public static void noCash(String paymentMethod) {
        // prints a message for the user if they choose not to pay with cash
        if (paymentMethod.equalsIgnoreCase("no")) {
            System.out.println("Pay using credit/debit card");
        }
    } // method ends here

    public static double finalDiscountCalculator(double discountedTotal, double subTotalMethod) {
        // returns the numeric value of discount by subtracting the discounted value
        // from the subtotal
        double finalDiscount = subTotalMethod - discountedTotal;
        return finalDiscount;
    } // method ends here

    public static double subTotal(double grossTotal, double tax) {
        // calculate the subtotal by adding tax to the grosstotal
        double subtotal = grossTotal + tax;
        return subtotal;
    }

    public static void printInvoice(int numItems, String[] basket, int[] quantities, double[] prices,
            double grossTotal, double finalDiscount, double change, double tax, double netTotal, double subTotal)
            throws IOException {
        // prints the invoice in a .txt file by receiving all the relevant information
        // as parameters

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
        Library.writeLine("Gross Total" + "\t\t\t\t" + String.valueOf(grossTotal), path);
        Library.writeLine("Tax" + "\t\t\t\t\t" + String.valueOf(tax), path);
        Library.writeLine("Sub-Total" + "\t\t\t\t" + String.valueOf(subTotal), path);
        Library.writeLine("Discount" + "\t\t\t\t" + String.valueOf(finalDiscount), path);
        Library.writeLine("Net-Total" + "\t\t\t\t" + String.valueOf(netTotal), path);
        Library.writeLine("Your change is " + "\t\t\t\t" + String.valueOf(change), path);
        Library.writeLine("Thank you for shopping with us today!", path);
        System.out.println("Please find your invoice in the folder");
        Library.automaticFileOpening(path);
    } // method ends here

}