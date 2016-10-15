package nf.framework.expand.widgets;

import nf.framework.expand.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NFToast {

	/**
	 * Toast提示
	 * @param context
	 * @param imageRes	Icon
	 * @param info		信息
	 * @param duration	显示时间
	 */
	public static void toast(Context context,int imageRes,String info,int duration){
		Toast toast = new Toast(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
        View layout = inflater.inflate(R.layout.expand_toast, null);  
        ImageView imageView = (ImageView) layout.findViewById(R.id.expand_toast_iv);  
        TextView textView = (TextView) layout.findViewById(R.id.expand_toast_tv);  
          
        imageView.setImageResource(imageRes);  
        textView.setText(info);  
          
        toast.setView(layout);  
        toast.setGravity(Gravity.CENTER, 0, 0);  
        toast.setDuration(duration);  
        toast.show();
	}

	/**
	 * Toast提示
	 * @param context
	 * @param info
	 */
	public static void toastSuccess(Context context,String info){
		if(context != null && !TextUtils.isEmpty(info)){
			toast(context,R.drawable.toast_icon_success,info,Toast.LENGTH_SHORT);
		}	
	}
	

	/**
	 * Toast提示
	 * @param context
	 * @param info
	 */
	public static void toastWarnning(Context context,String info){
		if(context != null && !TextUtils.isEmpty(info)){
			toast(context,R.drawable.toast_icon_warnning,info,Toast.LENGTH_SHORT);
		}	
	}
	
	/**
	 * Toast提示
	 * @param context
	 * @param info
	 */
	public static void toastSuccess(Context context,int resid){
		if(context != null){
			toastSuccess(context,context.getResources().getString(resid));
		}	
	}
	
	/**
	 * Toast提示
	 * @param context
	 * @param info
	 */
	public static void toastWarnning(Context context,int resid){
		if(context != null){
			toastWarnning(context,context.getResources().getString(resid));
		}	
	}
	
}
