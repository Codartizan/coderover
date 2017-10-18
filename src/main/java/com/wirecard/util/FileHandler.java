package com.wirecard.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class FileHandler {

    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    /*
   * Get file list from a dir
    */
    public ArrayList<File> getFileList(String dir) {
        // get file list where the path has
        File file = new File(dir);
        // get the folder list
        File[] array = file.listFiles();
        ArrayList<File> arrFile = new ArrayList<>();
        assert array != null;
        for (File anArray : array) {
            if (anArray.isFile()) {
                arrFile.add(anArray);
            } else if (anArray.isDirectory()) {
                getFileList(anArray.getPath());
            }
        }
        return arrFile;
    }

    /*
    * Read txt file into String ArrayList
     */
    public static ArrayList<String> txtToArray(File file) {
        ArrayList<String> lineArray = new ArrayList<>();

        try {
            String encoding = "GBK";
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);// 考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    lineArray.add(lineTxt);
                }
                bufferedReader.close();
                read.close();
            } else {
                logger.error("Cannot find target file; File name:" + file.getName());
            }

        } catch (Exception e) {
            logger.error("Read File " + file.getName() + "errors");
            e.printStackTrace();
        }
        return lineArray;
    }
}
