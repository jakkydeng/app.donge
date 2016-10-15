package nf.framework.core.http;

import java.util.Map;

public interface HttpPostRequestInterface extends HttpRequestInterface{

	
	public Map<String, String> getPostParamMap();
}
