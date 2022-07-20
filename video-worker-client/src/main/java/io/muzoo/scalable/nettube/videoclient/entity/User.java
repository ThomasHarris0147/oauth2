package io.muzoo.scalable.nettube.videoclient.entity;

import lombok.Data;

import javax.persistence.*;



import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @Column(length = 60)
    private String password;

    private String role;
    private boolean enabled = false;
}
