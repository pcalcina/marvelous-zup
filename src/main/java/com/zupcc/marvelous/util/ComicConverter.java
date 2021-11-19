package com.zupcc.marvelous.util;

import com.zupcc.marvelous.model.Comic;
import com.zupcc.marvelous.model.marvel.MarvelComic;
import com.zupcc.marvelous.model.marvel.MarvelCreatorSummary;
import com.zupcc.marvelous.rest.output.ComicResponse;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class ComicConverter {

    public static Comic fromMarvelComic(MarvelComic marvelComic) {
        Comic comic = new Comic();
        comic.setId(marvelComic.getId());
        comic.setDescription(marvelComic.getDescription());
        comic.setISBN(marvelComic.getIsbn());
        comic.setTitle(marvelComic.getTitle());
        if (!CollectionUtils.isEmpty(marvelComic.getPrices())) {
            Double price = marvelComic.getPrices().get(0).getPrice();
            comic.setPrice(price);
        }
        if (marvelComic.getCreators().getAvailable().intValue() > 0) {
            String authors = marvelComic.getCreators().getItems().stream()
                    .map(MarvelCreatorSummary::getName).collect(
                            Collectors.joining(", "));
            comic.setAuthors(authors);
        }
        return comic;
    }

    public static ComicResponse fromComicToResponse(Comic comic, boolean hasDiscount) {
        ComicResponse response = new ComicResponse();
        response.setId(comic.getId());
        response.setAuthors(comic.getAuthors());
        response.setTitle(comic.getTitle());
        response.setDescription(comic.getDescription());
        response.setPrice(comic.getPrice());
        response.setHasDiscount(hasDiscount);
        return response;
    }
}