package com.sinosoft.ops.cimp.export.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于处理Runtime.getRuntime().exec产生的错误流及输出流
 */
public class StreamGobbler extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(StreamGobbler.class);

    InputStream is;
    String type;
    OutputStream os;

    public StreamGobbler(InputStream is, String type) {
        this(is, type, null);
    }

    public StreamGobbler(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            if (os != null)
                pw = new PrintWriter(os);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (pw != null)
                    pw.println(line);
                logger.debug(type + " >>> " + line);
            }
            if (pw != null)
                pw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (pw != null)
                    pw.close();
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
