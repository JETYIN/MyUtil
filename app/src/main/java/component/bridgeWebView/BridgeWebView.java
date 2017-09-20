package component.bridgeWebView;

/**
 * Created by Administrator on 2017/8/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.reflect.Method;

/**
 * 封装js和android通信
 **/
public class BridgeWebView extends WebView {

    /***
     * js调用android方法的映射字符串
     **/
    private static final String JS_INTERFACE = "jsInterface";
    private String TAG = "BridgeWebView";

    public BridgeWebView(Context context) {
        super(context);
        //设置webview支持
        WebSettings settings = this.getSettings();
        setWebViewClient(new IternelWeb());//设置应用内的url跳转在webview中进行

    }

    public BridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public BridgeWebView(Context context, AttributeSet attrs, int def) {
        super(context, attrs, def);
    }


    /**
     * 注册js和android通信桥梁对象
     *
     * @param obj 桥梁类对象,该对象提供方法让js调用,默认开启JavaScriptEnabled=true
     */
    public void addBridgeInterface(Object obj) {//此处传入的是一个集中方法使用类,默认调用的是此方法
        this.getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new JavaJsBridgeObj(obj), JS_INTERFACE);
    }

    /**
     * 注册js和android通信桥梁对象
     *
     * @param obj 桥梁类对象,该对象提供方法让js调用
     * @param url 默认开启JavaScriptEnabled=true
     */
    public void addBridgeInterface(Object obj, String url) {
        this.getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new JavaJsBridgeObj(obj), JS_INTERFACE);
        loadUrl(url);
    }

    public void loadUrl() {

    }

    /**
     * 回调js方法
     *
     * @param json 参数，json格式字符串
     */
    public void callbackJavaScript(String json) {

    }

    private void invokeJavaScript(String callback, String params) {

    }

    //内置js--android通信桥梁类

    public class JavaJsBridgeObj {

        //需要传递的参数
        Object contentObj;
        //通过反射方法传递数据

        public JavaJsBridgeObj(Object obj) {
            this.contentObj = obj;
        }

        /**
         * 内置桥梁方法
         *
         * @param method 方法名--此处的方法名为js中传入，传入的方法名为"send"
         * @param json   js传递参数，json格式,声明此方法未js与android交互方法。此处是反射方法
         */
        @JavascriptInterface
        public void invokeBridgeMethod(String methodName, String[] json) {

            Log.e(TAG, "****invoke--invokeBridgeMethod");

            Class<?>[] params = new Class[]{String.class};//
            Method targetMethod = null;
            try {
                targetMethod = contentObj.getClass().getDeclaredMethod(methodName, params);//根据反射找到处理js-android交互方法
                targetMethod.invoke(contentObj, json);//反射调用js传递过来的方法，传参
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * 设置webview在应用内跳转-跳转将发生在webview中
     **/

    private class IternelWeb extends WebViewClient {

        /**实现对网页中超链接的拦截,此处url为超链接url，但是如果一个网页中有多个url，可通过loadResourece处理**/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
