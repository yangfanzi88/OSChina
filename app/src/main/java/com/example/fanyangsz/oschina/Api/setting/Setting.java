package com.example.fanyangsz.oschina.Api.setting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanyang.sz on 2016/9/22.
 */

public class Setting extends SettingBean implements Serializable {
    private static final long serialVersionUID = 4801654811733634325L;

    private Map<String, SettingExtra> extras;

    public Setting() {
        extras = new HashMap<String, SettingExtra>();
    }

    public Map<String, SettingExtra> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, SettingExtra> extras) {
        this.extras = extras;
    }
}
