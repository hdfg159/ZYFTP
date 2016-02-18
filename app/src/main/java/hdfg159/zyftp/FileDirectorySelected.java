package hdfg159.zyftp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import hdfg159.zyftp.utils.ToastUtil;

/**
 * Created by ZZY2015 on 2016/2/18.
 */
public class FileDirectorySelected extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView currentdir;
    private List<String> items = null;
    private List<String> paths = null;
    private ListView listView;
    private String curPath = "/";
    private FloatingActionButton fba;
    private Toolbar toolbar;
    private String rootpath = "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselect);
        initview();

        getFileDir("/");
    }

    public void initview() {
        toolbar = (Toolbar) findViewById(R.id.fileselecttoolbar);
        setSupportActionBar(toolbar);
        StatusBarCompat.compat(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("选择目录");

        currentdir = (TextView) findViewById(R.id.selecteddir);

        listView = (ListView) findViewById(R.id.dirlist);
        listView.setOnItemClickListener(this);

        fba = (FloatingActionButton) findViewById(R.id.selectfab);
        fba.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectfab:
                Intent data = new Intent(this, Settings.class);
                Bundle bundle = new Bundle();
                bundle.putString("file", curPath);
                data.putExtras(bundle);
                setResult(2, data);
                finish();
                break;
        }
    }

    public void getFileDir(String filepath) {
        currentdir.setText("当前目录:" + filepath);
        items = new ArrayList<String>();
        paths = new ArrayList<String>();
        File f = new File(filepath);
        File[] files = f.listFiles();
        files = sortFile(files);
        if (!filepath.equals(rootpath)) {
            items.add("返回根");
            paths.add(rootpath);
            items.add("返回");
            paths.add(f.getParent());
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            items.add(file.getName());
            paths.add(file.getPath());
        }
        listView.setAdapter(new FileAdapter(this, items, paths));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = new File(paths.get(position));
        if (file.canRead()) {
            if (file.isDirectory()) {
                curPath = paths.get(position);
                getFileDir(paths.get(position));
            } else {
                //可以打开文件
            }
        } else {
            ToastUtil.showToast(this, "无法访问");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public File[] sortFile(File[] files) {
        List<File> listfile = Arrays.asList(files);
        Collections.sort(listfile, new NameUpFileComparator()); // 名称升序
        File[] array = listfile.toArray(new File[listfile.size()]);
        return array;
    }

    private class NameUpFileComparator implements Comparator<File> {
        public int compare(File lhs, File rhs) {
            if (lhs.isDirectory() && rhs.isDirectory())
                return lhs.getName().toLowerCase(Locale.CHINA)
                        .compareTo(rhs.getName().toLowerCase(Locale.CHINA));
            else {
                if (lhs.isDirectory() && rhs.isFile()) {
                    return -1;
                } else if (lhs.isFile() && rhs.isDirectory()) {
                    return 1;
                } else {
                    return lhs.getName().toLowerCase(Locale.CHINA)
                            .compareTo(rhs.getName().toLowerCase(Locale.CHINA));
                }
            }
        }
    }
}
