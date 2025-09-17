package com.mcblacklist.autoBlacklistBan.Managers;

public class BlacklistEntry {
    private final String reason;
    private final String duration;
    private final String expires;

    public BlacklistEntry(String reason, String duration, String expires) {
        this.reason = reason;
        this.duration = duration;
        this.expires = expires;
    }

    public String getReason() {
        return reason;
    }

    public String getDuration() {
        return duration;
    }

    public String getExpires() {
        return expires;
    }
}
