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

    //TODO Replace the GC- with the valu as defined in the GSDs

    public static void main(String argv[]) throws IOException {
        LogPrinter logPrinter = new LogPrinter();
        Utility util = new Utility();
        logPrinter.printLog();
        ArrayList<Profile> pcodesArr = new ArrayList<>();
        ArrayList<Profile> pscodeArr = new ArrayList<>();
        AppMain appMain = new AppMain();

        ArrayList<Profile> pcodesIsp = appMain.extractKeys("PCODES", "Ispecs");
        ArrayList<Profile> pcodesRep = appMain.extractKeys("PCODES", "Reports");
        pcodesArr.addAll(pcodesIsp);
        pcodesArr.addAll(pcodesRep);
        util.toExcel(pcodesArr, "PCODES");

        ArrayList<Profile> pscodeIsp = appMain.extractKeys("PSCODE", "Ispecs");
        ArrayList<Profile> pscodeRep = appMain.extractKeys("PSCODE", "Reports");
        pscodeArr.addAll(pscodeIsp);
        pscodeArr.addAll(pscodeRep);
        util.toExcel(pscodeArr, "PSCODE");


    }

    private ArrayList<Profile> extractKeys(String profile, String folder) {

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
                        String className = anClass.getName();
                        profileObj.setClaseName(className.replace(className.substring(className.length() - 4), ""));
                        profileObj.setFolder(folder);
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
                            profileObj.setKey1(util.key1Modifier(keyList.get(0)));
                            profileObj.setKey2(keyList.get(1));
                            profileObj.setKey3(keyList.get(2));
                            profileObj.setGcValue(util.gcFinder(keyList.get(1)));
                        } else if (keyList.size() == 2) {
                            profileObj.setKey1(util.key1Modifier(keyList.get(0)));
                            profileObj.setKey2(keyList.get(1));
                            profileObj.setKey3(" ");
                            profileObj.setGcValue(util.gcFinder(keyList.get(1)));
                        } else if (keyList.size() == 1) {
                            profileObj.setKey1(util.key1Modifier(keyList.get(0)));
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

        return profileObjList;

    }
}
