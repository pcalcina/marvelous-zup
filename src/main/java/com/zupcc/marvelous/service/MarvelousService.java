package com.zupcc.marvelous.service;

import com.zupcc.marvelous.feign.MarvelFeignClient;
import com.zupcc.marvelous.model.Comic;
import com.zupcc.marvelous.model.Person;
import com.zupcc.marvelous.model.marvel.MarvelComic;
import com.zupcc.marvelous.model.marvel.MarvelData;
import com.zupcc.marvelous.model.marvel.MarvelResponse;
import com.zupcc.marvelous.repo.IComicRepo;
import com.zupcc.marvelous.repo.IUserRepo;
import com.zupcc.marvelous.rest.input.ComicUpdate;
import com.zupcc.marvelous.rest.output.ComicResponse;
import com.zupcc.marvelous.util.ComicConverter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MarvelousService {

    private final IUserRepo userRepo;

    private final IComicRepo comicRepo;

    private final MarvelFeignClient client;

    public MarvelousService(IUserRepo userRepo, IComicRepo comicRepo, MarvelFeignClient client) {
        this.userRepo = userRepo;
        this.comicRepo = comicRepo;
        this.client = client;
    }

    public List<Person> getUsers() {
        return userRepo.findAll();
    }

    public Long savePerson(Person person) {
        userRepo.save(person);
        return person.getId();
    }

    public void savePersonComics(ComicUpdate comicUpdate) {
        comicUpdate.getComicIds().stream().map(client::getComic)
                .forEach(this::saveMarvelComic);
    }

    private void saveMarvelComic(MarvelResponse<MarvelComic> response) {
        if ("200".equals(response.getCode())) {
            MarvelData<MarvelComic> data = response.getData();
            if (data.getCount().equals(1L)) {
                MarvelComic comic = data.getResults().get(0);
                comicRepo.save(ComicConverter.fromMarvelComic(comic));
            }
        }
    }

    public List<ComicResponse> getComicsByPersonId(Long personId) {
        List<Comic> comics = userRepo.findById(personId).map(Person::getComics)
                .orElseGet(Collections::emptyList);
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        return comics.stream().map(c -> {
            boolean hasDiscount = hasDiscount(c.getISBN(), dayOfWeek);
            ComicResponse comicResponse = ComicConverter.fromComicToResponse(c, hasDiscount);
            return comicResponse;
        }).collect(Collectors.toList());
    }

    private boolean hasDiscount(String isbn, DayOfWeek dayOfWeek) {
        if (StringUtils.hasLength(isbn)) {
            int len = isbn.length();
            char lastChar = isbn.charAt(len - 1);
            return (dayOfWeek.getValue() - (lastChar - '0' + 2)/2) == 1;
        }
        return false;
    }
}