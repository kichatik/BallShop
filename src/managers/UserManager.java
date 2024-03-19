package managers;

import entity.Buyer;
import entity.User;
import java.util.EnumSet;
import java.util.List;
import java.util.Scanner;
import sptv22ballshop.App;
import tools.PassEncrypt;

public class UserManager {
    
    private final Scanner scanner;
    private final DatabaseManager databaseManager;
    
    public UserManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public User addUser() {
        System.out.println("-----Add user-----");
        Buyer buyer = new Buyer();
        User user = new User();
        System.out.println("Enter Firstname:");
        buyer.setFirstname(scanner.nextLine());
        System.out.println("Enter Lastname:");
        buyer.setLastname(scanner.nextLine());
        System.out.println("Enter your balance:");
        buyer.setBalance(scanner.nextDouble());
        user.setBuyer(buyer);
        scanner.nextLine();
        
        System.out.println("Enter Username:");
        user.setUsername(scanner.nextLine());
        System.out.println("Enter Password:");
        String password = scanner.nextLine();
        PassEncrypt pe = new PassEncrypt();
        String encryptPassword = pe.getEncryptPassword(password, pe.getSalt());
        user.setPassword(encryptPassword);

        return user;
    }

    public void printListUsers() {
        List<User> users = getDatabaseManager().getListUsers();
        System.out.println("----- Buyer list -----");
        for (int i = 0; i < users.size(); i++) {
            System.out.println(i+1 + ". Client: " + users.get(i).getBuyer().getFirstname() + " " 
        + users.get(i).getBuyer().getLastname() + " Balance: " + users.get(i).getBuyer().getBalance() + " Euro");
        }
    }
    
    public void deleteAccount(User currentUser) {
    try {
        databaseManager.deleteUser(currentUser);
        System.out.println("Account deleted successfully.");
        System.exit(0);
    } catch (Exception e) {
        System.out.println("User deletion failed: " + e);
    }
}

    public void increaseBalance(User currentUser) {
        List<User> users = getDatabaseManager().getListUsers();
        Buyer buyer = currentUser.getBuyer();
        System.out.println("Buyer: " + buyer.getFirstname() + " " + buyer.getLastname() + " Balance: " + buyer.getBalance());
        
        System.out.println("How much money do you want to add to the balance?");
        double amount = scanner.nextDouble();
        
        double currentBalance = buyer.getBalance();
        double newBalance = currentBalance + amount;
        buyer.setBalance(newBalance);
        
        getDatabaseManager().saveUser(currentUser);

        System.out.println("Increased balance for " + buyer.getFirstname() + " " + buyer.getLastname() + " by " + amount + " Euro");
        System.out.println("Now the balance for " + buyer.getFirstname() + " " + buyer.getLastname() + " is " + buyer.getBalance() + " Euro");
    }
    
    public void changeRole() {
        List<User> users = getDatabaseManager().getListUsers();
        printListUsers();
        System.out.println("1. Delete role:");
        System.out.println("2. Add role:");
        int chooseNumber = scanner.nextInt();
       
        
        System.out.println("Choose the number of the user whose role you want to change:");
        int number = scanner.nextInt(); 
        scanner.nextLine();
        
        if (number >= 1 && number <= users.size()) {
            User selectedUser = users.get(number - 1);

            System.out.println("Enter the new role for the user:");
            System.out.println("1. ADMINISTRATOR");
            System.out.println("2. MANAGER");
            System.out.println("3. USER");
            int roleChoice = scanner.nextInt();
            scanner.nextLine();

            switch (roleChoice) {
                case 1:
                    selectedUser.getRoles().add(App.ROLES.ADMINISTRATOR.toString());
                    break;
                case 2:
                    selectedUser.getRoles().add(App.ROLES.MANAGER.toString());
                    break;
                case 3:
                    selectedUser.getRoles().add(App.ROLES.USER.toString());
                    break;
                default:
                    System.out.println("Invalid role choice. Role not changed.");
                    return;
            }

            getDatabaseManager().saveUser(selectedUser);

            System.out.println("Role changed successfully for user: " + selectedUser.getUsername());
        } else {
            System.out.println("Invalid user number. Please choose a valid number.");
        }
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}