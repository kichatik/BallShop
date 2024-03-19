package managers;

import entity.Buyer;
import entity.Ball;
import entity.Purchase;
import entity.User;
import java.util.HashMap;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PurchaseManager {

    private final Scanner scanner;
    private final BallManager ballManager;
    private final DatabaseManager databaseManager;
    private List<Purchase> ratingForDay;
    private Map<Ball, Integer> ballPurchaseSumMap;
    private List<Purchase> ratingForMonth;
    private List<Purchase> ratingForYear;
    private LocalDate firstDayOfMonth;
    private LocalDate lastDayOfMonth;
    private LocalDate firstDayOfYear;
    private LocalDate lastDayOfYear;
    private LocalDate today = LocalDate.now(); 
    
    public PurchaseManager(Scanner scanner, BallManager ballManager, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.ballManager = ballManager;
        this.databaseManager = databaseManager;
    }
    
    public Purchase BuyBall(Scanner scanner, User currentUser) {
        List<Ball> balls = getDatabaseManager().getListBalls();
        Purchase purchase = new Purchase();
        Buyer buyer = currentUser.getBuyer();
        ballManager.printBallCost(balls);

        System.out.print("Enter ball number from list:");
        int numberBall = scanner.nextInt(); scanner.nextLine();
        purchase.setBall(balls.get(numberBall - 1));

        System.out.println("Buyer: " + buyer.getFirstname() + " " + buyer.getLastname());
        purchase.setBuyer(buyer);

        System.out.println("How many balls do you want to buy");
        int requestedCount = scanner.nextInt(); scanner.nextLine();
        double ballsPrice = requestedCount * balls.get(numberBall - 1).getCost();

        if (buyer.getBalance() >= ballsPrice) {
            double buyerBalance = buyer.getBalance() - ballsPrice;
            buyer.setBalance(buyerBalance);
            purchase.setCount(requestedCount);
            
            purchase.setPurchaseDate(new Date(System.currentTimeMillis()));
            
            System.out.println("Buyer " + buyer.getFirstname() + " bought " + requestedCount + " " 
            + balls.get(numberBall - 1).getName() + " for " + ballsPrice + " Euro. Date: " + purchase.getPurchaseDate());
            
            return purchase;
        } else {
            System.out.println("You don't have enough money");
            return null;
        }
    }

    public void buyersByExpenses() {
        System.out.println("1. Buyer expenses for today");
        System.out.println("2. Buyer expenses for month");
        System.out.println("3. Buyer expenses for year");
        int number = scanner.nextInt(); scanner.nextLine();
        switch (number) {
                case 1:
                    expensesForDay();
                    break;
                case 2:
                    expensesForMonth();
                    break;
                case 3:
                    expensesForYear();
                    break;
                default:
                    System.out.println("Invalid number selected.");
            }
    }    
    
    public void expensesForDay() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        List<User> users = getDatabaseManager().getListUsers();
        Map<Buyer, Double> buyerExpensesMap = new HashMap<>();

        ratingForDay();

        for (User user : users) {
            if (user.getBuyer() != null) {
                Buyer buyer = user.getBuyer();
                double buyerExpenses = 0;

                for (Purchase purchase : ratingForDay) {
                    if (purchase.getBuyer().equals(buyer)) {
                        double purchaseCost = purchase.getCount() * purchase.getFlower().getCost();
                        buyerExpenses += purchaseCost;
                    }
                }
                buyerExpensesMap.put(buyer, buyerExpenses);
            }
        }

        List<Map.Entry<Buyer, Double>> sortedBuyersByExpenses = buyerExpensesMap.entrySet().stream()
                .sorted(Map.Entry.<Buyer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        for (Map.Entry<Buyer, Double> entry : sortedBuyersByExpenses) {
            System.out.println("Buyer: " + entry.getKey().getFirstname() + " " + entry.getKey().getLastname() + ", expenses: " + entry.getValue() + " Euro for: " + today);
        }
    }
    
    public void expensesForMonth() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        List<User> users = getDatabaseManager().getListUsers();
        Map<Buyer, Double> buyerExpensesMap = new HashMap<>();

        ratingForMonth();

        for (User user : users) {
            if (user.getBuyer() != null) {
                Buyer buyer = user.getBuyer();
                double buyerExpenses = 0;

                for (Purchase purchase : ratingForMonth) {
                    if (purchase.getBuyer().equals(buyer)) {
                        double purchaseCost = purchase.getCount() * purchase.getFlower().getCost();
                        buyerExpenses += purchaseCost;
                    }
                }
                buyerExpensesMap.put(buyer, buyerExpenses);
            }
        }

        List<Map.Entry<Buyer, Double>> sortedBuyersByExpenses = buyerExpensesMap.entrySet().stream()
                .sorted(Map.Entry.<Buyer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        for (Map.Entry<Buyer, Double> entry : sortedBuyersByExpenses) {
            System.out.println("Buyer: " + entry.getKey().getFirstname() + " " + entry.getKey().getLastname() + ", expenses: " 
            + entry.getValue() + " Euro for: (" + firstDayOfMonth + " - " + lastDayOfMonth + ")");
        }
    }
    
    public void expensesForYear() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        List<User> users = getDatabaseManager().getListUsers();
        Map<Buyer, Double> buyerExpensesMap = new HashMap<>();
        
        ratingForYear();

        for (User user : users) {
            if (user.getBuyer() != null) {
                Buyer buyer = user.getBuyer();
                double buyerExpenses = 0;

                for (Purchase purchase : ratingForYear) {
                    if (purchase.getBuyer().equals(buyer)) {
                        double purchaseCost = purchase.getCount() * purchase.getFlower().getCost();
                        buyerExpenses += purchaseCost;
                    }
                }
                buyerExpensesMap.put(buyer, buyerExpenses);
            }
        }

        List<Map.Entry<Buyer, Double>> sortedBuyersByExpenses = buyerExpensesMap.entrySet().stream()
                .sorted(Map.Entry.<Buyer, Double>comparingByValue().reversed())
                .collect(Collectors.toList());

        for (Map.Entry<Buyer, Double> entry : sortedBuyersByExpenses) {
            System.out.println("Buyer: " + entry.getKey().getFirstname() + " " + entry.getKey().getLastname() + ", expenses: " 
            + entry.getValue() + " Euro for: (" + firstDayOfYear + " - " + lastDayOfYear + ")");
        }
    }
    
    public void Income() {
        System.out.println("1. Whole income");
        System.out.println("2. Income for today");
        System.out.println("3. Income in this month");
        System.out.println("4. Income in this year");
        int number = scanner.nextInt(); scanner.nextLine();
        switch (number) {
                case 1:
                    wholeIncome();
                    break;
                case 2:
                    incomeForToday();
                    break;
                case 3:
                    incomeForMonth();
                    break;
                case 4:
                    incomeForYear();
                    break;
                default:
                    System.out.println("Invalid number selected.");
            }
    }

    public void wholeIncome() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        double totalIncome = 0;

        for (int i = 0; i < purchases.size(); i++) {
            double purchaseCost = purchases.get(i).getCount() * purchases.get(i).getBall().getCost();
            totalIncome += purchaseCost;
        }

        System.out.println("Total income earned by the store: " + totalIncome + " Euro");
    }
    
    public void incomeForToday() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        double totalIncomeForToday = 0;
        ratingForDay();

        totalIncomeForToday = ratingForDay.stream()
                .mapToDouble(purchase -> purchase.getFlower().getCost() * purchase.getCount())
                .sum();

        System.out.println("Income for today (" + today + "): " + totalIncomeForToday + " Euro");
    }
    
    public void incomeForMonth() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        double totalIncomeForMonth = 0;
        ratingForMonth();

        totalIncomeForMonth = ratingForMonth.stream()
                .mapToDouble(purchase -> purchase.getBall().getCost() * purchase.getCount())
                .sum();

        System.out.println("Income for month (" + firstDayOfMonth + " - " + lastDayOfMonth + "): " + totalIncomeForMonth + " Euro");
    }

    private void incomeForYear() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        double totalIncomeForYear = 0;
        ratingForYear();

        totalIncomeForYear = ratingForYear.stream()
                .mapToDouble(purchase -> purchase.getBall().getCost() * purchase.getCount())
                .sum();

        System.out.println("Income for year (" + firstDayOfYear + " - " + lastDayOfYear + "): " + totalIncomeForYear + " Euro");
    }
    
    public void boughtBalls() {
        System.out.println("1. Rating for day");
        System.out.println("2. Rating for month");
        System.out.println("3. Rating for year");
        
        int selectedNumber = scanner.nextInt(); scanner.nextLine();
        
        switch (selectedNumber) {
            case 1:
                boughtBallsForToday();
                break;
            case 2:
                boughtBallsForMonth();
                break;
            case 3:
                boughtBallsForYear();
                break;
            default:
                System.out.println("Invalid number selected.");
        }
   }
    
    public void boughtBallssForToday() {
        ratingForDay();

        for (Purchase purchase : ratingForDay) {
            Ball ball = purchase.getBall();
            int quantity = purchase.getCount();

            ballPurchaseSumMap.merge(ball, quantity, Integer::sum);
        }

        List<Ball> sortedBalls = ballPurchaseSumMap.entrySet().stream()
                .sorted(Map.Entry.<Ball, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("Bought Balls for " + today + ":");
        for (Ball ball : sortedBalls) {
            int totalQuantity = ballPurchaseSumMap.get(ball);
            System.out.println(totalQuantity + " " + ball.getName() + " were bought");
        }
    }
    
    public void boughtBallsForMonth() {
        ratingForMonth();

        for (Purchase purchase : ratingForMonth) {
            Ball ball = purchase.getBall();
            int quantity = purchase.getCount();

            ballPurchaseSumMap.merge(ball, quantity, Integer::sum);
        }

        List<Ball> sortedBalls = ballPurchaseSumMap.entrySet().stream()
                .sorted(Map.Entry.<Ball, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("Bought Balls for month(" + firstDayOfMonth + " - " + lastDayOfMonth + ":");
        for (Ball ball : sortedBalls) {
            int totalQuantity = ballPurchaseSumMap.get(ball);
            System.out.println(totalQuantity + " " + ball.getName() + " were bought");
        }
    }
    
    public void boughtBallsForYear() {
        ratingForYear();

        for (Purchase purchase : ratingForYear) {
            Ball ball = purchase.getBall();
            int quantity = purchase.getCount();

            ballPurchaseSumMap.merge(ball, quantity, Integer::sum);
        }

        List<Ball> sortedBalls = ballPurchaseSumMap.entrySet().stream()
                .sorted(Map.Entry.<Ball, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        System.out.println("Bought Balls for year(" + firstDayOfYear + " - " + lastDayOfYear + ":");
        for (Ball ball : sortedBalls) {
            int totalQuantity = ballPurchaseSumMap.get(ball);
            System.out.println(totalQuantity + " " + ball.getName() + " were bought");
        }
    }
    
    public void ratingForDay() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        ballPurchaseSumMap = new HashMap<>();

        ratingForDay = purchases.stream()
                .filter(purchase -> purchase.getPurchaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(today))
                .collect(Collectors.toList());
    }
    
    public void ratingForMonth() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        ballPurchaseSumMap = new HashMap<>();
        firstDayOfMonth = today.withDayOfMonth(1);
        lastDayOfMonth = today.withDayOfMonth(today.lengthOfMonth());
        ratingForMonth = purchases.stream()
                .filter(purchase -> {
                    LocalDate purchaseDate = purchase.getPurchaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !purchaseDate.isBefore(firstDayOfMonth) && !purchaseDate.isAfter(lastDayOfMonth);
                })
                .collect(Collectors.toList());
    }
    
    public void ratingForYear() {
        List<Purchase> purchases = getDatabaseManager().getListPurchases();
        ballPurchaseSumMap = new HashMap<>();
        ballDayOfYear = today.withDayOfYear(1);
        lastDayOfYear = today.withDayOfYear(today.lengthOfYear());
        ratingForYear = purchases.stream()
                .filter(purchase -> {
                    LocalDate purchaseDate = purchase.getPurchaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return !purchaseDate.isBefore(firstDayOfYear) && !purchaseDate.isAfter(lastDayOfYear);
                })
                .collect(Collectors.toList());
    }
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}