package ru.nesterov.app.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Nesterov
 */

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class MyHeader {
    @JsonIgnore
    private MyHeader parent;
    private String name;
    @JsonIgnore
    private int level;
    private int lineNumber;
    private List<MyHeader> subHeaders;

    public void addToSubHeaders(MyHeader myHeader){
        if(subHeaders == null) subHeaders = new ArrayList<>();
        subHeaders.add(myHeader);
    }
}
