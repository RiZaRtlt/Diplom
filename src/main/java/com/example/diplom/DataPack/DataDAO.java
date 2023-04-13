package com.example.diplom.DataPack;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.PreparedStatement;

public class DataDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String save_Token = "insert into token(login, token) VALUES (?,?)";
    private static final String get_User = "select * FROM my_User WHERE login = ?";
    private static final String get_Token = "select * FROM token WHERE token = ?";
    private static final String delete_Token = "DELETE FROM token WHERE token = ?";

    public DataDAO() {
        this.jdbcTemplate = new JdbcTemplate();
    }

    public DataDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public My_User getUser(String login) {
        try {
            return jdbcTemplate.queryForObject(get_User, rowMapperUser(), login);}
        catch (Exception e) {
            return new My_User();
        }
    }

    public String findToken(String token) {
        Token objToken = jdbcTemplate.queryForObject(get_Token, rowMapperToken(), token);
        if (!(objToken == null)) {
            return objToken.getLogin();
        } else {
            return "";
        }
    }

    public RowMapper<Token> rowMapperToken() {
        return (rs, rowNum) -> Token.builder()
                .login(rs.getString("login"))
                .token(rs.getString("token"))
                .build();
    }

    public RowMapper<My_User> rowMapperUser() {
        return (rs, rowNum) -> My_User.builder()
                .password(rs.getString("password"))
                .build();
    }

    public void saveToken(String login, String token) {
        jdbcTemplate.update(x -> {
            PreparedStatement preparedStatement = x.prepareStatement(save_Token);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, token);
            return preparedStatement;
        });
    }

    public boolean deleteToken(String token) {
        try {
            jdbcTemplate.update(delete_Token, token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
