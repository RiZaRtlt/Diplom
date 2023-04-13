package com.example.diplom.File;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileDAOImpl implements FileDAO {

    private static final String CREATE_FILE = "INSERT INTO files(userName, fileName, size, fileData) VALUES (?, ?, ?, ?)";
    private static final String GET_LIST = "SELECT fileName AS filename, size FROM files WHERE userName = :userName";
    private static final String DELETE_FILE = "DELETE FROM files WHERE fileName = :fileName AND userName = :userName";
    private static final String GET_FILE_Data = "SELECT fileData FROM files WHERE fileName = :fileName AND userName = :userName";
    private static final String UPDATE_FILE = "UPDATE files set fileName = :newFileName where fileName = :fileName AND userName = :userName";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate NamedParameterJdbcTemplate;

    @Override
    public void create(MultipartFile file, String userName) {
        jdbcTemplate.update(x -> {
            try {
                PreparedStatement preparedStatement = x.prepareStatement(CREATE_FILE);
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, file.getOriginalFilename());
                preparedStatement.setInt(3, (int) file.getSize());
                preparedStatement.setBytes(4, file.getBytes());
                return preparedStatement;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    @Override
    public List getList(String userName) {
        return NamedParameterJdbcTemplate.queryForList(GET_LIST, new MapSqlParameterSource("userName", userName));
    }

    @Override
    public void delete(String fileName, String userName) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("fileName", fileName);
        map.addValue("userName", userName);

        NamedParameterJdbcTemplate.update(DELETE_FILE, map);
    }

    @Override
    public byte[] download(String filename, String userName) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("fileName", filename);
        map.addValue("userName", userName);

        byte[] file = NamedParameterJdbcTemplate.queryForObject(GET_FILE_Data, map, byte[].class);
        return file;
    }

    @Override
    public void upgradeName(String lastName, String fileName, String userName) {
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("fileName", lastName);
        map.addValue("userName", userName);
        map.addValue("newFileName", fileName);

        NamedParameterJdbcTemplate.update(UPDATE_FILE, map);
    }
}
