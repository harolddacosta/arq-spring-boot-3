/* Decathlon (C)2023 */
package com.decathlon.data;

import com.decathlon.data.context.properties.JPAConfigParameters;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.decathlon.data")
@PropertySource("classpath:jpa.properties")
@EnableConfigurationProperties
@EnableTransactionManagement
public class JPAConfiguration {

    @Bean
    PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            Properties hibernateProperties,
            ObjectFactory<JPAConfigParameters> jpaConfigParametersFactory) {
        JPAConfigParameters jpaConfigParameters = jpaConfigParametersFactory.getObject();
        LocalContainerEntityManagerFactoryBean entityManagerFactory =
                new LocalContainerEntityManagerFactoryBean();

        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setPackagesToScan(jpaConfigParameters.getDomain().getPackageToScan());
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaProperties(hibernateProperties);

        return entityManagerFactory;
    }

    @Bean
    JpaTransactionManager transactionManager(
            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());

        return transactionManager;
    }
}
