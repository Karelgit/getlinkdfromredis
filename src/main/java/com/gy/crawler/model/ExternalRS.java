package com.gy.crawler.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hadoop on 2015/7/21.
 */
public class ExternalRS implements Serializable {
    private boolean result;
    private int size;
    private List<External> data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<External> getData() {
        return data;
    }

    public void setData(List<External> data) {
        this.data = data;
    }
}
