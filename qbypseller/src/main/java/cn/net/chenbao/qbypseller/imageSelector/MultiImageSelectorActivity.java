package cn.net.chenbao.qbypseller.imageSelector;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.yalantis.ucrop.UCrop;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.activity.FatherActivity;
import cn.net.chenbao.qbypseller.utils.WWToast;

import java.io.File;
import java.util.ArrayList;

/**
 * 多图选择
 */
public class MultiImageSelectorActivity extends FatherActivity implements
		MultiImageSelectorFragment.Callback {

	/** 最大图片选择次数，int类型，默认9 */
	public static final String EXTRA_SELECT_COUNT = "max_select_count";
	/** 图片选择模式，默认多选 */
	public static final String EXTRA_SELECT_MODE = "select_count_mode";
	/** 是否显示相机，默认显示 */
	public static final String EXTRA_SHOW_CAMERA = "show_camera";
	/** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合 */
	public static final String EXTRA_RESULT = "select_result";
	/** 默认选择集 */
	public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";
	/** 执行裁剪 */
	public static final String EXTRA_DO_CROP = "do_crop";

	/** 单选 */
	public static final int MODE_SINGLE = 0;
	/** 多选 */
	public static final int MODE_MULTI = 1;
	private static final int REQUEST_CODE_CROP_PICTURE = 10;

	private ArrayList<String> resultList = new ArrayList<String>();
	private Button mSubmitButton;
	private int mDefaultCount;

	/**
	 * 请求的模式
	 */
	private int mMode;

	/** 进行裁剪 */
	private boolean doCrop = false;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_default;
	}

	@Override
	protected void initValues() {

	}

	@Override
	protected void initView() {
		Intent intent = getIntent();
		mDefaultCount = intent.getIntExtra(EXTRA_SELECT_COUNT, 9);
		int mode = intent.getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
		mMode = mode;
		boolean isShow = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, true);
		if (mode == MODE_MULTI && intent.hasExtra(EXTRA_DEFAULT_SELECTED_LIST)) {
			resultList = intent
					.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
		}

		doCrop = intent.getBooleanExtra(EXTRA_DO_CROP, false);
		Bundle bundle = new Bundle();
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_COUNT,
				mDefaultCount);
		bundle.putInt(MultiImageSelectorFragment.EXTRA_SELECT_MODE, mode);
		bundle.putBoolean(MultiImageSelectorFragment.EXTRA_SHOW_CAMERA, isShow);
		bundle.putStringArrayList(
				MultiImageSelectorFragment.EXTRA_DEFAULT_SELECTED_LIST,
				resultList);

		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.image_grid,
						Fragment.instantiate(this,
								MultiImageSelectorFragment.class.getName(),
								bundle)).commit();

		// 返回按钮
		findViewById(R.id.btn_back).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						setResult(RESULT_CANCELED);
						finish();
					}
				});

		// 完成按钮
		mSubmitButton = (Button) findViewById(R.id.commit);
		if (resultList == null || resultList.size() <= 0) {
			mSubmitButton.setText(R.string.finish);
			mSubmitButton.setEnabled(false);
		} else {
			mSubmitButton.setText(getString(R.string.finish) + "("
					+ resultList.size() + "/" + mDefaultCount + ")");
			mSubmitButton.setEnabled(true);
		}
		mSubmitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (resultList != null && resultList.size() > 0) {
					// 返回已选择的图片数据
					Intent data = new Intent();
					data.putStringArrayListExtra(EXTRA_RESULT, resultList);
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});
	}

	@Override
	protected void doOperate() {

	}

	/** 裁剪图的输出 */
	private String mCropPath;

	/**
	 * 调起裁剪
	 * 
	 * @author xl
	 * @date:2016-7-13下午3:44:21
	 * @description
	 * @param uri
	 * @param outputX
	 * @param outputY

	 */
	private void cropImageUri(Uri uri, int outputX, int outputY) {

		if (uri != null) {
			startCropActivity(uri,outputX,outputY);
		} else {
			WWToast.showShort(R.string.get_img_fail_try_again);
		}
//		mCropPath = FileUtils.getRootFileUrl("IMG_"
//				+ System.currentTimeMillis() + ".jpg");
//		Intent intent = new Intent("com.android.camera.action.CROP");
//		intent.setDataAndType(uri, "image/*");// 可以选择图片类型，如果是*表明所有类型的图片
//		intent.putExtra("crop", "true");
//		// 宽高比
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		// 裁剪宽高
//		intent.putExtra("outputX", outputX);
//		intent.putExtra("outputY", outputY);
//		intent.putExtra("scale", true);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT,
//				Uri.parse("file://" + mCropPath));// 裁剪图片路径
//		// 是否将数据保留在Bitmap中返回
//		intent.putExtra("return-data", false);
//		// 设置输出的格式
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		intent.putExtra("noFaceDetection", true); // no face detection
//		startActivityForResult(intent, requestCode);
	}
	private void startCropActivity(@NonNull Uri uri, int outputX, int outputY) {
		String destinationFileName = System.currentTimeMillis() + ".jpg";

		UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

		uCrop = basisConfig(uCrop,outputX,outputY);
		uCrop = advancedConfig(uCrop);

		uCrop.start(MultiImageSelectorActivity.this);
	}
	/**
	 * In most cases you need only to set crop aspect ration and max size for resulting image.
	 *
	 * @param uCrop - ucrop builder instance
	 * @return - ucrop builder instance
	 */
	private UCrop basisConfig(@NonNull UCrop uCrop, int outputX, int outputY) {
		uCrop = uCrop.useSourceImageAspectRatio();
//		uCrop = uCrop.withMaxResultSize(outputX, outputY);
		return uCrop;
	}

	/**
	 * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
	 *
	 * @param uCrop - ucrop builder instance
	 * @return - ucrop builder instance
	 */
	private UCrop advancedConfig(@NonNull UCrop uCrop) {
		UCrop.Options options = new UCrop.Options();
		options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
		options.setHideBottomControls(false);
		options.setFreeStyleCropEnabled(false);
		options.setStatusBarColor(getResources().getColor(R.color.yellow_ww));
		options.setToolbarColor(getResources().getColor(R.color.yellow_ww));
		options.setRootViewBackgroundColor(getResources().getColor(R.color.gray_bg));
		options.setActiveWidgetColor(getResources().getColor(R.color.yellow_ww));
		return uCrop.withOptions(options);
	}
	private String uri2path(Uri uri) {
		String path = "";
		path = getImageAbsolutePath(this, uri);
		return path;
	}

	/**
	 * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
	 *
	 * @param context
	 * @param imageUri
	 * @author yaoxing
	 * @date 2014-10-12
	 */
	@TargetApi(19)
	public static String getImageAbsolutePath(Activity context, Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
									   String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	@Override
	public void onSingleImageSelected(String path) {

		if (doCrop) {
			Uri imageUri = Uri.parse("file://" + path);
			cropImageUri(imageUri, 400, 400);
		} else {
			Intent data = new Intent();
			resultList.add(path);
			data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			setResult(RESULT_OK, data);
			finish();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case UCrop.REQUEST_CROP:
					final Uri resultUri = UCrop.getOutput(data);

					Intent intent = new Intent();
					resultList.add(uri2path(resultUri));
					intent.putStringArrayListExtra(EXTRA_RESULT, resultList);
					setResult(RESULT_OK, intent);
					finish();
					break;
			default:
				break;
			}
		}
	}

	@Override
	public void onImageSelected(String path) {
		if (!resultList.contains(path)) {
			resultList.add(path);
		}
		// 有图片之后，改变按钮状态
		if (resultList.size() > 0) {
			mSubmitButton.setText(getString(R.string.finish) + "("
					+ resultList.size() + "/" + mDefaultCount + ")");
			if (!mSubmitButton.isEnabled()) {
				mSubmitButton.setEnabled(true);
			}
		}
	}

	@Override
	public void onImageUnselected(String path) {
		if (resultList.contains(path)) {
			resultList.remove(path);
			mSubmitButton.setText(getString(R.string.finish) + "("
					+ resultList.size() + "/" + mDefaultCount + ")");
		} else {
			mSubmitButton.setText(getString(R.string.finish) + "("
					+ resultList.size() + "/" + mDefaultCount + ")");
		}
		// 当为选择图片时候的状态
		if (resultList.size() == 0) {
			mSubmitButton.setText(R.string.finish);
			mSubmitButton.setEnabled(false);
		}
	}

	@Override
	public void onCameraShot(File imageFile) {
		if (imageFile != null) {
			Intent data = new Intent();
			if (mMode == MODE_SINGLE) {
				// TODO:改为list返回
				// data.putExtra(EXTRA_RESULT, imageFile.getAbsolutePath());
				if (doCrop) {
					Uri imageUri = Uri.parse("file://" + imageFile.getAbsolutePath());
					cropImageUri(imageUri, 400, 400);
					return;
				}
				resultList.add(imageFile.getAbsolutePath());
				data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			} else {

				resultList.add(imageFile.getAbsolutePath());
				data.putStringArrayListExtra(EXTRA_RESULT, resultList);
			}
			// Intent.ACTION_MEDIA_MOUNTED:媒体库进行全扫描；Intent.ACTION_MEDIA_SCANNER_SCAN_FILE扫描某个文件
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					Uri.parse("file://" + resultList.get(0))));// 通知系统媒体库更新,不然下次进来没有最新的拍照图片
			setResult(RESULT_OK, data);
			finish();
		}
	}
}
