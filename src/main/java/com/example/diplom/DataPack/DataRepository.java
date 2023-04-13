package com.example.diplom.DataPack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.UUID;

@Component
public class DataRepository implements com.example.diplom.DataPack.Repository {

    @Autowired
    DataSource dataSource;

    private DataDAO dataDAO;

    public DataRepository(DataSource dataSource) {
        this.dataDAO = new DataDAO(dataSource);
    }

    @Override
    public String proclogin(String login, String password) {
        My_User pers = dataDAO.getUser(login);

        if (!(pers.getPassword() == null) && pers.getPassword().equals(password)) {
            String uuid = UUID.randomUUID().toString();

            dataDAO.saveToken(login, uuid);

            return uuid;
        } else {
            return "";
        }
    }

    @Override
    public boolean deleteToken(String token) {
        return dataDAO.deleteToken(token);
    }

    @Override
    public String auth(String token) {
        return dataDAO.findToken(token);
    }
}
