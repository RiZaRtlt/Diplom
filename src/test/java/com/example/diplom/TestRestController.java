package com.example.diplom;

import com.example.diplom.DataPack.DataRepository;
import com.example.diplom.DataPack.My_User;
import com.example.diplom.File.FileService;
import com.example.diplom.RestPack.RestController;
import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestRestController {

    @Mock
    DataRepository mockDataController;

    @Mock
    FileService mockFileService;

    @InjectMocks
    RestController restController;

    @Test
    public void testLogin() throws JSONException {
        when(mockDataController.proclogin("user", "password")).thenReturn("123");
        when(mockDataController.proclogin("user1", "password")).thenReturn("");

        ResponseEntity<String> testResponse1 = restController.login(new My_User("user", "password"));
        ResponseEntity<String> testResponse2 = restController.login(new My_User("user1", "password"));

        Assertions.assertTrue(testResponse1.getStatusCode().equals(HttpStatus.OK));
        Assertions.assertTrue(testResponse2.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        Assertions.assertTrue(testResponse1.getBody().equals("{\"auth-token\":\"123\"}"));
        Assertions.assertTrue(testResponse2.getBody().equals("{\"action\":\"http:\\/\\/localhost:8080\\/Error\"}"));
    }

    @Test
    public void testLogout() {
        when(mockDataController.deleteToken("89")).thenReturn(true);
        when(mockDataController.deleteToken("890")).thenReturn(false);

        Assertions.assertTrue(restController.logout("123456789").equals(HttpStatus.OK));
        Assertions.assertTrue(restController.logout("1234567890").equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testList() {
        List list = new ArrayList<String>();
        list.add("1");
        list.add("2");

        when(mockDataController.auth("89")).thenReturn("user");
        when(mockDataController.auth("890")).thenReturn("");
        when(mockFileService.getList("user")).thenReturn(list);

        ResponseEntity<String> testResponse1 = new ResponseEntity<String>(new Gson().toJson(list).toString(), HttpStatus.OK);;
        ResponseEntity<String> testResponse2 = new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);

        Assertions.assertTrue(restController.list("123456789").getBody().equals(testResponse1.getBody()));
        Assertions.assertTrue(restController.list("1234567890").getBody().equals(testResponse2.getBody()));
        Assertions.assertTrue(restController.list("123456789").getStatusCode().equals(HttpStatus.OK));
        Assertions.assertTrue(restController.list("1234567890").getStatusCode().equals(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void testUploadSingleFile() throws IOException {
        when(mockDataController.auth("89")).thenReturn("user");
        when(mockDataController.auth("890")).thenReturn("");

        MockMultipartFile mpf = new MockMultipartFile("test.json", "", "application/json",
                "{\"key1\": \"value1\"}".getBytes());

        Mockito.doThrow(new IOException()).doNothing().when(mockFileService).upload(null, "user");
        Mockito.doNothing().when(mockFileService).upload(mpf, "user");

        Assertions.assertTrue(restController.uploadSingleFile("1234567890", mpf).equals(HttpStatus.UNAUTHORIZED));
        Assertions.assertTrue(restController.uploadSingleFile("123456789", mpf).equals(HttpStatus.CREATED));
        Assertions.assertTrue(restController.uploadSingleFile("123456789", null).equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testUpdateName() {
        HttpEntity<String> httpEntity = new HttpEntity<String>("{\"filename\": \"newFileName\"}");

        when(mockDataController.auth("89")).thenReturn("user");
        when(mockDataController.auth("890")).thenReturn("");

        Assertions.assertTrue(restController.updateName("1234567890", "fileName", httpEntity).equals(HttpStatus.UNAUTHORIZED));
        Assertions.assertTrue(restController.updateName("123456789", "fileName", null).equals(HttpStatus.valueOf(400)));
        Assertions.assertTrue(restController.updateName("123456789", "lastName", httpEntity).equals(HttpStatus.OK));
    }

    @Test
    public void testGetFile() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE));

        HttpEntity<String> httpEntity = new ResponseEntity("123456".getBytes(), headers, HttpStatus.OK);
        HttpEntity<String> wrongHttpEntity = new ResponseEntity("", headers, HttpStatus.valueOf(400));

        when(mockDataController.auth("89")).thenReturn("user");
        when(mockDataController.auth("890")).thenReturn("");
        when(mockFileService.download("fileName", "user")).thenReturn("123456".getBytes());

        Assertions.assertTrue(restController.getFile("1234567890", "fileName").getStatusCode().equals(HttpStatus.UNAUTHORIZED));
        Assertions.assertTrue(restController.getFile("123456789", "").equals(wrongHttpEntity));
        Assertions.assertTrue(restController.getFile("123456789", "fileName").equals(httpEntity));
    }
}
