package cn.net.chenbao.qbypseller.bigimage;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.WWToast;

public class ImageDetailFragment extends Fragment {
	private String mImageUrl;
	private ImageView mImageView;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString("url")
				: null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.image);
		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		progressBar = (ProgressBar) v.findViewById(R.id.loading);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		progressBar.setVisibility(View.VISIBLE);
		Glide.with(this).load(mImageUrl)
				.listener(new RequestListener<String, GlideDrawable>() {

					@Override
					public boolean onException(Exception arg0, String arg1,
							Target<GlideDrawable> arg2, boolean arg3) {
                        WWToast.showShort(arg1);
						progressBar.setVisibility(View.GONE);
						return false;
					}

					@Override
					public boolean onResourceReady(GlideDrawable arg0,
							String arg1, Target<GlideDrawable> arg2,
							boolean arg3, boolean arg4) {
						progressBar.setVisibility(View.GONE);
						mAttacher.update();
						return false;
					}
				}).into(mImageView);


	}
}
