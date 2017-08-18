package http.okhttp;

/**
 * Created by Administrator on 2017/8/16.
 */

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 本类封装okhttp,我们只应该执行一次new OkHttpClient()，将其作为全局的实例进行保存，从而在App的各处都只使用这一个实例对象，这样所有的HTTP请求都可以共用Response缓存、共用线程池以及共用连接池。
 **/

public class OkHttpUtils {
    private static OkHttpClient okHttpClient;
    private static SSLSocketFactory sslSocketFactory;
    private static String TAG = "OkHttpUtils";


    /**
     * 针对http：不需要信任证书
     **/
    public static OkHttpClient getOkHttpForHttp() {

        if (okHttpClient == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            //创建okHttp对象
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging) //设置拦截器
                    .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(15, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//设置失败重连
                    .build();
        }
        return okHttpClient;

    }


    //针对https信任所有证书的okhttp
    public static OkHttpClient getOkHttpForHttps() {
        if (okHttpClient != null) {
            return okHttpClient;
        }
        if (getInstanceSSLSocketFactory() == null) {
            Log.e(TAG, "-----SSLSocketFactory is null");
        }

        //此处如此创建时为了方便设置参数
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(getInstanceSSLSocketFactory());//设置信任--类似于httpUrlConnection设置: ((HttpsURLConnection) connection).setSSLSocketFactory(sSslSocketFactory);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;//此处https信任所有证书
            }
        });
        //设置httpClient
        okHttpClient =
                builder.connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)//失败是否设置重连
                        .build();
        return okHttpClient;
    }


    //针对https信任cer证书
    public static OkHttpClient getOkHttpForHttps(Context context) {

        if (okHttpClient != null) {
            return okHttpClient;
        }

        if (getInstanceSSLSocketFactory(context) == null) {
            Log.e(TAG, "------SSLSocketFactory is null ");
        }
        //创建Builder--进行构建参数
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.sslSocketFactory(getInstanceSSLSocketFactory(context));
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.e(TAG, "****HostnameVerifier params hostName is:" + hostname + ",session:" + session);
                return true;//是否认证通过
            }
        });


        okHttpClient = builder.connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)//失败重连
                .build();

        return okHttpClient;
    }


    /**
     * --------------------------------------SSLSocketFactory:https-针对是否对cer证书敏感--------------------------------------------
     **/


    //针对https：但信任所有，不针对专门证书，对证书不敏感
    public static SSLSocketFactory getInstanceSSLSocketFactory() {
        Log.e(TAG, "****invoke has no keystore SSLSocketFactory");
        if (sslSocketFactory != null) {
            return sslSocketFactory;
        }
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};

            }
        }};
        //创建信任
        SSLContext sslContext = null;
        try {

            if (sslContext == null) {
                sslContext = SSLContext.getInstance("TLS");//可以构建ssl或tsl
                sslContext.init(null, trustManagers, new SecureRandom());

                //获取sockctfactory
                sslSocketFactory = sslContext.getSocketFactory();
                return sslSocketFactory;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //针对https：但需要信任专门证书，对证书敏感
    public static SSLSocketFactory getInstanceSSLSocketFactory(Context context) {

        if (sslSocketFactory != null) {
            return sslSocketFactory;
        }
        //创建读取cer认证类
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //将cer信任文件从asst读入---此处需要传入asset报下的cer证书名称
            //BufferedInputStream--带缓冲区的输出流，缓冲区大小为8M，能减少磁盘访问次数提高效率，对应输出流BufferedOutPutStream
            InputStream cerInput = new BufferedInputStream(context.getAssets().open("cer证书名称"));
            //创建cer信息实体类
            Certificate certificate;
            try {

                certificate = cf.generateCertificate(cerInput);//此处需要将读入证书的流读入
            } finally {
                cerInput.close();//关闭流
            }

            //创建keyStore
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            //设置信任工厂实体
            keyStore.setCertificateEntry("ca", certificate);


            //创建信任工厂
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);//将创建的keystore传入

            //创建上下文
            SSLContext sslContext = SSLContext.getInstance("TLS");//设定协议级别
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            //创建sslSocketFactory
            sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
