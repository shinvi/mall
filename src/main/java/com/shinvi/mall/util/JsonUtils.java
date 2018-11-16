package com.shinvi.mall.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author 邱长海
 */
@Component
public class JsonUtils {
    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("实体解析成json时发生错误", e);
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJson(String content, Class<T> cls) {
        return fromJson(content, objectMapper.getTypeFactory().constructType(cls));
    }

    public <T> List<T> fromListJson(String content, Class<T> cls) {
        return fromJson(content, objectMapper.getTypeFactory().constructCollectionType(List.class, cls));
    }

    private <T> T fromJson(String content, JavaType javaType) {
        try {
            return objectMapper.readValue(content, javaType);
        } catch (IOException e) {
            logger.error("json解析成实体时发生错误", e);
            throw new RuntimeException(e);
        }
    }

}
