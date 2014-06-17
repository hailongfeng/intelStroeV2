/**  
 * @Package com.intel.store.model 
 * @FileName: StoryTypeEnum.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月16日 下午2:20:26 
 * @version V1.0  
 */
package com.intel.store.model;

/**
 * @ClassName: StoryTypeEnum
 * @Description: TODO(店铺的类型：OEM、DIY)
 * @author fenghl
 * @date 2013年10月16日 下午2:20:26
 */
public enum StoryTypeEnum {
	OEM(78), DIY(77);

	private Integer value;

	StoryTypeEnum(Integer type) {
		value = type;
	}

	public int value() {
		return value;
	}

}
