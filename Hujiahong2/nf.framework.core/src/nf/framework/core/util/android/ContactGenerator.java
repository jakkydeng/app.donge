package nf.framework.core.util.android;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import nf.framework.core.exception.LogUtil;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;
/**
 * 通讯录获取类
 * @author niufei
 * @date 2014-4-19
 * @todo TODO
 */
public class ContactGenerator {
	private Context context;

	private static final String[] PROJECTECTION = { Phone.DISPLAY_NAME,
			Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID, };
	private static final int DISPLAY_NAME_INDEX = 0;
	private static final int PHONE_NUMBER_INDEX = 1;
	private static final int PHOTO_ID_INDEX = 2;
	private static final int CONTACT_ID_INDEX = 3;

	public ContactGenerator(Context context) {
		this.context = context;
	}

	/*
     * 根据电话号码取得联系人姓名
     */
    public List<ContactItemVO> getContactNameByPhoneNumber(String charString) {
      
    		List<ContactItemVO> list = new ArrayList<ContactItemVO>();
    		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
    		String selection= ContactsContract.CommonDataKinds.Phone.NUMBER + " like '"
                    + charString + "' or "
                    + ContactsContract.PhoneLookup.DISPLAY_NAME + " like '"
                    + charString + "' or "
                    + "sort_key like '"
                    + charString + "'";
        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, // Which columns to return.
                selection , // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        for(int i=0;i<cursor.getColumnNames().length;i++)
    		Log.e("Cursor", cursor.getColumnNames()[i]);
        
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            // 取得联系人名字
            int nameFieldColumnIndex = cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cursor.getString(nameFieldColumnIndex);
            String mobile = cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			setDate(list, name, mobile.replaceAll("\\D", ""));
        }
        return list;
    }
