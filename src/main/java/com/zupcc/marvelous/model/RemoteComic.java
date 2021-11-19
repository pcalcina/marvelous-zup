package com.zupcc.marvelous.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.List;

//FIXME: BORRAR ESTA CLASE DESPUÉS
public class RemoteComic {
    private long id;
    private String title;
    private List<ComicPrice> prices;
    private CreatorList creators;
    private String description;
    private String isbn;

    public RemoteComic(String title, List<ComicPrice> prices, CreatorList creators, String description, String isbn) {
        this.title = title;
        this.prices = prices;
        this.creators = creators;
        this.description = description;
        this.isbn = isbn;
    }

    private Double getPrice(){
        int total = 0;
        Double price = 0.0;
        for(ComicPrice cp: this.prices){
            if(cp.price != null){
                price += cp.price;
                total++;
            }
        }
        if(total != 0){
            price /= total;
        }
        return price;
    }

    private String getCreatorsStr(){
        String creatorsStr = "";
        for(CreatorSummary cs: this.creators.getItems()){
            creatorsStr += ", " + cs.name;
        }
        return creatorsStr;
    }

//    public Comic toComic(){
//        return new Comic(this.title, this.getPrice(), this.getCreatorsStr(), this.description, this.isbn);
//    }

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

    public List<ComicPrice> getPrices() {
        return prices;
    }

    public void setPrice(List<ComicPrice> prices) {
        this.prices = prices;
    }

    public CreatorList getCreators() {
        return creators;
    }

    public void setCreators(CreatorList authors) {
        this.creators = authors;
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
