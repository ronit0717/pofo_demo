package com.vinava.pofo.service.helper;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vinava.pofo.util.ConstantUtil.EMAIL_REGEX;

@Service
public class ValidationService {

    public boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
