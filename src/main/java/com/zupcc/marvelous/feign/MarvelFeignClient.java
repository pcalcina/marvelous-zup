package com.zupcc.marvelous.feign;

import com.zupcc.marvelous.model.marvel.MarvelComic;
import com.zupcc.marvelous.model.marvel.MarvelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "MarvelClient", url = "https://gateway.marvel.com/")
public interface MarvelFeignClient {

    @GetMapping("/v1/public/comics")
    MarvelResponse<MarvelComic> getComics();

    @GetMapping("/v1/public/comics/{comicId}")
    MarvelResponse<MarvelComic> getComic(@PathVariable("comicId") Long comicId);
}