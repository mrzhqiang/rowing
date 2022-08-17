package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.system.data.DataDictItemForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class UserForm {

    @NotBlank
    private String nickname;
    @NotBlank
    private DataDictItemForm gender;
    @NotNull
    @Past
    private LocalDate birthday;
}
