package SystemConstraint;


import java.time.LocalDateTime;
import java.util.List;

import Entities.AdServingRecord;
import Entities.Campaign;
import Entities.User;

public class GlobalFrequencyConstraint implements SystemConstraint {
    private static final int MAX_GLOBAL_VIEWS = 5;
    private static final int TIME_WINDOW_MINUTES = 1;
    
    @Override
    public boolean isViolated(Campaign campaign, User user, List<AdServingRecord> userHistory, 
                             List<AdServingRecord> globalHistory) {
        LocalDateTime oneMinuteAgo = LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES);
        long count = globalHistory.stream()
                .filter(record -> record.getCampaignId().equals(campaign.getCampaignId()) &&
                                record.getServedAt().isAfter(oneMinuteAgo))
                .count();
        return count >= MAX_GLOBAL_VIEWS;
    }
    
    @Override
    public String getConstraintName() {
        return "GlobalFrequencyConstraint";
    }
}