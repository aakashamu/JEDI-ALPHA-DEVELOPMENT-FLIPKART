package com.flipfit;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.flipfit.rest.UserResource;
import com.flipfit.rest.GymCustomerResource;
import com.flipfit.rest.GymOwnerResource;
import com.flipfit.rest.GymAdminResource;

/**
 * The Class FlipFitApplication.
 *
 * @author Ananya
 * @ClassName  "FlipFitApplication"
 */
public class FlipFitApplication extends Application<FlipFitConfiguration> {

    /**
     * Entry point for the FlipFit Dropwizard application.
     *
     * @param args command-line arguments (config file, server, etc.)
     * @throws Exception if the application fails to start
     */
    public static void main(final String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    /**
     * Returns the application name used by Dropwizard.
     *
     * @return the application name "FlipFit"
     */
    @Override
    public String getName() {
        return "FlipFit";
    }

    /**
     * Initializes the application bootstrap (bundles, config, etc.).
     *
     * @param bootstrap the Dropwizard bootstrap
     */
    @Override
    public void initialize(final Bootstrap<FlipFitConfiguration> bootstrap) {
        // TODO: application initialization
    }

    /**
     * Runs the application: registers REST resources and health checks.
     *
     * @param configuration the parsed configuration
     * @param environment   the Dropwizard environment
     */
    @Override
    public void run(final FlipFitConfiguration configuration,
                    final Environment environment) {
        // Register Resources
        environment.jersey().register(new UserResource());
        environment.jersey().register(new GymCustomerResource());
        environment.jersey().register(new GymOwnerResource());
        environment.jersey().register(new GymAdminResource());

        // Register Health Checks
        environment.healthChecks().register("database", new com.flipfit.health.DatabaseHealthCheck());
    }

}