/**
 *  获取所有联系人内容
 * @param context
 * @return
 *@Param niufei
 *@Param 2014-4-19
 */
    public  List<ContactItemVO> getContacts(int pageIndex,int pageSize) {
        List<ContactItemVO> contactList= new ArrayList<ContactItemVO>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
        int index =0;
        if (cursor.moveToFirst()) {
            do {
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor
                                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String headChar = cursor.getString(cursor.getColumnIndex("phonebook_label"));
                Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId, null, null);
               
                
                if(pageIndex*pageSize>=phones.getCount()){
            	   return contactList;
               }
               
               while (phones.moveToNext()) {
                	if(index>=pageIndex*pageSize&&index<(pageIndex+1)*pageSize){
                	
	                	ContactItemVO contactItemVO =new ContactItemVO();
	                    String phoneNumber = phones.getString(phones
	                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	                    Long contactID = phones.getLong(phones
	                    		.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
	                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID); 
	                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr,uri);  
	                    contactItemVO.setFullName(name);
	                    contactItemVO.setContactId(contactId);
	                    contactItemVO.setMobile(phoneNumber);
	                    contactItemVO.setHeaderBitmap(BitmapFactory.decodeStream(input));
	                    contactItemVO.setHeadChar(headChar);
	                    contactList.add(contactItemVO);
                	}
                	index++;
                }
                phones.close();
                
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sort(contactList);
    }
	/**
	 * 
	 * @param constraint
	 * @return
	 * @param niufei
	 * @param 2014-4-18 下午3:21:24
	 * @return List<ContactItemVO>
	 * @throws
	 */
    @Deprecated
	public List<ContactItemVO> searchListByChar(CharSequence constraint) {

		List<ContactItemVO> list = new ArrayList<ContactItemVO>();
		if(context==null){
			return list;
		}
		if(constraint!=null&&constraint.length()!=0){
			String searchKey=null;
			if(checkContainChinese(constraint.toString())){
				searchKey="%"+constraint.toString()+"%";//	getPinYinHeadChar(constraint.toString())+"%";
			}else{
				searchKey="%"+constraint.toString()+"%";
			}
			list =getContactNameByPhoneNumber(searchKey);
		}
		return list;
	}
    
	private boolean isContain(String persionParam,CharSequence constraint){
		if(checkContainChinese(persionParam)&&!checkContainChinese(constraint.toString())){
			persionParam=getPinYinHeadChar(persionParam);
		}
		return  persionParam!=null&&persionParam.contains(constraint);
	}

	 /**
	 * 判断用户名
	 * @param str
	 * @return
	 */
	 public  boolean checkContainChinese(String str) {
		 boolean flag=false;
		 String regex =null;
		  regex = "([\u4e00-\u9fa5])+";//判定中文
		  flag= checkStr(regex,str);
		  return flag;
	}
	 /**
	  * 正则检查字符串
	  * @param regex
	  * @param input
	  * @return
	  */
	 private  boolean checkStr(String regex,String input){
		 
		 boolean flag=false;
			if(regex!=null){
			  Pattern p = Pattern.compile(regex);
			  Matcher m = p.matcher(input);
		  	  flag=m.matches();
			}
		 return flag;
	 }
	 
	 private static void getPinYinFullNameChar(ContactItemVO contactItemVO,String str) {
		StringBuffer convert =new StringBuffer();
		StringBuffer headCharBuffer=new StringBuffer();
		for (int j = 0; j < str.length(); j++) {

			char word = str.charAt(j);
			try{
				String[] pinyinArray =PinyinHelper.toHanyuPinyinStringArray(word);
				if (pinyinArray != null) {
					convert.append(pinyinArray[0].toString());
					headCharBuffer.append(pinyinArray[0].charAt(0));
				}
			}catch(Exception e){
				
			}
		}
		contactItemVO.setHeadChar(headCharBuffer.toString());
		contactItemVO.setFullNamePinYin(convert.toString());
	 }
	/**
	 * 返回中文的首字母
	 * 
	 * @param str
	 * @return
	 */

	public static String getPinYinHeadChar(String str) {
		String convert = "";

		for (int j = 0; j < str.length(); j++) {

			char word = str.charAt(j);

			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);

			if (pinyinArray != null) {

				convert += pinyinArray[0].charAt(0);

			} else {

				convert += word;

			}

		}
		return convert;

	}

	public static void setDate(List<ContactItemVO> list,
			String name, String mobile) {
		ContactItemVO employee = new ContactItemVO(
				name, mobile);
		if (!list.contains(employee)) {
			list.add(employee);
		}
	}

	/**
	 * 排序,去重
	 * 
	 * @param list
	 */
	public static List<ContactItemVO> sort(List<ContactItemVO> list) {
		// 排序
		Collections.sort(list,new Comparator<ContactItemVO>() {
					@Override
					public int compare(ContactItemVO lhs,
							ContactItemVO rhs) {
						return String.CASE_INSENSITIVE_ORDER.compare(
								lhs.getHeadChar(), rhs.getHeadChar());
					}
				});
		return list;

	}
	public static class ContactItemVO implements Serializable {
		public static final int CONTACTITEM_Person=0;
		public static final int CONTACTITEM_Group=1;
		private static final long serialVersionUID = 3509532437927415228L;
		String fullName;
	    String contactId;
	    String mobile;
	    int type;
	    Bitmap headerBitmap;
	    String headChar;
	    String fullNamePinYin;
	    public String getFullName() {
	        return fullName;
	    }

	    public String getmobile() {
	        return mobile;
	    }

		public Bitmap getHeaderBitmap() {
			return headerBitmap;
		}


		public void setHeaderBitmap(Bitmap headerBitmap) {
			this.headerBitmap = headerBitmap;
		}


		public String getHeadChar() {
			return headChar;
		}


		public void setHeadChar(String headChar) {
			this.headChar = headChar;
		}


		public ContactItemVO(String contactId,String fullName) {
			super();
			this.contactId=contactId;
			this.fullName = fullName;
		}
		
		
		
		public ContactItemVO() {
			super();
			// TODO Auto-generated constructor stub
		}


		public String getMobile() {
			return mobile;
		}


		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}

		public String getContactId() {
			return contactId;
		}


		public void setContactId(String contactId) {
			this.contactId = contactId;
		}


		public int getType() {
			return type;
		}


		public void setType(int type) {
			this.type = type;
		}

		public String getFullNamePinYin() {
			return fullNamePinYin;
		}

		public void setFullNamePinYin(String fullNamePinYin) {
			this.fullNamePinYin = fullNamePinYin;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
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
			ContactItemVO other = (ContactItemVO) obj;
			if (mobile == null) {
				if (other.mobile != null)
					return false;
			} else if (!mobile.equals(other.mobile))
				return false;
			return true;
		}
	}
}
