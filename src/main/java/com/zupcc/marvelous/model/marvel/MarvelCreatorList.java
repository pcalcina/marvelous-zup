package com.zupcc.marvelous.model.marvel;

import java.util.List;

public class MarvelCreatorList {

    private Long available;

    private List<MarvelCreatorSummary> items;

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public List<MarvelCreatorSummary> getItems() {
        return items;
    }

    public void setItems(List<MarvelCreatorSummary> items) {
        this.items = items;
    }
}