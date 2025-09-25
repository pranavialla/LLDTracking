package MatchingStrategy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Entities.AdServingRecord;
import Entities.Advertiser;
import Entities.Campaign;
import Entities.User;

class DefaultMatchingStrategy implements MatchingStrategy {
    @Override
    public List<Campaign> apply(List<Campaign> campaigns, User user, String city, 
                               List<AdServingRecord> userHistory, List<AdServingRecord> globalHistory,
                               Map<String, Advertiser> advertisers) {
        return campaigns.stream()
                .filter(Campaign::isActive)
                .filter(c -> c.matchesUser(user, city))
                .filter(c -> {
                    Advertiser advertiser = advertisers.get(c.getAdvertiserId());
                    return advertiser != null && advertiser.getBudget() >= c.getBidAmount();
                })
                .filter(c -> !violatesUserConstraint(c, userHistory))
                .filter(c -> !violatesGlobalConstraint(c, globalHistory))
                .collect(Collectors.toList());
    }
    
    private boolean violatesUserConstraint(Campaign campaign, List<AdServingRecord> userHistory) {
        return userHistory.stream()
                .filter(r -> r.getCampaignId().equals(campaign.getCampaignId()))
                .count() >= 10;
    }
    
    private boolean violatesGlobalConstraint(Campaign campaign, List<AdServingRecord> globalHistory) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(1);
        return globalHistory.stream()
                .filter(r -> r.getCampaignId().equals(campaign.getCampaignId()))
                .filter(r -> r.getServedAt().isAfter(oneMinuteAgo))
                .count() >= 5;
    }
    
    @Override
    public String getName() { return "DefaultMatching"; }
}