package com.wirecard;

import com.wirecard.util.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class Test {

    public static void main(String argv[]) throws IOException {
        String dir = "C:\\STD-07.05\\Reports";
        FileHandler fh = new FileHandler();
        ArrayList<File> classList = fh.getFileList(dir);

        for (File anClass : classList) {
            ArrayList<String> lineStrList;
            lineStrList = FileHandler.txtToArray(anClass);
            for(String lineStr : lineStrList) {
                if (lineStr.contains("PCODES")) {
                    System.out.println("|-- " + anClass.getName());
                    System.out.println("  |-- " + classList.indexOf(anClass));
                    System.out.println();
                }
            }
        }

    }
}
