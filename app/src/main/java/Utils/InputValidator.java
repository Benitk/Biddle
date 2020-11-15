//package Utils;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class InputValidator {
//    public boolean isValidEmail(String string){
//        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(string);
//        return matcher.matches();
//    }
//
//    public boolean isValidPassword(String string){
//        String PATTERN = "^[a-zA-Z]\\w{5,19}$";
//
//
//        Pattern pattern = Pattern.compile(PATTERN);
//        Matcher matcher = pattern.matcher(string);
//        return matcher.matches();
//    }
//
//    public boolean isNullOrEmpty(String string){
//        return TextUtils.isEmpty(string);
//    }
//
//    public boolean isNumeric(String string){
//        return TextUtils.isDigitsOnly(string);
//    }
//
//}
//
//public class InputValidatorHelper {
//
//    //Add more validators here if necessary
//}