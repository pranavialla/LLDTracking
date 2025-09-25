import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import Entities.Campaign;

public class AdEngineDemo {
    public static void main(String[] args) {
        AdvertisementEngine engine = new AdvertisementEngine();
        
        System.out.println("=== Advertisement Engine Demo ===\n");
        
        // Test Case 1: Basic Setup
        System.out.println("1. Setting up advertisers and users...");
        String advertiser1 = engine.addAdvertiser("TechCorp");
        String advertiser2 = engine.addAdvertiser("FashionBrand");
        
        engine.addBudgetToAdvertiser(advertiser1, 1000.0);
        engine.addBudgetToAdvertiser(advertiser2, 500.0);
        
        String user1 = engine.addUser("user1", LocalDate.of(1990, 5, 15), "male");
        String user2 = engine.addUser("user2", LocalDate.of(1995, 8, 20), "female");
        
        engine.addUserInterests(user1, Set.of("technology", "gaming"));
        engine.addUserInterests(user2, Set.of("fashion", "travel"));
        
        System.out.println("✓ Setup complete");
        
        // Test Case 2: Creating Campaigns
        System.out.println("\n2. Creating advertisement campaigns...");
        String campaign1 = engine.createCampaign(advertiser1, 10.0, "https://techcorp.com", 
                "video", 30, "bangalore", "male", Set.of("technology"));
        String campaign2 = engine.createCampaign(advertiser2, 15.0, "https://fashion.com", 
                "image", 25, "bangalore", "female", Set.of("fashion"));
        String campaign3 = engine.createCampaign(advertiser1, 8.0, "https://techcorp.com/gaming", 
                "video", 28, "bangalore", null, Set.of("gaming"));
        
        System.out.println("✓ Created " + engine.getTotalCampaigns() + " campaigns");
        
        // Test Case 3: Basic Ad Matching
        System.out.println("\n3. Testing basic ad matching...");
        Campaign matchedAd1 = engine.matchAdvertisement(user1, "bangalore");
        if (matchedAd1 != null) {
            System.out.println("✓ User1 matched with: " + matchedAd1.getUrl() + 
                             " (Bid: $" + matchedAd1.getBidAmount() + ")");
        }
        
        Campaign matchedAd2 = engine.matchAdvertisement(user2, "bangalore");
        if (matchedAd2 != null) {
            System.out.println("✓ User2 matched with: " + matchedAd2.getUrl() + 
                             " (Bid: $" + matchedAd2.getBidAmount() + ")");
        }
        
        // Test Case 4: Budget Constraint
        System.out.println("\n4. Testing budget constraints...");
        System.out.println("Advertiser1 budget before: $" + engine.getAdvertiserBudget(advertiser1));
        System.out.println("Advertiser2 budget before: $" + engine.getAdvertiserBudget(advertiser2));
        
        // Test Case 5: User Frequency Constraint
        System.out.println("\n5. Testing user frequency constraint (max 10 views)...");
        int consecutiveMatches = 0;
        for (int i = 0; i < 12; i++) {
            Campaign ad = engine.matchAdvertisement(user1, "bangalore");
            if (ad != null) {
                consecutiveMatches++;
            }
        }
        System.out.println("✓ User1 received " + consecutiveMatches + " ads out of 12 attempts");
        
        // Test Case 6: Global Frequency Constraint
        System.out.println("\n6. Testing global frequency constraint (max 5 per minute)...");
        String testUser = engine.addUser("testUser", LocalDate.of(1992, 1, 1), "male");
        engine.addUserInterests(testUser, Set.of("technology"));
        
        int globalMatches = 0;
        for (int i = 0; i < 8; i++) {
            Campaign ad = engine.matchAdvertisement(testUser, "bangalore");
            if (ad != null) {
                globalMatches++;
            }
        }
        System.out.println("✓ Test user received " + globalMatches + " ads out of 8 attempts");
        
        // Test Case 7: No Match Scenarios
        System.out.println("\n7. Testing no-match scenarios...");
        String noMatchUser = engine.addUser("noMatchUser", LocalDate.of(1970, 1, 1), "male");
        engine.addUserInterests(noMatchUser, Set.of("cooking"));
        
        Campaign noMatchAd = engine.matchAdvertisement(noMatchUser, "mumbai");
        System.out.println("✓ No match scenario result: " + 
                         (noMatchAd == null ? "NULL (Expected)" : "Unexpected match"));
        
        // Test Case 8: Concurrent Access
        System.out.println("\n8. Testing concurrent access...");
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                String concurrentUser = engine.addUser("concurrent" + threadId, 
                        LocalDate.of(1990, 1, 1), "male");
                engine.addUserInterests(concurrentUser, Set.of("technology"));
                
                for (int j = 0; j < 3; j++) {
                    engine.matchAdvertisement(concurrentUser, "bangalore");
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("✓ Concurrent access test completed");
        
        // Final Statistics
        System.out.println("\n=== Final Statistics ===");
        System.out.println("Total campaigns: " + engine.getTotalCampaigns());
        System.out.println("Total ad servings: " + engine.getTotalAdServings());
        System.out.println("Advertiser1 remaining budget: $" + engine.getAdvertiserBudget(advertiser1));
        System.out.println("Advertiser2 remaining budget: $" + engine.getAdvertiserBudget(advertiser2));
        
        System.out.println("\n=== Demo Complete ===");
    }
}