import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import Entities.AdServingRecord;
import Entities.Advertiser;
import Entities.Campaign;
import Entities.User;
import SystemConstraint.GlobalFrequencyConstraint;
import SystemConstraint.SystemConstraint;
import SystemConstraint.UserFrequencyConstraint;

class AdvertisementEngine {
    private final Map<String, Advertiser> advertisers;
    private final Map<String, User> users;
    private final Map<String, Campaign> campaigns;
    private final List<AdServingRecord> adServingHistory;
    private final List<SystemConstraint> systemConstraints;
    private final ReentrantReadWriteLock lock;
    private int campaignIdCounter;
    
    public AdvertisementEngine() {
        this.advertisers = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.campaigns = new ConcurrentHashMap<>();
        this.adServingHistory = Collections.synchronizedList(new ArrayList<>());
        this.systemConstraints = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
        this.campaignIdCounter = 1;
        
        // Initialize default system constraints
        addSystemConstraint(new UserFrequencyConstraint());
        addSystemConstraint(new GlobalFrequencyConstraint());
    }
    
    // P0 Requirements Implementation
    
    public String addAdvertiser(String name) {
        String advertiserId = "ADV_" + UUID.randomUUID().toString().substring(0, 8);
        Advertiser advertiser = new Advertiser(advertiserId, name);
        advertisers.put(advertiserId, advertiser);
        return advertiserId;
    }
    
    public boolean addBudgetToAdvertiser(String advertiserId, double budget) {
        Advertiser advertiser = advertisers.get(advertiserId);
        if (advertiser != null && budget > 0) {
            advertiser.addBudget(budget);
            return true;
        }
        return false;
    }
    
    public String addUser(String userId, LocalDate dateOfBirth, String gender) {
        User user = new User(userId, dateOfBirth, gender);
        users.put(userId, user);
        return userId;
    }
    
    public boolean addUserInterests(String userId, Set<String> interests) {
        User user = users.get(userId);
        if (user != null && interests != null) {
            interests.forEach(user::addInterest);
            return true;
        }
        return false;
    }
    
    public String createCampaign(String advertiserId, double bidAmount, String url, 
                               String contentType, int targetAge, String targetCity,
                               String targetGender, Set<String> targetInterests) {
        if (!advertisers.containsKey(advertiserId) || bidAmount <= 0) {
            return null;
        }
        
        String campaignId = "CAMP_" + (campaignIdCounter++);
        Campaign campaign = new Campaign(campaignId, advertiserId, bidAmount, url, 
                                       contentType, targetAge, targetCity, targetGender, targetInterests);
        campaigns.put(campaignId, campaign);
        return campaignId;
    }
    
    public Campaign matchAdvertisement(String userId, String city) {
        User user = users.get(userId);
        if (user == null) {
            return null;
        }
        
        lock.readLock().lock();
        try {
            // Get user's ad history (last 10 records)
            List<AdServingRecord> userHistory = adServingHistory.stream()
                    .filter(record -> record.getUserId().equals(userId))
                    .sorted((r1, r2) -> r2.getServedAt().compareTo(r1.getServedAt()))
                    .limit(10)
                    .collect(Collectors.toList());
            
            // Find matching campaigns
            List<Campaign> matchingCampaigns = campaigns.values().stream()
                    .filter(Campaign::isActive)
                    .filter(campaign -> campaign.matchesUser(user, city))
                    .filter(campaign -> {
                        Advertiser advertiser = advertisers.get(campaign.getAdvertiserId());
                        return advertiser != null && advertiser.getBudget() >= campaign.getBidAmount();
                    })
                    .filter(campaign -> !violatesSystemConstraints(campaign, user, userHistory))
                    .collect(Collectors.toList());
            
            if (matchingCampaigns.isEmpty()) {
                return null;
            }
            
            // Select campaign with highest bid
            Campaign selectedCampaign = matchingCampaigns.stream()
                    .max(Comparator.comparingDouble(Campaign::getBidAmount))
                    .orElse(null);
            
            if (selectedCampaign != null) {
                // Deduct budget and record serving
                Advertiser advertiser = advertisers.get(selectedCampaign.getAdvertiserId());
                if (advertiser.deductBudget(selectedCampaign.getBidAmount())) {
                    adServingHistory.add(new AdServingRecord(
                            selectedCampaign.getCampaignId(), userId, LocalDateTime.now()));
                    return selectedCampaign;
                }
            }
            
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // P1 Requirements - System Constraints Management
    
    public void addSystemConstraint(SystemConstraint constraint) {
        lock.writeLock().lock();
        try {
            systemConstraints.add(constraint);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private boolean violatesSystemConstraints(Campaign campaign, User user, 
                                            List<AdServingRecord> userHistory) {
        for (SystemConstraint constraint : systemConstraints) {
            if (constraint.isViolated(campaign, user, userHistory, adServingHistory)) {
                return true;
            }
        }
        return false;
    }
    
    // Utility methods for testing and monitoring
    
    public double getAdvertiserBudget(String advertiserId) {
        Advertiser advertiser = advertisers.get(advertiserId);
        return advertiser != null ? advertiser.getBudget() : -1;
    }
    
    public int getTotalCampaigns() {
        return campaigns.size();
    }
    
    public int getTotalAdServings() {
        return adServingHistory.size();
    }
    
    public List<AdServingRecord> getAdServingHistory() {
        return new ArrayList<>(adServingHistory);
    }
}
