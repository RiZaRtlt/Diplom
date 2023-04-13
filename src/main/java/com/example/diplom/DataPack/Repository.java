package com.example.diplom.DataPack;

public interface Repository {
    String proclogin(String login, String password);
    boolean deleteToken(String token);
    String auth(String token);
}
