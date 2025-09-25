import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import Entities.AdServingRecord;
import Entities.Advertiser;
import Entities.Campaign;
import Entities.User;
import MatchingStrategy.DefaultMatchingStrategy;
import MatchingStrategy.MatchingStrategy;

public class AdEngineDemo {
    public static void main(String[] args) {
        
        AdvertisementEngine engine = new AdvertisementEngine();
        engine.setMatchingStrategy(new DefaultMatchingStrategy());
        
        // Setup advertisers
        String techCorpId = engine.addAdvertiser("TechCorp");
        String fashionBrandId = engine.addAdvertiser("FashionBrand");
        
        engine.addBudgetToAdvertiser(techCorpId, 1000.0);
        engine.addBudgetToAdvertiser(fashionBrandId, 500.0);
        System.out.println("Added advertisers with budgets");
        
        // Create users
        engine.addUser("john_25", LocalDate.of(1998, 5, 15), "male");
        engine.addUserInterests("john_25", Set.of("technology", "gaming"));
        
        engine.addUser("sarah_30", LocalDate.of(1993, 8, 20), "female");
        engine.addUserInterests("sarah_30", Set.of("fashion", "travel"));
        System.out.println("Added users with interests");
        
        // Create campaigns
        String techCampaign = engine.createCampaign(
            techCorpId, 50.0, "https://techcorp.com/laptop", 
            "banner", 25, "bangalore", "male", Set.of("technology")
        );
        
        String fashionCampaign = engine.createCampaign(
            fashionBrandId, 30.0, "https://fashion.com/summer", 
            "video", 30, "bangalore", "female", Set.of("fashion")
        );
        
        System.out.println("Created targeted campaigns");
        System.out.println("  Tech campaign: " + techCampaign);
        System.out.println("  Fashion campaign: " + fashionCampaign);
        
        // Test matching
        System.out.println("\nTesting Ad Matching ");
        
        
        // Sarah should get fashion ad
        Campaign sarahAd = engine.matchAdvertisement("sarah_30", "bangalore");
        if (sarahAd != null) {
            System.out.println("Sarah matched with: " + sarahAd.getUrl());
            System.out.println("  Bid amount: " + sarahAd.getBidAmount());
        }
        
        // Test frequency constraint
        System.out.println("\n********Testing Frequency Limits --------");
        int johnAds = 0;
        for (int i = 0; i < 12; i++) {
            Campaign ad = engine.matchAdvertisement("john_25", "bangalore");
            if (ad != null) johnAds++;
        }
        System.out.println("John got " + johnAds);
        
        // Show budget deduction
        System.out.println("\n=== Budget Status ===");
        System.out.println("TechCorp remaining budget: " + 
            String.format("%.2f", engine.getAdvertiserBudget(techCorpId)));
        System.out.println("FashionBrand remaining budget: " + 
            String.format("%.2f", engine.getAdvertiserBudget(fashionBrandId)));
        
        // Test no match scenario
        System.out.println("\n=== Testing Edge Cases ===");
        engine.addUser("bob_50", LocalDate.of(1973, 3, 10), "male");
        engine.addUserInterests("bob_50", Set.of("gardening"));
        
        Campaign bobAd = engine.matchAdvertisement("bob_50", "mumbai");
        if (bobAd == null) {
            System.out.println(" No ad for Bob ");
        }

    }
}