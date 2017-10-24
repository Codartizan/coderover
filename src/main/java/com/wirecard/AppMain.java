package com.wirecard;

import com.wirecard.pojo.Profile;
import com.wirecard.util.FileHandler;
import com.wirecard.util.LogPrinter;
import com.wirecard.util.RegularExpression;
import com.wirecard.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class AppMain {

    //TODO Combind the CODES - ispecs and reports in on spreadsheet; same for SCODE
    //TODO Replace the GC- with the valu as defined in the GSDs
    //TODO Drop the ".isp" and ".report" off the class name
    //TODO where key 1 does not GLB.ZEROS can you replace the value with "Bank Number"

    public static void main(String argv[]) throws IOException {
        LogPrinter logPrinter = new LogPrinter();
        logPrinter.printLog();

        AppMain appMain = new AppMain();
        appMain.extractKeys("PCODES", "Ispecs");
        appMain.extractKeys("PCODES", "Reports");
        appMain.extractKeys("PSCODE", "Ispecs");
        appMain.extractKeys("PSCODE", "Reports");

    }

    private void extractKeys(String profile, String folder) {

        String dir = "C:\\STD-07.05\\" + folder;
        FileHandler fh = new FileHandler();
        ArrayList<File> classList = fh.getFileList(dir);
        ArrayList<Profile> profileObjList = new ArrayList<>();
        Utility util = new Utility();

        for (File anClass : classList) {
            ArrayList<String> lineStrList;
            lineStrList = FileHandler.txtToArray(anClass);
            for (int i = 0; i < lineStrList.size(); i++) {
                String lineStr = lineStrList.get(i);
                if (lineStr.contains(profile) && lineStr.contains("(")) {
                    Profile profileObj = new Profile();
                    int iPcodes = lineStr.indexOf(profile);
                    int iColon = lineStr.indexOf(":");
                    if (iColon < 0 || iColon > iPcodes) {

                        profileObj.setClaseName(anClass.getName());
                        profileObj.setLineNum(lineStrList.indexOf(lineStr) + 1);
                        String newAnLsStr = util.wipeOffComment(lineStr);

                        if (lineStr.contains("(") && !lineStr.contains(")")) {
                            if (lineStrList.get(i + 1).contains(")") && !lineStrList.get(i + 1).substring(0, 1).equals(":")) {
                                lineStr = newAnLsStr + util.wipeOffComment(lineStrList.get(i + 1));

                            } else {
                                lineStr = newAnLsStr + util.wipeOffComment(lineStrList.get(i + 1)) + util.wipeOffComment(lineStrList.get(i + 2));

                            }
                        }

                        String value = RegularExpression.getValueByRegexPat(lineStr, "(\\([^\\)]+\\))");
                        ArrayList<String> keyList = util.strToList(value);
                        if (keyList.size() == 3) {
                            profileObj.setKey1(keyList.get(0));
                            profileObj.setKey2(keyList.get(1));
                            profileObj.setKey3(keyList.get(2));
                        } else if (keyList.size() == 2) {
                            profileObj.setKey1(keyList.get(0));
                            profileObj.setKey2(keyList.get(1));
                            profileObj.setKey3(" ");
                        } else if (keyList.size() == 1) {
                            profileObj.setKey1(keyList.get(0));
                            profileObj.setKey2(" ");
                            profileObj.setKey3(" ");
                        }
                        //System.out.println(value);

                        if (profileObj.getKey1().length() > 2) {
                            if (!profileObj.getKey1().substring(0, 2).equals("M)")) {
                                profileObjList.add(profileObj);
                            }
                        }

                        //profileObjList.add(profileObj);
                    }
                    //profileObjList.add(profileObj);
                }
            }
        }

        for (Profile anProfile : profileObjList) {

            System.out.println("|-- Class Name: " + anProfile.getClaseName());
            System.out.println("     Line Number: " + anProfile.getLineNum());
            System.out.println("      Key 1: " + anProfile.getKey1());
            System.out.println("       Key 2: " + anProfile.getKey2());
            System.out.println("        Key 3: " + anProfile.getKey3());
        }
        System.out.println("The total volume of " + profile + " is " + profileObjList.size());

        util.toExcel(profileObjList, profile + "_IN_" + folder);

    }
}
