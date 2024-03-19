package managers;

import entity.Ball;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BallManager {
    private final Scanner scanner;
    private final DatabaseManager databaseManager;
    
    public BallManager(Scanner scanner, DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.databaseManager = databaseManager;
    }

    public Ball addBall() {
        System.out.println("-----Add ball-----");
        Ball ball = new Ball();
        System.out.println("Enter ball name: ");
        ball.setName(scanner.nextLine());
        System.out.println("Enter cost for 1 ball: ");
        ball.setCost(scanner.nextDouble());
        System.out.println("Default cost for 1 ball(for discount):");
        ball.setDefaultCost(scanner.nextDouble());
        System.out.println("Added ball: " + ball.toString());
        scanner.nextLine();
        return ball;
    }

    public void printBallCost(List<Ball> balls) {
        System.out.println("----- Ball Costs -----");
        for (int i = 0; i < balls.size(); i++) {
            System.out.println("Ball: " + balls.get(i).getName() + " | Cost per ball: " + balls.get(i).getCost() + " Euro");
        }
    }   
    
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public void printListBall() {
        List<Ball> balls = getDatabaseManager().getListBalls();
        System.out.println("----- Ball list -----");
        for (int i = 0; i < balls.size(); i++) {
            System.out.println(i+1 + ". Ball name: " + balls.get(i).getName() + " | Cost: " + balls.get(i).getCost() + " Euro");
        }
    }

    public void changeProduct() {
        List<Ball> balls = getDatabaseManager().getListBalls();
        printListBalls();
        System.out.println("Choose the number of the ball whose cost you want to change:");
        int number = scanner.nextInt(); 
        scanner.nextLine();
        
        if (number >= 1 && number <= ball.size()) {
            Ball selectedBall = ball.get(number - 1);

            System.out.println("Enter new cost for the ball:");
            double ballCost = scanner.nextDouble();
            scanner.nextLine();
            
            selectedFlower.setCost(ballCost);

            getDatabaseManager().saveBalls(selectedBall);

            System.out.println("Cost changed successfully for ball: " + selectedBall.getName());
        } else {
            System.out.println("Invalid user number. Please choose a valid number.");
        }
    }

        public void addDiscount() throws ParseException {
            List<Ball> balls = getDatabaseManager().getListBalls();
            System.out.println("Enter the ball from the list:");
            printListBalls();
            int number = scanner.nextInt();
            scanner.nextLine();
            Balls selectedFlower = balls.get(number - 1);
            System.out.println("You selected: " + selectedBall.getName() + ", ball cost :" + selectedBall.getCost());

            System.out.println("Enter ball discount cost:");
            double discount = scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter discount date (format: yyyy-MM-dd):");
            String discountDateString = scanner.nextLine();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date discountDate = dateFormat.parse(discountDateString);
            selectedBall.setDiscountDate(discountDate);

            LocalDate today = LocalDate.now();

            LocalDate discountLocalDate = discountDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (discountLocalDate.equals(today)) {
                selectedBall.setCost(discount);
            }
            
            if (today.isAfter(discountLocalDate)) {
            selectedBall.setCost(selectedBall.getDefaultCost());
            selectedBall.setDiscountDate(null);
        }
            
            getDatabaseManager().saveBall(selectedBall);
        }
}
