package com.application.signup.dto;



import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityDTO {
    private String email;
    private LocalDateTime activityTime;
}
