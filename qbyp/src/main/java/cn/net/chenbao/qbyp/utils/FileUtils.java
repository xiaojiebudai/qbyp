package cn.net.chenbao.qbyp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;

import cn.net.chenbao.qbyp.R;

/**
 * 文件工具
 * 
 * @author xl
 * 
 */
public class FileUtils {
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/Photo_CYK/";

	/**
	 * 根文件url
	 * 
	 * @param name
	 */
	public static synchronized String getRootFileUrl(String name) {
		File file = new File(Environment.getExternalStorageDirectory() + "/CYK/");
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		return new File(Environment.getExternalStorageDirectory() + "/CYK/",
				name).getPath();

	}

	/**
	 * APP包文件url
	 * 
	 * @param path
	 * @param name
	 */
	public static String getpackageFileUrl(Context context, String path,
			String name) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File file = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/CYK/");
			if (!file.exists() && !file.isDirectory()) {
				file.mkdir();
			}
			return new File(Environment.getExternalStorageDirectory() + "/CYK/"
					+ path, name).getPath();
		} else {
			WWToast.showShort(R.string.no_find_sdcard);
			return "SD Card Error!";
		}
	}

	/**
	 * APP包缓存dir
	 * 
	 * @param path
	 */
	public static String getpackageCacheDir(Context context, String path) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File externalCacheDir = context.getExternalCacheDir();
			if (externalCacheDir != null) {
				cachePath = externalCacheDir.getPath();
			}
		}
		if (cachePath == null) {
			File cacheDir = context.getCacheDir();
			if (cacheDir != null && cacheDir.exists()) {
				cachePath = cacheDir.getPath();
			}
		}
		return cachePath + File.separator + path;
	}

	/**
	 * 获取缓存目录大小
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static String getTotalCacheSize(Context context) {
		long cacheSize = 0;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				cacheSize = getFolderSize(context.getCacheDir());
				cacheSize += getFolderSize(context.getExternalCacheDir());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return getFormatSize(cacheSize);
	}

	/**
	 * 清空缓存
	 * 
	 * @param context
	 */
	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteDir(context.getExternalCacheDir());
		}
	}

	/**
	 * 删除文件/或者文件夹
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		if (dir != null) {
			return dir.delete();
		}
		return false;
	}

	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 * 
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			return "0K";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "KB";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "MB";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "GB";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "TB";
	}

	/**
	 * 获取版本号信息
	 * 
	 * @return 当前应用的版本号
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "无法获取版本";
		}
	}

	/**
	 * 获取版本号
	 * 
	 * @return 当前应用的版本code
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			int versionCode = info.versionCode;
			return versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得本地的语言环境
	 * 
	 * @author xl
	 * @version 创建时间：2015-10-28
	 */
	public static String getLocalLauguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 本地语言是不是为"en"
	 * 
	 * @author xl
	 * @version 创建时间：2015-10-28
	 */
	public static boolean isLocalLauguageEn() {
		return "en".equals(getLocalLauguage());
	}

	/**
	 * 获得缩放后的Bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getCompressedBitmap(String filePath, int height,
			int width) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, height, width);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static final int MESSAGE_SUCCESS = 1;
	public static final int MESSAGE_FAIL = -1;

	/**
	 * 
	 * 获得压缩后的图片的保存路径
	 * 
	 * @param filePath
	 *            路径
	 *            标记
	 * @param handler
	 * 
	 * @return
	 */
	public static void getCompressedBitmapFileUrl(final String filePath,
			final int tag, final Handler handler) {

		final Bitmap compressedBitmap = getCompressedBitmap(filePath, 0, 0);
		new Thread(new Runnable() {

			@Override
			public void run() {
				String newUrl = FileUtils.getRootFileUrl("IMG_"
						+ System.currentTimeMillis() + ".jpg");
				// TODO Auto-generated method stub
				try {
					FileOutputStream fos = new FileOutputStream(
							new File(newUrl));
					ZLog.showPost(fos + "");
					ZLog.showPost(compressedBitmap + "");
					compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 90,
							fos);
					compressedBitmap.recycle();
					fos.flush();
					fos.close();
					Message message = new Message();
					message.what = MESSAGE_SUCCESS;
					message.obj = newUrl;
					message.arg1 = tag;
					handler.sendMessage(message);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 计算压缩比例
	 * 
	 * @param options
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int h, int w) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		ZLog.showPost(height + "..." + width);
		int inSampleSize = 2;
		if (h == 0 || w == 0) {
			h = DEFAULT_HEIGHT;
			w = DEFAULT_WIDTH;
		}
		if (height > h || width > w) {
			final int heightRatio = (Math.round((float) height / (float) h)) + 1;
			final int widthRatio = (Math.round((float) width / (float) w)) + 1;
			inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
			ZLog.showPost(heightRatio + "..." + widthRatio + "..."
					+ inSampleSize);
			if (inSampleSize > 8) {
				inSampleSize += 4;
			}
			if (inSampleSize > 6) {
				inSampleSize += 3;
			}
			if (inSampleSize > 2) {
				inSampleSize += 2;
			}

		}
		return inSampleSize;

	}

	/**
	 * 创建Tmp文件
	 * 
	 * @author xl
	 * @version 创建时间：2015-11-11
	 */
	public static File createTmpFile(Context context) {

		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// 已挂载
			File pic = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.CHINA).format(new Date());
			String fileName = "Img_" + timeStamp + "";
			File tmpFile = new File(pic, fileName + ".jpg");
			return tmpFile;
		} else {
			File cacheDir = context.getCacheDir();
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.CHINA).format(new Date());
			String fileName = "Img_" + timeStamp + "";
			File tmpFile = new File(cacheDir, fileName + ".jpg");
			return tmpFile;
		}

	}

	// --------------------------------重新设计压缩上传----------------------------------//
	/**
	 * 默认压缩高
	 */
	private final static int DEFAULT_HEIGHT = 800;
	/**
	 * 默认压缩宽
	 */
	private final static int DEFAULT_WIDTH = 480;

	/**
	 * 修改的压缩图片
	 * 
	 * @author xl
	 * @date:2016-2-29下午5:46:53
	 * @description 摒弃之前的图片选择设计
	 * @param filePath
	 * @return
	 */
	public static String getCompressedImageFileUrl(String filePath) {

		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			int available = fis.available();
			fis.close();
			if (available < 300 * 1024) {
				return filePath;
			}
		} catch (IOException e1) {
		}

		final Bitmap compressedBitmap = getCompressedImageBitmap(filePath, 0, 0);

		String newUrl = FileUtils.getRootFileUrl("IMG_"
				+ System.currentTimeMillis() + ".jpg");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(newUrl));
			compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
			compressedBitmap.recycle();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUrl;
	}

	/**
	 * 修改的压缩图片
	 *
	 * @author xl
	 * @date:2016-2-29下午5:46:53
	 * @description 摒弃之前的图片选择设计
	 * @param filePath
	 * @return
	 */
	public static String getCompressedImageFileUrl1(String filePath,int maxInSampleSize) {

		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			int available = fis.available();
			fis.close();
			if (available < 300 * 1024) {
				return filePath;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
		}

		final Bitmap compressedBitmap = getCompressedImageBitmap1(filePath, 0, 0,maxInSampleSize);

		String newUrl = FileUtils.getRootFileUrl("IMG_"
				+ System.currentTimeMillis() + ".jpg");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(newUrl));
			compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
			compressedBitmap.recycle();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUrl;
	}
	/**
	 * 获得缩放后的Bitmap
	 *
	 * @param filePath
	 * @return
	 */
	private static Bitmap getCompressedImageBitmap1(String filePath, int height,
													int width,int maxInSampleSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateImageInSampleSize(options, height,
				width);
		options.inSampleSize=options.inSampleSize>maxInSampleSize?maxInSampleSize:options.inSampleSize;
		ZLog.showPost("inSampleSize------"+options.inSampleSize);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
	 /***
	 * Description:
	 * @author ZXJ
	 * @date 2016-9-6
	 * @param bitmap
	 * @return
	 */
	public static String getCompressedImageFileFromBitmap(Bitmap bitmap) {
		final Bitmap compressedBitmap =bitmap;
		String newUrl = FileUtils.getRootFileUrl("IMG_"
				+ System.currentTimeMillis() + ".jpg");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(newUrl));
			compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fos);
			compressedBitmap.recycle();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newUrl;
	}
	
	
	
	/**
	 * 获得缩放后的Bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getCompressedImageBitmap(String filePath, int height,
			int width) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateImageInSampleSize(options, height,
				width);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算压缩比例
	 * 
	 * @param options
	 * @return
	 */
	private static int calculateImageInSampleSize(
			BitmapFactory.Options options, int h, int w) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (h == 0 || w == 0) {
			h = DEFAULT_HEIGHT;
			w = DEFAULT_WIDTH;
		}
		if (height > h || width > w) {
			final int heightRatio = Math.round((float) height / (float) h);
			final int widthRatio = Math.round((float) width / (float) w);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;// 取大值
			return inSampleSize;
		}
		ZLog.showPost(inSampleSize + "....size");
		return inSampleSize;
	}

	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
