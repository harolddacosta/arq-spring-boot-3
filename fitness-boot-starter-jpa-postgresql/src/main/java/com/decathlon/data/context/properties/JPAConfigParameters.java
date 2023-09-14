/* Decathlon (C)2023 */
package com.decathlon.data.context.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(ignoreUnknownFields = true, prefix = "app")
@Getter
@Setter
@ToString
public class JPAConfigParameters implements Serializable {

    private static final long serialVersionUID = 695255049263841719L;

    private HibernateAppProperties hibernate;
    private DomainAppProperties domain;
    private JacksonProperties jackson;

    @Getter
    @Setter
    @ToString
    public static class JacksonProperties implements Serializable {

        private static final long serialVersionUID = 5541880522575588387L;

        private boolean hibernateModuleEnable;
    }

    @Getter
    @Setter
    @ToString
    public static class HibernateAppProperties implements Serializable {

        private static final long serialVersionUID = -1933292155545548658L;

        private String orderUpdates;
        private String jdbcBatchVersionedData;
        private String defaultBatchFetchSize;
        private String cacheUseSecondLevelCache;
        private String cacheUseQueryCache;
        private String connectionAutocommit;
        private String jdbcBatchSize;
        private String showSql;
        private String formatSql;
        private String cacheRegionFactoryClass;
        private String dialect;
        private String hbm2ddlAuto;
        private String timeZone;
        private String columnNamingStrategy;
        private String sequenceNamingStrategy;
    }

    @Getter
    @Setter
    @ToString
    public static class DomainAppProperties implements Serializable {

        private static final long serialVersionUID = -2144830085120212986L;

        private String packageToScan;
    }
}
