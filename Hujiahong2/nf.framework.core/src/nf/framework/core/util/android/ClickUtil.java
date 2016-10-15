package nf.framework.core.util.android;

public class ClickUtil {
	private static long lastClickTime;
	private static long lastClickTime2;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	public static boolean inThreeSecendsAfterClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime2;
		if (0 < timeD && timeD < 3000) {
			return true;
		}
		lastClickTime2 = time;
		return false;
	}
}
