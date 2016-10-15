/**
 * 
 */
package nf.framework.core.exception;


/**

 * @TODO 

 * @author niufei

 * @Time 2014-6-5 下午5:08:38

 *

 */
public class NFRuntimeException extends RuntimeException{

	/**
	 *
	 * niufei
	 *
	 * 2014-6-5 下午5:10:00 
	 */
	private static final long serialVersionUID = -2811354093932778641L;
	/**
	 * @param detailMessage
	 *
	 * niufei
	 *
	 * 2014-6-5 下午5:09:52
	 */
	public NFRuntimeException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 *
	 * niufei
	 *
	 * 2014-6-5 下午5:09:52
	 */
	public NFRuntimeException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
		LogUtil.writeExceptionLog(throwable);	
	}
}
