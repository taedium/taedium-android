package me.taedium.android.api;

public abstract class ApiParam {
    
    protected String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
}
