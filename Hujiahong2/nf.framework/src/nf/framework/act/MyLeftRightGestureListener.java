package nf.framework.act;

import nf.framework.core.exception.LogUtil;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;

public class MyLeftRightGestureListener extends SimpleOnGestureListener{
	
	//2014.06.30 niufei 
	public enum GestureExcuteMode{
		
		BothEdge,OnlyLeftEdge,OnlyRightEdge;
	}
	//默认为两侧执行
	private GestureExcuteMode gestureExcuteMode=GestureExcuteMode.BothEdge;
	
	private LeftRightGestureListenerCallback callback;
	
	public MyLeftRightGestureListener(LeftRightGestureListenerCallback callback) {
		this.callback = callback;
	}

	 @Override
     public boolean onScroll(MotionEvent e1, MotionEvent e2,
             float distanceX, float distanceY) {
     	
     	return false;
        
     }

     @Override
     public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
             float velocityY) {
    	 if(e1 == null || e2 == null) {
    		 return false;
    	 }
    	 float distanceX = e2.getX() - e1.getX();
    	 float distanceY = e2.getY() - e1.getY();
    	 
    	 LogUtil.d(MyLeftRightGestureListener.class.getSimpleName(), "### MyLeftRightGestureListener onFling distanceX:" + distanceX + " distanceY:" + distanceY);
    	 
//    	 NewsUtils.log(getClass(), "### MyLeftRightGestureListener onFling velocityX:" + velocityX + " velocityY:" + velocityY);
    	 
    	 float distance = Math.abs(Math.abs(distanceX) - Math.abs(distanceY));
//    	 NewsUtils.log(getClass(), "### MyLeftRightGestureListener onFling distance: " + distance);
    	 
    	 if(distance < 50 || Math.abs(distanceY) > 150 || (Math.abs(distanceY) - Math.abs(distanceX)>50)) {
    		 return false;
    	 }
    	 if(this.callback != null) {
	         if (gestureExcuteMode.equals(GestureExcuteMode.OnlyLeftEdge)) {
	        	 if(distanceX < 0) {
	        		 callback.onRight();
	    		 } else {
	    			 return false;
	    		 }
	         }else if(gestureExcuteMode.equals(GestureExcuteMode.OnlyRightEdge)){
	        	 if(distanceX < 0) {
	        		 return false;
	        	 }else{
	        		 callback.onLeft();
	        	 }
	         }else{
	        	 
	        	 if(velocityX < 0) {
	    			 callback.onRight();
	    		 } else {
	    			 callback.onLeft();
	    		 }
	         }
    	 }
//    	 if(this.callback != null) {
//    		 if(velocityX < 0) {
//    			 callback.onRight();
//    		 } else {
//    			 callback.onLeft();
//    		 }
//    	 }
//    	 
    	 return true;
     }

     @Override
     public boolean onSingleTapConfirmed(MotionEvent e) {

         return false;
     }

     @Override
     public boolean onDown(MotionEvent e) {
         // getView(mSelected).onDown(e);
         return false;
     }
     
     public void setGestureExcuteMode(GestureExcuteMode gestureExcuteMode) {
		this.gestureExcuteMode = gestureExcuteMode;
	}

	public interface LeftRightGestureListenerCallback{
    	 public void onLeft();
    	 public void onRight();
     }
}
