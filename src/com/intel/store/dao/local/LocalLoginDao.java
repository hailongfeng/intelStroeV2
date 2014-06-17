package com.intel.store.dao.local;

public class LocalLoginDao extends LocalCommanBaseDao {
	private static LocalLoginDao localLoginDao = new LocalLoginDao();

	private LocalLoginDao() {
		super();
	}

	public static LocalLoginDao getInstance() {
		return localLoginDao;
	}

	void test() {
	}

}
