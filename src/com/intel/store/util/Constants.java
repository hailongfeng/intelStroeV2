/**
 * 
 */
package com.intel.store.util;

/**
 * 
 * @Title: Constants.java
 * @Package com.intel.store.util
 * @Description:(保存全局访问的静态数据)
 * @author fenghl
 * @date 2013年10月9日 下午3:41:21
 * @version V1.0
 */

public class Constants {
    public static final String SHARED_PREFERENCE_NAME = "pactera_preferences";

	public static final String LINE_DX = "host_dx";
	public static final String LINE_WT = "host_wt";

	/**IREP接口host*/
	public static final String IREP_API_HTTP_HOST = "http://retailedgecn.intel.com/api";
	public static final String IREP_API_HTTPS_HOST = "https://retailedgecn.intel.com/api";
	public static final String IREP_API_AUTH_LOGIN = "/auth/login";

	
	
	public static final String SALE_WORD_DIR = "salesDOC";
	public static final String SALE_WORD_ZIP_FILENAME = "saleWords.zip";
	public static final String SALE_WORD_HTML_FILENAME = "index.html";
	/**
	 * 天翼云的key和秘钥
	 */
	public static final String ACCESS_KEY_ID = "c362c15ebf101b04908b";
	/**
	 * 天翼云的key和秘钥
	 */
	public static final String SECRET_KEY = "5097aff9352354b7d5b3fdc23d8ba8fab499eddf";
	public static final String BUCKET_PICTURE_NAME = "intel.store";
	public static final String ENDPOINT = "http://oos.ctyunapi.cn";
	public static final String PICTURE_DOMAIN = "http://"+BUCKET_PICTURE_NAME+".oos.ctyunapi.cn";
	
	
	/**
	 * 软件升级接口
	 */
	public static final String API_UPDATESERVLET = "getUpdateAPKServlet?fileurl=";
	/**
	 * 登陆接口
	 */
	public static final String API_LOGINSERVLET = "storeRspMgmt/login.do";
	/**
	 * 退出接口
	 */
	public static final String API_LOGOUTSERVLET = "storeMgmt/logOut.do";
	/**
	 * 获得当前SR的门店列表
	 */
	public static final String API_LISTRSPSTORE = "storeRspMgmt/listRspStore.do";
	/**
	 * 门店销量
	 */
	public static final String API_DIYSALESDATASERVLET = "queryDIYSalesDataServlet";
	/**
	 * 门店积分
	 */
	public static final String API_STOREBONUSSERVLET = "queryStoreBonusServlet";
	/**
	 * 获得店员列表
	 */
	public static final String API_GETCLERKLIST = "storeRspMgmt/listRsp.do";
	/**
	 * 增加店员
	 */
	public static final String API_ADDRSP = "storeRspMgmt/addRsp.do";
	/**
	 * 修改店员
	 */
	public static final String API_MDFRSP = "storeRspMgmt/modifyRsp.do";
	/**
	 * 删除店员
	 */
	public static final String API_DELRSP = "storeRspMgmt/deleteRsp.do";
	/**
	 * 图片类型获取
	 */
	public static final String PICTURE_CAT_SERVELET = "queryCategoryServlet";
	/**
	 * 获取门店图片头像
	 */
	public static final String READ_SINGLE_RSP_PIC_SERVELET = "readSingleRspPicServlet?pht_pth=";
	/**
	 * 上传图片
	 */
	public static final String PICTURE_UPLOAD_SERVELET = "rspImagUploadServlet";
	//检查zip包是否有更新
	public static final String API_CHECKTARVERSIONSERVLET = "checkTarVersionServlet";
	//修改密码
	public static final String API_CHANGE_PASSWORD = "storeRspMgmt/modifyPwd.do";
	public static final String API_ALLPRODUCTSERVLET = "allProductServlet";
	//oem销量申报 查询、提交
	public static final String API_LISTOEMSALEDATA = "storeRspMgmt/listOemSaleData.do";
	public static final String API_ADDOEMSALEDATA = "storeRspMgmt/addOemSaleData.do";
	//diy销量申报 查询、提交
	public static final String API_LISTDIYSALEDATA = "storeRspMgmt/listDiySaleData.do";
	public static final String API_ADDDIYSALEDATA = "storeRspMgmt/addDiySaleData.do";
	//扫码验证
	public static final String API_VALIDATEBARCODE = "storeMgmt/queryCPUModel.do";
	//获取当前季度
	public static final String API_QueryCurrentQuarter = "newStoreRspMgmt/queryCurrentQuarter.do";
	//获取轮次信息
	public static final String API_ListWvByQuarter = "newStoreRspMgmt/listWvByQuarter.do";
	//获取根据轮次获取图片分类
	public static final String API_ListCategoryByWv = "newStoreRspMgmt/listCategoryByWv.do";
	
	public static final String API_LISTHISTORYWV = "newStoreRspMgmt/listHistoryWv.do";
	 //根据分类获取已上传的图片
	public static final String API_ListPhotoByWvCategory = "newStoreRspMgmt/listPhotoByWvCategory.do";
}
