package com.example.emotionkeyboard.view.manager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.emotionkeyboard.R;
import com.example.emotionkeyboard.adapter.recycleradaper.BaseRecyclerAdapter;
import com.example.emotionkeyboard.decoration.DividerGridItemDecoration;
import com.example.emotionkeyboard.decoration.HorizontalDividerItemDecoration;
import com.example.emotionkeyboard.decoration.VerticalDividerItemDecoration;
import com.example.emotionkeyboard.util.FileUtil;
import com.example.emotionkeyboard.util.GenericTools;
import com.example.emotionkeyboard.view.BaseActivity;
import com.example.emotionkeyboard.view.EmotionFragment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by chasing on 2017/12/18.
 */
public class LocalPicManagerActivity extends BaseActivity {
    public static final int REQUEST_CODE_SELECT_PIC = 1;

    private RecyclerView mRcvLocalPic;
    private ImageButton mIbBack;
    private TextView mTvManager;
    private ManagerAdapter mManagerAdapter;

    private boolean hadChange;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_local_pic_manager);

        verifyStoragePermissions(this);

        initView();
        setListener();
        loadData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            sendBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 发送标志位给上个界面
     */
    private void sendBack() {
        Intent intent = new Intent();
        intent.putExtra(EmotionFragment.INTENT_BACK_HAD_CHANGE, hadChange);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initView() {
        initRcv();

        mIbBack = (ImageButton) findViewById(R.id.ib_back);
        mTvManager = (TextView) findViewById(R.id.tv_manager);
    }

    private void setListener() {
        mIbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBack();
            }
        });

        mTvManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    private void initRcv() {
        mRcvLocalPic = (RecyclerView) findViewById(R.id.rcv_local_pic_manager);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRcvLocalPic.setLayoutManager(gridLayoutManager);
        mRcvLocalPic.addItemDecoration(new DividerGridItemDecoration(this).colorResId(android.R.color.darker_gray));
        mManagerAdapter = new ManagerAdapter(this, R.layout.item_emotion, GenericTools.getScreenWidth(this) / 4);
        mRcvLocalPic.setAdapter(mManagerAdapter);
        mManagerAdapter.setItemClickListener(new BaseRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (position == 0) {
                    if (verifyStoragePermissions(LocalPicManagerActivity.this)) {
                        Intent intent = FileUtil.getSystemImageIntent();
                        startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
                    }
                }
            }
        });
    }

    private void loadData() {
        ArrayList<String> data = new ArrayList<>();
        data.add(String.valueOf(R.mipmap.add_pic_bg));
        File[] emotionFiles = FileUtil.getEmotionFiles();
        if (emotionFiles != null && emotionFiles.length != 0) {
            for (File file : emotionFiles) {
                data.add(file.getAbsolutePath());
            }
        }
        mManagerAdapter.addAll(data);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean verifyStoragePermissions(final Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = FileUtil.getSystemImageIntent();
            startActivityForResult(intent, REQUEST_CODE_SELECT_PIC);
        } else {
            Toast.makeText(this, "需要开启访问读取权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_PIC && data != null) {
            showLoading("加载中...");
            Uri uri = data.getData();
            // 保存本地
            FileUtil.saveSystemBitmap2Local(this, uri, handlerSystemPic);
            // todo 保存到服务器
        }
    }

    public static final int HANDLER_HAD_EXIST = 0;
    public static final int HANDLER_SUCCESS = 1;
    public static final int HANDLER_FAIL = 2;
    private Handler handlerSystemPic = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_HAD_EXIST:
                    Toast.makeText(LocalPicManagerActivity.this, "图片已经存在", Toast.LENGTH_SHORT).show();
                    break;

                case HANDLER_SUCCESS:
                    String fileName = (String) msg.obj;
                    mManagerAdapter.add(fileName);
                    hadChange = true;
                    break;

                case HANDLER_FAIL:
                    Toast.makeText(LocalPicManagerActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            hideLoading();
        }
    };
}
