# Managing Marvel Comics with Spring

In this article we show how to use Spring and the Java Programming Language to manage Marvel Comics through a REST API.

The complete code used in this tutorial can be found in this repository 
[https://github.com/pcalcina/marvelous-zup](https://github.com/pcalcina/marvelous-zup)

## Objectives

- User registration
- Comic registration
- List of registered users
- List of comics by user


## Preliminaries

### Creating the project
We will use the IntelliJ IDEA IDE for Java and the Spring Boot F

### Creating the data model
We begin by creating a basic User entity, illustrated below: 

```Java
@Entity
public class User {
  @Id
  private long id;
  private String cpf;
  private String name;
  private String email;
  private LocalDate birthday;
  private List<Comic> comics;
}
```

The purpose of the `@Entity` annotation is to map the `User` class to a table in a 
relational database management system (RDBMS), such as PostgreSQL and MariaDB. 
In this case we will use a simple database called [Derby](http://db.apache.org/derby/).

#### Data validation
- Since the fields `CPF` and `email` are meant to be unique, any of them could be used as the
  entity id. However,  

```Java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private long id;
```

- To validate that the fields name, cpf and email are not empty, we use the `@NotEmpty` validator, as shown below;

```Java
    @NotEmpty
    private String name;
```

- In order to validate the CPF field we take advantage of the `CPF` validator present in `org.hibernate.validator.constraints.br.CPF` 

```Java
    @CPF(message="Invalid CPF")
    @NotEmpty
    private String cpf;
```

- Next, we associate a list of comics for a given person. Since a person may have many comics and a comic may belong to many people, we associate both entities through a _many to many_ relationship, as follows.

```Java
  @ManyToMany
  @JoinTable(
  name = "person_comic",
  joinColumns = @JoinColumn(name = "person_id"),
  inverseJoinColumns = @JoinColumn(name = "comic_id"))
  private List<Comic> comics;
```

### Creating a REST Controller
Now we create the `MarvelousRestController` class, that will actually manage the REST request in our API. 
To achieve this behaviour we use the `@RestController` decorator.  

By using the `@RequestMapping` decorator, we define the path for the API URL, e.g. `http://localhost:8080/users`.


```Java
@RestController
@RequestMapping("/users")
public class MarvelousRestController {

  private final MarvelousService service;

  @Autowired
  public MarvelousRestController(MarvelousService service) {
    this.service = service;
  }
```

As we know, a REST API relies on the use of the four HTTP methods: GET, POST, PUT and DELETE.
To associate a method of the controller with a given method and path, we use the respective decorators,
such as `@GetMapping` for GET, and `@PostMapping` for POST, as illustrated below.

In this case, the `listUsers` method in response to a GET request on `/users` provides a list of the registered users, through the use of a Spring Service (more on this later).

Accordingly, the method `createUser` responds to a POST request on `/users`, which receives a Person object in JSON format.
This JSON object is mapped to a Person instance, with the help of the `@RequestBody` decorator.
Additionally, the `@Valid` makes sure the constraints defined in the Person entity will apply and throw an Exception if any of them is not fulfilled. 

```Java
  @GetMapping
  public List<Person> listUsers() {
    return service.getUsers();
  }

  @PostMapping
  public void createUser(@Valid @RequestBody Person p){
      service.savePerson(p);
  }
```
#### Exception Handling
The exceptions thrown in the `createUser` method above, are captured by the `handleValidationExceptions`,
and with the use of the `@ResponseStatus` decorator, this exception is transformed in a corresponding HTTP Response,
including a specific HTTP status and error message.  

```Java
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
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
```
### Manipulating the Model

As we could observe from the previous example, the `MarvelousService` class actually manages the User data, through the use of a Repository class, as follows:
The IUserRepo provides a unified way to access an Entity, that abstracts the low level details for getting the data from the underlying database manager.

```Java
public interface IUserRepo extends JpaRepository<Person, Long> {
    @Query("select u from Person u where u.email = ?1")
    Person findByEmail(String email);
}
```

This `IUserRepo` interface is used with `MarvelousService` in order to provide useful method that rely on access to the entities.
The `getUsers` and `savePerson` methods constitute the basic management of the Person entity, for listing and creating users.

```Java
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
}
```
### Comic management
Similarly to the Person entity, we create the Comics entity devoted to store the comics associated with each person.
Next we show the Comic entity that implements basic validation, such as NotEmpty title, price, authors and description.
Additionally, it defines a unique ISBN for comics, and mirrors the many-to-many relationship with Person. 

```Java
@Entity
public class Comic {
  @Id
  private Long id;

  @NotEmpty
  private String title;

  @NotNull
  private Double price;

  @NotEmpty
  private String authors;

  @NotEmpty
  private String description;

  @NotEmpty
  @Column(unique = true)
  private String ISBN;

  @ManyToMany(mappedBy = "comics")
  private List<Person> persons;
}
```

#### Controller Mapping for Comics
Similarly to the mappings used in `MarvelousRestController` to manipulated users, we define the corresponding mappings
to access comics.

The `setComicsForUser` method receives the user information and the associated comic ids. 
As we'll see later, the complete information from comicId is obtained from the Marvel API through the use of the Feign library. 

The `getComics` method provides a complete list of comics for a given userId. The userId param is part of the URL, 
which is permitted by the use of the `@PathVariable` decorator.


```Java
    @PostMapping("/update")
    public void setComicsForUser(@RequestBody ComicUpdate cup){
        service.savePersonComics(cup);
    }

    @GetMapping("/{userId}/comics")
    public List<ComicResponse> getComics(@PathVariable Long userId){
        return service.getComicsByPersonId(userId);
    }
```


### Getting Comics from Marvel API
In order to obtain a comic from the Marvel REST API we use the Feign library.
The first step we need is to create a FeighClient, that maps to the corresponding entrypoints 
in the Marvel API.

To capture the Response, we create a set of classes that mimic the corresponding classes used by
the Marvel API, such as the response (MarvelResponse), and the obtained comic (MarvelComic).
These classes are summarized in the `com.zupcc.marvelous.model.marvel` package, and include MarvelComic, MarvelComicPrice, MarvelCreatorList, MarvelCreatorSummary, MarvelData, and MarvelResponse.

```Java
@FeignClient(value = "MarvelClient", url = "https://gateway.marvel.com/")
public interface MarvelFeignClient {

    @GetMapping("/v1/public/comics")
    MarvelResponse<MarvelComic> getComics();

    @GetMapping("/v1/public/comics/{comicId}")
    MarvelResponse<MarvelComic> getComic(@PathVariable("comicId") Long comicId);
}
```
#### Comic conversion 

It is important to note that the MarvelComic, that represents a comic object in the original format from Marvel,
is not fully compatible with the Comic entity we defined. To map between both classes, we created a ComicConverter class, 
that resolves these subtleties, as illustrated in the code below.

```Java
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
```


#### Storing a comic locally

```Java
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
```

#### Checking the discount for a comic
As defined in the business logic, we create a method for computing the discount for a comic in a specific day of the week.
To achieve this, we get the last character of the ISBN and convert to integer in order to use the given rule, as follows.

```Java
 private boolean hasDiscount(String isbn, DayOfWeek dayOfWeek) {
    if (StringUtils.hasLength(isbn)) {
        int len = isbn.length();
        char lastChar = isbn.charAt(len - 1);
        return (dayOfWeek.getValue() - (lastChar - '0' + 2)/2) == 1;
    }
    return false;
}
```