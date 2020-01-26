/*
package com.vritti.petrosoft;

	import android.app.Activity;
	import android.graphics.Bitmap;
	import android.graphics.BitmapFactory;
	import android.graphics.Canvas;
	import android.graphics.Color;
	import android.graphics.Paint;
	import android.os.Bundle;*/
/*
	import android.support.design.widget.FloatingActionButton;
	import android.support.design.widget.Snackbar;
	import android.support.v7.app.AppCompatActivity;
	import android.support.v7.widget.Toolbar;*//*

	import android.util.Base64;
	import android.util.DisplayMetrics;
	import android.util.Log;
	import android.view.View;
	import android.view.Menu;
	import android.view.MenuItem;
	import android.widget.Toast;

	import com.hp.mss.hpprint.model.ImagePrintItem;
	import com.hp.mss.hpprint.model.PrintItem;
	import com.hp.mss.hpprint.model.PrintJobData;
	import com.hp.mss.hpprint.model.asset.ImageAsset;
	import com.hp.mss.hpprint.util.PrintUtil;

	import java.io.UnsupportedEncodingException;


public class Printtest extends Activity {
	float paperht, paperwt;


	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_printtest);

	        String msg = "Test test" +
					"\n  Deepashree" +
					"\n  Deepashree" +"\n  Deepashree" +
					"\n  Deepashree" +
					"\n  Deepashree" +"\n  Deepashree" +
					"\n  Deepashree" +
					"\n  Deepashree" +"\n  Deepashree" +
					"\n  Deepashree" +
					"\n  Deepashree" +"\n  Deepashree" +

					"\n Vritti ";

            */
/*String parts[] = msg.split("\n");
			for(int i=0; )*//*



			Bitmap myBitmapAgain = textAsBitmap(msg, 20, Color.BLACK);


	        //ImageAsset bitmapAsset = new ImageAsset(this, myBitmapAgain, ImageAsset.MeasurementUnits.INCHES,4,4);
			Toast.makeText(this,String.valueOf(paperwt),Toast.LENGTH_LONG).show();
			ImageAsset bitmapAsset = new ImageAsset(this, myBitmapAgain, ImageAsset.MeasurementUnits.INCHES ,paperwt,paperht);
			//ImageAsset bitmapAsset = new ImageAsset()
	        //ImageAsset bitmapAsset = new ImageAsset(this, R.drawable.ic_launcher, ImageAsset.MeasurementUnits.INCHES, 4, 6);
	        PrintItem printItemDefault = new ImagePrintItem(PrintItem.ScaleType.CENTER, bitmapAsset);

	        PrintJobData printJobData = new PrintJobData(Printtest.this, printItemDefault);
	        printJobData.setJobName("Example");
	        PrintUtil.setPrintJobData(printJobData);
	        Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
	        PrintUtil.print(Printtest.this);
	        Toast.makeText(this,"ok2",Toast.LENGTH_LONG).show();
			*/
/*}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}*//*

	    }

	public static Bitmap decodeBase64(String input)
	{
		byte[] decodedBytes = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}

		public Bitmap textAsBitmap(String text, float textSize, int textColor) {
			// adapted from http://stackoverflow.com/a/8799344/1476989
			String text2[] =text.split("\n");
			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			//paint.setColor(Color.TRANSPARENT);
			paint.setTextSize(textSize);
			paint.setColor(textColor);
			paint.setTextAlign(Paint.Align.LEFT);
			float baseline = -paint.ascent(); // ascent() is negative
			int width = (int) (paint.measureText(text) + 0.0f); // round
			int height = (int) (baseline + paint.descent() + 0.0f);
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			float mXDpi = metrics.xdpi;

			int trueWidth = width;
			if(width>height)height=width; else width=height;
			Bitmap image = Bitmap.createBitmap((int)(Math.round(mXDpi*4)), (text2.length*20), Bitmap.Config.ARGB_8888);
			//Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
			Toast.makeText(this,String.valueOf((Math.round(mXDpi*4))),Toast.LENGTH_LONG).show();
			//Toast.makeText(this,String.valueOf(width/2-trueWidth/2),Toast.LENGTH_LONG).show();

			paperht = ((text2.length*20)/mXDpi);
			paperwt= (float) 3.8;
			//paperht = ;
			Toast.makeText(this,String.valueOf((text2.length*20)/mXDpi),Toast.LENGTH_LONG).show();
			//Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

			Canvas canvas = new Canvas(image);
			//canvas.drawColor(Color.TRANSPARENT);
			//canvas.drawRGB(0,0,0);
			//
			canvas.drawColor(Color.WHITE);
			//canvas.drawColor(Color.BLUE);
			//canvas.drawText(text, width/2-trueWidth/2, baseline, paint);

			for (int i =0; i<text2.length; i++){
				canvas.drawText(text2[i], width/2-trueWidth/2, baseline+(i*20), paint);
			}

			//canvas.drawText(text,0,11, paint);
			//canvas.drawText(text, 1, 5, paint);
			canvas.drawBitmap(image, 0, 0, null);
			return image;
		}


	}



*/
