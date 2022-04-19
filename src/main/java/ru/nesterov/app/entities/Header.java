package ru.nesterov.app.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Nesterov
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Header{
    @JsonIgnore
    private Header parent;
    private String name;
    @JsonIgnore
    private int level;
    private int lineNumber;
    private List<Header> child;

    public void push(Header header){
        if(child == null) child = new ArrayList<>();
        child.add(header);
    }
}
