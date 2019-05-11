package com.houchaojie.blog.enums;

import lombok.Data;

/**
 * 角色状态
 *
 * @author 侯超杰
 */

public enum Role {
    /**
     *
     */

    ADMIN( 1, "博客" ),
    VISITOR( 0, "访客" );

    private Integer value;

    private String message;

    Role(Integer value, String message) {
        this.value = value;
        this.message = message;
    }

    Role() {
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }}
