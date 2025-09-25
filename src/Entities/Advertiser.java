package Entities;
// Advertiser model
public class Advertiser {
    private final String advertiserId;
    private final String name;
    private double budget;
    
    public Advertiser(String advertiserId, String name) {
        this.advertiserId = advertiserId;
        this.name = name;
        this.budget = 0.0;
    }
    
    public synchronized void addBudget(double amount) {
        this.budget += amount;
    }
    
    public synchronized boolean deductBudget(double amount) {
        if (budget >= amount) {
            budget -= amount;
            return true;
        }
        return false;
    }
    
    // Getters
    public String getAdvertiserId() { return advertiserId; }
    public String getName() { return name; }
    public synchronized double getBudget() { return budget; }
}