package SystemConstraint;

import java.util.List;

import Entities.AdServingRecord;
import Entities.Campaign;
import Entities.User;

// User frequency constraint implementation


public class UserFrequencyConstraint implements SystemConstraint {
    private static final int MAX_USER_VIEWS = 10;
    
    @Override
    public boolean isViolated(Campaign campaign, User user, List<AdServingRecord> userHistory, 
                             List<AdServingRecord> globalHistory) {
        long count = userHistory.stream()
                .filter(record -> record.getCampaignId().equals(campaign.getCampaignId()))
                .count();
        return count >= MAX_USER_VIEWS;
    }
    
    @Override
    public String getConstraintName() {
        return "UserFrequencyConstraint";
    }

}