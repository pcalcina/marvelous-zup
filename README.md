# Managing Marvel Comics with Spring

In this article we show how to use Spring and the Java Programming Language to manage Marvel Comics through a REST API.

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


    @NotEmpty
    @Email
    private String email;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate birthday;


    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comic> comics;
