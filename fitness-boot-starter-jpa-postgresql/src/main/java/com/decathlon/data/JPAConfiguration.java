/* Decathlon (C)2023 */
package com.decathlon.data;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.decathlon.data")
@PropertySource("classpath:jpa.properties")
@EnableConfigurationProperties
@EnableTransactionManagement
public class JPAConfiguration {

    //    @Bean
    //    PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
    //        return new PersistenceExceptionTranslationPostProcessor();
    //    }

    //    @Bean
    //    LocalContainerEntityManagerFactoryBean entityManagerFactory(
    //            DataSource dataSource, Properties hibernateProperties) {
    //        //        JPAConfigParameters jpaConfigParameters =
    // jpaConfigParametersFactory.getObject();
    //        LocalContainerEntityManagerFactoryBean entityManagerFactory =
    //                new LocalContainerEntityManagerFactoryBean();
    //
    //        entityManagerFactory.setDataSource(dataSource);
    //        //
    //        //
    //        //
    // entityManagerFactory.setPackagesToScan(jpaConfigParameters.getDomain().getPackageToScan());
    //        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
    //        entityManagerFactory.setJpaProperties(hibernateProperties);
    //
    //        return entityManagerFactory;
    //    }

    //    @Bean
    //    JpaTransactionManager transactionManager(
    //            LocalContainerEntityManagerFactoryBean entityManagerFactory) {
    //        JpaTransactionManager transactionManager = new JpaTransactionManager();
    //        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
    //
    //        return transactionManager;
    //    }
}
