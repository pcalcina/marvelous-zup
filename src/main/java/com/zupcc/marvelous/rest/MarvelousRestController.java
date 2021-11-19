package com.zupcc.marvelous.rest;

import com.zupcc.marvelous.model.Person;
import com.zupcc.marvelous.rest.input.ComicUpdate;
import com.zupcc.marvelous.rest.output.ComicResponse;
import com.zupcc.marvelous.service.MarvelousService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class MarvelousRestController {

    private final MarvelousService service;

    @Autowired
    public MarvelousRestController(MarvelousService service) {
        this.service = service;
    }

    @GetMapping
    public List<Person> listUsers(){
        return service.getUsers();
    }

    @PostMapping
    public void createUser(@Valid @RequestBody Person p){
        try{
            service.savePerson(p);
            System.out.println(p.getComics());

        }
        catch (ConstraintViolationException e){
            System.out.println(e.getMessage());
            System.out.println(e.getCause().toString());
            //throw new MethodArgumentNotValidException();
        }
    }

    @PostMapping("/update")
    public void setComicsForUser(@RequestBody ComicUpdate cup){
        service.savePersonComics(cup);
    }

    @GetMapping("/{userId}/comics")
    public List<ComicResponse> getComics(@PathVariable Long userId){
        return service.getComicsByPersonId(userId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, Exception.class})
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}