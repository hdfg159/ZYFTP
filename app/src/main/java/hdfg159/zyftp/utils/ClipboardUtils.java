package hdfg159.zyftp.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by ZZY2015 on 2016/2/15.
 */
public class ClipboardUtils {
    public ClipboardUtils() {
    }

    public static void Copy(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(text);
        ToastUtil.showToast(context, "已复制到剪贴板");
    }

    public static String Paste(Context context, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        text = clipboardManager.getText().toString();
        return text;
    }
}
