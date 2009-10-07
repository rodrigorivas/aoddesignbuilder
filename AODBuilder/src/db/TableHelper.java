package db;

import beans.db.DBBean;

public abstract class TableHelper {

	DatabaseManager db;
	
	protected TableHelper(DatabaseManager db) {
		this.db = db;
	}
	
	public abstract int createTable() throws Exception;
	public abstract int insert(DBBean bean) throws Exception;
	public abstract int update(DBBean bean) throws Exception;
	public abstract DBBean getById(int id) throws Exception;
}
