package com.example.diplom.File;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileDAO {

    void create(MultipartFile file, String userName);
    List getList(String userName);
    void delete(String fileName, String userName);
    byte[] download(String filename, String userName);
    void upgradeName(String lastName, String fileName, String userName);
}
