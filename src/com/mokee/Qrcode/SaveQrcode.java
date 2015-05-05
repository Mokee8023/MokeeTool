package com.mokee.Qrcode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mokee.API.API;
import com.mokee.FilePath.FilePath;
import com.mokee.tools.R;

public class SaveQrcode extends Activity implements OnClickListener {
	private static final String TAG = "SaveQrcode";

	private Bitmap qrcodeBitmap = null;
	private byte[] dataByte;

	private ImageView iv_QrcodeSave;
	private EditText et_SaveName;
	private Spinner sp_SaveFormat;
	private Button btn_SvaeOk;
	private Button btn_SaveCancle;

	private String bmpSavePath = "/sdcard/MokeeQrCode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setFinishOnTouchOutside(false);//设置点击Dialog之外空白区域不关闭Dialog
		setContentView(R.layout.activity_save);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("qrcode");
		dataByte = bundle.getByteArray("qrcode");

		initView();
	}

	private void initView() {
		iv_QrcodeSave = (ImageView) findViewById(R.id.iv_QrcodeSave);
		et_SaveName = (EditText) findViewById(R.id.et_SaveName);
		sp_SaveFormat = (Spinner) findViewById(R.id.sp_SaveFormat);
		btn_SvaeOk = (Button) findViewById(R.id.btn_SvaeOk);
		btn_SaveCancle = (Button) findViewById(R.id.btn_SaveCancle);

		sp_SaveFormat.setSelection(0);

		if ((qrcodeBitmap = API.Byte2Bitmap(dataByte)) == null) {
			Toast.makeText(getApplicationContext(), "Byte[] to Bitmap Failed!",
					Toast.LENGTH_SHORT).show();
		} else {
			iv_QrcodeSave.setImageBitmap(qrcodeBitmap);
		}

		btn_SvaeOk.setOnClickListener(this);
		btn_SaveCancle.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_SvaeOk:

			bmpSavePath = FilePath.GetFilePath();
			if (bmpSavePath == null) {
				bmpSavePath = "/sdcard/MokeeQrCode";
				Toast.makeText(getApplicationContext(),
						"No External Sdcard,Save to '/sdcard/MokeeQrCode'",
						Toast.LENGTH_SHORT).show();
			} else {
				/////////////////////Android 4.4之后的系统///////////////////////////////////////
				
				/*
				 * Android 4.4之后的系统，由于有内置存储卡，所有如若再外置存储卡上保存文件，创建目录
				 * 必须先执行该句话，创建/Android/data/包名/的目录，然后才能在外置存储卡上创建目录，
				 * 否则创建目录会失败
				 */
				this.getExternalFilesDir(null);
				
				////////////////////////////////////////////////////////////////////////////////
				bmpSavePath = bmpSavePath + "/Android/data/"
						+ this.getApplicationContext().getPackageName()
						+ "/MokeeQrCode";
			}

			String saveName = et_SaveName.getText().toString().trim();
			String format = sp_SaveFormat.getSelectedItem().toString();
			if (!saveName.equals("")) {
				if (qrcodeBitmap != null) {
					SaveQrCodeBitmap(saveName, format);
				} else {
					Toast.makeText(getApplicationContext(),
							"Byte[] to Bitmap Failed!", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"Please Input Save Name!", Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.btn_SaveCancle:
			this.setResult(RESULT_CANCELED);
			finish();
			onDestroy();
			break;
		default:
			break;
		}
	}

	private void SaveQrCodeBitmap(String saveName, String format) {
		File file = new File(bmpSavePath);
		if (!file.exists()) {
			Log.e(TAG, "" + file.mkdirs());
		}

		if (qrcodeBitmap == null) {
			Toast.makeText(getApplicationContext(), "QrCode is Empty!",
					Toast.LENGTH_SHORT).show();
		} else {
			String filePath = bmpSavePath + File.separator + saveName + "."
					+ format;
			Log.i(TAG, "filePath:" + filePath);
			file = new File(filePath);
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				qrcodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				Toast.makeText(getApplicationContext(), "Save File success! \nPath："+bmpSavePath,
						Toast.LENGTH_SHORT).show();
				this.setResult(RESULT_OK);
				finish();
				onDestroy();
			} catch (IOException e) {
				Log.e(TAG, "Create File IOException:" + e.toString());
				Toast.makeText(getApplicationContext(),
						"Create File IOException:" + e.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
