package com.jiangtao.shuzicai.basic.network;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/**
 * Created by Nicky on 2017/1/23.
 * 比目上传文件
 */

public class BmobUpload {


    private static boolean IS_INIT = false;
    private static int TIME_OUT = 10000;

    private static String STRING_EMPTY = "";
    private static String APP_ID = STRING_EMPTY;
    private static String REST_API_KEY = STRING_EMPTY;
    private static String MASTER_KEY = STRING_EMPTY;

    private static final String BMOB_APP_ID_TAG = "X-Bmob-Application-Id";
    private static final String BMOB_REST_KEY_TAG = "X-Bmob-REST-API-Key";
    private static final String BMOB_MASTER_KEY_TAG = "X-Bmob-Master-Key";
    private static final String BMOB_EMAIL_TAG = "X-Bmob-Email";
    private static final String BMOB_PASSWORD_TAG = "X-Bmob-Password";
    private static final String CONTENT_TYPE_TAG = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_DELETE = "DELETE";

    private static final String UTF8 = "UTF-8";
    private static final String CHAR_RISK = ":";

    public static final String MSG_NOT_FOUND = "Not Found";
    public static final String MSG_FILE_NOT_FOUND = "file Not Found";
    public static final String MSG_ERROR = "Error";
    public static final String MSG_UNREGISTERED = "Unregistered";


    /**
     * 初始化Bmob
     *
     * @param appId   填写 Application ID
     * @param restKey 填写 REST API Key
     * @param timeout 设置超时＿1000~20000ms＿
     * @return 注册结果
     */
    public static boolean initBmob(String appId, String restKey, int timeout) {
        APP_ID = appId;
        REST_API_KEY = restKey;
        if (!APP_ID.equals(STRING_EMPTY) && !REST_API_KEY.equals(STRING_EMPTY)) {
            IS_INIT = true;
        }
        if (timeout > 1000 && timeout < 20000) {
            TIME_OUT = timeout;
        }
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, null, null);
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            IS_INIT = false;
        }
        return isInit();
    }

    private static HttpURLConnection connectionCommonSetting(HttpURLConnection conn, URL url, String method) throws
            IOException {
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setReadTimeout(TIME_OUT);

        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);

        conn.setRequestProperty(BMOB_APP_ID_TAG, APP_ID);
        conn.setRequestProperty(BMOB_REST_KEY_TAG, REST_API_KEY);
        if (!MASTER_KEY.equals(STRING_EMPTY)) {
            conn.setRequestProperty(BMOB_MASTER_KEY_TAG, MASTER_KEY);
        }

        conn.setRequestProperty(CONTENT_TYPE_TAG, CONTENT_TYPE_JSON);
        return conn;
    }


    private static String getResultFromConnection(HttpURLConnection conn)
            throws UnsupportedEncodingException, IOException {
        StringBuffer result = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                conn.getInputStream(), UTF8));
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        reader.close();
        return result.toString();
    }

    /**
     * 是否初始化Bmob
     *
     * @return 初始化结枿
     */
    public static boolean isInit() {
        return IS_INIT;
    }

    /**
     * * 文件上传
     *
     * @param file     file 完整路径文件
     * @param callBack
     */
    public static void uploadFile(String file, IUpLoadResult callBack) {
        String result = STRING_EMPTY;
        if (isInit()) {
            HttpURLConnection conn = null;
            // 获取文件吿
            String fileName = file.trim();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            String mURL = "https://api.bmob.cn/2/files/" + fileName;
            try {
                FileInputStream fis = new FileInputStream(file);
                conn = connectionCommonSetting(conn, new URL(mURL), METHOD_POST);
                conn.setDoOutput(true);
                conn.connect();
                // 丢多个字节
                byte[] tempbytes = new byte[1];
                OutputStream os = conn.getOutputStream();
                while ((fis.read(tempbytes)) != -1) {
                    os.write(tempbytes);
                }
                os.flush();
                os.close();
                fis.close();
                result = getResultFromConnection(conn);
                conn.disconnect();
            } catch (FileNotFoundException e) {
                result = MSG_FILE_NOT_FOUND + CHAR_RISK + e.getMessage();
            } catch (Exception e) {
                result = MSG_ERROR + CHAR_RISK + e.getMessage();
            }
        } else {
            result = MSG_UNREGISTERED;
        }
        callBack.onUpLoadResult(result);
    }

    public interface IUpLoadResult {
        void onUpLoadResult(String result);
    }
}
