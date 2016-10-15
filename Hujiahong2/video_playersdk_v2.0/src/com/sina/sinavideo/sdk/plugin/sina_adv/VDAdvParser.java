package com.sina.sinavideo.sdk.plugin.sina_adv;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.plugin.sina_adv.VDAdvResponseData.VDAdvRespContentData;

/**
 * 解析类
 * 
 * @author alexsun
 * 
 */
public class VDAdvParser {

    private VDAdvResponseData mRespData = null;

    private final static String TAG = "VDAdvParser";

    public int parseJson(String json) {
        try {
            mRespData = new VDAdvResponseData();
            JSONObject jsonObj = new JSONObject(json);
            JSONArray jsonAdArr = jsonObj.getJSONArray("ad");
            if (jsonAdArr != null && jsonAdArr.length() > 0) {
                for (int i = 0; i < jsonAdArr.length(); i++) {
                    JSONObject jsonADObj = (JSONObject) jsonAdArr.get(i);
                    String adID = jsonADObj.getString("id");
                    mRespData.mAdvId = adID;
                    JSONArray jsonContentArr = jsonADObj.getJSONArray("content");
                    if (jsonContentArr != null && jsonContentArr.length() > 0) {
                        mRespData.mAD = new ArrayList<VDAdvResponseData.VDAdvRespContentData>();
                        for (int j = 0; j < jsonContentArr.length(); j++) {
                            VDAdvRespContentData data = new VDAdvRespContentData();
                            JSONObject jsonContentObj = (JSONObject) jsonContentArr.get(j);
                            JSONArray jsonContSrcArr = new JSONArray();
                            try {
                                jsonContSrcArr = (JSONArray) jsonContentObj.getJSONArray("src");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "src is null");
                            }
                            JSONArray jsonContTypeArr = new JSONArray();
                            try {
                                jsonContTypeArr = (JSONArray) jsonContentObj.getJSONArray("type");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "type is null");
                            }
                            JSONArray jsonContLinkArr = new JSONArray();
                            try {
                                jsonContLinkArr = (JSONArray) jsonContentObj.getJSONArray("link");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "link is null");
                            }
                            JSONArray jsonContPVArr = new JSONArray();
                            try {
                                jsonContPVArr = (JSONArray) jsonContentObj.getJSONArray("pv");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "pv is null");
                            }
                            JSONArray jsonContMonitorArr = new JSONArray();
                            try {
                                jsonContMonitorArr = (JSONArray) jsonContentObj.getJSONArray("monitor");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "monitor is null");
                            }
                            JSONArray jsonContCloseArr = new JSONArray();
                            try {
                                jsonContCloseArr = (JSONArray) jsonContentObj.getJSONArray("close");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "close is null");
                            }
                            int jsonFreqNum = 0;
                            try {
                                jsonFreqNum = jsonContentObj.getInt("freq");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "freq is null");
                            }
                            String jsonBeginTime = "";
                            try {
                                jsonBeginTime = jsonContentObj.getString("begin_time");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "begin_time is null");
                            }
                            String jsonEndTime = "";
                            try {
                                jsonEndTime = jsonContentObj.getString("end_time");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "end_time is null");
                            }
                            String jsonLineItemID = "";
                            try {
                                jsonLineItemID = jsonContentObj.getString("lineitem_id");
                            } catch (JSONException ex) {
                                LogS.e(TAG, "lineitem_id is null");
                            }

                            data.mSrc = parserJsonStrArr(jsonContSrcArr);
                            data.mType = parserJsonStrArr(jsonContTypeArr);
                            data.mLink = parserJsonStrArr(jsonContLinkArr);
                            data.mPV = parserJsonStrArr(jsonContPVArr);
                            data.mMonitor = parserJsonStrArr(jsonContMonitorArr);
                            data.mClose = parserJsonStrArr(jsonContCloseArr);
                            data.mFreq = jsonFreqNum;
                            try {
                                data.mBeginTime = Integer.parseInt(jsonBeginTime);
                                data.mEndTime = Integer.parseInt(jsonEndTime);
                            } catch (NumberFormatException ex) {

                            }
                            data.mLineItemID = jsonLineItemID;
                            if (data.mSrc != null && data.mSrc.size() > 0) {
                                for (String src : data.mSrc) {
                                    mRespData.mAdvUrl.add(src);
                                    mRespData.mAdvDuraction.add(0);
                                }
                            }
                            mRespData.mAD.add(data);
                        }
                    }
                }
            }
        } catch (JSONException ex) {
            mRespData = null;
            return VDAdvConstants.ERROR_JSON_ERROR;
        }
        return 0;
    }

    public VDAdvResponseData getResult() {
        return mRespData;
    }

    private List<String> parserJsonStrArr(JSONArray src) {
        ArrayList<String> retArr = null;
        if (src == null || src.length() <= 0) {
            return null;
        }
        try {
            retArr = new ArrayList<String>();
            for (int i = 0; i < src.length(); i++) {
                retArr.add((String) src.get(i));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return retArr;
    }
}
