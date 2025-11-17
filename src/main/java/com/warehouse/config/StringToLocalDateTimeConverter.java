package com.warehouse.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    // 支持多种常见格式
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,      // yyyy-MM-dd'T'HH:mm:ss
        DateTimeFormatter.ISO_DATE_TIME             // 带时区的 ISO 格式
    );

    @Override
    public LocalDateTime convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        String trimmed = source.trim();
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // 尝试下一个格式
            }
        }
        throw new IllegalArgumentException("无法解析日期时间: " + trimmed +
            "，支持格式：yyyy-MM-dd HH:mm:ss、yyyy-MM-dd HH:mm、ISO 8601");
    }
}