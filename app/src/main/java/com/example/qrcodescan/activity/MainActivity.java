package com.example.qrcodescan.activity;

import com.example.qr_codescan.R;
import com.example.qrcodescan.service.DownloadService;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private final static int SCANNIN_GREQUEST_CODE = 1;
    /**
     * 显示扫描结果
     */
    private TextView mTextView;
    /**
     * 显示扫描拍的图片
     */
    private ImageView mImageView;
    private Button mScanButton;
    private Button mOpenButton;
    private Button mDownloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mScanButton = (Button) findViewById(R.id.button1);
        mTextView = (TextView) findViewById(R.id.result);
        mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
        mOpenButton = (Button) findViewById(R.id.btn_open);
        mDownloadButton = (Button) findViewById(R.id.btn_download);
        
        // 点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
        // 扫描完了之后调到该界面
        mScanButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });
        
        
        mOpenButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(mTextView.getText().toString());
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                MainActivity.this.startActivity(it);
            }
        });
        mDownloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.putExtra(DownloadService.APK_DOWNLOAD_URL, Uri.parse(mTextView.getText().toString()));
                intent.putExtra(DownloadService.APK_DOWNLOAD_APKNAME, "lofter.apk");
                intent.putExtra(DownloadService.APK_DOWNLOAD_APPNAME, "网易LOFTER");
                intent.putExtra(DownloadService.APK_DOWNLOAD_ICON, android.R.drawable.stat_sys_download_done);
                MainActivity.this.startService(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        case SCANNIN_GREQUEST_CODE:
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                // 显示扫描到的内容
                mTextView.setText(bundle.getString("result"));
                mTextView.setVisibility(View.VISIBLE);
                // 显示
                mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                mImageView.setVisibility(View.VISIBLE);
                mOpenButton.setVisibility(View.VISIBLE);
                mDownloadButton.setVisibility(View.VISIBLE);
            }
            break;
        }
    }

}
