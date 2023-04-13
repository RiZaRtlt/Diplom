package com.example.diplom.File;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    void upload(MultipartFile resource, String userName) throws IOException;
    List getList(String userName);
    void delete(String fileName, String userName);
    byte[] download(String fileName, String userName);
    void upgradeName(String lastName, String fileName, String userName);
}
