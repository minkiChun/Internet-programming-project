package info.androidhive.speechtotext;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

	private TextView txtSpeechInput;
	private ImageButton btnSpeak;
	private TextView JavacodeTextview;
	private final int REQ_CODE_SPEECH_INPUT = 100;
	String param1 = "input";
	String param2 = "HI Hello";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtSpeechInput = (TextView) findViewById(R.id.txtSpeechInput);
		btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

		// hide the action bar
		getActionBar().hide();

		btnSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				promptSpeechInput();
			}
		});

	}

	class InsertData extends AsyncTask<String, Void, String>{
		ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progressDialog = ProgressDialog.show(MainActivity.this,
					"Please Wait", null, true, true);
		}


		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			progressDialog.dismiss();

			//JavacodeTextview = (TextView)findViewById(R.id.xmlTextview);
			//mTextViewResult.setText(result);
			//JavacodeTextview.setText(result);
			if(result.charAt(0) == '1') {
				Intent intent1 = new Intent(MainActivity.this, ResActivity2.class);
				startActivity(intent1);
				result = null;
			}
			else if (result.charAt(0) == '2') {
				Intent intent2 = new Intent(MainActivity.this, ResActivity1.class);
				startActivity(intent2);
				result = null;
			}

			//결과 출력 화면에 //결과가 여기로 오는듯

			//Log.d(TAG, "POST response  - " + result);
		}


		@Override
		protected String doInBackground(String... params) {

			String param1 = (String)params[0];
			String param2 = (String)params[1];

			String serverURL = "http://10.0.1.111/cscp2.php";
			String postParameters = "param1=" + param1 + "&param2=" + param2;


			try {

				URL url = new URL(serverURL);
				HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


				//httpURLConnection.setReadTimeout(5000);
				//httpURLConnection.setConnectTimeout(5000);
				httpURLConnection.setRequestMethod("POST");
				//httpURLConnection.setRequestProperty("content-type", "application/json");
				httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				httpURLConnection.setDoInput(true);
				httpURLConnection.connect();


				OutputStream outputStream = httpURLConnection.getOutputStream();
				outputStream.write(postParameters.getBytes("UTF-8"));
				outputStream.flush();
				outputStream.close();


				int responseStatusCode = httpURLConnection.getResponseCode();
				//Log.d(TAG, "POST response code - " + responseStatusCode);

				InputStream inputStream;
				if(responseStatusCode == HttpURLConnection.HTTP_OK) {
					inputStream = httpURLConnection.getInputStream();
				}
				else{
					inputStream = httpURLConnection.getErrorStream();
				}


				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				StringBuilder sb = new StringBuilder();
				String line = null;

				while((line = bufferedReader.readLine()) != null){
					sb.append(line);
				}


				bufferedReader.close();


				return sb.toString();


			} catch (Exception e) {

				//Log.d(TAG, "InsertData: Error ", e);

				return new String("Error: " + e.getMessage());
			}

		}
	}
	/**
	 * Showing google speech input dialog
	 * */
	private void promptSpeechInput() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.speech_prompt));
		try {
			startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
		} catch (ActivityNotFoundException a) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.speech_not_supported),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQ_CODE_SPEECH_INPUT: {
				if (resultCode == RESULT_OK && null != data) {

					ArrayList<String> result = data
							.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
					txtSpeechInput.setText(result.get(0));

					param1 = result.get(0);
					InsertData task = new InsertData();
					task.execute(param1, param2);

				}
				break;
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}