package com.github.changhee_choi.jubo.core.domain;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Changhee Choi
 * @since 23/07/2020
 */
@Converter(autoApply = true)
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String s) {
        return "Y".equals(s);
    }
}
