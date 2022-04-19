package ru.nesterov.app.entities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Sergey Nesterov
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyResponse {
    private List<String> lines;
    private List<MyHeader> myHeaders;
}
