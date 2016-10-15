package com.sina.sinavideo.sdk.log;

/**
 * @author Seven
 */
public class Statistic {

    // log version
    public static final String TAG_LOGVERSION = "1.0.1";

    public static final String TAG_EQ = "=";
    public static final String TAG_AND = "&";
    public static final String TAG_LOGID = "lid";
    public static final String TAG_LOGSUBID = "logsubid";
    public static final String TAG_OR = "<=>";

    /*
     * common data
     */
    public static final String TAG_NETTYPE = "net";
    public static final String TAG_SESSIONID = "sid";
    public static final String TAG_TS = "ts"; // timestamp

    /*
     * base data
     */
    public static final String TAG_APP = "app";
    public static final String TAG_APPVERSION = "av";
    public static final String TAG_NETSTREAMTYPE = "nst";
    public static final String TAG_VIDEOID = "vid";
    public static final String TAG_USERID = "uid";
    public static final String TAG_DEVICEID = "did";
    public static final String TAG_DEVICESYS = "dsys";
    public static final String TAG_DEVICETYPE = "dtype";
    public static final String TAG_LOGSYSVERSION = "logsv";
    public static final String TAG_PLATFORM = "pt";

    /*
     * first frame data
     */
    public static final String TAG_FIRSTFRAMETSEC = "tt";
    public static final String TAG_PLOADSTREAMTIME = "lst"; // buffer start
    public static final String TAG_PSTREAMREADYTIME = "srt"; // buffer end
    public static final String TAG_PFIRSTBUFFERFULL = "bft";

    /*
     * buffer empty data high ping start data
     */
    public static final String TAG_HIGHPINGSTARTCSEC = "ct";
    public static final String TAG_HIGHPINGSTARTTSEC = "tt"; // current video
                                                             // time
    public static final String TAG_HIGHPINGSTARTQUALITY = "qual";
    public static final String TAG_HIGHPINGSTARTEVENTID = "eid";
    public static final String TAG_HIGHPINGSTARTEVENT = "evt";

    /*
     * high ping end data
     */
    public static final String TAG_HIGHPINGENDCSEC = "ct";
    public static final String TAG_HIGHPINGENDTSEC = "tt"; // current video time
    public static final String TAG_HIGHPINGENDQUELITY = "qual";
    public static final String TAG_HIGHPINGENDEVENTID = "eid";
    public static final String TAG_HIGHPINGENDEVENT = "evt";
    public static final String TAG_HIGHPINGENDBUFFERTIME = "tdiff";

    /*
     * play fail data
     */
    public static final String TAG_ERROR = "etype";
    public static final String TAG_ERRORDOMAIN = "edomain";
    public static final String TAG_ERRORINFO = "einfo";
    public static final String TAG_ERROREXTEND = "extend";

    /*
     * pause data
     */
    public static final String TAG_PAUSECSEC = "ct";
    public static final String TAG_PAUSEEVENT = "evt";

    /*
     * resume data
     */
    public static final String TAG_BEGINSEC = "bt";
    public static final String TAG_PAUSETIME = "pt";
    public static final String TAG_RESUMEEVENT = "evt";

    /*
     * seek data
     */
    public static final String TAG_DRAGFROM = "drf";
    public static final String TAG_DRAGTO = "drt";

    /*
     * APP play data
     */

    /*
     * start player module data(lenth of plist)
     */
    public static final String TAG_PLAYLISTCOUNT = "plc";

    /*
     * play video operation data
     */
    public static final String TAG_TITLE = "title";
    public static final String TAG_URL = "url";
    public static final String TAG_NEW_VIDEOID = "videoid";
    public static final String TAG_VDEF = "vdef";
    public static final String TAG_VIASK = "viask";
    public static final String TAG_VLIVE = "vlive";

    /*
     * stop play video data
     */
    public static final String TAG_CLOSETYPE = "ct";

    /*
     * tag that represent the video is from back to front or from front to back
     */
    public static final String TAG_ENTERBACK = "entb";

    /*
     * tag that 进入后台，和返回前台，配对id enterId
     */
    public static final String TAG_ENTERID = "entid";

    /*
     * tag that log overtime
     */
    public static final String TAG_PRETIME = "prt";

    /*
     * Statistics type enumeration. same name as iOS.
     */
    public static enum SVPLogIDS {
        SVPLogIDBaseInfo(0), SVPLogIDModuleStart(1), SVPLogIDVideoPlay(2), SVPLogIDVideoFirstFrame(3), SVPLogIDVideoBufferEmptyStart(
                4), SVPLogIDVideoBufferEmptyEnd(5), SVPLogIDVideoFail(6), SVPLogIDVideoPause(7), SVPLogIDVideoResume(8), SVPLogIDVideoDrag(
                9), SVPLogIDVideoStop(10), SVPLogIDVideoAccesslog(11), SVPLogIDVideoErrorlog(12), SVPLogIDVideoPretime(
                13), SVPLogIDVideoBackground(14), SVPLogIDVideoHeartBeat(15);

        private int index;

        private SVPLogIDS(int _index) {
            index = _index;
        }

        public int index() {
            return index;
        }
    };

    /*
     * Statistics sub type enumeration. same name as iOS.
     */
    public static enum SVPLogSubIDS {
        SVPLogIDVideoELiveParseM3u8(0), SVPLogIDVideoELiveParseNoContent(1), SVPLogIDVideoENoliveGetRealMp4(2);

        private int index;

        private SVPLogSubIDS(int _index) {
            index = _index;
        }

        public int index() {
            return index;
        }
    };

    /*
     * data entities
     */

    // common data
    public static final String ENT_IOS_NOTREACHABLE = "kSVPNotReachable";
    public static final String ENT_IOS_WIFIREACHABLE = "kSVPReachableViaWiFi";
    public static final String ENT_IOS_MOBILEREACHABLE = "kSVPReachableViaWWAN";

    // base data
    public static final String ENT_PLATFORM = "android";

    // high ping start data
    public static final String ENT_HIGHPINGSTART_EVENT = "sbuffer";

    // high ping end data
    public static final String ENT_HIGHPINGEND_EVENT = "ebuffer";

    // pause data
    public static final String ENT_PAUSE = "pause";

    // resume data
    public static final String ENT_RESUME = "resume";

    // resolution value
    public static final String ENT_RESOLUTION_SD = "sd";
    public static final String ENT_RESOLUTION_HD = "hd";
    public static final String ENT_RESOLUTION_XHD = "xhd";

    // 参数枚举常量值
    public static final String CLOSE_TYPE_TIMEOVER = "timeover";
    public static final String CLOSE_TYPE_USERCLOSE = "userclose";

}
