/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import entity.Ball; 
import entity.Purchase;
import entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author admin
 */
public class DatabaseManager {
    private EntityManager em;
    
    public DatabaseManager() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SPTV22FlowerShopPU");
        this.em = emf.createEntityManager();
    }
    
    public void saveBall(Ball ball){ 
        try {
            em.getTransaction().begin();
            if(ball.getId() == null){ 
                em.persist(ball); 
            }else{
                em.merge(ball); 
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }
    public void saveUser(User user){
        try {
            em.getTransaction().begin();
                if(user.getBuyer().getId() == null){
                    em.persist(user.getBuyer());
                }else{
                    em.merge(user.getBuyer());
                }
            if(user.getId() == null){
                em.persist(user);
            }else{
                em.merge(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Пользователь не сохранен: "+e);
        }
    }
    
    public void savePurchase(Purchase purchase) {
        try {
            em.getTransaction().begin();
            if (purchase.getId() == null) {
                em.persist(purchase);
            } else {
                em.merge(purchase);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
    
    public List<Ball> getListBalls() { 
        return em.createQuery("SELECT ball FROM Ball ball").getResultList(); 
    }

    public List<User> getListUsers() {
        return em.createQuery("SELECT user FROM User user").getResultList();
    }
    
    List<Purchase> getListPurchases() {
        return em.createQuery("SELECT h FROM Purchase h").getResultList();
    }
    
    public User authorization(String login, String password) {
        try {
            return (User) em.createQuery("SELECT user FROM User user WHERE user.username = :login AND user.password = :password")
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Ball getBall(Long id) { 
        try {
            return em.find(Ball.class, id); 
        } catch (Exception e) {
            return null;
        }
    }
    public User getUser(Long id) {
        try {
            return em.find(User.class,id);
        } catch (Exception e) {
            return null;
        }
    }

    Purchase getPurchase(Long id) {
        try {
            return em.find(Purchase.class,id);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void deleteUser(User user) {
        try {
            em.getTransaction().begin();
            if (user.getBuyer() != null) {
                em.remove(user.getBuyer());
            }
            em.remove(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("User deletion failed: " + e);
        }
    }
}
