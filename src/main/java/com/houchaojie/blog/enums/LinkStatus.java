package com.houchaojie.blog.enums;

/**
 * @author 侯超杰
 */

public enum LinkStatus {
    /**
     *
     */
    NORMAL( 1, "正常" ),
    HIDDEN( 0, "隐藏" );

    private Integer value;
    private String message;

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
    }

    LinkStatus(Integer value, String message) {
        this.value = value;
        this.message = message;
    }
}
