package com.example.diplom.DataPack;

import lombok.*;

import javax.persistence.Id;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    private String login;
    private String token;
}
