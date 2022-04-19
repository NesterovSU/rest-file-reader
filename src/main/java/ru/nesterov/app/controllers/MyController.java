package ru.nesterov.app.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.nesterov.app.entities.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sergey Nesterov
 */

@Controller
public class MyController {

    private List<Header> headers;

    @PostMapping(name = "parsefile")
    public ResponseEntity fromFile(@RequestParam(name = "file") MultipartFile file) throws IOException {

        //проверяем текстовый ли файл
        if (!file.getOriginalFilename().endsWith(".txt")) {
            return new ResponseEntity("File format must be .txt", HttpStatus.BAD_REQUEST);
        }

        //записываем поток байтов как текст
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



        headers = new ArrayList<>();
        Header before = null;
        //форимируем оглавление файла
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("#")) {
                before = myMetod(before, line, i);
            }
        }

        MyResponse myResponse = new MyResponse();
        myResponse.setLines(lines);
        myResponse.setHeaders(headers);

        // формируем ответ в виде json
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity(myResponse, responseHeaders, HttpStatus.OK);
    }

    private Header myMetod(Header before, String line, int i) {
        System.out.println(before);
        if (before == null) {
            Header now = new Header(null, line, line.lastIndexOf("#"), i, null);
            headers.add(now);
            return now;
        } else if (before.getLevel() < line.lastIndexOf("#")) {
            Header now = new Header(null, line, line.lastIndexOf("#"), i, null);
            before.push(now);
            System.out.println(before);
            return now;
        } else {
            return myMetod(before.getParent(),line,i);
        }
    }
}