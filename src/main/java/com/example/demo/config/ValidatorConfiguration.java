package com.example.demo.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

@Configuration
public class ValidatorConfiguration implements WebMvcConfigurer {

    @Bean
    public jakarta.validation.Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            // 开启 fail fast 机制
            .failFast(true)
            .buildValidatorFactory();
        return validatorFactory.getValidator();
    }

    @Override
    public Validator getValidator() {
        return new SpringValidatorAdapter(validator());
    }

}
