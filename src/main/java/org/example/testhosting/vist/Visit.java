package org.example.testhosting.vist;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
@Table(name = "visits",
        indexes = {
                @Index(name = "idx_visits_device_ts", columnList = "device_id, timestamp"),
                @Index(name = "idx_visits_domain_ts", columnList = "domain, timestamp"),
                @Index(name = "idx_visits_pkg_ts", columnList = "package_name, timestamp")
        })
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, length = 128)
    private String deviceId;

    @Column(name = "user_id", length = 64)
    private String userId; // ممكن تخليها FK بعدين

    @Column(name = "package_name", length = 256)
    private String packageName;

    @Column(name = "app_name", length = 256)
    private String appName;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url; // TEXT مناسب لـ MySQL

    @Column(name = "domain", length = 256)
    private String domain;

    @Column(name = "source", length = 32)
    private String source; // webview | accessibility | vpn

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata; // بديل jsonb في PostgreSQL

    @Column(name = "is_sensitive")
    private Boolean sensitive = false;

    @Column(name = "is_deleted")
    private Boolean deleted = false;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        if (timestamp == null) timestamp = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
