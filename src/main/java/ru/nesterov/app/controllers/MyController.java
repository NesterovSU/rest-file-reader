package ru.nesterov.app.controllers;

import entities.MyResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Nesterov
 */

@Controller
public class MyController {

    private MyResponse myResponse;

    @PostMapping(name = "parsefile")
    public ResponseEntity fromFile(@RequestParam(name = "file") MultipartFile file) throws IOException {

        //проверяем текстовый ли файл
        if (!file.getOriginalFilename().endsWith(".txt")) {
            return new ResponseEntity("File format must be .txt", HttpStatus.BAD_REQUEST);
        }

        //записываем поток байтов как симольную строку
        InputStream io = file.getInputStream();
        StringBuilder temp = new StringBuilder();
        int c;
        while ((c = io.read()) != -1) {
            temp.append((char) c);
        }
        String body = temp.toString();

        //разбиваем на строки
        List<String> lines = Arrays.asList(body.split("\\n|\\r"));
        lines = lines.stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());

        myResponse = new MyResponse();
        myResponse.setLines(lines);

        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();

        //форимируем оглавление файла
        StringBuilder headers = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("#"))
                map.put(lines.get(i), i);
        }
        myResponse.setHeaders(map);

        // формируем ответ в виде json
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity(myResponse, responseHeaders, HttpStatus.OK);
    }
}
