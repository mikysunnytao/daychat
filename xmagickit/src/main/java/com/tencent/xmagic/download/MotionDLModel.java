package com.tencent.xmagic.download;

public class MotionDLModel {
    private final String category;
    private final String name;
    private final String url;

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public MotionDLModel(String category, String name, String url) {
        this.category = category;
        this.name = name;
        this.url = url;
    }
}
