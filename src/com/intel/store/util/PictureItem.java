package com.intel.store.util;

import java.io.Serializable;

public class PictureItem implements Serializable {

    private static final long serialVersionUID = 1L;
    public static PictureItem pictureItemPlusSign = new PictureItem("", "", "",
            "", "", "", "", "", "", "", "", "", "", "", "", true,"");
    public static PictureItem template;
    
    // for post
    public String mSlsprsId;
    public String mStoreId;
    public String mWhen;
    public String mComment;
    public String mLongitude;
    public String mLatitude;
    public String mRoleId;
    public String mCategoryId;
    public String mModelId;
    public String mCityType;
    public String mStoreAddr;
    public String mPictureFileName;
    public String mRep_id;

    // for display and control
    public String mStoreName;
    public String mCategoryName;
    public String mAbsolutePckFileName;
    public boolean mIsPlusBtn;

    public PictureItem() {
    }
    
    public PictureItem(PictureItem item) {
        this.mSlsprsId = item.mSlsprsId;
        this.mStoreId = item.mStoreId;
        this.mWhen = item.mWhen;
        this.mComment = item.mComment;
        this.mLongitude = item.mLongitude;
        this.mLatitude = item.mLatitude;
        this.mRoleId = item.mRoleId;
        this.mCategoryId = item.mCategoryId;
        this.mModelId = item.mModelId;
        this.mCityType = item.mCityType;
        this.mStoreAddr = item.mStoreAddr;
        this.mPictureFileName = item.mPictureFileName;

        this.mStoreName = item.mStoreName;
        this.mCategoryName = item.mCategoryName;
        this.mAbsolutePckFileName = item.mAbsolutePckFileName;
        this.mIsPlusBtn = item.mIsPlusBtn;
    }
    public PictureItem(String slsprsId, String storeId, String when,
            String comment, String longitude, String latitude, String roleId,
            String categoryId, String modelId, String cityType, String storeAddr,
            String pictureFileName, String categoryName,
            String absolutePckFileName, boolean isPlusBtn,String rep_id) {
    	/*
    	 * sessionId  
    	 * store_id  
    	 * photo_comment 
    	 * photo_cat 
    	 * cityType  
    	 * mdl_id  
    	 * role_id  
    	 * stor_addr  
    	 * rep_id 
    	 * loc_longitude 
    	 * loc_latitude
    	 * */
        this.mSlsprsId = slsprsId;
        this.mStoreId = storeId;
        this.mWhen = when;
        this.mComment = comment;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mRoleId = roleId;
        this.mCategoryId = categoryId;
        this.mModelId = modelId;
        this.mCityType = cityType;
        this.mStoreAddr = storeAddr;
        this.mPictureFileName = pictureFileName;

        this.mCategoryName = categoryName;
        this.mAbsolutePckFileName = absolutePckFileName;
        this.mIsPlusBtn = isPlusBtn;
        this.mRep_id=rep_id;
        this.mPictureFileName=pictureFileName;
    }
    public PictureItem(String slsprsId, String storeId, String when,
            String comment, String longitude, String latitude, String roleId,
            String categoryId, String modelId, String cityType, String storeAddr,
            String pictureFileName, String storeName, String categoryName,
            String absolutePckFileName, boolean isPlusBtn,String rep_id ) {
    	this.mRep_id=rep_id;
        this.mSlsprsId = slsprsId;
        this.mStoreId = storeId;
        this.mWhen = when;
        this.mComment = comment;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
        this.mRoleId = roleId;
        this.mCategoryId = categoryId;
        this.mModelId = modelId;
        this.mCityType = cityType;
        this.mStoreAddr = storeAddr;
        this.mPictureFileName = pictureFileName;

        this.mStoreName = storeName;
        this.mCategoryName = categoryName;
        this.mAbsolutePckFileName = absolutePckFileName;
        this.mIsPlusBtn = isPlusBtn;
    }

    public String toString() {
        return String
                .format("mSlsprsId = %s, mStoreId = %s, mWhen = %s, mComment = %s, mLongitude = %s"
                        + ", mLatitude = %s, mRoleId = %s, mCategoryId = %s, mModelId = %s, mCityType = %s"
                        + ", mStoreAddr = %s, mPictureFileName = %s, mStoreName = %s, mCategoryName = %s"
                        + ", mAbsolutePckFileName = %s, mIsPlusBtn = %b",
                        mSlsprsId, mStoreId, mWhen, mComment, mLongitude,
                        mLatitude, mRoleId, mCategoryId, mModelId, mCityType,
                        mStoreAddr, mPictureFileName, mStoreName, mCategoryName,
                        mAbsolutePckFileName, mIsPlusBtn);
    }
}