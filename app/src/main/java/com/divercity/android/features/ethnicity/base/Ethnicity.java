package com.divercity.android.features.ethnicity.base;

/**
 * Created by lucas on 17/10/2018.
 */

public class Ethnicity {
    private int drawable;
    private String id;
    private int textId;

    public Ethnicity(String id, int drawable, int textId) {
        this.drawable = drawable;
        this.id = id;
        this.textId = textId;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTextId() {
        return textId;
    }

    public void setTextId(int textId) {
        this.textId = textId;
    }
}
