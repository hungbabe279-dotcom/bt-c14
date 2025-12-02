package com.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String choice;
        
        do {
            System.out.println("\n--- Email List Application ---");
            System.out.println("1. Add a user");
            System.out.println("2. View all users");
            System.out.println("3. Update a user");
            System.out.println("4. Delete a user");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            
            choice = sc.nextLine();
            
            switch (choice) {
                case "1":
                    addUser(sc);
                    break;
                case "2":
                    viewAllUsers();
                    break;
                case "3":
                    updateUser(sc);
                    break;
                case "4":
                    deleteUser(sc);
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!choice.equals("5"));
        
        sc.close();
        DBUtil.close();
    }
    
    private static void addUser(Scanner sc) {
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        
        System.out.print("Enter first name: ");
        String firstName = sc.nextLine();
        
        System.out.print("Enter last name: ");
        String lastName = sc.nextLine();
        
        User user = new User(email, firstName, lastName);
        UserDB.insert(user);
        System.out.println("User added successfully!");
    }
    
    private static void viewAllUsers() {
        List<User> users = UserDB.getUsers();
        
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("\n--- All Users ---");
            for (User user : users) {
                System.out.println(user);
            }
        }
    }
    
    private static void updateUser(Scanner sc) {
        System.out.print("Enter user ID to update: ");
        Long userID = Long.parseLong(sc.nextLine());
        
        User user = UserDB.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.print("Enter new email (or press Enter to keep current): ");
        String email = sc.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        
        System.out.print("Enter new first name (or press Enter to keep current): ");
        String firstName = sc.nextLine();
        if (!firstName.isEmpty()) {
            user.setFirstName(firstName);
        }
        
        System.out.print("Enter new last name (or press Enter to keep current): ");
        String lastName = sc.nextLine();
        if (!lastName.isEmpty()) {
            user.setLastName(lastName);
        }
        
        UserDB.update(user);
        System.out.println("User updated successfully!");
    }
    
    private static void deleteUser(Scanner sc) {
        System.out.print("Enter user ID to delete: ");
        Long userID = Long.parseLong(sc.nextLine());
        
        User user = UserDB.getUser(userID);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        UserDB.delete(user);
        System.out.println("User deleted successfully!");
    }
}
