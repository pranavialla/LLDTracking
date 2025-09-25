package Entities;

import java.util.HashSet;
import java.util.Set;

public class Campaign {
    private final String campaignId;
    private final String advertiserId;
    private final double bidAmount;
    private final String url;
    private final String contentType;
    private final int targetAge;
    private final String targetCity;
    private final String targetGender;
    private final Set<String> targetInterests;
    private final boolean isActive;
    
    public Campaign(String campaignId, String advertiserId, double bidAmount, 
                   String url, String contentType, int targetAge, String targetCity,
                   String targetGender, Set<String> targetInterests) {
        this.campaignId = campaignId;
        this.advertiserId = advertiserId;
        this.bidAmount = bidAmount;
        this.url = url;
        this.contentType = contentType;
        this.targetAge = targetAge;
        this.targetCity = targetCity;
        this.targetGender = targetGender;
        this.targetInterests = targetInterests != null ? new HashSet<>(targetInterests) : new HashSet<>();
        this.isActive = true;
    }
    
    public boolean matchesUser(User user, String city) {
        // Age matching (within ±5 years for flexibility)
        if (Math.abs(user.getAge() - targetAge) > 5) {
            return false;
        }
        
        // Gender matching
        if (targetGender != null && !targetGender.equalsIgnoreCase(user.getGender())) {
            return false;
        }
        
        // City matching
        if (targetCity != null && !targetCity.equalsIgnoreCase(city)) {
            return false;
        }
        
        // Interest matching (at least one common interest)
        if (!targetInterests.isEmpty()) {
            Set<String> userInterests = user.getInterests();
            boolean hasCommonInterest = targetInterests.stream()
                    .anyMatch(interest -> userInterests.contains(interest));
            if (!hasCommonInterest) {
                return false;
            }
        }
        
        return true;
    }
    
    // Getters
    public String getCampaignId() { return campaignId; }
    public String getAdvertiserId() { return advertiserId; }
    public double getBidAmount() { return bidAmount; }
    public String getUrl() { return url; }
    public String getContentType() { return contentType; }
    public boolean isActive() { return isActive; }
}
