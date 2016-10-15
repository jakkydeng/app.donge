/**   
 * @Title: AbsUIResquestHandler.java 
 * @Package com.example.edittexttest.httprequest 
 * @author niufei
 * @date 2014-5-13 ����9:51:41 
 * @version V1.0   
*/
package nf.framework.core.http;

public interface AbsUIResquestHandler<T> {

	
	public void onPreExcute(AbsBaseRequestData<?> baseRequestData);
	
	public void onSuccessPostExecute(AbsBaseRequestData<?> baseRequestData,T object,boolean hasNext);
	
	public void onFailurePostExecute(AbsBaseRequestData<?> baseRequestData,String failureMsg);
	
	public void onCompleteExcute(AbsBaseRequestData<?> baseRequestData);
	
	
}
