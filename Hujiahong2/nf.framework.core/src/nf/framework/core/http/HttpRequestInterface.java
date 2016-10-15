package nf.framework.core.http;

public interface HttpRequestInterface{

	
	public String buildUrl();
	

	public void onRequestCompleted(String responseData);
	
	
	public void onRequestFailured(int errorCode ,String requestErrorMsg);
}
