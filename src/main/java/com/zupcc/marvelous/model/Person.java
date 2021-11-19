package com.zupcc.marvelous.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "cpf"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CPF(message="Invalid CPF")
    @NotEmpty
    private String cpf;

    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    @JsonFormat(pattern="dd/MM/yyyy")
    @Column(nullable = false)
    private LocalDate birthday;


    @ManyToMany
    @JoinTable(
            name = "person_comic",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "comic_id"))
    private List<Comic> comics;


    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(LocalDate birthday){
        this.birthday = birthday;
    }


    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public List<Comic> getComics() {
        return comics;
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
    }

    public long getId() {
        return id;
    }

}
