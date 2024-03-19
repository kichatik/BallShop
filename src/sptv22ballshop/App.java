package sptv22ballshop;

import entity.Ball; 
import managers.UserManager;
import managers.PurchaseManager;
import managers.BallManager; 
import managers.DatabaseManager;
import entity.Purchase;
import entity.User;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import tools.InputProtection;
import tools.PassEncrypt;


public class App {
    private final DatabaseManager databaseManager;
    public static User user;

    public static enum ROLES { ADMINISTRATOR, MANAGER, USER };
    private Scanner scanner = new Scanner(System.in);
    private final BallManager ballManager; 
    private final PurchaseManager purchaseManager;
    private final UserManager userManager;
    
    public App() {
       this.scanner = new Scanner(System.in);
       this.databaseManager = new DatabaseManager();
       this.ballManager = new BallManager(scanner, databaseManager); 
       this.purchaseManager = new PurchaseManager(scanner, ballManager, databaseManager); 
       this.userManager = new UserManager(scanner, databaseManager);
    }
    
    void run() throws ParseException {
        checkAdmin();
        System.out.println("If you have a login and password press y, otherwise press n");
        String word = scanner.nextLine();
        if("n".equals(word)){
            databaseManager.saveUser(userManager.addUser());
        }
        for(int n=0;n<3;n++){
            System.out.print("Please enter your login: ");
            String login = scanner.nextLine();
            System.out.print("Please enter your password: ");
            String password = scanner.nextLine().trim();
            PassEncrypt pe = new PassEncrypt();
            String encryptPassword = pe.getEncryptPassword(password, pe.getSalt());
            App.user = databaseManager.authorization(login, encryptPassword);
            if(App.user != null){
                break;
            }
            System.out.println("Invalid login or password");
        }
        if(App.user == null) return;
        System.out.printf("Hello %s %s, welcome to the library%n",App.user.getBuyer().getFirstname(),App.user.getBuyer().getLastname());
        boolean repeat = true;
        System.out.println("------- Ball Shop -------");
        do {
            System.out.println("List tasks");
            System.out.println("0. Exit");
            System.out.println("1. Add new product");
            System.out.println("2. Add new client");
            System.out.println("3. Buy product");
            System.out.println("4. Show all products");
            System.out.println("5. Show all clients");
            System.out.println("6. Add money to clients account");
            System.out.println("7. how many times the product was sold");
            System.out.println("8. who bought for a large amount");
            System.out.println("9. Shop income");
            System.out.println("10. Delete account");
            System.out.println("11. Product editing");
            System.out.println("12. User editing");
            System.out.println("13. Set ball discount");
            System.out.println("Enter task number: ");
            int task = InputProtection.intInput(0,13); 
            System.out.printf("You select task %d, for exit press \"0\", to continue press \"1\": ",task);
            int toContinue = InputProtection.intInput(0,1);
            if(toContinue == 0) continue;
            switch (task) {
                case 0:
                    repeat = false;
                    break;
                case 1:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    databaseManager.saveBall(ballManager.addBall()); 
                    break;
                case 2:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    databaseManager.saveUser(userManager.addUser());
                    break;
                case 3:
                    databaseManager.savePurchase(purchaseManager.BuyBall(scanner, App.user)); 
                    break;
                case 4:
                    ballManager.printListBall();
                    break;
                case 5:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    userManager.printListUsers();
                    break;
                case 6:
                    userManager.increaseBalance(App.user);
                    break;
                case 7:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    purchaseManager.boughtBalls(); 
                    break;
                case 8:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    purchaseManager.buyersByExpenses();
                    break;
                case 9:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    purchaseManager.Income();
                    break;
                case 10:
                    userManager.deleteAccount(App.user);
                    break;
                case 11:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    ballManager.changeProduct();
                    break;
                case 12:
                    if(!App.user.getRoles().contains(App.ROLES.ADMINISTRATOR.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    userManager.changeRole();
                    break;
                default:
                    System.out.println("Select task from list");
                case 13:
                    if(!App.user.getRoles().contains(App.ROLES.MANAGER.toString())){
                        System.out.println("You Don't have permission");
                        break;
                    }
                    ballManager.addDiscount(); 
                    break;
            }
        } while (repeat);
        
    }
    private void checkAdmin() {
        if(!(databaseManager.getListUsers().size()>0)){
            User admin = new User();
            admin.setUsername("admin");
            PassEncrypt pe =  new PassEncrypt();
            admin.setPassword(pe.getEncryptPassword("12345", pe.getSalt()));
            admin.getRoles().add(App.ROLES.ADMINISTRATOR.toString());
            admin.getRoles().add(App.ROLES.MANAGER.toString());
            admin.getRoles().add(App.ROLES.USER.toString());
            Buyer buyer = new Buyer();
            buyer.setFirstname("Anzhelika");
            buyer.setLastname("Kichatova");
            buyer.setBalance(10000);
            admin.setBuyer(buyer);
            databaseManager.saveUser(admin);
        }
    }
}  
