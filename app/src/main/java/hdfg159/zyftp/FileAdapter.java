package hdfg159.zyftp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

/**
 * Created by ZZY2015 on 2016/2/18.
 */
public class FileAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Bitmap mFolder, mFile;
    private List<String> items;
    private List<String> paths;

    public FileAdapter(Context context, List<String> it, List<String> pa) {
        mInflater = LayoutInflater.from(context);
        items = it;
        paths = pa;
        mFolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.folder);
        mFile = BitmapFactory.decodeResource(context.getResources(), R.drawable.file);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.file_row, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f = new File(paths.get(position).toString());
        if (items.get(position).toString().equals("返回根")) {
            holder.text.setText("返回根目录...");
            holder.icon.setImageBitmap(mFolder);
        } else if (items.get(position).toString().equals("返回")) {
            holder.text.setText("返回上一层...");
            holder.icon.setImageBitmap(mFolder);
        } else {
            holder.text.setText(f.getName());
            if (f.isDirectory()) {
                holder.icon.setImageBitmap(mFolder);
            } else {
                holder.icon.setImageBitmap(mFile);
            }
        }
        return convertView;
    }

    private class ViewHolder {
        TextView text;
        ImageView icon;
    }
}
