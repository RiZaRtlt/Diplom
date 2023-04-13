package com.example.diplom.RestPack;

import com.example.diplom.DataPack.DataRepository;
import com.example.diplom.DataPack.My_User;
import com.example.diplom.File.FileService;
import com.google.gson.Gson;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
public class RestController {

    @Autowired
    private DataRepository dataController;

    @Autowired
    private FileService fileService;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody My_User user) {
        JSONObject entity = new JSONObject();
        String request = dataController.proclogin(user.getLogin(), user.getPassword());
        if (request.equals("")) {
            entity.put("action", "http://localhost:8080/Error");
            return new ResponseEntity<String>(entity.toString(), HttpStatus.BAD_REQUEST);
        } else {
            entity.put("auth-token", request);
            return new ResponseEntity<String>(entity.toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    @ResponseBody
    public HttpStatus logout(@RequestHeader("auth-token") String token) {
        if (dataController.deleteToken(token.substring(7))) {
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<String> list(@RequestHeader("auth-token") String token) {
        String userName = dataController.auth(token.substring(7));
        if (userName.equals("")) {
            return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED);
        }

        List request = fileService.getList(userName);

        return new ResponseEntity<String>(new Gson().toJson(request).toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/file",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public HttpStatus uploadSingleFile(@RequestHeader("auth-token") String token, @RequestParam MultipartFile file) {
        String userName = dataController.auth(token.substring(7));
        if (userName.equals("")) {
            return HttpStatus.UNAUTHORIZED;
        }

        try {
            fileService.upload(file, userName);
            return HttpStatus.CREATED;
        } catch (IOException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @DeleteMapping(value = "/file")
    public HttpStatus deleteFile(@RequestHeader("auth-token") String token, @RequestParam("filename") String fileName) {
        String userName = dataController.auth(token.substring(7));
        if (userName.equals("")) {
            return HttpStatus.UNAUTHORIZED;
        }

        fileService.delete(fileName, userName);
        return HttpStatus.OK;
    }

    @PutMapping(value = "/file")
    public HttpStatus updateName(@RequestHeader("auth-token") String token, @RequestParam("filename") String fileName,
                                 HttpEntity<String> httpEntity) {
        String userName = dataController.auth(token.substring(7));
        if (userName.equals("")) {
            return HttpStatus.UNAUTHORIZED;
        }

        Gson g = new Gson();
        HashMap<String, String> p;

        try {
            String json = httpEntity.getBody();

            p = g.fromJson(json, HashMap.class);
        } catch (Exception e) {
            return HttpStatus.valueOf(400);
        }

        fileService.upgradeName(fileName, p.get("filename"), userName);

        return HttpStatus.OK;
    }

    @GetMapping(value = "/file")
    @ResponseBody
    public ResponseEntity<Response> getFile(@RequestHeader("auth-token") String token, @RequestParam("filename") String fileName) {
        String userName = dataController.auth(token.substring(7));
        if (userName.equals("")) {
            return new ResponseEntity(new Response(), HttpStatus.UNAUTHORIZED);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE));

        if (fileName.equals("")) {
            return new ResponseEntity("", headers, HttpStatus.valueOf(400));
        }

        return new ResponseEntity(fileService.download(fileName, userName), headers, HttpStatus.OK);
    }
}