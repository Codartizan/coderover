package com.wirecard.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class LogPrinter {

    public void printLog() throws FileNotFoundException {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String strTime = dateFormat.format(now);
        String logName = "CODEROVER_LOG_" + strTime;
        File log = new File(ConstantPath.LOG + logName);
        try {
            if (!log.createNewFile()) {
                log = new File(ConstantPath.LOG + logName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintStream report = new PrintStream(new FileOutputStream(log));
        System.setOut(report);
    }
}
