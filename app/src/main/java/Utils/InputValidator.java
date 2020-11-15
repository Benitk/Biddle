package Utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {



    /*
    source : https://mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
    regex explain
    [_a-zA-Z1-9]+     #  accept A-Z,a-z, 0-9 and _ (+ mean it must be occur at least one)
    (\.[A-Za-z0-9])   #  optional accept . and A-Z, a-z, 0-9( * mean its optional)
    @[A-Za-z0-9]+     #  accept @ then A-Z,a-z,0-9
    \.[A-Za-z0-9]+    #  accept. and A-Z,a-z,0-9
    (\.[A-Za-z0-9])   # optional

     */
    public boolean isValidEmail(String s){
        final String email_pattern = "[_a-zA-Z1-9]+(\\.[A-Za-z0-9]*)*@[A-Za-z0-9]+\\.[A-Za-z0-9]+(\\.[A-Za-z0-9]*)*";
        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    /*
    source: https://mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
    regex explain:
    ^                                 # start of line
    (?=.*[0-9])                       # positive lookahead, digit [0-9]
    (?=.*[a-z])                       # positive lookahead, one lowercase character [a-z]
    (?=.*[A-Z])                       # positive lookahead, one uppercase character [A-Z]
    .                                 # matches anything
    {8,20}                            # length at least 8 characters and maximum of 20 characters
    $                                 # end of line

     */

    public boolean isValidPassword(String s){
        String PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public boolean isEqual(String a, String b){
        return a.equals(b) ? true : false;
    }

}
