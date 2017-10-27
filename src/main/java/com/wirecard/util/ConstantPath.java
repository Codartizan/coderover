package com.wirecard.util;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class ConstantPath {
    private static Utility util = new Utility();
    private static String propFile = System.getProperty("user.home") + "\\Documents\\AUTOMATION\\coderover\\src\\main\\resources\\path";

    public static final String LOG =System.getProperty("user.home") +  util.getPropertyValue(propFile, "LOG");
    public static final String GSD = util.getPropertyValue(propFile, "GSD");
}
