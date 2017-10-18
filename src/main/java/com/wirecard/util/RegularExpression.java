package com.wirecard.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ${CLASS}
 * Created by tshi on 6/07/2017.
 * Utility for extract Data
 */
public class RegularExpression {

    /*
    * First level extraction
    */
    public static String getValueByRegexPat(String inputStr, String regexPat) {

        String result = "";

        Pattern pat = Pattern.compile(regexPat, Pattern.CASE_INSENSITIVE);

        Matcher mat = pat.matcher(inputStr);

        if (mat.find()) result = mat.group(0);

        return result;

    }

    /*
    * Second level extraction
    */
    public static String extractData(String inputStr, String desireText, String regexPat) {

        String data = getValueByRegexPat(inputStr, desireText);

        return getValueByRegexPat(data, regexPat);

    }
}
