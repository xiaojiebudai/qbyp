package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.dialog.EditCategoryDialog;
import cn.net.chenbao.qbypseller.dialog.EditCategoryDialog.DialogHolder;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.net.chenbao.qbypseller.R;

/**
 * 添加类目的适配器
 * 
 * @author xl
 * @date 2016-8-2 上午12:30:26
 * @description 根据不同业务有不同操作
 */
public class CategoryUpdateAdapter extends FatherAdapter<Category> {

	public CategoryUpdateAdapter(Context ctx) {
		super(ctx);
	}

	/** 添加模式 */
	public static final int MODE_ADD = 0;
	/** 选择模式 */
	public static final int MODE_SELECT = 1;
	/** 模式 */
	private int mMode;

	public CategoryUpdateAdapter(Context ctx, int mode) {
		this(ctx);
		mMode = mode;
	}

	@Override
	public int getCount() {
		switch (mMode) {
		case MODE_ADD:
			return super.getCount();
		case MODE_SELECT:
			return super.getCount() + 1;

		default:
			return super.getCount();
		}

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	private static final int TYPE_EXIST = 0;
	private static final int TYPE_ADD = 1;

	@Override
	public int getItemViewType(int position) {
		if (mMode == MODE_SELECT) {// 选择模式有个新增条目
			if (position < (getCount() - 1)) {
				return TYPE_EXIST;
			}
			return TYPE_ADD;
		}
		return TYPE_EXIST;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			switch (getItemViewType(position)) {
			case TYPE_ADD:
				convertView = mInflater.inflate(
						R.layout.listview_category_save, parent, false);
				ViewHolder add = new ViewHolder();
				add.edt_category = (EditText) convertView
						.findViewById(R.id.edt_category);
				add.save = convertView.findViewById(R.id.tv_save);
				convertView.setTag(add);
				break;
			case TYPE_EXIST:
				ViewHolder exist = new ViewHolder();
				convertView = mInflater.inflate(
						R.layout.listview_category_delete, parent, false);
				exist.tv_category = (TextView) convertView
						.findViewById(R.id.tv_category);
				exist.delete = convertView.findViewById(R.id.tv_delete);
				exist.edit = convertView.findViewById(R.id.tv_edit);
				switch (mMode) {
				case MODE_ADD:
					break;
				case MODE_SELECT:
					exist.edit.setVisibility(View.GONE);
					exist.delete.setVisibility(View.GONE);
					break;

				default:
					break;
				}
				convertView.setTag(exist);
				break;
			default:
				break;
			}
		}
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		switch (getItemViewType(position)) {
		case TYPE_ADD:
			holder.save.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String s = holder.edt_category.getText().toString();
					if (TextUtils.isEmpty(s)) {
						return;
					} else {
						requestAdd(holder, s);
					}
				}

			});
			break;
		case TYPE_EXIST:
			final Category item = getItem(position);
			holder.tv_category.setText(mContext.getString(
					R.string.format_class_count, item.ClassName,
					item.GoodsCount+""));
			if (mMode == MODE_ADD) {
				holder.delete.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 原策略需要弹窗二次确认
						// if (item.GoodsCount > 0) {
						// showDeleteHintDialog(position);
						// } else {
						// requestDelete(position);
						//
						// }
						requestDelete(position);
					}

				});
				holder.edit.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						showDialog(position);
					}
				});
			}
			break;

		default:
			break;
		}
		return convertView;
	}

	private CommonDialog mCommonDialog;

	/**
	 * 删除提示弹窗
	 * 
	 * @param position
	 */
	private void showDeleteHintDialog(final int position) {
		if (mCommonDialog == null) {
			mCommonDialog = DialogUtils.getCommonDialogTwiceConfirm(
					(Activity) mContext, R.string.hint_delete_category, true);
		}
		mCommonDialog.getButtonRight().setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						requestDelete(position);
					}
				});
		mCommonDialog.show();
	}

	private EditCategoryDialog mDialog;

	private void showDialog(final int position) {
		if (mDialog == null) {
			mDialog = new EditCategoryDialog(mContext);
		}
		final DialogHolder holder = mDialog.getTag();
		if (position == -1) {// 新增
			((TextView) mDialog.findViewById(R.id.tv_title))
					.setText(R.string.add_category);
			holder.right.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

				}
			});
		} else {
			Category item = getItem(position);
			((TextView) mDialog.findViewById(R.id.tv_title))
					.setText(R.string.edit_category);
			holder.content.setText(item.ClassName);
			holder.right.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					requestEdit(position, holder.content.getText().toString());
				}
			});
		}
		mDialog.show();
	}

	/** 删除 */
	private void requestDelete(final int position) {
		Category item = getItem(position);
		JSONObject object = new JSONObject();
		object.put(Api.KEY_SESSIONID, WWApplication.getInstance()
				.getSessionId());
		object.put("classId", item.ClassId);
		RequestParams params = ParamsUtils.getPostJsonParams(object,
				ApiSeller.classDelete());
		x.http().post(params, new WWXCallBack("ClassDelete") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				ZLog.showPost("position" + position);
				removeItem(position);
			}

			@Override
			public void onAfterFinished() {
			}
		});
	}

	/** 编辑 */
	private void requestEdit(int position, final String s) {
		final Category item = getItem(position);
		JSONObject object = new JSONObject();
		Category category = new Category();
		category.ClassName = s;
		category.ClassId = item.ClassId;
		category.ParentId = item.ParentId;
		category.SellerId = item.SellerId;
		object.put("data", category);
		RequestParams params = ParamsUtils.getPostJsonParamsWithSession(object,
				ApiSeller.classUpdate());
		x.http().post(params, new WWXCallBack("ClassUpdate") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				item.ClassName = s;
				notifyDataSetChanged();
				mDialog.dismiss();
			}

			@Override
			public void onAfterFinished() {
			}
		});
	}

	/** 添加 */
	private void requestAdd(final ViewHolder holder, String s) {
		JSONObject object = new JSONObject();
		Category category = new Category();
		category.ClassName = s;
		object.put(Api.KEY_SESSIONID, WWApplication.getInstance()
				.getSessionId());
		object.put("data", category);
		RequestParams params = ParamsUtils.getPostJsonParams(object,
				ApiSeller.classUpdate());
		x.http().post(params, new WWXCallBack("ClassUpdate") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {

				Category new_c = JSON.parseObject(
						data.getJSONObject(Api.KEY_DATA).toJSONString(),
						Category.class);
				addItem(new_c);
				holder.edt_category.setText("");
			}

			@Override
			public void onAfterFinished() {

			}
		});
	}

	private static class ViewHolder {
		TextView tv_category;
		EditText edt_category;
		View delete;
		View save;
		View edit;
	}
}
