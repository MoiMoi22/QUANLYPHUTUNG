package com.tld_store.DemoDao.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToSqlDateConverter implements Converter<String, java.sql.Date> {
    @Override
    public java.sql.Date convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        try {
            return java.sql.Date.valueOf(source);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}
