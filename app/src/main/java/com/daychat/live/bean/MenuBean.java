package com.daychat.live.bean;

public class MenuBean {

    private Integer id;

    private Integer icon;

    private String name;

    public MenuBean(Integer id, Integer icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
