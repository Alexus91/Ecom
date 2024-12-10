package com.EComP.backend.Entity;

import com.EComP.backend.Enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name ="Users")
public class User {

    @Id
    @GeneratedValue

    private Long id;
    private String name;
    private String password;
    private String email;
    private UserRole role;
    @Lob
    @Column
    private byte[] image;
}
