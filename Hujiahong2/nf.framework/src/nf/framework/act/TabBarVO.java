package nf.framework.act;

import java.io.Serializable;


public class TabBarVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5708626539597671928L;

	String tabTitle;

	int tabId;

	String description;

	int imageResId;

	Object object;

	int level = -1;
	
	boolean read = false;

	public String getTabTitle() {
		return tabTitle;
	}

	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}

	public int getTabId() {
		return tabId;
	}

	public void setTabId(int tabId) {
		this.tabId = tabId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImageResId() {
		return imageResId;
	}

	public void setImageResId(int imageResId) {
		this.imageResId = imageResId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tabId;
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
		TabBarVO other = (TabBarVO) obj;
		if (tabId != other.tabId)
			return false;
		return true;
	}

	public TabBarVO(String tabTitle, int tabId, String description, int imageResId) {
		super();
		this.tabTitle = tabTitle;
		this.tabId = tabId;
		this.description = description;
		this.imageResId = imageResId;
	}

	public TabBarVO(String tabTitle, int tabId, int imageResId) {
		super();
		this.tabTitle = tabTitle;
		this.tabId = tabId;
		this.imageResId = imageResId;
	}

	public TabBarVO(int tabId, String tabTitle) {
		super();
		this.tabTitle = tabTitle;
		this.tabId = tabId;
	}

	public TabBarVO(int tabId, String tabTitle, Object object) {
		super();
		this.tabTitle = tabTitle;
		this.tabId = tabId;
		this.object = object;
	}

	public TabBarVO() {
		super();
	}
	
	
}