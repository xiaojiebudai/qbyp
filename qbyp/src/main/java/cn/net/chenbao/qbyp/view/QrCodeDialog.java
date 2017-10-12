package cn.net.chenbao.qbyp.view;

import cn.net.chenbao.qbyp.utils.ImageUtils;

import cn.net.chenbao.qbyp.R;

import com.google.zxing.WriterException;
import com.karics.library.zxing.encode.CodeCreator;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * Description:我的我二维码弹窗 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-30
 */
public class QrCodeDialog extends Dialog {
    private TextView title;
    private ImageView iv_img;
    private Context context;

    public QrCodeDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        setContentView(R.layout.dialog_qrcode);
        LayoutParams attributes = getWindow().getAttributes();
//        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        getWindow().setAttributes(attributes);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        iv_img = (ImageView) findViewById(R.id.iv_img);
    }

    public void setImg(String url) {
        ImageUtils.setCommonImage(context, url, iv_img);
    }

    public void setQrImg(String info) {
        try {
            iv_img.setImageBitmap(CodeCreator.createQRCode(info));
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setTitle(String titleStr) {
        title.setText(titleStr);
    }

}
