/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.net.chenbao.qbypseller.R;

public class PullToRefreshScrollView extends PullToRefreshBase<ObservableScrollView> {

	public PullToRefreshScrollView(Context context) {
		super(context);
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullToRefreshScrollView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshScrollView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);

	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected ObservableScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ObservableScrollView scrollView = new ObservableScrollView(context, attrs);
		scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	public void firstReFreshing(boolean doScroll) {
		if (doScroll) {
			new AsyncTask<Integer, Integer, Integer>() {//该处是针对PullToRefreshScrollView控件的bug进行补充的
				@SuppressWarnings("ResourceType")
				@Override
				protected Integer doInBackground(Integer... params) {
					while (true) {
						if (mHeaderLayout.getHeight() > 0) {
							return null;
						}
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}


				@Override
				protected void onPostExecute(Integer result) {
					Log.e("shiyan", "zhixingle");
					setRefreshing(true);
				}
			}.execute();
		} else {
			setRefreshing(false);
		}
	}

	private PullScrollViewListener pullScrollViewListener = null;

	public interface PullScrollViewListener {
		void onScrollChanged(PullToRefreshScrollView pullToRefreshScrollView, int x, int y, int oldX, int oldY);
	}

	public void setPullScrollViewListener(PullScrollViewListener pullScrollViewListener) {
		this.pullScrollViewListener = pullScrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {
		super.onScrollChanged(x, y, oldX, oldY);
		if (pullScrollViewListener != null) {
			pullScrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
		}
	}
}
