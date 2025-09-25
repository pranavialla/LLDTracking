package MatchingStrategy;

import java.util.List;
import java.util.Map;

import Entities.AdServingRecord;
import Entities.Advertiser;
import Entities.Campaign;
import Entities.User;

public interface MatchingStrategy {
    List<Campaign> apply(List<Campaign> campaigns, User user, String city, 
                        List<AdServingRecord> userHistory, List<AdServingRecord> globalHistory,
                        Map<String, Advertiser> advertisers);
    String getName();
} 
    
