package com.github.alog;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 */
public class AUtil {

    /**
     * @return Current Process ID
     */
    public static int myPid() {
        return android.os.Process.myPid();
    }

    /**
     * Get App name(package name) name by current process id.
     *
     * @return App Name, ex) com.api.demo
     */
    public static String getMyAppName() {
        return getMyAppName(myPid());
    }

    /**
     * Get AppName(package name) Name by specific process id.
     *
     * @return App Name, ex) com.api.demo
     */
    public static String getMyAppName(int pid) {
        final String PATH = "/proc/" + pid + "/cmdline";

        BufferedReader cmdlineReader = null;
        StringBuilder processName = new StringBuilder();
        int c = 0;

        try {
            cmdlineReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(PATH)));

            while ((c = cmdlineReader.read()) > 0) {
                processName.append((char)c);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (cmdlineReader != null)
                    cmdlineReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return processName.toString();
    }
}
