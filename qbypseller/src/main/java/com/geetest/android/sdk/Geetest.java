package com.geetest.android.sdk;

import android.text.TextUtils;
import android.util.Log;

import cn.net.chenbao.qbypseller.bean.GeeTestBean;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Geetest {

    private String captchaURL;
    private String validateURL;

    private String CaptchaId;
    private String ClientId;
    private String challenge;
    private int success;
    private CookieManager cookieManager;
    private HttpURLConnection mReadConnection;
    private HttpURLConnection mSubmitConneciton;
    private HttpsURLConnection mSSLReadConnection;
    private HttpsURLConnection mSSLSubmitConnection;
    private Timer timer;
    private int responseCode;
    private int mTimeout = 10000;//默认10000ms

    public Boolean isOperating;

    public Geetest(String captchaURL, String validateURL) {
        this.captchaURL = captchaURL;
        this.validateURL = validateURL;
    }

    public String getGt() {
        return this.CaptchaId;
    }

    public String getClientId() {
        return this.ClientId;
    }

    public String getChallenge() {
        return this.challenge;
    }

    public boolean getSuccess() {
        return success == 1;
    }

    public void setTimeout(int timeout) {
        mTimeout = timeout;
    }

    public void cancelReadConnection() {
        if (isOperating) {
            mReadConnection.disconnect();
            isOperating = false;
        }
    }

    public interface GeetestListener {
        void readContentTimeout();

        void submitPostDataTimeout();
    }

    private GeetestListener geetestListener;

    public void setGeetestListener(GeetestListener listener) {
        geetestListener = listener;
    }

    public boolean checkServer() {

        try {

            String info = readContentFromGet(captchaURL);

            if (info.length() > 0) {

                Log.i("Geetest", "checkServer: " + info);

                GeeTestBean GeeTestBean = com.alibaba.fastjson.JSONObject.parseObject(info, GeeTestBean.class);
                CaptchaId = GeeTestBean.GeeValidResult.Data.CaptchaId;
                ClientId = GeeTestBean.GeeValidResult.Data.ClientId;
                challenge = GeeTestBean.GeeValidResult.Data.Challenge;
                success = GeeTestBean.GeeValidResult.Data.Success;

                if (CaptchaId.length() == 32) {
                    return getSuccess();
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    private String readContentFromGet(String getURL) throws IOException {

        isOperating = true;

        URL url = new URL(getURL);

        if ("https".equals(url.getProtocol().toLowerCase())) {

            final StringBuffer sBuffer = new StringBuffer();

            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (responseCode != HttpURLConnection.HTTP_OK || sBuffer.toString().length() == 0) {
                        if (mSSLReadConnection != null) {
                            mSSLReadConnection.disconnect();
                        }
                        isOperating = false;
                        if (geetestListener != null) {
                            geetestListener.readContentTimeout();
                        }
                    }
                }
            };
            timer.schedule(timerTask, mTimeout, 1);

            try {

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrustAllManager()}, null);

                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });

                HttpsURLConnection sslReadConnection = (HttpsURLConnection) url.openConnection();
                mSSLReadConnection = sslReadConnection;

                cookieManager = new CookieManager();

                Map<String, List<String>> headerFields = sslReadConnection.getHeaderFields();
                if (headerFields.get("Set-Cookie") != null) {
                    List<String> cookiesHeader = headerFields.get("Set-Cookie");
                    if (cookiesHeader != null) {
                        for (String cookie : cookiesHeader) {
                            cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                        }
                    }
                }

                sslReadConnection.setDoInput(true);
                sslReadConnection.setDoOutput(false);
                sslReadConnection.setRequestMethod("GET");

                sslReadConnection.setConnectTimeout(mTimeout / 2);

                sslReadConnection.setReadTimeout(mTimeout / 2);

                sslReadConnection.connect();

                byte[] buf = new byte[1024];

                InputStream inStream = sslReadConnection.getInputStream();

                for (int n; (n = inStream.read(buf)) != -1; ) {

                    sBuffer.append(new String(buf, 0, n, "UTF-8"));

                }

                inStream.close();

                responseCode = sslReadConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    timer.cancel();
                    timer.purge();

                    return sBuffer.toString();
                }

                if (responseCode == HttpsURLConnection.HTTP_CLIENT_TIMEOUT || responseCode == -1) {
                    if (geetestListener != null) {
                        geetestListener.readContentTimeout();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {
                mSSLReadConnection.disconnect();
                isOperating = false;
            }

        } else {

            final StringBuffer sBuffer = new StringBuffer();

            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (responseCode != HttpURLConnection.HTTP_OK || sBuffer.toString().length() == 0) {
                        if (mReadConnection != null) {
                            mReadConnection.disconnect();
                        }
                        isOperating = false;
                        if (geetestListener != null) {
                            geetestListener.readContentTimeout();
                        }
                    }
                }
            };
            timer.schedule(timerTask, mTimeout, 1);

            HttpURLConnection readConnection = (HttpURLConnection) url.openConnection();
            mReadConnection = readConnection;

            cookieManager = new CookieManager();

            Map<String, List<String>> headerFields = readConnection.getHeaderFields();
            if (headerFields.get("Set-Cookie") != null) {
                List<String> cookiesHeader = headerFields.get("Set-Cookie");
                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        cookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }
            }

            try {

                readConnection.setConnectTimeout(mTimeout / 2);

                readConnection.setReadTimeout(mTimeout / 2);

                readConnection.connect();

                byte[] buf = new byte[1024];

                InputStream inStream = readConnection.getInputStream();

                for (int n; (n = inStream.read(buf)) != -1; ) {

                    sBuffer.append(new String(buf, 0, n, "UTF-8"));

                }

                inStream.close();

                responseCode = readConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    timer.cancel();
                    timer.purge();
                    return sBuffer.toString();
                }

                if (responseCode == HttpsURLConnection.HTTP_CLIENT_TIMEOUT || responseCode == -1) {
                    if (geetestListener != null) {
                        geetestListener.readContentTimeout();
                    }
                }

            } catch (EOFException e) {

                e.printStackTrace();

            } finally {
                mReadConnection.disconnect();
                isOperating = false;
            }
        }

        return "";
    }

    private URL getValidateURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (Exception e) {

        }
        return null;
    }

    public String submitPostData(Map<String, String> params, String encode) {

        isOperating = true;

        URL url = getValidateURL(validateURL);

        if ("https".equals(url.getProtocol().toLowerCase())) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        if (mSSLSubmitConnection != null) {
                            mSSLSubmitConnection.disconnect();
                        }
                        isOperating = false;
                        if (geetestListener != null) {
                            geetestListener.submitPostDataTimeout();
                        }
                    }
                }
            };
            timer.schedule(timerTask, mTimeout, 1);

            byte[] data = getRequestData(params, encode).toString().getBytes();

            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrustAllManager()}, null);

                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });

                HttpsURLConnection sslSubmitConnection = (HttpsURLConnection) url.openConnection();
                mSSLSubmitConnection = sslSubmitConnection;

                if (cookieManager.getCookieStore().getCookies().size() > 0) {
                    sslSubmitConnection.setRequestProperty("Cookie",
                            TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }

                sslSubmitConnection.setConnectTimeout(mTimeout);
                sslSubmitConnection.setDoInput(true);
                sslSubmitConnection.setDoOutput(true);
                sslSubmitConnection.setRequestMethod("POST");
                sslSubmitConnection.setUseCaches(false);

                sslSubmitConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                sslSubmitConnection.setRequestProperty("Content-Length",
                        String.valueOf(data.length));

                OutputStream outputStream = sslSubmitConnection.getOutputStream();
                outputStream.write(data);

                int response = sslSubmitConnection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    timer.cancel();
                    timer.purge();
                    InputStream inptStream = sslSubmitConnection.getInputStream();
                    return dealResponseResult(inptStream);
                }

                if (response == -1) {
                    if (geetestListener != null) {
                        geetestListener.submitPostDataTimeout();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                mSSLSubmitConnection.disconnect();

                isOperating = false;
            }

        } else {

            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        if (mSubmitConneciton != null) {
                            mSubmitConneciton.disconnect();
                        }
                        isOperating = false;
                        if (geetestListener != null) {
                            geetestListener.submitPostDataTimeout();
                        }
                    }
                }
            };
            timer.schedule(timerTask, mTimeout, 1);

            byte[] data = getRequestData(params, encode).toString().getBytes();

            try {
                HttpURLConnection submitConnection = (HttpURLConnection) url.openConnection();
                mSubmitConneciton = submitConnection;

                if (cookieManager.getCookieStore().getCookies().size() > 0) {
                    submitConnection.setRequestProperty("Cookie",
                            TextUtils.join(";", cookieManager.getCookieStore().getCookies()));
                }

                submitConnection.setConnectTimeout(mTimeout);
                submitConnection.setDoInput(true);
                submitConnection.setDoOutput(true);
                submitConnection.setRequestMethod("POST");
                submitConnection.setUseCaches(false);

                submitConnection.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");

                submitConnection.setRequestProperty("Content-Length",
                        String.valueOf(data.length));

                OutputStream outputStream = submitConnection.getOutputStream();
                outputStream.write(data);

                int response = submitConnection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    timer.cancel();
                    timer.purge();
                    InputStream inptStream = submitConnection.getInputStream();
                    return dealResponseResult(inptStream);
                }

                if (response == -1) {
                    if (geetestListener != null) {
                        geetestListener.submitPostDataTimeout();
                    }
                }

            } catch (IOException e) {

                e.printStackTrace();

            } finally {

                mSubmitConneciton.disconnect();

                isOperating = false;
            }
        }
        return "";
    }

    private StringBuffer getRequestData(Map<String, String> params,
                                        String encode) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    private String dealResponseResult(InputStream inputStream) {
        String resultData;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    public class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
