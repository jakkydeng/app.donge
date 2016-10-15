package nf.framework.core.http;

import java.io.File;
import java.util.Map;

public interface HttpPostFilesRequestInterface extends HttpPostRequestInterface{

	
	public Map<String, File> getPostFilesMap();
}
