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
import MatchingStrategy.MatchingStrategy;

public class AdEngineDemo {
    public static void main(String[] args) {
        AdvertisementEngine engine = new AdvertisementEngine();
        
        // Setup
        String advertiser = engine.addAdvertiser("TechCorp");
        engine.addBudgetToAdvertiser(advertiser, 1000.0);
        
        String user = engine.addUser("user1", LocalDate.of(1990, 5, 15), "male");
        engine.addUserInterests(user, Set.of("technology"));
        
        engine.createCampaign(advertiser, 10.0, "https://techcorp.com", 
                "video", 30, "bangalore", "male", Set.of("technology"));
        engine.createCampaign(advertiser, 8.0, "https://techcorp.com/image", 
                "image", 30, "bangalore", "male", Set.of("technology"));
        
        // Test default strategy
        System.out.println("=== Default Strategy ===");
        System.out.println("Current: " + engine.getCurrentStrategy());
        Campaign ad1 = engine.matchAdvertisement(user, "bangalore");
        System.out.println("Match: " + (ad1 != null ? ad1.getUrl() + " (" + ad1.getContentType() + ")" : "None"));
        
        // Custom inline strategy
        System.out.println("\n=== Custom Strategy ===");
        engine.setMatchingStrategy(new MatchingStrategy() {
            @Override
            public List<Campaign> apply(List<Campaign> campaigns, User user, String city, 
                                       List<AdServingRecord> userHistory, List<AdServingRecord> globalHistory,
                                       Map<String, Advertiser> advertisers) {
                // Only campaigns with bid > 9
                return campaigns.stream()
                        .filter(Campaign::isActive)
                        .filter(c -> c.matchesUser(user, city))
                        .filter(c -> c.getBidAmount() > 9.0)
                        .collect(Collectors.toList());
            }
            
            @Override
            public String getName() { return "HighBidOnly"; }
        });
        
        System.out.println("Current: " + engine.getCurrentStrategy());
        Campaign ad3 = engine.matchAdvertisement(user, "bangalore");
        System.out.println("Match: " + (ad3 != null ? ad3.getUrl() + " (Bid: $" + ad3.getBidAmount() + ")" : "None"));
        
        System.out.println("\n=== Stats ===");
        System.out.println("Total servings: " + engine.getTotalAdServings());
        System.out.println("Remaining budget: $" + engine.getAdvertiserBudget(advertiser));
    }
}