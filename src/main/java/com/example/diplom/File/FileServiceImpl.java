package com.example.diplom.File;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDAO fileDAO;

    @Transactional(rollbackFor = {IOException.class})
    @Override
    public void upload(MultipartFile resource, String userName) throws IOException {
        fileDAO.create(resource, userName);
    }

    @Override
    public List getList(String userName) {
        return fileDAO.getList(userName);
    }

    @Override
    public void delete(String fileName, String userName) {
        fileDAO.delete(fileName, userName);
    }

    @Override
    public byte[] download(String filename, String userName) {
        return fileDAO.download(filename, userName);
    }

    @Override
    public void upgradeName(String lastName, String fileName, String userName) {
        fileDAO.upgradeName(lastName, fileName, userName);
    }
}
