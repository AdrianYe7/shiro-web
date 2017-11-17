package com.xyz.main;

import org.junit.Test;

public class StringTest {

    @Test
    public void test() {
        String a = "*a" ;
        String[] ss = a.split("\\*");
        System.out.println(ss.length);
        for(String s : ss) {
            System.out.println(s.equals(""));
        }
    }
}
