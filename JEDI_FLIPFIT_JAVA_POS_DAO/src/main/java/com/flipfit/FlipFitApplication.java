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

    public static void main(final String[] args) throws Exception {
        new FlipFitApplication().run(args);
    }

    @Override
    public String getName() {
        return "FlipFit";
    }

    @Override
    public void initialize(final Bootstrap<FlipFitConfiguration> bootstrap) {
        // TODO: application initialization
    }

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
