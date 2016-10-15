package nf.framework.core.http;

public class HttpReponseConfig {

	
	public static String getReponseDataByCode(int code){
		
		String reponseDdata=null;
		switch (code) {
	
		case HttpRequest.HTTP_OK:
			reponseDdata ="请求成功";
			break;

		case HttpRequest.HTTP_REQUEST_ERROR_CANCELED:
			
			reponseDdata ="请求已取消";
			
			break;
			
		case HttpRequest.HTTP_REQUEST_ERROR_CONNECT:
			
			reponseDdata= "网络连接失败";
			break;
		case HttpRequest.HTTP_REQUEST_ERROR_INPUT:
			
			reponseDdata ="请求参数输入非法";
			break;
		case HttpRequest.HTTP_REQUEST_ERROR_URL:
			
			reponseDdata ="请求网络地址错误";
			break;
		case HttpRequest.HTTP_REQUEST_EXCEPTION:
		case 500:
		case 504:
		case 502:
		case 400:
			reponseDdata ="请求服务异常";
			
			break;
		case HttpRequest.HTTP_REQUEST_NETWORK_ERROR:
			
			reponseDdata ="网络连接不可用";
			break;
		case HttpRequest.TIMEOUT:
			
			reponseDdata ="请求超时";
			break;
		}
		return reponseDdata;
	}
}
