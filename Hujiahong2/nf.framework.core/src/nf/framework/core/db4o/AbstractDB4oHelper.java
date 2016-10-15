package nf.framework.core.db4o;

import java.io.File;
import java.io.IOException;
import java.util.List;

import nf.framework.core.exception.LogUtil;
import nf.framework.core.exception.NFRuntimeException;
import android.content.Context;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.query.Query;

public abstract class AbstractDB4oHelper<T> {
	private ObjectContainer oc = null;
	private Context context;
	public AbstractDB4oHelper(Context ctx) {
		context = ctx;
	}
	protected synchronized  ObjectContainer db() {
		try {
			if(hasChangedFilePath()){
				close();
			}
			if (oc == null || oc.ext().isClosed()) {
				String dbFilePath=db4oDBFullPath(context);
				LogUtil.d(context, "-----------excute get db-----------"+dbFilePath);
				oc = Db4o.openFile(dbConfig(),dbFilePath);
			}
		} catch (Exception e) {
			e.getStackTrace();
			LogUtil.e(AbstractDB4oHelper.class.getName(), e.toString());
		}
		return oc;
	}

	protected Configuration dbConfig() {
		Configuration c = Db4o.newConfiguration();
		// Index entries by Id
		c.objectClass(getClassT()).objectField(setObjectIndexedField()).indexed(true);
		// Configure proper activation + update depth
		return c;
	}
	
	protected abstract Class<T> getClassT();

	public String db4oDBFullPath(Context ctx) {
		String dbFilePath = getDbFilePath();
		try {
			File file = new File(dbFilePath);
			if (!file.getParentFile().isDirectory()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.e(context, "clearDatabase is error");
		}
		return dbFilePath;
	}
	protected abstract String setObjectIndexedField();

	protected abstract String getDbFilePath();

	
	protected abstract boolean hasChangedFilePath();
	public void close() {
		LogUtil.i(context, "++++++++++close close close++++++++++++");
		if (oc != null) {
			oc.close();
			oc = null;
		}
	}

	public void commit() {
		db().commit();
	}

	public void rollback() {
		db().rollback();
	}

	public void deleteDatabase() {
		close();
		new File(db4oDBFullPath(context)).delete();
	}

	public void backup(String path) throws Exception{
		db().ext().backup(path);
	}

	/***
	 * 
	 * 
	 * @param niufei
	 * @param 2014-3-21 ����11:45:35
	 * @return void
	 * @throws
	 */
	//清楚数据
	public void clearDatabase() {
		deleteDatabase();
		try {
			File file = new File(db4oDBFullPath(context));
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtil.e(context, "clearDatabase is error");
		}
	}

	/**
	 * get all data list
	 * 
	 * @return
	 * @param niufei
	 * @param 2014-3-25 ����10:37:10
	 * @return List<?>
	 * @throws
	 */
	public List<T> fetchAllRows()  throws Exception{
		return db().query(getClassT());
	}
	/**
	 * down
	 * @return
	 * @param niufei
	 * @param 2014-4-8 ����3:35:03
	 * @return ObjectSet<T>
	 * @throws
	 */
	protected List<T> fetchAllRowsOrderDescending(String fieldParam)  throws Exception{
		Query query = db().query();
		query.constrain(getClassT());
		query.descend(fieldParam).orderDescending();
		return query.execute();
	}
	/**
	 * ��ѯ��¼����
	 * 
	 * @return
	 * @param niufei
	 * @param 2014-3-21 ����11:46:31
	 * @return int
	 * @throws
	 */
	public int getObjectCount()  throws Exception{
		List<T> list = fetchAllRows();
		return list == null ? 0 : list.size();
	}

	/***
	 * 
	 * @param fieldParam
	 * @return
	 * @param niufei
	 * @param 2014-3-21 ����11:50:23
	 * @return boolean
	 * @throws
	 */
	public boolean isContainObject(Object fieldParam)  throws Exception{
		T currObj = fetchObjectById(fieldParam);
		if (currObj != null) {
			return true;
		}
		return false;
	}

	protected ObjectSet<T> fetchListById(Object fieldParam) throws Exception{
		Query query = db().query();
		query.constrain(getClassT());
		query.descend(setObjectIndexedField()).constrain(fieldParam);
		ObjectSet<T> list = query.execute();
		return list;
	}

	protected ObjectSet<T> fetchListByColumnField(String columnField,Object fieldParam)throws Exception{
		Query query = db().query();
		query.constrain(getClassT());
		query.descend(columnField).constrain(fieldParam);
		ObjectSet<T> list = query.execute();
		return list;
	}

	public T fetchObjectById(Object fieldParam)throws Exception{
		ObjectSet<T> result = fetchListById(fieldParam);
		if (result.hasNext())
			return result.next();
		else
			return null;
	}

	/***
	 * save Object
	 * 
	 * @param Object
	 * @param niufei
	 * @param 2014-3-21 ����11:46:54
	 * @return void
	 * @throws
	 */
	public void saveObject(T object)throws Exception{
		db().store(object);
		this.commit();
		LogUtil.d(context, "******saveObject success *****");
	}
	/***
	 * update Object
	 * 
	 * @param Object
	 * @param niufei
	 * @param 2014-3-21 ����11:46:54
	 * @return void
	 * @throws
	 */
	public void updateObject(T object) throws Exception{
		db().ext().set(object);
		this.commit();
		LogUtil.d(context, "******update Object success *****");
	}
	/***
	 * 
	 * @param list
	 * @param niufei
	 * @param 2014-3-21 ����12:19:54
	 * @return void
	 * @throws
	 */
	public void saveObjectList(List<T> list)throws Exception{
		ObjectContainer onjContainer=db();
		if(onjContainer==null){
			throw new NFRuntimeException("onjContainer is empty");
		}
		for (T object : list) {
			onjContainer.store(object);
		}
		this.commit();
		LogUtil.i(context, "******saveObjectList success******");
	}

	/**
	 * delete
	 * 
	 * @param itemId
	 * @param niufei
	 * @param 2014-3-21 ����11:47:10
	 * @return void
	 * @throws
	 */
	public void deleteObject(T object) throws Exception{
		db().delete(object);
		this.commit();
		LogUtil.w(context, "******deleteObject success******");
	}
}
