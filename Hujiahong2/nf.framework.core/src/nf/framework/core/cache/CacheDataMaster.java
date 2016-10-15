package nf.framework.core.cache;

import java.util.List;

import android.content.Context;

/**
 * 	�����ļ��������
 * @author tingting
 *
 */
public class CacheDataMaster extends AbstractCacheFileMaster {

	private Context context;
	
	private String fileFolderName;
	public CacheDataMaster(Context mcontext,String fileFolderName){
		
		this.context=mcontext;
		this.fileFolderName=fileFolderName;
	}
	
	/***
	 * ���浽�����ļ��� 
	 */
	@Override
	public void saveToCacheFile(String jsonData,String fileName){
		if(jsonData==null){
			return;
		}
		if(fileName== null){
			return;
		}
		try {
			 //save to /data/data
			SaveToSysFile(context, jsonData,fileFolderName, fileName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/***
	 * 
	 */
	@Override
	public boolean deleteCacheFileByName(String fileName) {
		// TODO Auto-generated method stub
		if(fileName== null){
			return false;
		}
		try {
			return deleteCacheFileByName(context, fileName, fileFolderName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/***
	 * ��ȡ�����ļ�
	 */
	@Override
	public String readCacheFile(String fileName) {
		// TODO Auto-generated method stub
		String content = null;
		if(fileName== null){
			return null;
		}
		try {
			content=	readCacheFile(context, fileName, fileFolderName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	@Override
	public boolean deleteAllCacheFiles() {
		// TODO Auto-generated method stub
		try {
			return	deleteAllCacheFile(context, fileFolderName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public List<String> queryAllCacheFileNameList(){
		try {
			return queryAllCacheFile(context, fileFolderName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
