package com.example.be.config;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisNotifyConfig {

    private final RedisConnectionFactory connectionFactory;

    @PostConstruct
    public void enableKeyspaceNotifications() {
        var connection = connectionFactory.getConnection();
        try {
            // Không cần ép kiểu nữa, vì getConfig() trả về Map<String, String>
            String currentSetting = connection.getConfig("notify-keyspace-events")
                    .get("notify-keyspace-events").toString();

            System.out.println("🔍 Redis current notify-keyspace-events: " + currentSetting);

            if (currentSetting == null || !currentSetting.contains("E") || !currentSetting.contains("x")) {
                String newSetting = (currentSetting == null ? "" : currentSetting) + "Ex";
                connection.setConfig("notify-keyspace-events", newSetting);
                System.out.println("✅ Updated notify-keyspace-events to: " + newSetting);
            } else {
                System.out.println("✅ notify-keyspace-events already contains Ex");
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to update notify-keyspace-events: " + e.getMessage());
        } finally {
            connection.close();
        }
    }


}
