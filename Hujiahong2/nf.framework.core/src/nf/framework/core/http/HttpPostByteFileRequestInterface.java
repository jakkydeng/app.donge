package nf.framework.core.http;

public interface HttpPostByteFileRequestInterface extends HttpRequestInterface{
	
	
	public byte[] getByteData();
	
	
	public String getFilePath();
}
