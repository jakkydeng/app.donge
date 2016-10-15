package nf.framework.core.http;

import java.io.File;
import java.util.Map;

public interface NetworkRequest {

	

	public boolean downloadFile(final String url, String filePath);
	
	public String postRequest(String url, Map<String, String> params);
	
	public String postRequest(String url, byte[] data);
	
	public String postFileRequest(String strUrl, byte[] data, String filePath);
	
	public String getRequest(String url);
	
	public String postParamAndFile(String actionUrl, Map<String, String> params, Map<String, File> files);

	public void cancel();
	
	public int getResponseCode();
	
	public String getResponseMessage();

	public int getRequestErrorCode();
	
	public String getSession();
	
	public void setSessionToHeader(String session);

	public void setConnectOutTime(int millisecond);
}
