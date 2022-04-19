package ru.nesterov.app.controllers;

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
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Nesterov
 */

@Controller
public class MyController {

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

        //находим количество заголовков
        List<String> lines = Arrays.asList(body.split("\\n|\\r"));
        lines = lines.stream().filter(s-> !s.isEmpty()).collect(Collectors.toList());
        long countHeads = lines.stream().filter(s -> s.startsWith("#")).count();

        //форимируем оглавление файла
        StringBuilder headers = new StringBuilder();
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("#")) {
                headers
                        .append(lines.get(i))
                        .append(" -- line ")
                        .append(i + countHeads + 2)
                        .append("\n");
            }
        }

        // формируем ответ в виде файла с оглавлением
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename= Changed " + file.getOriginalFilename());
        responseHeaders.add("Content-Type", "application/download; charset=UTF-8");
        return new ResponseEntity(headers.append("\n").append(body).toString(), responseHeaders, HttpStatus.OK);
    }
}
