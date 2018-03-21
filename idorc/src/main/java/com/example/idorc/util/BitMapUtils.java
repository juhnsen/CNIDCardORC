package com.example.idorc.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/10/19.
 */

public class BitMapUtils {


    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            return "";
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                return "";
            }
        }
        return result;
    }
	/**
	 * bitmap压缩
	 *
	 * @param
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 90;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset(); // 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
			Log.d("baos大小",baos.toByteArray().length/1024+"");
			Log.d("options大小",options+"");
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/**
	 * rawImage转换成bitmap后切割成合适大小
	 *
	 * @param
	 * @return
	 */
	public static Bitmap transform_Cut(byte[] rawImage){
		int width,height;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
		BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
		options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
		if (options.outWidth<1000){
			options.inSampleSize = 1;
		}else if (options.outWidth<2400) {
			options.inSampleSize = 2; // 设置为刚才计算的压缩比例
		}else{
			options.inSampleSize = 4;
		}
		options.inPreferredConfig = Bitmap.Config.RGB_565;//该模式是默认的,可不设
		options.inPurgeable = true;// 同时设置才会有效
		options.inInputShareable = true;//。当系统内存不够时候图片自动被回收
		Bitmap bm = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options); // 解码文件
		//Log.d("bm的width",bm.getWidth()+"");
		//Log.d("bm的height",bm.getHeight()+"");
		width=bm.getWidth();
		height=bm.getHeight();
		double x=(float)1/(float)4;
		double y=(float)2/(float)9;
		double _width=(float)1/(float)2;
		double _height=(float)4/(float)7;
		Bitmap bmp= Bitmap.createBitmap(bm,(int)(width*x),(int)(height*y),(int)(width*_width),(int)(height*_height));
		return  bmp;
	}
	/**
	 * bitmap保存到本地
	 *
	 * @param
	 * @return
	 */
	public static void saveBitmap(Bitmap bmp, String bitName) throws IOException {
		File dirFile = new File("./sdcard/DCIM/Camera/");
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File f = new File("./sdcard/DCIM/Camera/" + bitName + ".jpg");
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			bmp.compress(Bitmap.CompressFormat.JPEG, 50, fOut);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * 读取本地图片转为bitmap
	 *
	 * @param
	 * @return
	 */
	public static Bitmap decodeFile(String filePath) throws IOException{
		Bitmap b = null;
		int IMAGE_MAX_SIZE = 600;
		File f = new File(filePath);
		if (f == null){
			return null;
		}
		//Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;

		FileInputStream fis = new FileInputStream(f);
		BitmapFactory.decodeStream(fis, null, o);
		fis.close();

		int scale = 1;
		if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
			scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
		}

		//Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		fis = new FileInputStream(f);
		b = BitmapFactory.decodeStream(fis, null, o2);
		fis.close();
		return b;
	}

	/**
	 * byte数组转Bitmap
	 *
	 * @param
	 * @return
	 */
	public static Bitmap base64ToBitmap(String msg) {
		byte[] bytes = Base64.decode(msg, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

}



