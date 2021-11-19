package com.zupcc.marvelous.model.marvel;

import java.util.List;

public class MarvelComic {

    private long id;
    private String title;
    private List<MarvelComicPrice> prices;
    private MarvelCreatorList creators;
    private String description;
    private String isbn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<MarvelComicPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<MarvelComicPrice> prices) {
        this.prices = prices;
    }

    public MarvelCreatorList getCreators() {
        return creators;
    }

    public void setCreators(MarvelCreatorList creators) {
        this.creators = creators;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}