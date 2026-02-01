package com.flipfit;

import io.dropwizard.core.Configuration;
import io.dropwizard.db.DataSourceFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * The Class FlipFitConfiguration.
 *
 * @author Ananya
 * @ClassName  "FlipFitConfiguration"
 */
public class FlipFitConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    /**
     * Sets the database data source factory from configuration.
     *
     * @param factory the data source factory (e.g. from config YAML)
     */
    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    /**
     * Returns the database data source factory for JDBC connections.
     *
     * @return the configured data source factory
     */
    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }
}
