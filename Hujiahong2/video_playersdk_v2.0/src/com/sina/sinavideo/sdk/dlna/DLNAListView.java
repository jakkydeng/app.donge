package com.sina.sinavideo.sdk.dlna;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sina.sinavideo.coreplayer.util.VDVideoUrlParser;
import com.sina.sinavideo.coreplayer.util.VDVideoUrlParser.VideoParseCallBack;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.data.VDSDKLogData;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.dlna.DLNAEventListener.OnDLNAListSwitchListener;
import com.sina.sinavideo.sdk.dlna.DLNAEventListener.OnDLNASwitchListener;
import com.sina.sinavideo.sdk.dlna.DLNAEventListener.OnDLNAVisibleChangeListener;
import com.sina.sinavideo.sdk.dlna.DLNAEventListener.OnMediaRenderNumChangedListener;
import com.sina.sinavideo.sdk.utils.VDSDKLogManager;
import com.sina.sinavideo.sdk.utils.VDUtility;

/**
 * 搜索到支持DLNA的设置列表
 * 
 * @author sina
 * 
 */
public class DLNAListView extends ListView implements OnMediaRenderNumChangedListener, OnDLNAListSwitchListener,
        OnDLNASwitchListener, OnDLNAVisibleChangeListener {

    private MRContentAdapter mAdapter;
    // private DLNALinearLayout mDLNALinearLayout;
    private boolean mShowDLNA = false;

    private VDVideoInfo mVideoInfo = null;
    private MRContent mMRContent;
    private int mPosition = -1;
    private Context mContext = null;

    private VDVideoUrlParser mVideoUrlParser;

    public DLNAListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DLNAListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DLNAListView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context DLNALinearLayout) {
        Log.i("DLNA", "SinaDLNA addOnDLNASwitch ");
        mContext = DLNALinearLayout;
        DLNAEventListener.getInstance().addOnDLNAListSwitchListener(this);
        DLNAEventListener.getInstance().addOnDLNASwitchListener(this);
        DLNAEventListener.getInstance().addOnDLNAVisiableChangeListener(this);
    }

    // public void setDLNABackLayout(DLNALinearLayout l){
    // mDLNALinearLayout = l;
    // }

    @Override
    public void onMediaRenderAdded(String uuid, String name) {
        Log.i("DLNA", "onMediaRenderAdded : uuid = " + uuid + " , name = " + name);
        mAdapter.addMR(uuid, name);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMediaRenderRemoved(String uuid, String name) {
        mAdapter.removeMR(uuid, name);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDLNAListSwitch(boolean open) {
        Log.i("DLNA", "onSwitch : open = " + open);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUp() {
        if (mAdapter == null) {
            setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    VDVideoViewController controller = VDVideoViewController
                            .getInstance(DLNAListView.this.getContext());
                    if (controller != null) {
                        mVideoInfo = controller.getCurrentVideo();
                    }
                    mMRContent = (MRContent) mAdapter.getItem(position);
                    mPosition = position;
                    if (mVideoInfo != null && VDUtility.isLocalUrl(mVideoInfo.mPlayUrl)) {
                        mVideoUrlParser = new VDVideoUrlParser(new MyVideoParseCallBack(), mVideoInfo.getVideoId());
                        mVideoUrlParser.startParser(mVideoInfo.getNetUrl());
                    } else {
                        if (0 == DLNAController.getInstance(getContext()).setMediaRender(mMRContent.getUuid())) {
                            mShowDLNA = true;
                            DLNAEventListener.getInstance().notifyOnPreOpenDLNA();
                            Toast.makeText(getContext(), "正在连接设备", Toast.LENGTH_SHORT).show();
                            mAdapter.setSelectPosition(position);
                            DLNAController.mIsDLNA = true;
                            DLNAEventListener.getInstance().notifyDLNAMediaRenderSelected(mMRContent.getName(),
                                    mMRContent.getUuid());
                        } else {
                            DLNAController.mIsDLNA = false;
                        }
                    }
                }
            });

            mAdapter = new MRContentAdapter(getContext());
            setAdapter(mAdapter);
            DLNAEventListener.getInstance().addOnMediaRenderNumChangedListener(this);
        }
    }

    @Override
    public void toggle() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDLNASwitch(boolean open) {
        if (mAdapter != null && !open) {
            mAdapter.removeAll();
        }
    }

    @Override
    public void onDLNAIndicaterVisibleChange(boolean visible) {
        if (visible) {
            if (mShowDLNA /* && mDLNALinearLayout != null */) {
                // mDLNALinearLayout.setVisibility(VISIBLE);
                VDVideoViewController controller = VDVideoViewController.getInstance(this.getContext());
                if (controller != null)
                    controller.notifySetDLNALayoutVisible(true);
                mShowDLNA = false;
            }
        } else {
            VDVideoViewController controller = VDVideoViewController.getInstance(this.getContext());
            if (controller != null)
                controller.notifySetDLNALayoutVisible(false);
            // if(mDLNALinearLayout != null){
            // mDLNALinearLayout.setVisibility(GONE);
            // }
        }
    }

    class MyVideoParseCallBack implements VideoParseCallBack {

        public MyVideoParseCallBack() {

        }

        @Override
        public void onParseComplete(String finalUrl) {

            if (0 == DLNAController.getInstance(getContext()).setMediaRender(mMRContent.getUuid())) {
                mShowDLNA = true;
                DLNAEventListener.getInstance().notifyOnPreOpenDLNA();
                Toast.makeText(getContext(), "正在连接设备", Toast.LENGTH_SHORT).show();
                mAdapter.setSelectPosition(mPosition);
                DLNAController.mIsDLNA = true;
                DLNAEventListener.getInstance().notifyDLNAMediaRenderSelected(mMRContent.getName(),
                        mMRContent.getUuid());
            } else {
                DLNAController.mIsDLNA = false;
            }
        }

        @Override
        public void onParsePrepared() {
            VDSDKLogData.getInstance().mRedirectUrls.clear();
        }

        @Override
        public void onParseError(int error_msg) {
            if (error_msg == VDVideoUrlParser.ERROR_MP4_PARSE) {
                VDVideoViewController controller = VDVideoViewController.getInstance(mContext);
                if (controller != null) {
                    VDSDKLogManager.getInstance().onErrorMp4Parse(controller.mVDVideoListInfo.mIndex);
                }
            }
        }

        @Override
        public void onGetLocationUri(String url) {
            VDSDKLogData.getInstance().mRedirectUrls.add(url);
        }

        @Override
        public void onParseVIAskContent(String viaskContent) {
            // TODO Auto-generated method stub
            VDSDKLogData.getInstance().mVIAsk = viaskContent;
        }

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }
}
