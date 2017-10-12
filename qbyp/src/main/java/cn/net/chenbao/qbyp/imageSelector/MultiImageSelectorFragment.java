package cn.net.chenbao.qbyp.imageSelector;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.FileUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.ZLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择Fragment
 */
public class MultiImageSelectorFragment extends Fragment implements PermissionUtil.PermissionCallbacks {

    private static final String TAG = "MultiImageSelector";

    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;

    // 结果数据
    private ArrayList<String> resultList = new ArrayList<String>();
    // 文件夹数据
    private final ArrayList<Folder> mResultFolder = new ArrayList<Folder>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 预览按钮
    private Button mPreviewBtn;
    // 底部View
    private View mPopupAnchorView;

    private int mDesireImageCount;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;

    private int mGridWidth, mGridHeight;

    private File mTmpFile;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);

        // 图片选择模式
        final int mode = getArguments().getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if (mode == MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(
                    EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }

        // 是否显示照相机
        mIsShowCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
        mImageAdapter = new ImageGridAdapter(getActivity(), mIsShowCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

        mPopupAnchorView = view.findViewById(R.id.footer);

        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        // 初始化，加载所有图片
        mCategoryText.setText(R.string.folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFolderPopupWindow == null) {
                    createPopupFolderList(mGridWidth, mGridHeight);
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mPreviewBtn = (Button) view.findViewById(R.id.preview);
        // 初始化，按钮状态初始化
        if (resultList == null || resultList.size() <= 0) {
            mPreviewBtn.setText(R.string.preview);
            mPreviewBtn.setEnabled(false);
        }
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 预览
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {

                final Picasso picasso = Picasso.with(getActivity());
                if (state == SCROLL_STATE_IDLE
                        || state == SCROLL_STATE_TOUCH_SCROLL) {
                    picasso.resumeTag(getActivity());
                } else {
                    picasso.pauseTag(getActivity());
                }

                if (state == SCROLL_STATE_IDLE) {
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                } else if (state == SCROLL_STATE_FLING) {
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter()
                            .getCount() ? view.getAdapter().getCount() - 1
                            : firstVisibleItem + 1;
                    Image image = (Image) view.getAdapter().getItem(index);
                    if (image != null) {
                        mTimeLineText.setText(TimeUtil
                                .formatPhotoDate(image.path));
                    }
                }
            }
        });
        mGridView.setAdapter(mImageAdapter);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onGlobalLayout() {

                        final int width = mGridView.getWidth();
                        final int height = mGridView.getHeight();

                        mGridWidth = width;
                        mGridHeight = height;

                        final int desireSize = getActivity().getResources()
                                .getDisplayMetrics().widthPixels / 3;
                        final int numCount = width / desireSize;
                        final int columnSpace = getResources()
                                .getDimensionPixelOffset(R.dimen.space_size);
                        int columnWidth = (width - columnSpace * (numCount - 1))
                                / numCount;
                        mImageAdapter.setItemSize(columnWidth);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mGridView.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            mGridView.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0) {
                        //TODO 照相机权限,写内存权限
                        if (PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                            showCameraAction();
                        } else {
                            PermissionUtil.requestPermissions(MultiImageSelectorFragment.this, REQUEST_CODE_CAMERA, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
                        }

                    } else {
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(
                                i);
                        selectImageFromGrid(image, mode);
                    }
                } else {
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });

        mFolderAdapter = new FolderAdapter(getActivity());
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {

        float PRO_HEIGHT = 0.8F;
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow
                .setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight((int) (height * PRO_HEIGHT));
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {

                        mFolderAdapter.setSelectIndex(i);

                        final int index = i;
                        final AdapterView v = adapterView;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mFolderPopupWindow.dismiss();

                                if (index == 0) {
                                    getActivity().getSupportLoaderManager()
                                            .restartLoader(LOADER_ALL, null,
                                                    mLoaderCallback);
                                    mCategoryText.setText(R.string.folder_all);
                                    if (mIsShowCamera) {
                                        mImageAdapter.setShowCamera(true);
                                    } else {
                                        mImageAdapter.setShowCamera(false);
                                    }
                                } else {
                                    Folder folder = (Folder) v.getAdapter()
                                            .getItem(index);
                                    if (null != folder) {
                                        mImageAdapter.setData(folder.images);
                                        mCategoryText.setText(folder.name);
                                        // 设定默认选择
                                        if (resultList != null
                                                && resultList.size() > 0) {
                                            mImageAdapter
                                                    .setDefaultSelected(resultList);
                                        }
                                    }
                                    mImageAdapter.setShowCamera(false);
                                }

                                // 滑动到最初始位置
                                mGridView.smoothScrollToPosition(0);
                            }
                        }, 100);

                    }
                });
    }

    final public static int REQUEST_CODE_READ_SD = 123;
    final public static int REQUEST_CODE_CAMERA = 234;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //判断是否有读内存权限  Manifest.permission.CAMERA,
        if (PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})) {
            loadPic();
        } else {
            PermissionUtil.requestPermissions(MultiImageSelectorFragment.this, REQUEST_CODE_READ_SD, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == REQUEST_CODE_READ_SD) {
            loadPic();
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            showCameraAction();
        }
    }

    public void loadPic() {
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null,
                mLoaderCallback);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!PermissionUtil.hasPermissions(getActivity(), list.get(i))) {
                if (requestCode == REQUEST_CODE_READ_SD) {
//                    getActivity().finish();
                }
                WWToast.showShort(list.get(i) + "被拒绝");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            } else {
                try {
                    getActivity().getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "_display_name=?", new String[]{mTmpFile.getName()});
                }catch (Exception e){
                    ZLog.showPost("空文件未删除");
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onGlobalLayout() {

                        final int height = mGridView.getHeight();

                        final int desireSize = getActivity().getResources()
                                .getDisplayMetrics().widthPixels / 3;

                        Log.d(TAG, "Desire Size = " + desireSize);
                        final int numCount = mGridView.getWidth() / desireSize;
                        Log.d(TAG, "Grid Size = " + mGridView.getWidth());
                        Log.d(TAG, "num count = " + numCount);
                        final int columnSpace = getResources()
                                .getDimensionPixelOffset(R.dimen.space_size);
                        int columnWidth = (mGridView.getWidth() - columnSpace
                                * (numCount - 1))
                                / numCount;
                        mImageAdapter.setItemSize(columnWidth);

                        if (mFolderPopupWindow != null) {
                            mFolderPopupWindow.setHeight(height * 8 / 10);
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mGridView.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        } else {
                            mGridView.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this);
                        }
                    }
                });

        super.onConfigurationChanged(newConfig);

    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = FileUtils.createTmpFile(getActivity());
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, mTmpFile.getAbsolutePath());
            Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);//向上适配7.0
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    uri);
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            WWToast.showShort(R.string.msg_no_camera);
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if (resultList.size() != 0) {
                        mPreviewBtn.setEnabled(true);
                        mPreviewBtn.setText(getResources().getString(
                                R.string.preview)
                                + "(" + resultList.size() + ")");
                    } else {
                        mPreviewBtn.setEnabled(false);
                        mPreviewBtn.setText(R.string.preview);
                    }
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        WWToast.showShort(R.string.msg_amount_limit);
                        return;
                    }

                    resultList.add(image.path);
                    mPreviewBtn.setEnabled(true);
                    mPreviewBtn.setText(getResources().getString(
                            R.string.preview)
                            + "(" + resultList.size() + ")");
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2]
                        + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION, IMAGE_PROJECTION[0] + " like '%"
                        + args.getString("path") + "%'", null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                List<Image> images = new ArrayList<Image>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data
                                .getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data
                                .getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data
                                .getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        images.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!mResultFolder.contains(folder)) {
                                List<Image> imageList = new ArrayList<Image>();
                                imageList.add(image);
                                folder.images = imageList;
                                mResultFolder.add(folder);
                            } else {
                                // 更新
                                Folder f = mResultFolder.get(mResultFolder
                                        .indexOf(folder));
                                f.images.add(image);
                            }
                        }

                    } while (data.moveToNext());

                    mImageAdapter.setData(images);

                    // 设定默认选择
                    if (resultList != null && resultList.size() > 0) {
                        mImageAdapter.setDefaultSelected(resultList);
                    }

                    mFolderAdapter.setData(mResultFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 回调接口
     */
    public interface Callback {
        void onSingleImageSelected(String path);

        void onImageSelected(String path);

        void onImageUnselected(String path);

        void onCameraShot(File imageFile);
    }
}
