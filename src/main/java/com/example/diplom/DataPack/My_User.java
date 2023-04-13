package com.example.diplom.DataPack;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class My_User {
    @Id
    private String login;
    private String password;

}
