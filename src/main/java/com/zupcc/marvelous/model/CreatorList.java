package com.zupcc.marvelous.model;

import java.util.List;

class CreatorList {

    private Long available;

    private List<CreatorSummary> items;

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public List<CreatorSummary> getItems() {
        return items;
    }

    public void setItems(List<CreatorSummary> items) {
        this.items = items;
    }
}