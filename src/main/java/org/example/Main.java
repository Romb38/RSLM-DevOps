package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
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