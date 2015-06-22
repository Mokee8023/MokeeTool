package com.mokee.widget.Camera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.mokee.tools.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 自定义相机类，使用此类之前，请先在Mainifest中添加此类的Activity、添加权限：
  			<uses-permission android:name="android.permission.CAMERA" />
    		<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
   			<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
   			
   	此类中的相片地址是自动生成，如果使用自定义的url可更改小部分代码，然后再Intent中传入url即可
   	此相机类支持：闪光灯的开启与关闭、前后摄像头的切换、自动调焦（点击屏幕）、快门声的关闭与打开
 * @author Mokee_Work
 *
 */
public class MyCameraActivity extends Activity implements OnClickListener, Callback {
private static final String tag = "MyCamera";
	
	private SurfaceView sv_CameraPreview;
	private TextView tv_CameraTopText;
	private LinearLayout layout_More, linearLayout_More;
	private ImageButton ib_Back, ib_More, ib_GetPhoto, ib_Flash, ib_CameraNum, ib_Shutter;
	
	private SurfaceHolder mSurfaceHolder;
	private Camera mCamera;
	
	private int pictureWidth = 768, pictureHeight = 1024;
	private static String photoUrl;
	private int indexCamera = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_camera);
        
		// Bundle data = getIntent().getBundleExtra("photo_data");
		// photoUrl = data.getString("photo_url");

        initView();
        initEvent();
    }
    
	private void initView() {
		sv_CameraPreview = (SurfaceView) findViewById(R.id.sv_CameraPreview);
		tv_CameraTopText = (TextView) findViewById(R.id.tv_CameraTopText);
		ib_Back = (ImageButton) findViewById(R.id.ib_Back);
		ib_More = (ImageButton) findViewById(R.id.ib_More);
		ib_GetPhoto = (ImageButton) findViewById(R.id.ib_GetPhoto);
		ib_Flash = (ImageButton) findViewById(R.id.ib_Flash);
		ib_CameraNum = (ImageButton) findViewById(R.id.ib_CameraNum);
		ib_Shutter = (ImageButton) findViewById(R.id.ib_Shutter);
		layout_More = (LinearLayout) findViewById(R.id.layout_More);
		linearLayout_More = (LinearLayout) findViewById(R.id.linearLayout_More);
	}
	
	private void initEvent() {
		ib_Back.setOnClickListener(this);
		linearLayout_More.setOnClickListener(this);
		ib_GetPhoto.setOnClickListener(this);
		ib_Flash.setOnClickListener(this);
		ib_CameraNum.setOnClickListener(this);
		ib_Shutter.setOnClickListener(this);
		
		linearLayout_More.setTag("1");
		ib_Flash.setTag("1");
		ib_CameraNum.setTag("1");
		ib_Shutter.setTag("1");
		
		mSurfaceHolder = sv_CameraPreview.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置SurfaceHolder类型
		
		sv_CameraPreview.setFocusable(true);  
		sv_CameraPreview.setFocusableInTouchMode(true); 
		sv_CameraPreview.setClickable(true); 
		sv_CameraPreview.setOnClickListener(new OnClickListener() {// 点击SurfaceView进行自动对焦
			
			@Override
			public void onClick(View arg0) {
				mCamera.autoFocus(null);
			}
		});
	}
	
	private PictureCallback pictureCallBack = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// File photoFile = new File(photoUrl);
			File photoFile = getOutputMediaFile();
			Bitmap bitmap = rotatePhoto(data);
			try {
				FileOutputStream fos = new FileOutputStream(photoFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				
				Intent intent = new Intent();
				intent.setData(Uri.fromFile(photoFile));
				MyCameraActivity.this.setResult(RESULT_OK, intent);
				
				finish();
				onDestroy();
			} catch (Exception e) {
				Log.d(tag, "MyCamera Exception: " + e.getMessage());
				finish();
				onDestroy();
			}
		}
	};
	
	private ShutterCallback shutter = new ShutterCallback() {
		
		@Override
		public void onShutter() { }
	};
	
	@Override
	public void onClick(View view) {
		
		switch (view.getId()) {
		case R.id.ib_Back:
			finish();
			onDestroy();
			break;
			
		case R.id.ib_GetPhoto:
			mCamera.autoFocus(null);//自动对焦一次
			if(ib_Shutter.getTag().equals("1")){
				mCamera.takePicture(shutter, null, pictureCallBack);
			} else {
				mCamera.takePicture(null, null, pictureCallBack);
			}
			
			break;
			
		case R.id.linearLayout_More:
			if(linearLayout_More.getTag().equals("1")){
				ib_More.setImageResource(R.drawable.camera_more_pressed);
				linearLayout_More.setTag("2");
				layout_More.setVisibility(View.VISIBLE);
			} else if(linearLayout_More.getTag().equals("2")){
				ib_More.setImageResource(R.drawable.camera_more_nomal);
				linearLayout_More.setTag("1");
				layout_More.setVisibility(View.GONE);
			}
			break;
			
		case R.id.ib_Flash:
			Parameters parameters = mCamera.getParameters();
			if(ib_Flash.getTag().equals("1") && ib_CameraNum.getTag().equals("1")){
				ib_Flash.setImageResource(R.drawable.camera_flash_pressed);
				ib_Flash.setTag("2");
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			} else if(ib_Flash.getTag().equals("2")){
				ib_Flash.setImageResource(R.drawable.camera_flash_nomal);
				ib_Flash.setTag("1");
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			}
			mCamera.setParameters(parameters);
			mCamera.startPreview();
			break;
			
		case R.id.ib_CameraNum:
			if(Camera.getNumberOfCameras() == 2){
				if(ib_CameraNum.getTag().equals("1")){
					ib_CameraNum.setImageResource(R.drawable.camera_forwardcamera);
					ib_CameraNum.setTag("2");
					indexCamera = 1;
				} else if(ib_CameraNum.getTag().equals("2")){
					ib_CameraNum.setImageResource(R.drawable.camera_backcamera);
					ib_CameraNum.setTag("1");
					indexCamera = 0;
				}
				destoryCamera();
				mCamera = Camera.open(indexCamera);
				try {
					mCamera.setPreviewDisplay(mSurfaceHolder);
				} catch (IOException e) {
					e.printStackTrace();
				}
				initCamera();
				mCamera.startPreview();
			} else {
				Toast.makeText(this, "不可切换摄像头", Toast.LENGTH_SHORT).show();
			}
			break;
			
		case R.id.ib_Shutter:
			if(ib_Shutter.getTag().equals("1")){
				ib_Shutter.setTag("2");
				ib_Shutter.setImageResource(R.drawable.camera_no_sound);
			} else if(ib_Shutter.getTag().equals("2")){
				ib_Shutter.setTag("1");
				ib_Shutter.setImageResource(R.drawable.camera_sound);
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		int cameraNum = Camera.getNumberOfCameras();
		if (cameraNum > 0) {
			mCamera = Camera.open(indexCamera);
		} else {
			Toast.makeText(getApplicationContext(), "没有摄像头", Toast.LENGTH_SHORT).show();
			finish();
			onDestroy();
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		initCamera();
	}
	private void initCamera(){
		// 设置照片大小
		setPhotoSize();
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPictureSize(pictureWidth, pictureHeight);
		mCamera.setParameters(parameters);

		try {
			mCamera.setDisplayOrientation(90);// 竖屏应用，预览画面旋转90度
			mCamera.setPreviewDisplay(mSurfaceHolder);// 设置显示
			mCamera.startPreview();
			mCamera.autoFocus(null);// 先自动对焦一次
		} catch (IOException e) {
			Log.e(tag, "surfaceChanged.IOException:" + e.toString());
			destoryCamera();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		destoryCamera();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destoryCamera();
	}
	
	private void setPhotoSize() {
		Camera.Parameters parameters = mCamera.getParameters();
		List<Size> sizeList = parameters.getSupportedPictureSizes();				
		
		if(sizeList.size() > 1){
			int tempWidth, tempHeight;
			for (int i = sizeList.size() - 1; i > 0; i--) {
				tempWidth = sizeList.get(i).width;
				tempHeight = sizeList.get(i).height;
				if(tempWidth > pictureWidth ||  tempHeight > pictureHeight){
					pictureWidth = sizeList.get(i - 1).width;
					pictureHeight = sizeList.get(i - 1).height;
					break;
				}
			}
			Log.i(tag, "surfaceViewWidth:" + pictureWidth + ",surfaceViewHeight:" + pictureHeight);
		} else if(sizeList.size() == 1) {
			pictureWidth = sizeList.get(0).width;
			pictureHeight = sizeList.get(0).height;
			Log.i(tag, "surfaceViewWidth:" + pictureWidth + ",surfaceViewHeight:" + pictureHeight);
		}
	}
	
	/**将得到的照片进行90°旋转，使其竖直*/
	private Bitmap rotatePhoto(byte[] photo) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
		Matrix matrix = new Matrix();
		if(ib_CameraNum.getTag().equals("1")){
			matrix.preRotate(90);  
		} else if(ib_CameraNum.getTag().equals("2")){
			matrix.preRotate(-90);  
		}
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return bitmap;
	}
	
	private void destoryCamera(){
		if(mCamera != null){
			mCamera.stopPreview(); 
			mCamera.release();
			mCamera = null;
		}
	}
	
	private static File getOutputMediaFile(){
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraPic");
		
		if(!mediaStorageDir.exists()){
			if (!mediaStorageDir.mkdirs()) {
				Log.e(tag, "getOutputMediaFile.mkdirs:failed to create directory");
				return null;
			}
		}
		photoUrl = mediaStorageDir.getPath() + File.separator + getFileName();
		File mediaFile = new File(photoUrl);
		
		return mediaFile;
	}

	private static String getFileName() {
		return "MPIC_"+DateFormat.format("yyyy-MM-dd_hh:mm:ss", System.currentTimeMillis()) + ".jpg";
	}
}
