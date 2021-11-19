package com.zupcc.marvelous.model;

import java.util.List;

public class ComicUpdate {
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getComicIds() {
        return comicIds;
    }

    public void setComicIds(List<Long> comicIds) {
        this.comicIds = comicIds;
    }

    List<Long> comicIds;
}
