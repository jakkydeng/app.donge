package com.sina.sinavideo.coreplayer.vms;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.coreplayer.util.VDResolutionData;
import com.sina.sinavideo.coreplayer.util.VDResolutionData.VDResolution;
import com.sina.sinavideo.coreplayer.vms.VDVMSVideoRequest.OnVMSRequestCallback;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

/**
 * Created by GengHongchao on 2015/5/22.
 */
public class VDVMSVideoParser {

    private static final String TAG = VDVMSVideoParser.class.getSimpleName();
    private DefaultHttpClient sHttpClient;
    private VMSRequestAsyncTask mTask;
    private Context mContext;
    private OnVMSRequestCallback mCallback;
    private VMSVideoInfo mInfo;
    private static final int CONNETED_TIMEOUT = 20;
    /** 成功 */
    private static final long RESULT_OK = 1L;

    /** 转码类型：m3u8 */
    private static final String STR_TRANSCODE_OVS = "ovs";
    /** 转码类型：mp4 */
    private static final String STR_TRANSCODE_COLONEL = "colonel";

    private static String STR_TAG = "videoapp_android";

    /** mp4格式视频拼接地址 */
    private static final String URL_FORMAT_MP4 = "http://v.iask.com/v_play_ipad.php?vid=%1$s&tags=%2$s";
    /** m3u8格式视频拼接地址 */
    private static final String URL_FORMAT_M3U8 = "http://wtv.v.iask.com/player/ovs1_vod_rid_%1$s_br_9_pn_weitv_tn_0_sig_md5.m3u8 ";

    /**
     * 构造函数
     * 
     * @param context
     */
    public VDVMSVideoParser(Context context, OnVMSRequestCallback callback) {
        mCallback = callback;
        mContext = context;
        if (sHttpClient == null) {
            sHttpClient = createHttpClient();
        }
    }

    /**
     * 开始VMS网络请求
     * 
     * @param url
     */
    public void startRequest(String url) {
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new VMSRequestAsyncTask(url);
        mTask.execute();
    }

    /**
     * 取消VMS网络请求
     */
    public void cancalRequest() {
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    public class VMSRequestAsyncTask extends AsyncTask<Void, Void, Void> {

        private String mUrl;

        public VMSRequestAsyncTask(String url) {
            super();
            mUrl = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            vmsParserJsonComplete();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return request();
        }

        private Void request() {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || networkInfo.getState() != NetworkInfo.State.CONNECTED) {
                // 网络不可达直接返回
                vmsNetworkError();
                return null;
            }

            HttpGet request = null;
            try {
                LogS.d(TAG, "parse url " + mUrl);

                request = new HttpGet(mUrl);
                if (sHttpClient == null) {
                    sHttpClient = createHttpClient();
                }
                HttpResponse response = sHttpClient.execute(request);

                int statusCode = response.getStatusLine().getStatusCode();
                LogS.w(TAG, " retryConnect statusCode = " + statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    if (response.getEntity() != null) {
                        // 取得返回的数据
                        String result = EntityUtils.toString(response.getEntity());
                        parseJson(new String(result.getBytes("ISO-8859-1"), HTTP.UTF_8));
                        LogS.d(TAG, "status OK, read file");
                    } else {
                        LogS.e(TAG, "parse error");
                        return null; // 提示错误
                    }
                } else {
                    LogS.e(TAG, "parse error");

                    vmsRequestError();
                    return null; // 提示错误
                }
            } catch (ClientProtocolException e) {
                LogS.e(TAG, "ClientProtocolException " + e);
                vmsRequestError();
                e.printStackTrace();
            } catch (ConnectTimeoutException e) {
                LogS.e(TAG, "ConnectTimeoutException " + e);
                vmsRequestError();
                e.printStackTrace();
            } catch (ConnectException e) {
                LogS.e(TAG, "ConnectException " + e);
                vmsRequestError();
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                LogS.e(TAG, "SocketTimeoutException " + e);
                vmsRequestError();
                e.printStackTrace();
            } catch (SocketException e) {
                LogS.e(TAG, "SocketException " + e);
                vmsRequestError();
                e.printStackTrace();
            } catch (IOException e) {
                LogS.e(TAG, "IOException " + e);
                vmsRequestError();
                e.printStackTrace();
            } finally {
            }
            return null;
        }
    }

    private void vmsRequestError() {
        if (mCallback != null) {
            mCallback.onError(VDVMSVideoRequest.ERROR_REQUEST_ERROR);
        }
    }

    private void vmsNetworkError() {
        if (mCallback != null) {
            mCallback.onError(VDVMSVideoRequest.ERROR_NETWORK_DISABLED);
        }
    }

    private void vmsParserResponseError() {
        if (mCallback != null) {
            mCallback.onError(VDVMSVideoRequest.ERROR_RESPONSE_ERROR);
        }
    }

