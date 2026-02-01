package com.flipfit.health;

import com.codahale.metrics.health.HealthCheck;
import com.flipfit.utils.DBConnection;
import java.sql.Connection;

/**
 * Dropwizard health check that verifies the application can connect to the database.
 * Used by the admin/health endpoint to report database connectivity status.
 *
 * @author Ananya
 * @ClassName "DatabaseHealthCheck"
 */
public class DatabaseHealthCheck extends HealthCheck {

    /**
     * Performs the health check by obtaining a connection and validating it.
     *
     * @return {@link Result#healthy(String)} if the connection is valid,
     *         {@link Result#unhealthy(String)} otherwise
     * @throws Exception if an unexpected error occurs during the check
     */
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
