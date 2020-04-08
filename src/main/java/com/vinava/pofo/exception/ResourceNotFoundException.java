package com.vinava.pofo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName1;
    private Object fieldValue1;
    private String fieldName2;
    private Object fieldValue2;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName1 = fieldName;
        this.fieldValue1 = fieldValue;
        this.fieldName2 = null;
        this.fieldValue2 = null;
    }

    public ResourceNotFoundException(String resourceName, String fieldName1, String fieldName2, Object fieldValue1, Object fieldValue2) {
        super(String.format("%s not found with %s : '%s' and %s : '%s'",
                resourceName, fieldName1, fieldValue1, fieldName2, fieldValue2));
        this.resourceName = resourceName;
        this.fieldName1 = fieldName1;
        this.fieldValue1 = fieldValue1;
        this.fieldName2 = fieldName2;
        this.fieldValue2 = fieldValue2;
    }

    public String getResourceName() {
        return resourceName;
    }
    public String getFieldName1() {
        return fieldName1;
    }
    public Object getFieldValue1() {
        return fieldValue1;
    }
    public String getFieldName2() {
        return fieldName2;
    }
    public Object getFieldValue2() {
        return fieldValue2;
    }

}
