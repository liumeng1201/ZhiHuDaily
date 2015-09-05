package com.android.liumeng.zhihudaily.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;

public class ImageUtils {
	/**
	 * Drawable转Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * 从资源中获取Bitmap
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public static Bitmap getBitmapFromResource(Context context, int id) {
		Resources res = context.getResources();
		return BitmapFactory.decodeResource(res, id);
	}

	/**
	 * Bitmap转byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}
	
	public static byte[] Image2Bytes(String imagepath) {
		byte[] data = null;
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(imagepath));
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int numBytesRead = 0;
			while ((numBytesRead = input.read(buf)) != -1) {
				output.write(buf, 0, numBytesRead);
			}
			data = output.toByteArray();
			output.close();
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * byte[]转Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 从assets目录下读取图片并转换为Bitmap
	 * 
	 * @param fileName
	 * @param am
	 * @return
	 */
	public static Bitmap getBitmapFromAssetsFile(String fileName, AssetManager am) {
		Bitmap image = null;
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	/**
	 * 从sd卡中获取图片
	 */
	public static Bitmap getBitmapFromSDCard(String path) {
		Bitmap image = null;
		File file = new File(path);
		if (file.exists()) {
			image = BitmapFactory.decodeFile(path);
		}
		return image;
	}
	
	/**
	 * 根据Uri获取图片真实路径
	 */
	@SuppressWarnings("deprecation")
	public static String getPathFromUri(Activity context, Uri uri) {
		String img_path = null;
		if (uri != null) {
			if (android.os.Build.VERSION.SDK_INT < 17) {
				// Android 4.2之前版本的处理
				String[] projection = { MediaStore.Images.Media.DATA };
				Cursor cursor = context.managedQuery(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				img_path = cursor.getString(column_index);
			} else {
				// Android 4.2及之后版本的处理
				String[] projection = { MediaStore.Images.Media.DATA };
				CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
				Cursor cursor = loader.loadInBackground();
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				img_path = cursor.getString(column_index);
			}
		}
		return img_path;
	}
	
	/**
	 * 获取图片并进行压缩
	 */
	public static Bitmap getBitmapAndCompress(String path) {
		Bitmap newBitmap = null;
		try {
			newBitmap = PicCompressUtil.getSmallBitmap(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			// 如果大于80kb则再次压缩
			while (baos.toByteArray().length / 1024 > 80 && options != 10) {
				// 清空baos
				baos.reset();
				options -= 10;
				// 这里压缩options%，把压缩后的数据存放到baos中
				newBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBitmap;
	}
	
	/**
	 * 获取图片并进行压缩
	 */
	public static Bitmap getAvatarBitmapAndCompress(String path) {
		Bitmap newBitmap = null;
		try {
			newBitmap = PicCompressUtil.getSmallBitmap(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
			int options = 90;
			// 如果大于80kb则再次压缩
			while (baos.toByteArray().length / 1024 > 80 && options != 10) {
				// 清空baos
				baos.reset();
				options -= 10;
				// 这里压缩options%，把压缩后的数据存放到baos中
				newBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBitmap;
	}
	
	/**
	 * 缩放图像到指定大小
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
		return scaleImage(org, (float) newWidth / org.getWidth(),
				(float) newHeight / org.getHeight());
	}

	public static Bitmap scaleImage(Bitmap org, float scaleWidth,
			float scaleHeight) {
		if (org == null) {
			return null;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(),
				matrix, true);
	}
	
	//生成圆角图片
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 14;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}
	
	/**
	 * 图片转base64
	 */
	public static String imgToBase64(Bitmap bitmap, String imgFormat) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			if (imgFormat.equalsIgnoreCase("png")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String imgToBase64(String imgPath, String imgFormat) {
		Bitmap bitmap = null;
		if (imgPath != null && imgPath.length() > 0) {
			bitmap = getBitmapFromSDCard(imgPath);
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			if (imgFormat.equalsIgnoreCase("png")) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			} else {
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}

			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Bitmap rotationBitmap(Bitmap bm, final int orientationDegree) {
		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

		try {
			Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
			return bm1;
		} catch (OutOfMemoryError ex) {
		}
		return null;
	}
}
