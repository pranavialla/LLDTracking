package Entities;

import java.time.LocalDateTime;

public class AdServingRecord {
    private final String campaignId;
    private final String userId;
    private final LocalDateTime servedAt;
    
    public AdServingRecord(String campaignId, String userId, LocalDateTime servedAt) {
        this.campaignId = campaignId;
        this.userId = userId;
        this.servedAt = servedAt;
    }
    
    public String getCampaignId() { return campaignId; }
    public String getUserId() { return userId; }
    public LocalDateTime getServedAt() { return servedAt; }
}