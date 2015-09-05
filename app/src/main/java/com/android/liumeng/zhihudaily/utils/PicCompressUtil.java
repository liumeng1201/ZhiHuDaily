package com.android.liumeng.zhihudaily.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PicCompressUtil {
	static String newPath = "";
	static Bitmap newBitmap = null;
	
	public static String save(String path) {
		try {
			File f = new File(path);
			Bitmap bm = getSmallBitmap(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			File file = new File(f.getParent(), f.getName().replace(".",
					"_crop."));
			newPath = file.getAbsolutePath();
			FileOutputStream fos = new FileOutputStream(file);
			int options = 100;
			// 如果大于80kb则再次压缩,最多压缩三次
			while (baos.toByteArray().length / 1024 > 80 && options != 10) {
				// 清空baos
				baos.reset();
				// 这里压缩options%，把压缩后的数据存放到baos中
				bm.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 30;
			}
			fos.write(baos.toByteArray());
			fos.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPath;
	}

	public static String save(Bitmap bitmap, String path) {
		try {
			newBitmap = bitmap;
			File f = new File(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			File file = new File(f.getParent(), f.getName().replace(".",
					"_crop."));
			newPath = file.getAbsolutePath();

			FileOutputStream fos = new FileOutputStream(file);
			int options = 100;
			// 如果大于80kb则再次压缩,最多压缩三次
			while (baos.toByteArray().length / 1024 > 80 && options != 10) {
				// 清空baos
				baos.reset();
				// 这里压缩options%，把压缩后的数据存放到baos中
				newBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 30;
			}
			fos.write(baos.toByteArray());
			fos.close();
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newPath;
	}

	public static Bitmap save(Bitmap bitmap) {
		try {
			newBitmap = bitmap;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			// 如果大于80kb则再次压缩,最多压缩三次
			while (baos.toByteArray().length / 1024 > 80 && options != 10) {
				// 清空baos
				baos.reset();
				// 这里压缩options%，把压缩后的数据存放到baos中
				newBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 30;
			}
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBitmap;
	}

	public static Bitmap compressByPath(String path) {
		try {
			newBitmap = getSmallBitmap(path);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 100;
			// 如果大于80kb则再次压缩,最多压缩三次
			while (baos.toByteArray().length / 1024 > 80 && options != 10) {
				// 清空baos
				baos.reset();
				// 这里压缩options%，把压缩后的数据存放到baos中
				newBitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
				options -= 30;
			}
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newBitmap;
	}

	/**
	 * 获取保存图片的目录
	 */
	public static File getAlbumDir(String path) {
		File dir = new File(path.replace(".", "_crop."));
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inSampleSize = calculateInSampleSize(options, 480, 800);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算图片的缩放值
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	
	public static String getPath(){
		return newPath;
	}
	
	public static Bitmap getBitmap(){
		return newBitmap;
	}
}