package com.wirecard.util;

import com.wirecard.pojo.Profile;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class Utility {

    /*
    * Get Property value base on property key.
    */
    public String getPropertyValue(String propFile, String key) {

        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(propFile));
            props.load(in);
            //logger.debug(key+value);
            in.close();
            return props.getProperty(key);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void repPropertyValue(String propFile, String key, String newValue) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(propFile));
            props.load(in);
            //logger.debug(key+value);
            in.close();
            props.setProperty(key, newValue);
            FileOutputStream fos = new FileOutputStream(propFile);
            props.store(fos, "RE");
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    * Wipe off comments from String
     */
    public String wipeOffComment(String str) {
        if (str.contains(":")) {
            int i = str.indexOf(":");
            String newValue = str.substring(i, str.length());
            str = str.replace(newValue, "");
            //System.out.println(str);
            return str;
        } else {
            return str;
        }
    }

    /*
    * Extract KEYs from the brackets.
     */
    public ArrayList<String> strToList(String str) {
        ArrayList<String> lsStr = new ArrayList<>();
        String[] strArr = str.split(" ");

        for(String anStrArr : strArr) {
            if (anStrArr.length() > 2) {
                if (anStrArr.contains("(")) {
                    anStrArr = anStrArr.replace("(", "");
                } else if (anStrArr.contains(")")) {
                    anStrArr = anStrArr.replace(")", "");
                } else if (anStrArr.contains("(") && anStrArr.contains(")")) {
                    anStrArr = anStrArr.replace(")", "").replace("(", "");
                }
                lsStr.add(anStrArr);
            }
        }

        return lsStr;
    }

    public void toExcel(ArrayList<Profile> profileObjList, String outName) {

        HSSFWorkbook workbook=new HSSFWorkbook();
        HSSFSheet sheet=workbook.createSheet();
        HSSFRow header=sheet.createRow(0);

        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        HSSFCell cell=header.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Class Name");

        cell = header.createCell(1);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Line Number");

        cell = header.createCell(2);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Key 1");

        cell = header.createCell(3);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Key 2");

        cell = header.createCell(4);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Key 3");

        cell = header.createCell(5);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("GC-Value");

        cell = header.createCell(6);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("Folder");

        for (int i = 0; i < profileObjList.size(); i++) {
            HSSFRow row = sheet.createRow(i + 1);
            Profile profileObj = profileObjList.get(i);

            row.createCell(0).setCellValue(profileObj.getClaseName());
            row.createCell(1).setCellValue(profileObj.getLineNum());
            row.createCell(2).setCellValue(profileObj.getKey1());
            row.createCell(3).setCellValue(profileObj.getKey2());
            row.createCell(4).setCellValue(profileObj.getKey3());
            row.createCell(5).setCellValue(profileObj.getGcValue());
            row.createCell(6).setCellValue(profileObj.getFolder());

        }

        try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\tshi\\Desktop\\" + outName+ ".xls");
            workbook.write(fos);
            fos.close();
            System.out.println("Output Successful!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Output failed!");
        }

    }

    /*
    * If Key 1 doesn't equal GLB.ZEROS,
    * Replace it with BANK.NUMBER
     */
    public String key1Modifier(String key1) {

        String newKey1;
        if (!key1.equals("GLB.ZEROS")) {
            newKey1 = "BANK.NUMBER";
        } else {
            newKey1 = key1;
        }

        return newKey1;
    }

    /*
    * If Key value type is a GC
    * Extract out the corresponding value from GLOBALSD.GSD file
     */
    public String gcFinder (String gcKey) {

        String gcValue = "";
        File gsd = new File(ConstantPath.GSD);
        ArrayList<String> gsdArr = FileHandler.txtToArray(gsd);
        if (gcKey.substring(0, 3).equals("GC-")) {
            for (String aGsdArr : gsdArr) {
                if (aGsdArr.contains(gcKey)) {
                    gcValue = RegularExpression.getValueByRegexPat(aGsdArr, "(?<=\\()[^\\)]+");
                }
            }
        } else {
            gcValue = "";
        }

        return gcValue;
    }
}
