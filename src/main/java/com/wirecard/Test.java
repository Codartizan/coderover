package com.wirecard;

import com.wirecard.util.Utility;

import java.io.IOException;

/**
 * ${CLASS} Created by tshi on 11/10/2017.
 */
public class Test {

    public static void main(String argv[]) throws IOException {
        Utility util = new Utility();
        String s = util.gcFinder("GC-MP-ACCEL-RESN");

        System.out.println(s);

    }
}
