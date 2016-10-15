
package com.sina.sinavideo.sdk.plugin.sina_adv;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.monitor.VDMonitorData;
import com.sina.sinavideo.sdk.utils.VDMonitorManager;
import com.sina.sinavideo.sdk.utils.VDSDKAdvManager.VDAdvRequestListener;
import com.sina.sinavideo.sdk.utils.VDSDKAdvManager.VDSDKAdvRequest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

/**
 * 广告的请求类
 * 
 * @author alexsun
 */
public class VDAdvRequest extends VDSDKAdvRequest {

    private VDAdvParser mParser = new VDAdvParser();
    private Context mContext = null;
    private VDAdvRequestAsyncTask mTask = null;
    private VDAdvRequestData mReqData = null;
    private boolean mIsDebug = false;

    private VDAdvRequestListener mListener = null;

    public VDAdvRequest() {
        super();
    }

    public class VDAdvRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        private String mUrl = null;

        public VDAdvRequestAsyncTask(String url) {
            mUrl = url;
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ConnectivityManager connectivity = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                // 网络不可达直接返回
                mListener.onADVReqError(VDAdvConstants.ERROR_NETWORK_DISABLED);
                return null;
            }
            HttpPost request = null;
            try {
                if (mUrl == null) {
                    mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                    return null;
                }
                request = new HttpPost(mUrl);
                // request.setHeader(HTTP.CONTENT_TYPE,
                // "application/x-www-form-urlencoded");
                request.setHeader(HTTP.CONTENT_TYPE, "application/json");
                String jsonStr = mReqData.toJson().toString();
                StringEntity se = new StringEntity(jsonStr);
                request.setEntity(se);
                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                int status = 0;
                if (statusCode == HttpStatus.SC_OK) {
                    if (response.getEntity() != null) {
                        // 取得返回的数据
                        String result = EntityUtils.toString(response.getEntity());
                        int retCode = mParser.parseJson(new String(result.getBytes("ISO-8859-1"), HTTP.UTF_8));
                        if (retCode != 0) {
                            mListener.onADVReqError(VDAdvConstants.ERROR_JSON_ERROR);
                            status = 11002;
                            VDMonitorManager.getInstance().setInteger(VDMonitorData.ERRTYPE_KEY, status);
                            VDMonitorManager.getInstance().pile(VDMonitorManager.STATUS_ADV_END);
                        } else {
                            mListener.onADVReqComplete(mParser.getResult());
                        }
                    } else {
                        mListener.onADVReqError(VDAdvConstants.ERROR_JSON_ERROR);
                        status = 11002;
                        VDMonitorManager.getInstance().setInteger(VDMonitorData.ERRTYPE_KEY, status);
                        VDMonitorManager.getInstance().pile(VDMonitorManager.STATUS_ADV_END);
                        return null; // 提示错误
                    }
                } else {
                    mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                    status = 10102;
                    VDMonitorManager.getInstance().setInteger(VDMonitorData.ERRTYPE_KEY, status);
                    VDMonitorManager.getInstance().pile(VDMonitorManager.STATUS_ADV_END);
                    return null; // 提示错误
                }
                VDMonitorManager.getInstance().setInteger(VDMonitorData.ERRTYPE_KEY, status);
                VDMonitorManager.getInstance().pile(VDMonitorManager.STATUS_ADV_END);
            } catch (ClientProtocolException e) {
                mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                e.printStackTrace();
            } catch (ConnectException e) {
                mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                e.printStackTrace();
            } catch (SocketException e) {
                mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                e.printStackTrace();
            } catch (IOException e) {
                mListener.onADVReqError(VDAdvConstants.ERROR_REQUEST_ERROR);
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void request() {
        // TODO Auto-generated method stub
        if (mTask != null) {
            mTask.cancel(true);
        }
        String url = VDAdvConstants.ADV_URL;
        if (mIsDebug) {
            url = VDAdvConstants.ADV_DEBUG_URL;
        }
        mReqData = new VDAdvRequestData();
        ((VDAdvRequestData) mReqData).mContext = mContext;
        mReqData.readFrom(VDVideoViewController.getInstance(mContext).getCurrentVideo());
        if (mReqData == null) {
            mListener.onADVReqError(VDAdvConstants.ERROR_PARAMETERS_ERROR);
            return;
        }
        VDMonitorManager.getInstance().pile(VDMonitorManager.STATUS_ADV_BEGIN);
        mTask = new VDAdvRequestAsyncTask(url);
        mTask.execute();
    }

    @Override
    public void cancel() {
        // TODO Auto-generated method stub
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    @Override
    public void setDebug(boolean isDebug) {
        // TODO Auto-generated method stub
        mIsDebug = isDebug;
    }

    @Override
    public void setContext(Context context) {
        // TODO Auto-generated method stub
        mContext = context;
    }

    @Override
    public void setListener(VDAdvRequestListener listener) {
        // TODO Auto-generated method stub
        mListener = listener;
    }

}
