package com.voyager.meducation.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.voyager.meducation.R;
import com.voyager.meducation.utils.DateUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Picture;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ImageButton;
import android.widget.ProgressBar;;


public class TakeExamPhotoActivity extends Activity implements OnClickListener, OnItemSelectedListener{

	public static final String TAG = TakeExamPhotoActivity.class
			.getSimpleName();

	private static final int PREVIEW_WIDTH = 85;
	private static final int PREVIEW_HEIGHT = 100;
	
	
	private SurfaceView surface_view;
	private Camera mCamera;
	SurfaceHolder.Callback sh_ob = null;
	SurfaceHolder surface_holder = null;
	SurfaceHolder.Callback sh_callback = null;
	static String studentName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_exam_photo_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Camera settings
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		surface_view = (SurfaceView) findViewById(R.id.surface);
		if (surface_holder == null) {
			surface_holder = surface_view.getHolder();
		}
		sh_callback = my_callback();
		surface_holder.addCallback(sh_callback);

		findViewById(R.id.btnPhotoCapture).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Log.d(TAG, ">>>SHUTTER");
						((ImageButton)findViewById(R.id.btnPhotoCapture)).setVisibility(View.GONE);
						((ProgressBar)findViewById(R.id.progressBarWaitCapture)).setVisibility(View.VISIBLE);
						final PhotoHandler photoHandler = new PhotoHandler(
								TakeExamPhotoActivity.this);
						mCamera.takePicture(null, null, photoHandler);

					}
				});
		
		((Spinner)findViewById(R.id.dropdownStudentList)).setOnItemSelectedListener(this);

	}

	SurfaceHolder.Callback my_callback() {
		SurfaceHolder.Callback ob1 = new SurfaceHolder.Callback() {

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				mCamera = Camera.open();
				mCamera.setDisplayOrientation(90);
				mCamera.startPreview();
//				Camera.Parameters params = mCamera.getParameters();
//				params.setPreviewFormat(ImageFormat.JPEG);
//				params.setPictureSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
//				mCamera.setParameters(params);

				try {
					mCamera.setPreviewDisplay(holder);
				} catch (IOException exception) {
					mCamera.release();
					mCamera = null;
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				mCamera.startPreview();
			}
		};
		return ob1;
	}

	class PhotoHandler implements Camera.PictureCallback {

		private final Context context;

		public PhotoHandler(Context context) {
			this.context = context;
		}

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			
			
			camera.startPreview();
			
			
			StorePhotoTask storePhotoTask = new StorePhotoTask();
			storePhotoTask.execute(data);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		studentName = parent.getItemAtPosition(pos).toString();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}
	
	class StorePhotoTask extends AsyncTask<byte[], String, String>{

		@Override
		protected String doInBackground(byte[]... args) {
			File pictureFileDir = getDir();

			byte[] data = args[0];
			
			if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {
				cancel(true);
			}

			String date = DateUtils.getDateString();
			String photoFile = "Mathematics_Division_Section A_"+studentName + "_testpaper_-_" + date + ".jpg";

			String filename = pictureFileDir.getPath() + File.separator
					+ photoFile;

			File pictureFile = new File(filename);

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				return photoFile;
			} catch (Exception error) {
				
			}

			
			return null;
		}
		
		@Override
		protected void onPostExecute(String filePath){
			if(filePath!=null){
				Toast.makeText(TakeExamPhotoActivity.this, "New Image saved:" + filePath,
						Toast.LENGTH_LONG).show();
			}
			else{
				Toast.makeText(TakeExamPhotoActivity.this, "Image could not be saved.",
						Toast.LENGTH_LONG).show();
			}
			((ImageButton)findViewById(R.id.btnPhotoCapture)).setVisibility(View.VISIBLE);
			((ProgressBar)findViewById(R.id.progressBarWaitCapture)).setVisibility(View.GONE);
		}
		
		@Override
		protected void onCancelled(){
			Toast.makeText(TakeExamPhotoActivity.this,
					"Can't create directory to save image.",
					Toast.LENGTH_LONG).show();
			((ImageButton)findViewById(R.id.btnPhotoCapture)).setVisibility(View.VISIBLE);
			((ProgressBar)findViewById(R.id.progressBarWaitCapture)).setVisibility(View.GONE);
		}
		
		private File getDir() {
			File sdDir = Environment
					.getExternalStorageDirectory();
			Log.i(TAG, ">>>DIRECTORY: "+sdDir.getPath());
			return new File(sdDir, "MEducation");
		}
		
	}

}