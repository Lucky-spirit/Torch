package com.blogspot.leved_notes.torch;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.view.Menu;
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

		if (!checkCameraHardware()) {
			Toast.makeText(this, "You have not Flash", Toast.LENGTH_LONG)
					.show();
			finish();
		}

		mCamera = getCameraInstance();
		checkFlash();
		light();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mCamera != null)
			mCamera.release();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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
		}

		return result;
	}

	/** A safe way to get an instance of the Camera object. */
	public Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			e.printStackTrace();
		}

		return c; // returns null if camera is unavailable
	}

	private void checkFlash() {
		if (mCamera != null) {
			mParams = mCamera.getParameters();
			mState = mParams.getFlashMode().equals(Parameters.FLASH_MODE_TORCH) ? true
					: false;
		}
	}

	private void light() {
		String mode = mState ? Parameters.FLASH_MODE_TORCH
				: Parameters.FLASH_MODE_OFF;

		mParams.setFlashMode(mode);
		mCamera.setParameters(mParams);
	}

}