package org.example.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Purchase {

    private Student studentName;
    private Course courseName;
    private Integer price;
    private LocalDate subscriptionDate;

}
