package org.igetwell.common.uitls;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class HttpUtils {

    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder ret;
            br = request.getReader();

            String line = br.readLine();
            if (line != null) {
                ret = new StringBuilder();
                ret.append(line);
            } else {
                return "";
            }

            while ((line = br.readLine()) != null) {
                ret.append('\n').append(line);
            }

            return ret.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
