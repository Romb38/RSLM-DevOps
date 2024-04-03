package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Scanner;

public class Main {

    /**
     * @brief Main method
     *
     * @details This method is the entry point of the program
     */
    public static void main(String[] args) {
        System.out.println("Hello world! And root");
        System.out.println("Enter your username: ");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        ObjectMapper objectMapper = new ObjectMapper();
//        Object[] values = objectMapper.
        System.out.println("Your username is " + username);
        scanner.close();
    }

    class Dataframe {
        private Object[] val;
        Dataframe(Object[] val) {
            this.val = val;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (this.val != null){
                for(Object value : this.val){
                    builder.append(value.toString());
                }
            }
            return builder.toString();
        }
    }
}