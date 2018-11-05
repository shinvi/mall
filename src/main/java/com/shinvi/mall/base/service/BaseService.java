package com.shinvi.mall.base.service;

import com.shinvi.mall.base.exception.ServerResponseException;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class BaseService {
    private final Map<String, PropertyValidator> validators = new HashMap<>();

    public BaseService() {
        registerPropertyValidator(validators);
    }

    /**
     * 效验do实体某个属性的值是否存在于数据库表中
     *
     * @param property 属性名
     * @param content  值
     * @param exist    true代表效验已存在,false代表效验不存在
     */
    protected void validPropertyExist(String property, String content, boolean exist) {

        PropertyValidator validator = validators.get(property);
        if (validator == null) {
            return;
        }
        int count = validator.getValidFuncation().apply(content);
        String message = exist ? validator.getExistTips() : validator.getNotExistTips();

        if (exist && count > 0 || !exist && count == 0) {
            throw new ServerResponseException(message);
        }
    }

    /**
     * 如果子类要使用validPropertyExist,就必须重写此方法添加自定义的效验员
     */
    protected void registerPropertyValidator(Map<String, PropertyValidator> validators) {

    }

    protected static class PropertyValidator {
        private final Function<String, Integer> validFuncation;
        private final String existTips;
        private final String notExistTips;

        public PropertyValidator(@Nonnull Function<String, Integer> validFuncation, String existTips, String notExistTips) {
            this.validFuncation = validFuncation;
            this.existTips = existTips;
            this.notExistTips = notExistTips;
        }

        public Function<String, Integer> getValidFuncation() {
            return validFuncation;
        }

        public String getExistTips() {
            return existTips;
        }

        public String getNotExistTips() {
            return notExistTips;
        }
    }
}
