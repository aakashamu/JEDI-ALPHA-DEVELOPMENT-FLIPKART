package com.flipfit.health;

import com.codahale.metrics.health.HealthCheck;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;

public class DatabaseHealthCheck extends HealthCheck {

    @Override
    protected Result check() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn.isValid(1)) {
                return Result.healthy("Database is connected");
            } else {
                return Result.unhealthy("Database connection is invalid");
            }
        } catch (Exception e) {
            return Result.unhealthy("Cannot connect to database: " + e.getMessage());
        }
    }
}
