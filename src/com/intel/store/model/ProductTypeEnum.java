/**  
 * @Package com.intel.store.model 
 * @FileName: ProductTypeEnum.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月16日 下午2:20:26 
 * @version V1.0  
 */
package com.intel.store.model;

/**
 * @ClassName: ProductTypeEnum
 * @Description: TODO(店铺的类型：OEM、DIY)
 * @author fenghl
 * @date 2013年10月16日 下午2:20:26
 */
public enum ProductTypeEnum {
	Mobile(0), Desktop(1);

	private Integer value;

	ProductTypeEnum(Integer type) {
		value = type;
	}

	public int value() {
		return value;
	}

}
