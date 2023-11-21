/* Decathlon (C)2023 */
package com.decathlon.data.config;

import com.decathlon.data.context.properties.JPAConfigParameters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class HibernatePropertiesConfiguration {

    @Bean
    Properties hibernateProperties(ObjectFactory<JPAConfigParameters> jpaConfigParametersFactory) {
        JPAConfigParameters jpaConfigParameters = jpaConfigParametersFactory.getObject();
        Properties props = new Properties();

        props.put("hibernate.order_updates", jpaConfigParameters.getHibernate().getOrderUpdates());
        props.put(
                "hibernate.jdbc.batch_versioned_data",
                jpaConfigParameters.getHibernate().getJdbcBatchVersionedData());
        props.put(
                "hibernate.default_batch_fetch_size",
                jpaConfigParameters.getHibernate().getDefaultBatchFetchSize());
        props.put(
                "hibernate.cache.use_second_level_cache",
                jpaConfigParameters.getHibernate().getCacheUseSecondLevelCache());
        props.put(
                "hibernate.cache.use_query_cache",
                jpaConfigParameters.getHibernate().getCacheUseQueryCache());
        props.put(
                "hibernate.connection.autocommit",
                jpaConfigParameters.getHibernate().getConnectionAutocommit());
        props.put(
                "hibernate.jdbc.batch_size", jpaConfigParameters.getHibernate().getJdbcBatchSize());
        props.put("hibernate.show_sql", jpaConfigParameters.getHibernate().getShowSql());
        props.put("hibernate.format_sql", jpaConfigParameters.getHibernate().getFormatSql());
        props.put(
                "hibernate.cache.region.factory_class",
                jpaConfigParameters.getHibernate().getCacheRegionFactoryClass());
        props.put("hibernate.jdbc.time_zone", jpaConfigParameters.getHibernate().getTimeZone());
        props.put("hibernate.id.new_generator_mappings", "true");

        if (StringUtils.isNotBlank(jpaConfigParameters.getHibernate().getColumnNamingStrategy())
                && "snakeCase"
                        .equalsIgnoreCase(
                                jpaConfigParameters.getHibernate().getColumnNamingStrategy())) {
            props.put(
                    "hibernate.physical_naming_strategy",
                    "io.hypersistence.utils.hibernate.naming.CamelCaseToSnakeCaseNamingStrategy");
        }

        if (StringUtils.isNotBlank(jpaConfigParameters.getHibernate().getSequenceNamingStrategy())
                && "suffix-id-seq"
                        .equalsIgnoreCase(
                                jpaConfigParameters.getHibernate().getSequenceNamingStrategy())) {
            props.put(
                    "hibernate.id.db_structure_naming_strategy",
                    "com.decathlon.data.strategies.SuffixIdSeqNamingStrategy");
        }

        if (StringUtils.isNotBlank(jpaConfigParameters.getHibernate().getHbm2ddlAuto())
                && !"none".equalsIgnoreCase(jpaConfigParameters.getHibernate().getHbm2ddlAuto())) {
            props.put(
                    "hibernate.hbm2ddl.auto", jpaConfigParameters.getHibernate().getHbm2ddlAuto());
        }

        return props;
    }
}
