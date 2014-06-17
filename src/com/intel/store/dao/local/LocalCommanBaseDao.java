package com.intel.store.dao.local;

import com.intel.store.db.StoreDBHelperImpl;
import com.pactera.framework.dao.local.DBConstants;
import com.pactera.framework.dao.local.DBHelperImpl;
import com.pactera.framework.dao.local.LocalBaseDao;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.util.Loger;

public class LocalCommanBaseDao extends LocalBaseDao {
	public static LocalDBConstants localDBConstants = new LocalDBConstants();

	public LocalCommanBaseDao() {
		super();
		try {
			init(localDBConstants);
		} catch (DBException e) {
			Loger.d(e.getMessage());
		}
	}

	@Override
	protected DBHelperImpl getDBHelperImpl(DBConstants dbConstants) {
		return new StoreDBHelperImpl(DBConstants.DATABASE_NAME,
				DBConstants.DATABASE_VERSION, DBConstants.DB_CREATE_SQL);
	}
}
