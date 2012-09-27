package sample.application.fingerpaint;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileListAdapter extends ArrayAdapter<Object> {

	public static final int text1 = android.R.id.text1;
	public static final int text2 = android.R.id.text2;
	public static final int icon = android.R.id.icon;
	public File[] fc;
	public LayoutInflater mInflater;

	public FileListAdapter(Context context, Object[] objects){
		super(context, text1, objects);

		fc = (File[])objects;

		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_with_icon, null);
		}

		TextView fName = (TextView)convertView.findViewById(text1);
		TextView fTime = (TextView)convertView.findViewById(text2);

		ImageView fIcon = (ImageView)convertView.findViewById(icon);

		fName.setText(fc[position].getName());

		fTime.setText(DateFormat.getDateTimeInstance().format(new Date(fc[position].lastModified())));

		if(fc[position].isDirectory()){

			//フォルダアイコンを設定
			fIcon.setImageResource(R.drawable.folder);
		}else{

			Pattern p = Pattern.compile(
					"\\.png$|\\.jpg$|\\.gif$|\\.jpeg$|\\.bmp$",Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(fc[position].getName());

			if(m.find()){
				String path= fc[position].getPath();
				BitmapFactory.Options options = new BitmapFactory.Options();

				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, options);

				int scaleW = options.outWidth/64;
				int scaleH = options.outHeight/64;

				int scale = Math.max(scaleW, scaleH);
				options.inJustDecodeBounds = false;
				options.inSampleSize = scale;

				Bitmap bmp = BitmapFactory.decodeFile(fc[position].getPath(), options);
				fIcon.setImageBitmap(bmp);
			}
		}
		return convertView;
	}
}