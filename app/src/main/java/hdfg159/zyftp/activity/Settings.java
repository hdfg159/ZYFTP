package hdfg159.zyftp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import hdfg159.zyftp.R;
import hdfg159.zyftp.ui.StatusBarCompat;
import hdfg159.zyftp.utils.DialogUtils;
import hdfg159.zyftp.utils.SharedPreferencesUtils;


/**
 * Created by ZZY2015 on 2016/1/29.
 */
public class Settings extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private TextInputLayout textInputLayoutusername;
    private TextInputLayout textInputLayoutpasswords;
    private TextInputLayout textInputLayoutport;
    private TextInputLayout textInputLayoutdir;
    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private Button reset, readconfig, selectdir;
    private FloatingActionButton floatingActionButton;
    private CheckBox nmvisit;
    private SwipeRefreshLayout clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.settingcoordl);

        toolbar = (Toolbar) findViewById(R.id.settingtoolbar);
        setSupportActionBar(toolbar);
        StatusBarCompat.compat(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("设置");

        initEditText();

        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(this);
        readconfig = (Button) findViewById(R.id.readconfig);
        readconfig.setOnClickListener(this);
        selectdir = (Button) findViewById(R.id.selectdir);
        selectdir.setOnClickListener(this);

        nmvisit = (CheckBox) findViewById(R.id.nmvisit);
        nmvisit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textInputLayoutpasswords.getEditText().setEnabled(false);
                    textInputLayoutusername.getEditText().setEnabled(false);
                    textInputLayoutpasswords.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "password", "admin"));
                    textInputLayoutusername.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "username", "admin"));
                } else {
                    textInputLayoutpasswords.getEditText().setEnabled(true);
                    textInputLayoutusername.getEditText().setEnabled(true);
                    textInputLayoutpasswords.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "password", "admin"));
                    textInputLayoutusername.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "username", "admin"));
                }
            }
        });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        clean = (SwipeRefreshLayout) findViewById(R.id.refreshclean);
        clean.setOnRefreshListener(this);
    }

    public void initEditText() {
        textInputLayoutusername = (TextInputLayout) findViewById(R.id.tiusername);
        textInputLayoutpasswords = (TextInputLayout) findViewById(R.id.tiuserpasswords);
        textInputLayoutport = (TextInputLayout) findViewById(R.id.tiuserport);
        textInputLayoutdir = (TextInputLayout) findViewById(R.id.tidir);
        textInputLayoutpasswords.setHint("密码 (1-16位字母或数字)");
        textInputLayoutusername.setHint("用户名 (1-16位字母或数字)");
        textInputLayoutport.setHint("端口号 (4位数字)");
        textInputLayoutdir.setHint("输入路径 例如:/mnt/sdcard/");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                DialogUtils.showAlertTwo(this, getString(R.string.tips), "确认重置?将会丢失之前的设置", "确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (SharedPreferencesUtils.clear(Settings.this)) {
                            nmvisit.setChecked(false);
                            Snackbar.make(coordinatorLayout, "重置成功", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                }, "取消", null);
                break;
            case R.id.fab:
                if ((textInputLayoutdir.getEditText().getText().length() != 0) && (textInputLayoutpasswords.getEditText().getText().length() != 0) && (textInputLayoutusername.getEditText().getText().length() != 0) && (textInputLayoutport.getEditText().getText().length() == 4)) {
                    FbaWriteSharedPreferences();
                } else {
                    Snackbar.make(coordinatorLayout, "输入内容不合法！", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.readconfig:
                ReadSharedPreferences();
                break;
            case R.id.selectdir:
                Intent intent = new Intent(Settings.this, FileDirectorySelected.class);
                startActivityForResult(intent, 1);
                break;
        }
    }

    public void FbaWriteSharedPreferences() {
        SharedPreferencesUtils.putString(this, "username", textInputLayoutusername.getEditText().getText().toString());
        SharedPreferencesUtils.putString(this, "password", textInputLayoutpasswords.getEditText().getText().toString());
        SharedPreferencesUtils.putString(this, "portNum", textInputLayoutport.getEditText().getText().toString());
        SharedPreferencesUtils.putString(this, "chrootDir", textInputLayoutdir.getEditText().getText().toString());
        SharedPreferencesUtils.putBoolean(this, "allow_anonymous", nmvisit.isChecked());
        Snackbar.make(coordinatorLayout, "保存成功", Snackbar.LENGTH_LONG)
                .setAction("关闭页面", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();
    }

    public void ReadSharedPreferences() {
        textInputLayoutpasswords.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "password", "admin"));
        textInputLayoutusername.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "username", "admin"));
        textInputLayoutport.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "portNum", "2121"));
        textInputLayoutdir.getEditText().setText(SharedPreferencesUtils.getString(Settings.this, "chrootDir", "/"));
        nmvisit.setChecked(SharedPreferencesUtils.getBoolean(Settings.this, "allow_anonymous", false));
        if (nmvisit.isChecked()) {
            textInputLayoutpasswords.getEditText().setEnabled(false);
            textInputLayoutusername.getEditText().setEnabled(false);
        } else {
            textInputLayoutpasswords.getEditText().setEnabled(true);
            textInputLayoutusername.getEditText().setEnabled(true);
        }
    }

    @Override
    public void onRefresh() {
        nmvisit.setChecked(false);
        textInputLayoutpasswords.getEditText().setText("");
        textInputLayoutusername.getEditText().setText("");
        textInputLayoutport.getEditText().setText("");
        textInputLayoutdir.getEditText().setText("");
        clean.setRefreshing(false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //什么都不做
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //什么都不做
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == requestCode) {
            Bundle bundle = null;
            if (data != null && (bundle = data.getExtras()) != null) {
                textInputLayoutdir.getEditText().setText(bundle.getString("file")+"/");
            }
        }
    }
}
