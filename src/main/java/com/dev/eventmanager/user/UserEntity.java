package com.dev.eventmanager.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "age", nullable = false)
    private int age;

    public UserEntity() {
    }


    public UserEntity(Long id, String login, int age) {
        this.id = id;
        this.login = login;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
