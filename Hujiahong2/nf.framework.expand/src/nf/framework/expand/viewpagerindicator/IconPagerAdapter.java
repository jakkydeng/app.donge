package nf.framework.expand.viewpagerindicator;

import android.R.integer;

public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
    
    boolean getReadPoint(int index);
}
