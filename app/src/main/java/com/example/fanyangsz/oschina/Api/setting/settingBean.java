package com.example.fanyangsz.oschina.Api.setting;

import java.io.Serializable;

/**
 * Created by fanyang.sz on 2016/9/22.
 */

public class SettingBean implements Serializable{
    private static final long serialVersionUID = -3694407301270573142L;

    private String description;

    private String type;

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
