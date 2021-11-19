
package com.zupcc.marvelous.model.marvel;

public class MarvelResponse<T> {

    private String code;

    private MarvelData<T> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MarvelData<T> getData() {
        return data;
    }

    public void setData(MarvelData<T> data) {
        this.data = data;
    }
}