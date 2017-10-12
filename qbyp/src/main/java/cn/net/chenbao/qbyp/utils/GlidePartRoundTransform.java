package cn.net.chenbao.qbyp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

public class GlidePartRoundTransform extends BitmapTransformation {

	private boolean top_left_radius = false;
	private boolean top_right_radius = false;
	private boolean bottom_left_radius = false;
	private boolean bottom_right_radius = false;

	private float radius = 0F;

	public GlidePartRoundTransform(Context context) {
		this(context, false, false, false, false, 0);
	}

	public GlidePartRoundTransform(Context context, boolean top_left,
			boolean top_right, boolean bottom_left, boolean bottom_right,
			int radius) {
		super(context);
		float density = Resources.getSystem().getDisplayMetrics().density;
		ZLog.showPost(density + "");
		this.top_left_radius = top_left;
		this.top_right_radius = top_right;
		this.bottom_left_radius = bottom_left;
		this.bottom_right_radius = bottom_right;
		this.radius = density * radius;
	}

	@Override
	protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
			int outWidth, int outHeight) {
		return roundCrop(pool, toTransform);
	}

	private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
		if (source == null)
			return null;

		Bitmap result = pool.get(source.getWidth(), source.getHeight(),
				Bitmap.Config.ARGB_4444);
		if (result == null) {
			result = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
					Bitmap.Config.ARGB_4444);
		}
		Canvas canvas = new Canvas(result);
		Paint paint = new Paint();
		paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP,
				BitmapShader.TileMode.CLAMP));
		paint.setAntiAlias(true);
		RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
		canvas.drawRoundRect(rectF, radius, radius, paint);
		// 不是圆角用矩形画出来
		if (!top_left_radius) {
			canvas.drawRect(0, 0, radius, radius, paint);
		}
		if (!top_right_radius) {
			canvas.drawRect(rectF.right - radius, 0, rectF.right, radius, paint);
		}
		if (!bottom_left_radius) {
			canvas.drawRect(0, rectF.bottom - radius, radius, rectF.bottom,
					paint);
		}
		if (!bottom_right_radius) {
			canvas.drawRect(rectF.right - radius, rectF.bottom - radius,
					rectF.right, rectF.bottom, paint);
		}
		return result;
	}

	@Override
	public String getId() {
		return getClass().getName() + Math.round(radius);
	}
}