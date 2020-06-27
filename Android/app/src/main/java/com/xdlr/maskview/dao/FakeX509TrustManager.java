package com.xdlr.maskview.dao;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

<<<<<<< HEAD

=======
>>>>>>> '测试'
/**
 * 对所有证书信任,屏蔽证书
 */

public class FakeX509TrustManager implements X509TrustManager {
<<<<<<< HEAD
    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
        //To change body of implemented methods use File | Settings | File Templates.
=======

    private static TrustManager[] mTrustManagers;
    private static final X509Certificate[] mAcceptedIssuers = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
>>>>>>> '测试'
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws java.security.cert.CertificateException {
<<<<<<< HEAD
        //To change body of implemented methods use File | Settings | File Templates.
=======
>>>>>>> '测试'
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
<<<<<<< HEAD
        return _AcceptedIssuers;
=======
        return mAcceptedIssuers;
>>>>>>> '测试'
    }

    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
<<<<<<< HEAD

=======
>>>>>>> '测试'
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                // TODO Auto-generated method stub
                return true;
            }
<<<<<<< HEAD

        });

        SSLContext context = null;
        if (trustManagers == null) {
            trustManagers = new TrustManager[] { new FakeX509TrustManager() };
        }

        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }}
=======
        });
        SSLContext context = null;
        if (mTrustManagers == null) {
            mTrustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, mTrustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
}
>>>>>>> '测试'
