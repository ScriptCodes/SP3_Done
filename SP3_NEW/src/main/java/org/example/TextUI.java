package org.example;
import java.util.Scanner;
/**
 * @author Mads, Kevin, Daniel
 * This class is an ui class containing getters and display method
 * to get rid of println throughout the program
 */
public class TextUI {
    private Scanner scan = new Scanner(System.in);

    /**
     * Method to display a message to the user
     * @param msg return a String msg. String name could be anything as long as it is a String
     * @return
     */
    public String displayMsg(String msg){
        System.out.println(msg);
        return msg;

    }
    /**
     * Following methods are getters
     * used to get input from the user
     * @param input
     * @return return the input of the user
     */
    public String getInput(String input){

        return scan.nextLine();
    }

    public int getIntInput(String msg){
        System.out.println(msg);
        int result = scan.nextInt();
        scan.nextLine();
        return result;
    }
}
