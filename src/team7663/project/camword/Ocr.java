package team7663.project.camword;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.googlecode.tesseract.android.TessBaseAPI;

@SuppressWarnings("deprecation")
public class Ocr extends Activity {
	public static final String PACKAGE_NAME = "package team7663.project.camword";
	public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/CamWord/";
	public static final String lang = "eng";
	private static final String TAG = "Ocr.java";
	protected static final String PHOTO_TAKEN = "photo_taken";
	private String translatedText = "Output";
	private String definedText = "Output";
	protected Button button,copy,translate,define,gallery;
	protected ImageView image;
	protected EditText field;
	protected boolean _taken;
	protected SharedPreferences preferences;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.ocr);
		preferences = this.getSharedPreferences("team7663.project.camword",Context.MODE_PRIVATE);
        MainActivity.ocr.setBackgroundResource(R.drawable.ocr);
        preferences.edit().putBoolean("team7663.project.camword.OCR.mainoff",true).apply();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
		image = (ImageView) findViewById(R.id.image);
		field = (EditText) findViewById(R.id.field);
		button = (Button) findViewById(R.id.scan);
		translate = (Button) findViewById(R.id.translate);
		copy = (Button) findViewById(R.id.copy);
		define = (Button) findViewById(R.id.define);
		gallery = (Button) findViewById(R.id.load);
		gallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,53);
			}
		});
		//AIzaSyCr9C8xNq0uODvZMNH_0Jb7BZpBCZMagOo
		//AIzaSyA-AyEsG8goTqRv1g7S9ZQ0e5FCz6ShB4k
		field.setText(preferences.getString("team7663.project.camword.OCR.saveocr",""));
        copy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				clip.setText(field.getText());
				Toast.makeText(Ocr.this,"Text Copied", Toast.LENGTH_SHORT).show();
			}
		});
        define.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			dictionaryClass define = new dictionaryClass();
			if(field.getText().toString().length()>0)
			{
			Toast.makeText(Ocr.this,"Only first word will be defined...", Toast.LENGTH_SHORT).show(); 
			definedText = define.defineWord(define.firstWord(field.getText().toString()),Ocr.this);
			AlertDialog.Builder dlgAlert;
	        dlgAlert = new AlertDialog.Builder(Ocr.this);
	        dlgAlert.setTitle(R.string.app_name);
			dlgAlert.setCancelable(true);
			dlgAlert.setPositiveButton("Copy",new android.content.DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					clip.setText(definedText);
					Toast.makeText(Ocr.this,"Text Copied", Toast.LENGTH_SHORT).show();
				}
			});
			dlgAlert.setMessage(definedText);
			dlgAlert.create().show();
			}
			}
		});
		translate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String setlang = preferences.getString("team7663.project.camword.OCR.language","english");
				int langpos = preferences.getInt("team7663.project.camword.OCR.lanposition",0);
				translate translate = new translate();
				AlertDialog.Builder dlgAlert;
		        dlgAlert = new AlertDialog.Builder(Ocr.this);
		        dlgAlert.setTitle(R.string.app_name);
				dlgAlert.setCancelable(true);
				dlgAlert.setPositiveButton("Copy",new android.content.DialogInterface.OnClickListener()
				{
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						clip.setText(translatedText);
						Toast.makeText(Ocr.this,"Text Copied", Toast.LENGTH_SHORT).show();
					}
				});
				if(field.getText().toString().length()>0)
				{
			    try {
			    	if(setlang=="arabic" || langpos == 1 )
			    	{
			    	Toast.makeText(Ocr.this,String.format("Translating to ARABIC"),Toast.LENGTH_SHORT).show();
					translatedText = translate.trans(field.getText().toString(),"en","ar");
			    	}
			    	else if(setlang=="french" || langpos == 2)
			    	{
			        Toast.makeText(Ocr.this,String.format("Translating to FRENCH"), Toast.LENGTH_SHORT).show();
			        translatedText = translate.trans(field.getText().toString(),"en","fr");
			    	}
			    	else if(setlang=="german" || langpos == 3)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to GERMAN"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","de");
			    	}
			    	else if(setlang=="spanish" || langpos == 4)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to SPANISH"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","es");
			    	}
			    	else if(setlang=="hindi" || langpos == 5)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to HINDI"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","hi");
			    	}
			    	else if(setlang=="filipino" || langpos == 6)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to FILIPINO"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","tl");
			    	}
			    	else if(setlang=="chinese" || langpos == 7)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to CHINESE"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","zh-CN");
			    	}
			    	else if(setlang=="dutch" || langpos == 8)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to DUTCH"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","nl");
			    	}
			    	else if(setlang=="croatian" || langpos == 9)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to CROATIAN"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","hr");
			    	}
			    	else if(setlang=="italian" || langpos == 10)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to ITALIAN"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","it");
			    	}
			    	else if(setlang=="japanese" || langpos == 11)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to JAPANESE"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","ja");
			    	}
			    	else if(setlang=="latin" || langpos == 12)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to LATIN"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","la");
			    	}
			    	else if(setlang=="russain" || langpos == 13)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to RUSSIAN"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","ru");
			    	}
			    	else if(setlang=="georgian" || langpos == 14)
			    	{
			    		Toast.makeText(Ocr.this,String.format("Translating to GEORGIAN"), Toast.LENGTH_SHORT).show();
				        translatedText = translate.trans(field.getText().toString(),"en","ka");
			    	}
			    	else if(setlang=="english" || langpos == 0)
			    	{
			    		dlgAlert.setPositiveButton("Open Settings",new android.content.DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent newactivity = new Intent("team7663.project.camword.SETTINGS");
								startActivity(newactivity);
							}
						});
				        translatedText = "Change Language in Settings";
			    	}
			    	dlgAlert.setMessage(translatedText);
					dlgAlert.create().show();
			    
				}
			    catch (Exception e) 
				{
					Toast.makeText(Ocr.this,"Oops! Error in Translation", Toast.LENGTH_SHORT).show();
			    	//Toast.makeText(Ocr.this,e.toString(), Toast.LENGTH_SHORT).show();
				}
				}
			}
		});
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    Log.v(TAG, "Starting Camera app");
				startCameraActivity();
			}
		});
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };
		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/" + lang + ".traineddata");
				OutputStream out = new FileOutputStream(DATA_PATH + "tessdata/" + lang + ".traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
	}
	@Override
	protected void onDestroy()
	{
		preferences.edit().putString("team7663.project.camword.OCR.saveocr",field.getText().toString()).apply();
		super.onDestroy();
	}
	
	protected void startCameraActivity()
	{
		File Mkdir = new File(DATA_PATH + "/.OCR");
		Mkdir.mkdir();
		File file = new File(DATA_PATH + "/.OCR/ocr.jpg");
		Uri outputFileUri = Uri.fromFile(file);
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		try
		{
		startActivityForResult(intent,0);
		}
		catch(Exception ex)
		{
			//Do Nothing
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(TAG, "resultCode: " + resultCode);
		if (requestCode == 0 && resultCode == RESULT_OK)
		{
			String temp = DATA_PATH + "/.OCR/ocr.jpg";
			Toast.makeText(Ocr.this,"Processing Please Wait...", Toast.LENGTH_LONG).show();
			preferences.edit().putString("team7663.project.camword.OCR.path",temp).apply();			
			onPhotoTaken();
		}
		else if (requestCode == 53 && resultCode == RESULT_OK)
			{
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};
	            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	            cursor.moveToFirst();
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);
	            cursor.close();
	            preferences.edit().putString("team7663.project.camword.OCR.path",picturePath).apply();
	            onPhotoTaken();
	        }
		else
		{
			Log.v(TAG, "User cancelled");
		}
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean(Ocr.PHOTO_TAKEN, _taken);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.i(TAG, "onRestoreInstanceState()");
		if (savedInstanceState.getBoolean(Ocr.PHOTO_TAKEN)) {
			onPhotoTaken();
		}
	}

	protected void onPhotoTaken()
	{
		AlertDialog.Builder dlgAlert;
        dlgAlert = new AlertDialog.Builder(Ocr.this);
        dlgAlert.setTitle(R.string.app_name);
		dlgAlert.setCancelable(true);
		dlgAlert.setPositiveButton("Yes",new android.content.DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				_taken = true;
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				String _path = preferences.getString("team7663.project.camword.OCR.path","");
				Bitmap bitmap = BitmapFactory.decodeFile(_path, options);
				try {
					ExifInterface exif = new ExifInterface(_path);
					int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
					Log.v(TAG, "Orient: " + exifOrientation);
					int rotate = 0;
					switch (exifOrientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						rotate = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						rotate = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						rotate = 270;
						break;
					}
					Log.v(TAG, "Rotation: " + rotate);
					if (rotate != 0) {

						// Getting width & height of the given image.
						int w = bitmap.getWidth();
						int h = bitmap.getHeight();

						// Setting pre rotate
						Matrix mtx = new Matrix();
						mtx.preRotate(rotate);

						// Rotating Bitmap
						bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
					}
					// Convert to ARGB_8888, required by tess
					bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
				} catch (IOException e) {
					Log.e(TAG, "Couldn't correct orientation: " + e.toString());
				}

				image.setImageBitmap(bitmap);
				Log.v(TAG, "Before baseApi");
		        TessBaseAPI baseApi = new TessBaseAPI();
				baseApi.setDebug(true);
				baseApi.init(DATA_PATH, lang);
				baseApi.setImage(bitmap);
				String recognizedText = baseApi.getUTF8Text();
				baseApi.end();
				// You now have the text in recognizedText var, you can do anything with it.
				// We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
				// so that garbage doesn't make it to the display.

				Log.v(TAG, "OCRED TEXT: " + recognizedText);

				if ( lang.equalsIgnoreCase("eng") ) {
					recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
				}
				
				recognizedText = recognizedText.trim();

				if ( recognizedText.length() != 0 ) {
					field.setText(field.getText().toString().length() == 0 ? recognizedText : field.getText() + " " + recognizedText);
					field.setSelection(field.getText().toString().length());
				}
				// Cycle done.
			}
		});
		dlgAlert.setNegativeButton("No",new android.content.DialogInterface.OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				//Do Nothing
			}
		});
		dlgAlert.setMessage("Start Processing");
		dlgAlert.create().show();
	}
}
