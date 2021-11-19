package com.zupcc.marvelous.rest.input;

import java.util.List;

public class ComicUpdate {

    private Long userId;

    private List<Long> comicIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getComicIds() {
        return comicIds;
    }

    public void setComicIds(List<Long> comicIds) {
        this.comicIds = comicIds;
    }
}