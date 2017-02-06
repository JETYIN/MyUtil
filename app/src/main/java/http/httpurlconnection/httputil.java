package http.httpurlconnection;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by Administrator on 2017/1/6.
 */
public class httputil {

    private static SSLSocketFactory sslSocketFactory;

    /**
     * 在使用android原生的网络请求进行网络连接https需要进行安全整数ca的验证
     **/

    //在每一次进行网络请求时都需要进行验证
    public static synchronized boolean buildHttpsCa(Context context) {

        if (null == sslSocketFactory) {
            return false;
        }
        try {
            //信任工厂
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            //从项目中通过io流的形式读入证书
            InputStream inputStream = new BufferedInputStream(context.getAssets().open("tjut.cer"));
            Certificate certificate;
            try {
                //实例化证书
                certificate = certificateFactory.generateCertificate(inputStream);
            } finally {
                //关闭流
                inputStream.close();
            }

            //创建keystore信任证书

            String keyStroeType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStroeType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", certificate);
            //创建信任管理

            String trustType = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(trustType);
            tmf.init(keyStore);

            //创建ssl
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, tmf.getTrustManagers(), null);

            sslSocketFactory = sslContext.getSocketFactory();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
