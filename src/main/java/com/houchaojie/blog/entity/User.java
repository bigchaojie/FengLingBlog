package com.houchaojie.blog.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 侯超杰
 */
@Data
public class User implements Serializable {
    private Integer userId;
    private String userName;
    private String userPass;
    private String userNickname;
    private String userEmail;
    private String userUrl;
    private String userAvatar;
    private String userLastLoginIp;
    private Date userRegisterTime;
    private Date userLastLoginTime;
    private Integer userStatus;
    /**
     *文章数量
     */
    private Integer articleCount;
}
