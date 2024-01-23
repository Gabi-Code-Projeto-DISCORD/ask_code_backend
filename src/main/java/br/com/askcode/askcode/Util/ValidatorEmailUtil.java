package br.com.askcode.askcode.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorEmailUtil {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+[?:\\.[a-zA-Z0-9]+]*@[?:[a-zA-Z0-9]+\\.]+[a-zA-Z]{2,7}$";
    private Pattern pattern;
    private Matcher matcher;

    public ValidatorEmailUtil() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    public boolean validate(final String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
