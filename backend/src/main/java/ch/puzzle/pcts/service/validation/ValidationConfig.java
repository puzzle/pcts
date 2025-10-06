// package ch.puzzle.pcts.service.validation;
////
//// import org.springframework.context.annotation.Bean;
// import org.springframework.context.MessageSource;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import
// org.springframework.context.support.ReloadableResourceBundleMessageSource;
// import
// org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
// import jakarta.validation.MessageInterpolator;
//
// @Configuration
// public class ValidationConfig {
//////
////// @Bean
////// public LocalValidatorFactoryBean validator() {
////// LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
//////
////// MessageInterpolator defaultInterpolator = bean.getMessageInterpolator();
////// bean.setMessageInterpolator(new
// FieldAwareMessageInterpolator(defaultInterpolator));
//////
////// return bean;
////// }
////
// @Bean
// public MessageSource messageSource() {
// ReloadableResourceBundleMessageSource messageSource
// = new ReloadableResourceBundleMessageSource();
//
// messageSource.setBasename("classpath:messages");
// messageSource.setDefaultEncoding("UTF-8");
// return messageSource;
// }
// }
