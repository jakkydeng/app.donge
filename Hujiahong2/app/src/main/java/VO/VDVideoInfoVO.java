package VO;

import java.io.Serializable;

/**
 * 
 * @author leihen
 *
 */
public class VDVideoInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

    private int videoId ;
	private String url;
	private String title;
	boolean isLive;
    int  duration;//播放时长
    int curDuration =0 ;//当前播放进度
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCurDuration() {
        return curDuration;
    }

    public void setCurDuration(int curDuration) {
        this.curDuration = curDuration;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }
}
