package hdfg159.zyftp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hdfg159.zyftp.R;
import hdfg159.zyftp.adapter.FileAdapter;
import hdfg159.zyftp.ui.StatusBarCompat;
import hdfg159.zyftp.utils.ToastUtil;

/**
 * Created by ZZY2015 on 2016/2/18.
 */
public class FileDirectorySelected extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<String> items;
    private List<String> paths;
    private ListView listView;
    private String curPath = "/";
    private FileAdapter fileAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileselect);
        initview();

        getFileDir("/");
        if (fileAdapter == null) {
            fileAdapter = new FileAdapter(this, items, paths);
        }
        listView.setAdapter(fileAdapter);
    }

    public void initview() {
        toolbar = (Toolbar) findViewById(R.id.fileselecttoolbar);
        setSupportActionBar(toolbar);
        StatusBarCompat.compat(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("选择目录");
        toolbar.setSubtitle("/");

        listView = (ListView) findViewById(R.id.dirlist);
        listView.setOnItemClickListener(this);

        FloatingActionButton fba = (FloatingActionButton) findViewById(R.id.selectfab);
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
        if (items != null && paths != null) {
            items.clear();
            paths.clear();
        } else {
            items = new ArrayList<>();
            paths = new ArrayList<>();
        }
        toolbar.setSubtitle(filepath);
        File f = new File(filepath);
        File[] files = f.listFiles();
        files = sortFile(files);
        if (!filepath.equals("/")) {
//            items.add("返回根");
//            paths.add("/");
            items.add("返回");
            paths.add(f.getParent());
        }
        for (File file : files) {
            items.add(file.getName());
            paths.add(file.getPath());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = new File(paths.get(position));
        if (file.canRead()) {
            if (file.isDirectory()) {
                curPath = paths.get(position);
                getFileDir(paths.get(position));
                fileAdapter.notifyDataSetChanged();
                listView.setAdapter(fileAdapter);//进去文件夹时候要用，否则进去保留位置不变
            } else {
                ToastUtil.showToast(FileDirectorySelected.this, getString(R.string.dontopenfile));
            }
        } else {
            ToastUtil.showToast(this, "文件夹或文件不可读");
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
        List<File> lisle = Arrays.asList(files);
        Collections.sort(lisle, new NameUpFileComparator()); // 名称升序
        return lisle.toArray(new File[lisle.size()]);
    }

    private class NameUpFileComparator implements Comparator<File> {
        public int compare(File lhs, File rhs) {
            if (lhs.isDirectory() && !rhs.isDirectory()) {
                return -1;
            } else if (!lhs.isDirectory() && rhs.isDirectory()) {
                return 1;
            } else {
                return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }
}
