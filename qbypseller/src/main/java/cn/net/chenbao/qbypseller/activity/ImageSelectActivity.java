package cn.net.chenbao.qbypseller.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yalantis.ucrop.UCrop;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.FileUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * 图片选择界面
 *
 * @author xl
 * @date:2016-8-2下午3:18:31
 * @description 单图选择(调用系统相机, 相册和裁剪)
 */
public class ImageSelectActivity extends Activity implements PermissionUtil.PermissionCallbacks {

    public static final String KEY_MODE = "mode";
    /**
     * 拍照
     */
    public static final int MODE_TAKE_PICTURE = 0;
    /**
     * 相册
     */
    public static final int MODE_PHOTO_ALBUM = 1;
    /**
     * 执行裁剪
     */
    public static final String KEY_DO_CROP = "do_crop";
    /**
     * 方形裁剪
     */
    public static final String KEY_IS_SQUARE = "is_square";
    private boolean isSquare;
    private boolean isCrop;
    private static final int REQUEST_CODE_CAMERA = 10;
    private static final int REQUEST_CODE_CROP_PICTURE = 11;
    private static final int REQUEST_CODE_PHOTO_ALBUM = 12;
    private int mMode;



    private File mTmpFile;
    /**
     * 裁剪图的输出
     */
    final public static int REQUEST_CODE_ASK_CAMERA = 123;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMode = getIntent().getIntExtra(KEY_MODE, MODE_TAKE_PICTURE);
        isCrop = getIntent().getBooleanExtra(KEY_DO_CROP, false);
        isSquare = getIntent().getBooleanExtra(KEY_IS_SQUARE, false);
        switch (mMode) {
            case MODE_TAKE_PICTURE:// 拍照

                onOpenCamera();

                break;
            case MODE_PHOTO_ALBUM:

                if (PermissionUtil.hasPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    selectPicFromLocal();
                } else {
                    PermissionUtil.requestPermissions(this, REQUEST_CODE_PHOTO_ALBUM, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
                }

                break;

            default:
                break;
        }
    }

    public void onOpenCamera() {
        if (PermissionUtil.hasPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})) {
            startActAndCrop();
        } else {
            PermissionUtil.requestPermissions(this, REQUEST_CODE_ASK_CAMERA, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private void startActAndCrop() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mTmpFile = FileUtils.createTmpFile(this);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mTmpFile.getAbsolutePath());
            //向上适配7.0
            uri = getContentResolver().insert(Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    uri);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        } else {
            WWToast.showShort(R.string.msg_no_camera);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ZLog.showPost(resultCode + "-----");
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    if (mTmpFile != null) {
                        // Intent.ACTION_MEDIA_MOUNTED:媒体库进行全扫描;
                        // Intent.ACTION_MEDIA_SCANNER_SCAN_FILE扫描某个文件
                        sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                uri));// 通知系统媒体库更新,不然下次进来没有最新的拍照图片
                        if (isCrop) {// 裁剪
                            if (uri != null) {
                                startCropActivity(uri);
                            } else {
                                WWToast.showShort(R.string.get_img_fail_try_again);
                            }
                        } else {// 直接返回
                            Intent intent = new Intent();
                            intent.putExtra(Consts.KEY_DATA,
                                    mTmpFile.getAbsolutePath());
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }
                } else {
                    try {
                        getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_display_name=?", new String[]{mTmpFile.getName()});
                    } catch (Exception e) {
                        ZLog.showPost("空文件未删除");
                    }
                    finish();
                }
                break;

            case UCrop.REQUEST_CROP:

                if (resultCode == RESULT_OK) {
                    final Uri resultUri = UCrop.getOutput(data);

                    Intent intent = new Intent();
                    intent.putExtra(Consts.KEY_DATA, uri2path(resultUri));
                    setResult(RESULT_OK, intent);
                } else {

                }
                finish();
                break;
            case REQUEST_CODE_PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    if (data != null) {

                        Uri selectImageUri = data.getData();
                        // 拿到相册图片后进入裁剪
                        if (selectImageUri != null) {
                            String BasePath = Environment.getExternalStorageDirectory().toString() + "/" + getPackageName() + "/";
                            String CropPicPath = BasePath + System.currentTimeMillis() + ".jpg";
                            // 将选取的图片uri转化为path
                            String selectImagePath = uri2path(selectImageUri);
                            // 复制一份到新的路径
                            copyFile(selectImagePath, CropPicPath);
                            // 将截取的图片路径转化为uri
                            Uri tempUri = Uri.fromFile(new File(CropPicPath));
                            // 带入截图
                            if (selectImageUri != null) {
                                if (!TextUtils.isEmpty(selectImagePath)) {
                                    if (isCrop) {
                                        if (tempUri != null) {
                                            startCropActivity(tempUri);
                                        } else {
                                            WWToast.showShort(R.string.get_img_fail_try_again);
                                        }
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra(Consts.KEY_DATA,
                                                selectImagePath);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }

                                }
                            } else {
                                WWToast.showShort(R.string.have_not_choice_file);
                            }
                        }
                    }
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 系统图库
     *
     * @author xl
     * @date:2016-8-2下午3:55:15
     * @description
     */
    public void selectPicFromLocal() {
        try{
            Intent intent;
            if (Build.VERSION.SDK_INT < 19) {
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

            } else {
                intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            }
            startActivityForResult(intent, REQUEST_CODE_PHOTO_ALBUM);
        }catch (Exception e){
            finish();
            WWToast.showShort(R.string.open_phone_gallery_fail);
        }

    }

    /**
     * 复制图片
     *
     * @param oldPath 旧的图片路径
     * @param newPath 新的图片路径
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                File file = new File(newPath);
                if (file.exists()) {
                    file.delete();
                } else {
                    new File(file.getParent()).mkdirs();
                    file.createNewFile();
                }
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                String[] selectionArgs = new String[]{split[1]};
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
        String[] projection = {column};
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
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = System.currentTimeMillis() + ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(ImageSelectActivity.this);
    }

    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.useSourceImageAspectRatio();
        if(isSquare){
            uCrop.withAspectRatio(1,1);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PHOTO_ALBUM) {
            selectPicFromLocal();
        } else {
            startActAndCrop();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        for (int i = 0; i < perms.size(); i++) {
            if (!PermissionUtil.hasPermissions(this, perms.get(i))) {
                WWToast.showShort(perms.get(i) + "被拒绝");
            }
        }
        finish();
    }
}
