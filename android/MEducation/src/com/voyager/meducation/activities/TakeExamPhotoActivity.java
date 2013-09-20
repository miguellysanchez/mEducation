package com.voyager.meducation.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.voyager.meducation.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class TakeExamPhotoActivity extends Activity implements OnClickListener, OnItemSelectedListener{

	public static final String TAG = TakeExamPhotoActivity.class
			.getSimpleName();

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

	static class PhotoHandler implements Camera.PictureCallback {

		private final Context context;

		public PhotoHandler(Context context) {
			this.context = context;
		}

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			File pictureFileDir = getDir();

			if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

				Toast.makeText(context,
						"Can't create directory to save image.",
						Toast.LENGTH_LONG).show();
				return;

			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
			String date = dateFormat.format(new Date());
			String photoFile = studentName + "_testpaper_" + date + ".jpg";

			String filename = pictureFileDir.getPath() + File.separator
					+ photoFile;

			File pictureFile = new File(filename);

			try {
				FileOutputStream fos = new FileOutputStream(pictureFile);
				fos.write(data);
				fos.close();
				Toast.makeText(context, "New Image saved:" + photoFile,
						Toast.LENGTH_LONG).show();
			} catch (Exception error) {
				Toast.makeText(context, "Image could not be saved.",
						Toast.LENGTH_LONG).show();
			}
		}

		private File getDir() {
			File sdDir = Environment
					.getExternalStorageDirectory();
			Log.i(TAG, ">>>DIRECTORY: "+sdDir.getPath());
			return new File(sdDir, "MEducation");
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

}