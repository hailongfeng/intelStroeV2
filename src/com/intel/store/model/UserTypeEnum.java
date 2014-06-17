/**  
 * @Package com.intel.store.model 
 * @FileName: UserTypeEnum.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月30日 下午3:37:18 
 * @version V1.0  
 */
package com.intel.store.model;

/**
 * @ClassName: UserTypeEnum
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fenghl
 * @date 2013年10月30日 下午3:37:18
 */
public enum UserTypeEnum {
	MANAGER(0, "manager"), CLERK(1, "clerk");

	private Integer value;
	private String name;

	UserTypeEnum(Integer type, String name) {
		value = type;
		this.name = name;
	}

	public int value() {
		return value;
	}

	public String getName() {
		return this.name;
	}
}
