package com.ptu.util;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class j2ptest {
    public static void main(String[] args) {
        try {
            String[] args1 = new String[] { "python", "F:\\BigData\\src\\main\\java\\com\\ptu\\util\\hbase_py.py", "symptom"};
            Process proc = Runtime.getRuntime().exec(args1);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

