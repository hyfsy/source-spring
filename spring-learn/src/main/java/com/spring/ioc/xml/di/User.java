package com.spring.ioc.xml.di;

import java.util.*;

public class User {
    private Integer userId;
    private String userName;
    private Date birthday;

    private String[] strArray;
    private List<String> strList;
    private Set<String> strSet;
    private Map<String, String> strMap;
    private Properties prop;

    public User() {
    }

    public User(Integer userId, String userName, Date birthday) {
        this.userId = userId;
        this.userName = userName;
        this.birthday = birthday;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String[] getStrArray() {
        return strArray;
    }

    public void setStrArray(String[] strArray) {
        this.strArray = strArray;
    }

    public List<String> getStrList() {
        return strList;
    }

    public void setStrList(List<String> strList) {
        this.strList = strList;
    }

    public Set<String> getStrSet() {
        return strSet;
    }

    public void setStrSet(Set<String> strSet) {
        this.strSet = strSet;
    }

    public Map<String, String> getStrMap() {
        return strMap;
    }

    public void setStrMap(Map<String, String> strMap) {
        this.strMap = strMap;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }
}
