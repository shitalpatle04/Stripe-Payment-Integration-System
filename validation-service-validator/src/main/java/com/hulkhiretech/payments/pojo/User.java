package com.hulkhiretech.payments.pojo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {

    @NotBlank(message = "USER_ID_MISSING")
    @Size(max = 50)
    private String endUserID;

    @NotBlank(message = "FIRSTNAME_MISSING")
    @Size(max = 50)
    private String firstname;

    @NotBlank(message = "LASTNAME_MISSING")
    @Size(max = 50)
    private String lastname;

    @NotBlank(message = "EMAIL_MISSING")
    @Email(message = "EMAIL_INVALID")
    private String email;

    @NotBlank(message = "MOBILE_PHONE_MISSING")
    @Pattern(
        regexp = "^\\+?[0-9]{8,15}$",
        message = "MOBILE_PHONE_INVALID"
    )
    private String mobilePhone;
}