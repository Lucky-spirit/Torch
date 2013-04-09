package com.blogspot.leved_notes.torch;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ActivityMain extends Activity implements OnClickListener {

	private boolean mState;
	private Camera mCamera;
	private Parameters mParams;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.btn_light).setOnClickListener(this);
		findViewById(R.id.btn_text).setOnClickListener(this);

		if (!checkCameraHardware())
			gameOver();

		mCamera = getCameraInstance();
		checkFlash();
		light();
	}

	@Override
	public void finish() {
		super.finish();
		if (mCamera != null)
			mCamera.release();
	}

	public void gameOver() {
		finish();
		Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://vk.com/id22849605"));
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		mState = !mState;

		ToggleButton btn = (ToggleButton) findViewById(R.id.btn_text);
		btn.setChecked(mState);

		light();
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware() {
		boolean result = false;
		PackageManager pm = getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
				&& pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
			// this device has a camera and flash
			result = true;
		} else
			gameOver();

		return result;
	}

	/** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance() {
		CameraInfo info = new CameraInfo();
		Camera c = null;

		for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK) {
				try {
					c = Camera.open(i); // attempt to get a Camera instance
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return c; // returns null if camera is unavailable
	}

	private void checkFlash() {
		if (mCamera != null) {
			mParams = mCamera.getParameters();
			mState = mParams.getFlashMode().equals(Parameters.FLASH_MODE_TORCH) ? true
					: false;
		} else
			gameOver();
	}

	private void light() {
		if (mCamera != null) {
			String mode = mState ? Parameters.FLASH_MODE_TORCH
					: Parameters.FLASH_MODE_OFF;

			mParams.setFlashMode(mode);
			mCamera.setParameters(mParams);
		}
	}

}
