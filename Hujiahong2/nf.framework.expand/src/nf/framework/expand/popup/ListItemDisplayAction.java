package nf.framework.expand.popup;

import java.util.ArrayList;
import java.util.List;

import nf.framework.core.util.android.DensityUtil;
import nf.framework.expand.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * QuickAction dialog, shows action list as icon and text like the one in
 * Gallery3D app. Currently supports vertical and horizontal layout.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 * 
 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
 */
public class ListItemDisplayAction extends PopupWindows implements
		OnDismissListener {
	private OnActionItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;

	private List<ListActionItem> actionItems = new ArrayList<ListActionItem>();

	private int mAnimStyle;
	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_AUTO = 5;
	private static int Orientation=LinearLayout.VERTICAL;
	private LinearLayout mRootViewLayout;

	/**
	 * Constructor for default vertical layout
	 * 
	 * @param context
	 *            Context
	 */
	public ListItemDisplayAction(Context context) {
		this(context, LinearLayout.VERTICAL);
	}

	/**
	 * Constructor allowing orientation override
	 * 
	 * @param context
	 *            Context
	 * @param orientation
	 *            Layout orientation, can be vartical or horizontal
	 */
	public ListItemDisplayAction(Context context, int orientation) {
		super(context);
		this.Orientation=orientation;
		mRootViewLayout.setOrientation(Orientation);
		mAnimStyle = ANIM_AUTO;
	}

	/**
	 * Get action item at an index
	 * 
	 * @param index
	 *            Index of item (position from callback)
	 * 
	 * @return Action Item at the position
	 */
	public ListActionItem getActionItem(int index) {
		return actionItems.get(index);
	}

	public int getItemViewPosition(ListActionItem action){
		if(actionItems.contains(action)){
			return actionItems.indexOf(action);
		}
		 return -1;
	}
	/* (non-Javadoc)
	 * @see com.Apricotforest_epocket.ActionBar.PopupWindows#getRootViewLayout()
	 */
	@Override
	protected View getRootViewLayout() {
		// TODO Auto-generated method stub
		mRootViewLayout = new LinearLayout(mContext);
		mRootViewLayout.setOrientation(Orientation);
		mRootViewLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
		int padding=DensityUtil.dip2px(mContext, 1);
		mRootViewLayout.setPadding(padding, padding, padding, 0);
		mRootViewLayout.setLayoutParams(new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		return mRootViewLayout;
	}
	
	public void setRootViewBackgroundColor(int color){
		
		if(mRootViewLayout!=null){
			mRootViewLayout.setBackgroundColor(color);
		}
		
	}
	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnActionItemClickListener(OnActionItemClickListener listener) {
		mItemClickListener = listener;
	}

	public void updateActionItem(ListActionItem action){
		int position=getItemViewPosition(action);
		if(position!=-1){
			mRootViewLayout.removeViewAt(position);
			addActionItem(action);
		}
	}
	public void addActionItem(final ListActionItem action) {
		addActionItem(action, -1);
	}
	/**
	 * Add action item
	 * 
	 * @param action
	 *            {@link ListActionItem}
	 */
	public void addActionItem(final ListActionItem action, int position) {
		actionItems.add(action);

		String title = action.getTitle();
		Drawable icon = action.getIcon();

		ViewHolder viewHolder=buildRootView();
		if(position==-1){
			mRootViewLayout.addView(viewHolder.container);
		}else{
			mRootViewLayout.addView(viewHolder.container, position);
		}
		if (icon != null) {
			viewHolder.imageView.setImageDrawable(icon);
		} else {
			viewHolder.imageView.setVisibility(View.GONE);
		}

		if (title != null) {
			viewHolder.txtView.setText(title);
		} else {
			viewHolder.txtView.setVisibility(View.GONE);
		}

		final int actionId = action.getActionId();

		viewHolder.container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick(ListItemDisplayAction.this,
							v,actionId);
				}

				if (!getActionItem(getItemViewPosition(action)).isSticky()) {
					mDidAction = true;

					dismiss();
				}
			}
		});

		viewHolder.container.setFocusable(true);
		viewHolder.container.setClickable(true);

	}

	protected ViewHolder buildRootView(){
		
		ViewHolder viewholder=new ViewHolder();
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (mRootViewLayout.getOrientation() != LinearLayout.HORIZONTAL) {
			viewholder.container = (ViewGroup) mInflater.inflate(
					R.layout.popup_list_horizonal_item, null);
		} else {
			viewholder.container = (ViewGroup) mInflater.inflate(
					R.layout.popup_list_vertical_item, null);
		}
		viewholder.imageView= (ImageView) viewholder.container
				.findViewById(R.id.menu_action_bar_item_image);
		viewholder.txtView= (TextView) viewholder.container
				.findViewById(R.id.menu_action_bar_item_text);
		
		return viewholder;
	}
	
	public static class ViewHolder{
		
		public	ImageView imageView;
		public	TextView txtView;
		public	ViewGroup container;
	}
	
	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener(
			ListItemDisplayAction.OnDismissListener listener) {
		setOnDismissListener(this);

		mDismissListener = listener;
	}

	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}

	/**
	 * Listener for item click
	 * 
	 */
	public interface OnActionItemClickListener {
		public abstract void onItemClick(ListItemDisplayAction source,
				View currentView,int actionId);
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}

	/**
	 * Action item, displayed as menu with icon and text.
	 * 
	 * @author Lorensius. W. L. T <lorenz@londatiga.net>
	 * 
	 *         Contributors: - Kevin Peck <kevinwpeck@gmail.com>
	 * 
	 */
	public static class ListActionItem {
		private Drawable icon;
		private Bitmap thumb;
		private String title;
		private int actionId = -1;
		private boolean selected;
		private boolean sticky;

		/**
		 * Constructor
		 * 
		 * @param actionId
		 *            Action id for case statements
		 * @param title
		 *            Title
		 * @param icon
		 *            Icon to use
		 */
		public ListActionItem(int actionId, String title, Drawable icon) {
			this.title = title;
			this.icon = icon;
			this.actionId = actionId;
		}

		public ListActionItem(int actionId, String title, Drawable icon,
				boolean sticky) {
			super();
			this.icon = icon;
			this.title = title;
			this.actionId = actionId;
			this.sticky = sticky;
		}

		/**
		 * Constructor
		 */
		public ListActionItem() {
			this(-1, null, null);
		}

		/**
		 * Constructor
		 * 
		 * @param actionId
		 *            Action id of the item
		 * @param title
		 *            Text to show for the item
		 */
		public ListActionItem(int actionId, String title) {
			this(actionId, title, null);
		}

		/**
		 * Constructor
		 * 
		 * @param icon
		 *            {@link Drawable} action icon
		 */
		public ListActionItem(Drawable icon) {
			this(-1, null, icon);
		}

		/**
		 * Constructor
		 * 
		 * @param actionId
		 *            Action ID of item
		 * @param icon
		 *            {@link Drawable} action icon
		 */
		public ListActionItem(int actionId, Drawable icon) {
			this(actionId, null, icon);
		}

		/**
		 * Set action title
		 * 
		 * @param title
		 *            action title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/**
		 * Get action title
		 * 
		 * @return action title
		 */
		public String getTitle() {
			return this.title;
		}

		/**
		 * Set action icon
		 * 
		 * @param icon
		 *            {@link Drawable} action icon
		 */
		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		/**
		 * Get action icon
		 * 
		 * @return {@link Drawable} action icon
		 */
		public Drawable getIcon() {
			return this.icon;
		}

		/**
		 * Set action id
		 * 
		 * @param actionId
		 *            Action id for this action
		 */
		public void setActionId(int actionId) {
			this.actionId = actionId;
		}

		/**
		 * @return Our action id
		 */
		public int getActionId() {
			return actionId;
		}

		/**
		 * Set sticky status of button
		 * 
		 * @param sticky
		 *            true for sticky, pop up sends event but does not disappear
		 */
		public void setSticky(boolean sticky) {
			this.sticky = sticky;
		}

		/**
		 * @return true if button is sticky, menu stays visible after press
		 */
		public boolean isSticky() {
			return sticky;
		}

		/**
		 * Set selected flag;
		 * 
		 * @param selected
		 *            Flag to indicate the item is selected
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}

		/**
		 * Check if item is selected
		 * 
		 * @return true or false
		 */
		public boolean isSelected() {
			return this.selected;
		}

		/**
		 * Set thumb
		 * 
		 * @param thumb
		 *            Thumb image
		 */
		public void setThumb(Bitmap thumb) {
			this.thumb = thumb;
		}

		/**
		 * Get thumb image
		 * 
		 * @return Thumb image
		 */
		public Bitmap getThumb() {
			return this.thumb;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + actionId;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ListActionItem other = (ListActionItem) obj;
			if (actionId != other.actionId)
				return false;
			return true;
		}
		
		
	}

}