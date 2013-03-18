package wsu.cs558.roadmonitoring.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import wsu.cs558.roadmonitoring.bean.AccelData;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccelerometerViewActivity extends Activity implements SensorEventListener,OnClickListener{

	private SensorManager sensorManager;
	final String FILE_NAME = "SAMPLEFILE.txt";
	final String TEST_STRING = new String("Hello Android");
	private ArrayList<AccelData> sensorData;
	private Button btnStart, btnStop, btnUpload;
	private boolean started = false;
	private LinearLayout layout;
		
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			TextView tv = new TextView(this);
			sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			sensorData = new ArrayList<AccelData>();
			btnStart = (Button) findViewById(R.id.btnStart);
			btnStop = (Button) findViewById(R.id.btnStop);
			btnUpload = (Button) findViewById(R.id.btnUpload);
			btnStart.setOnClickListener(this);
			btnStop.setOnClickListener(this);
			btnUpload.setOnClickListener(this);
			btnStart.setEnabled(true);
			btnStop.setEnabled(false);
			if (sensorData == null || sensorData.size() == 0) {
				btnUpload.setEnabled(false);
			}
		}

		@Override
		protected void onResume() {
			super.onResume();

		}

		@Override
		protected void onPause() {
			super.onPause();
			if (started == true) {
				sensorManager.unregisterListener(this);
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		private void fileCreate() {
			  try {
			   OutputStream os = openFileOutput(FILE_NAME, MODE_WORLD_READABLE);
			   OutputStreamWriter osw = new OutputStreamWriter(os);
			   osw.write(TEST_STRING);
			   osw.close();
			  } catch (Exception e) {
			   Log.i("ReadNWrite, fileCreate()", "Exception e = " + e);
			  }
			 }

		private String readFile() {
			  try {
			   FileInputStream fin = openFileInput(FILE_NAME);
			   InputStreamReader isReader = new InputStreamReader(fin);
			   char[] buffer = new char[TEST_STRING.length()];
			   // Fill the buffer with data from file
			   isReader.read(buffer);
			   return new String(buffer);
			  } catch (Exception e) {
			   Log.i("ReadNWrite, readFile()", "Exception e = " + e);
			   return null;
			  }
			 }
				
		public void onSensorChanged(SensorEvent event){
			
			if (started) {
				double x = event.values[0];
				double y = event.values[1];
				double z = event.values[2];
				long timestamp = System.currentTimeMillis();
				AccelData data = new AccelData(timestamp, x, y, z);
				sensorData.add(data);
			}
			/*
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
	            return;
	        float mSensorX, mSensorY;
	        switch (mDisplay.getRotation()) {
	            case Surface.ROTATION_0:
	                mSensorX = event.values[0];
	                mSensorY = event.values[1];
	                break;
	            case Surface.ROTATION_90:
	                mSensorX = -event.values[1];
	                mSensorY = event.values[0];
	                break;
	            case Surface.ROTATION_180:
	                mSensorX = -event.values[0];
	                mSensorY = -event.values[1];
	                break;
	            case Surface.ROTATION_270:
	                mSensorX = event.values[1];
	                mSensorY = -event.values[0];
	         }
	       
			if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
				return;
			else {

				// assign directions
				float x = event.values[0];
				float y = event.values[1];
				float z = event.values[2];

				xCoor.setText("X: " + x);
				yCoor.setText("Y: " + y);
				zCoor.setText("Z: " + z);
			} */ 
	}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnStart:
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
				btnUpload.setEnabled(false);
				sensorData = new ArrayList<AccelData>();
				// save prev data if available
				started = true;
				try {
					File root = android.os.Environment.getExternalStorageDirectory();
					File dir = new File (root.getAbsolutePath() + "/download");
				    dir.mkdirs();
                    File sensorFile = new File(dir,"acc.txt");
                    
                   // sensorFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(sensorFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
               //     myOutWriter.append(sensorData);
                    myOutWriter.close();
                    fOut.close();
				}catch(Exception e){
					
				}
				Sensor accel = sensorManager
						.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				sensorManager.registerListener(this, accel,
						SensorManager.SENSOR_DELAY_FASTEST);
				break;
			case R.id.btnStop:
				btnStart.setEnabled(true);
				btnStop.setEnabled(false);
				btnUpload.setEnabled(true);
				started = false;
				sensorManager.unregisterListener(this);
				layout.removeAllViews();
				
				// don't need chart
			//	openChart();

				// show data in chart
				break;
			case R.id.btnUpload:

				break;
			default:
				break;
			}

		}

}
