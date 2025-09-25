package SystemConstraint;


import java.util.List;

import Entities.AdServingRecord;
import Entities.Campaign;
import Entities.User;

public interface SystemConstraint {
    boolean isViolated(Campaign campaign, User user, List<AdServingRecord> userHistory, 
                      List<AdServingRecord> globalHistory);
    String getConstraintName();
}