    private void vmsParserJsonError() {
        if (mCallback != null) {
            mCallback.onError(VDVMSVideoRequest.ERROR_JSON_ERROR);
        }
    }

    private void vmsParserJsonComplete() {
        if (mCallback != null && mInfo != null) {
            mCallback.onComplete(mInfo);
        }
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

    /**
     * @param content
     */
    private void parseJson(String content) {
        try {
            JSONObject jsonObj = new JSONObject(content);
            long code = jsonObj.getLong("code");
            String message = jsonObj.getString("message");
            if (RESULT_OK != code) {
                vmsParserResponseError();
            }
            JSONObject data = jsonObj.getJSONObject("data");
            String video_id = data.getString("video_id");
            String title = data.getString("title");
            String length = data.getString("length");
            String from = data.getString("from");
            String media_tag = data.getString("media_tag");
            String transcode_system = data.getString("transcode_system");
            String image = data.getString("image");
            String channel_path = data.getString("channel_path");

            mInfo = new VMSVideoInfo();
            mInfo.setVideoID(video_id);
            mInfo.setTitle(title);
            if (!TextUtils.isEmpty(length) && TextUtils.isDigitsOnly(length)) {
                mInfo.setDuration(Integer.valueOf(length));
            }
            mInfo.setFrom(from);
            mInfo.setMediaTag(media_tag);
            mInfo.setTranscodeSystem(transcode_system);
            mInfo.setThumbnailUrl(image);
            mInfo.setChannelPath(channel_path);
            JSONArray videos = data.getJSONArray("videos");
            // HashMap<String, String> definitionMap = new HashMap<String,
            // String>();
            VDResolutionData resolutionData = new VDResolutionData();
            for (int i = 0; i < videos.length(); i++) {
                JSONObject video = ((JSONObject) videos.opt(i));
                String file_id = video.getString("file_id");
                String file_api = video.getString("file_api");
                String definition = video.getString("definition");
                String type = video.getString("type");
                length = video.getString("length");
                String playUrl = getPlayUrl(file_id, transcode_system);
                // definitionMap.put(definition, playUrl);
                if (definition == VDResolutionData.TYPE_DEFINITION_CIF) {
                    resolutionData.addResolution(VDResolutionData.TYPE_DEFINITION_CIF, new VDResolution(
                            VDResolutionData.TYPE_DEFINITION_CIF, playUrl, 0, 0));
                } else if (definition == VDResolutionData.TYPE_DEFINITION_SD) {
                    resolutionData.addResolution(VDResolutionData.TYPE_DEFINITION_SD, new VDResolution(
                            VDResolutionData.TYPE_DEFINITION_SD, playUrl, 0, 0));
                } else if (definition == VDResolutionData.TYPE_DEFINITION_HD) {
                    resolutionData.addResolution(VDResolutionData.TYPE_DEFINITION_HD, new VDResolution(
                            VDResolutionData.TYPE_DEFINITION_HD, playUrl, 0, 0));
                } else if (definition == VDResolutionData.TYPE_DEFINITION_FHD) {
                    resolutionData.addResolution(VDResolutionData.TYPE_DEFINITION_FHD, new VDResolution(
                            VDResolutionData.TYPE_DEFINITION_FHD, playUrl, 0, 0));
                } else if (definition == VDResolutionData.TYPE_DEFINITION_3D) {
                    resolutionData.addResolution(VDResolutionData.TYPE_DEFINITION_3D, new VDResolution(
                            VDResolutionData.TYPE_DEFINITION_3D, playUrl, 0, 0));
                }
                if (TextUtils.isEmpty(mInfo.getDefaultPlayUrl())) {
                    mInfo.setDefaultPlayUrl(playUrl);
                    mInfo.setDefaultDefKey(definition);
                }
            }

            mInfo.setDefinitionInfo(resolutionData);
        } catch (JSONException e) {
            mInfo = null;
            vmsParserJsonError();
            e.printStackTrace();
        }
    }

    /**
     * 获取视频播放地址
     * 
     * @param fileID
     * @param transcodeSystem
     * @return
     */
    private String getPlayUrl(String fileID, String transcodeSystem) {
        String playUrl = null;
        if (STR_TRANSCODE_OVS.equals(transcodeSystem)) {
            playUrl = makeUri(URL_FORMAT_M3U8, fileID);
        } else if (STR_TRANSCODE_COLONEL.equals(transcodeSystem)) {
            playUrl = makeUri(URL_FORMAT_MP4, fileID, STR_TAG);
        }
        return playUrl;
    }

    /**
     * 拼接URL地址
     * 
     * @param uriPath
     * @param params
     * @return
     */
    public static String makeUri(String uriPath, Object... params) {
        if (!TextUtils.isEmpty(uriPath)) {
            uriPath = String.format(uriPath, params);
        }
        return uriPath;
    }

}
