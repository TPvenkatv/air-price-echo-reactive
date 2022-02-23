package com.travelport.air.price.reactive.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public PolymorphicTypeValidator ptv() {
        return new PolymorphicTypeValidator() {
            @Override
            public Validity validateBaseType(MapperConfig<?> mapperConfig, JavaType javaType) {
                return Validity.ALLOWED;
            }

            @Override
            public Validity validateSubClassName(MapperConfig<?> mapperConfig, JavaType javaType, String s) throws JsonMappingException {
                return Validity.ALLOWED;
            }

            @Override
            public Validity validateSubType(MapperConfig<?> mapperConfig, JavaType javaType, JavaType javaType1) throws JsonMappingException {
                return Validity.ALLOWED;
            }

        };
    }

    @Bean
    public ObjectMapper objectMapper(PolymorphicTypeValidator ptv) {
        ObjectMapper objectMapper = new ObjectMapper();
        //objectMapper.activateDefaultTypingAsProperty(ptv, ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE, "@type");
        objectMapper.deactivateDefaultTyping();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        //objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false);
        //objectMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return objectMapper;
    }

    @Bean
    @Primary
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

}