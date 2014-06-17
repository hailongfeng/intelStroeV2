package com.intel.store.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.intel.store.R;
import com.pactera.framework.util.CameraHelper;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PictureFileHelper;
import com.pactera.framework.util.PictureUtil;

public class TakePictureActivity extends BaseActivity {

    private ImageButton ibtn_title_back;
    private TextView txt_title_msg;
    private Button btn_title_send;
    private FrameLayout flPreview;
    private Button btnCapture;
    private Camera mCamera;
   // private TakePictureCameraView mPreview;
    private String m_strPictureFilePath = "";
    //private PreviewThread mPreviewThread = null;
    private TextView txtCategory;
    private TextView txtDate;
    private String m_strCategory = "";
    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("", "onPictureTaken");
            File pictureFile = PictureFileHelper
                    .getOutputMediaFile(PictureFileHelper.MEDIA_TYPE_IMAGE);
            Log.d("", pictureFile == null ? "pictureFile is null"
                    : "pictureFile is not null");
            if (pictureFile == null) {
                Log.d("", "pictureFile is null");
                m_strPictureFilePath = "";
                return;
            }

            m_strPictureFilePath = pictureFile.getAbsolutePath();
            Log.d("", "take pic filepath = " + m_strPictureFilePath);
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            toPicturePreview();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_picture);
        
        getActivityParameter();
    }
    
    private void getActivityParameter() {
        Intent intent = getIntent();
        m_strCategory = intent.getStringExtra("strCategory");
    }

    @Override
	public void onResume() {
        super.onResume();

        mCamera = CameraHelper.getCameraInstance();
        CameraHelper.setCameraDisplayOrientation(this, mCamera);

        //mPreview = new TakePictureCameraView(this, mCamera);
        //mPreviewThread = mPreview.getThread();
        initView();
        setListener();
    }

    @SuppressLint("WrongViewCast")
	private void initView() {
        flPreview = (FrameLayout) findViewById(R.id.flPreview);
       // flPreview.addView(mPreview);
        
        // set title
        ibtn_title_back = (ImageButton) findViewById(R.id.ibtn_title_back);
        txt_title_msg = (TextView) findViewById(R.id.txt_title_msg);
        btn_title_send = (Button) findViewById(R.id.btn_title_send);
        
        txt_title_msg.setText(this.getString(R.string.txt_take_picture));
        btn_title_send.setVisibility(View.GONE);

        btnCapture = (Button) findViewById(R.id.btnCapture);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtDate = (TextView) findViewById(R.id.txtDate);

        txtCategory.setText(m_strCategory);
        String strDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss")
                .format(new Date());
        txtDate.setText(strDate);
    }

    private void setListener() {
        ibtn_title_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Loger.d("take picture back btn clicked");
                releaseCamera();
                setResult(2, new Intent());
                finish();
            }
        });
        
        btnCapture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.d("", "btn click");
                mCamera.takePicture(null, null, mPicture);
                Log.d("", mCamera == null ? "mCamera is null"
                        : "mCamera is not null");
            }
        });
        btn_title_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Log.d("", "btn click");
			}
		});
    }

    public boolean checkHardwareCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
	public void onPause() {
        releaseCamera();
        super.onPause();
    }

    private void releaseCamera() {
//        if (mPreviewThread != null) {
//            mPreviewThread.pause();
//        }
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private String savePhoto() {
        if (m_strPictureFilePath != null) {
            FileOutputStream fos = null;
            Bitmap bm = null;
            try {
                File f = new File(m_strPictureFilePath);
                bm = PictureUtil.getSmallBitmap(m_strPictureFilePath);
                File nFile = new File(PictureUtil.getAlbumDir(), "small_"
                        + f.getName());
                fos = new FileOutputStream(nFile);
                bm.compress(Bitmap.CompressFormat.JPEG, 40, fos);
                return nFile.getPath();
            } catch (Exception e) {
            } finally {
                if (null != fos) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
                if (null != bm) {
                    bm.recycle();
                    bm = null;
                }
            }
        }
        return null;
    }
    
    private void toPicturePreview() {
        m_strPictureFilePath = savePhoto();
        
       // this.setContentView(new TakePictureCameraView(this, mCamera));
        /*Intent toPicturePreview = new Intent();
        toPicturePreview.setClass(TakePictureActivity.this,
                TakePictureCameraView.class);
        
        
        Log.d("", "take picture path = " + m_strPictureFilePath);
        toPicturePreview
                .putExtra("picture_path", m_strPictureFilePath);
        toPicturePreview.putExtra("category", m_strCategory);
        startActivityForResult(toPicturePreview, 1);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
            finish();
        } else {
            // Re take picture
        }
    }

}
