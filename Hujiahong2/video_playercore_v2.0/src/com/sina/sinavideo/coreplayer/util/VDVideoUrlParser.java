package com.sina.sinavideo.coreplayer.util;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.ArrayList;

/**
 * 对新浪视频url进行解析
 * 
 * @author GengHongchao
 */
public class VDVideoUrlParser {

    private static final String TAG = "VideoUrlParser";
    private static final String tag = "VideoUrlParser";
    private String finalUrl;
    private DefaultHttpClient sHttpClient;
    private RedirectHandler mRedirectHandler;
    private VideoParseCallBack mVideoParseCallBack;
    private ParseAsyncTask task;
    private ArrayList<String> redirectUrls;
    @SuppressLint("unused")
    // private Context mContext;
    /**
     * 解析mp4错误
     */
    public static final int ERROR_MP4_PARSE = 1;

    private static final int CONNETED_TIMEOUT = 20;

    public interface VideoParseCallBack {

        /**
         * 解析开始前准备工作
         */
        void onParsePrepared();

        /**
         * 解析结束
         * 
         * @param finalUrl
         *            最终url
         */
        void onParseComplete(String finalUrl);

        /**
         * 解析中，获取相应的viask_content头信息，然后返回设置相应的日志数据<br/>
         * NOTE:奇葩的需求
         * 
         * @param viaskContent
         */
        void onParseVIAskContent(String viaskContent);

        /**
         * 解析错误
         * 
         * @param error_msg
         *            对应错误信息值
         */
        void onParseError(int error_msg);

        /**
         * 通过RedirectHandler获取对应的url
         * 
         * @param url
         */
        void onGetLocationUri(String url);
    }

    // public VDVideoUrlParser(VideoParseCallBack callBack, String _vid, Context
    // ctt) {
    // this.mVideoParseCallBack = callBack;
    // redirectUrls = new ArrayList<String>();
    // if (sHttpClient == null) {
    // createRedirectHandler();
    // sHttpClient = createHttpClient();
    // sHttpClient.setRedirectHandler(mRedirectHandler);
    // }
    // mContext = ctt;
    // }

    public VDVideoUrlParser(VideoParseCallBack callBack, String _vid) {
        this.mVideoParseCallBack = callBack;
        redirectUrls = new ArrayList<String>();
        this.mVideoParseCallBack.onParsePrepared();
        if (sHttpClient == null) {
            createRedirectHandler();
            sHttpClient = createHttpClient();
            sHttpClient.setRedirectHandler(mRedirectHandler);
        }
    }

    public VDVideoUrlParser(VideoParseCallBack callBack) {
        this.mVideoParseCallBack = callBack;
        redirectUrls = new ArrayList<String>();
        this.mVideoParseCallBack.onParsePrepared();
        if (sHttpClient == null) {
            createRedirectHandler();
            sHttpClient = createHttpClient();
            sHttpClient.setRedirectHandler(mRedirectHandler);
        }
    }

    public void cancel() {
        if (task != null) {
            task.cancel(true);
        }
    }

    public void startParser(String url) {
        if (task != null) {
            task.cancel(true);
        }
        task = new ParseAsyncTask(url);
        task.execute();
    }

    public class ParseAsyncTask extends AsyncTask<Void, Void, Void> {

        private String mUrl;

        public ParseAsyncTask(String url) {
            mUrl = url;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mVideoParseCallBack != null) {
                mVideoParseCallBack.onParseComplete(finalUrl);
            }
        }

        private Void retryConnect() {

            HttpGet request = null;
            try {
                LogS.d(TAG, "parse url " + mUrl);

                request = new HttpGet(mUrl);
                // request.addHeader("User-Agent",
                // "sinavideo/2.0.1 CFNetwork/672.0.2 Darwin/14.0.0");
                // request.setHeader("Accept-Encoding", "gzip, deflate");
                // request.setHeader("Accept-Language", "zh-cn");
                // request.setHeader("Accept", "*/*");
                if (sHttpClient == null) {
                    sHttpClient = createHttpClient();
                }
                HttpResponse response = sHttpClient.execute(request);

                int statusCode = response.getStatusLine().getStatusCode();
                LogS.w(TAG, " retryConnect statusCode = " + statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    if (response.getEntity() != null) {
                        LogS.d(TAG, "status OK, read file");
                        // return parse(response.getEntity().getContent());
                    } else {
                        LogS.e(TAG, "parse error"+statusCode);
                        return null; // 提示错误
                    }
                } else {
                    LogS.e(TAG, "parse error");

                    logNoliveGetRealMp4();
                    return null; // 提示错误
                }
            } catch (ClientProtocolException e) {
                LogS.e(TAG, "ClientProtocolException " + e);
                logNoliveGetRealMp4();
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                LogS.e(TAG, "ConnectTimeoutException " + e);
                logNoliveGetRealMp4();
                e.printStackTrace();
            } catch (ConnectException e) {
                LogS.e(TAG, "ConnectException " + e);
                logNoliveGetRealMp4();
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                LogS.e(TAG, "SocketTimeoutException " + e);
                logNoliveGetRealMp4();
                e.printStackTrace();
            } catch (SocketException e) {
                LogS.e(TAG, "SocketException " + e);
                logNoliveGetRealMp4();
                e.printStackTrace();
            } catch (Exception e) {
                LogS.e(TAG, "IOException " + e);
                logNoliveGetRealMp4();
                e.printStackTrace();
            } finally {
            }
            return null;

        }

        @Override
        protected Void doInBackground(Void... params) {
            return retryConnect();
        }

    }

    private void logNoliveGetRealMp4() {
        mVideoParseCallBack.onParseError(ERROR_MP4_PARSE);
    }

    private void createRedirectHandler() {
        mRedirectHandler = new RedirectHandler() {

            @Override
            public boolean isRedirectRequested(HttpResponse res, HttpContext arg1) {
                LogS.i(tag, "isRedirectRequested code = " + res.getStatusLine().getStatusCode());
                LogS.i(tag, "isRedirectRequested content_type = " + res.getEntity().getContentType());
                return res.getStatusLine().getStatusCode() == 302;
            }

            @Override
            public URI getLocationURI(HttpResponse res, HttpContext arg1) throws ProtocolException {
                // @add by sunxiao1
                // 添加信息部需要的channel_id信息
                Header[] iaskHeader = res.getHeaders("viask_content");
                if (iaskHeader != null && iaskHeader.length > 0) {
                    mVideoParseCallBack.onParseVIAskContent(iaskHeader[0].getValue());
                }
                // @add end
                Header[] headers = res.getHeaders("Location");
                if (headers != null && headers.length > 0) {
                    finalUrl = headers[0].getValue();
                    redirectUrls.add(finalUrl);
                    mVideoParseCallBack.onGetLocationUri(finalUrl);
                    LogS.i(tag, "getLocationURI location = " + headers[0].getValue());
                }
                return URI.create(finalUrl);
            }
        };
    }

    private DefaultHttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
        HttpProtocolParams.setUseExpectContinue(params, true);
        HttpConnectionParams.setConnectionTimeout(params, CONNETED_TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, CONNETED_TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        ConnManagerParams.setMaxTotalConnections(params, 4);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);

        return new DefaultHttpClient(connMgr, params);
    }
}
