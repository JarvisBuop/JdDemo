package com.jd.jarvisdemo1.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jd.jarvisdemo1.R;
import com.jd.jarvisdemo1.app.BaseBuildInfo;
import com.jd.jarvisdemo1.app.BuildInfo;
import com.jd.jarvisdemo1.util.Utils;
import com.tencent.tinker.lib.library.TinkerLoadLibrary;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;

import java.io.File;

/**
 * Created by JarvisDong on 2017/10/9.
 * OverView:
 */

public class HotFixAct extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        Utils.setBackground(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotfix);
        final TextView txtInfo = (TextView) findViewById(R.id.txt_info);

        findViewById(R.id.btn_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载路径
                loadPatcher();
                txtInfo.performClick();
            }
        });
        findViewById(R.id.btn_load_lib).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLib();
            }
        });
        findViewById(R.id.btn_clear_patch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPatch();
                txtInfo.performClick();
            }
        });

        findViewById(R.id.kill_self).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killSelf();
            }
        });

        txtInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(txtInfo, HotFixAct.this);
            }
        });

        fixBug();
    }

    private void fixBug() {
        Toast.makeText(this,"text3",Toast.LENGTH_SHORT).show();
        ((TextView)findViewById(R.id.text_bug)).setText("已修复版本3");
        ((ImageView)findViewById(R.id.text_bug2)).setImageResource(R.mipmap.ic_help);
    }

    private void loadPatcher() {
        String pathLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";
        File file = new File(pathLocation);
        if(!file.exists()){
            Toast.makeText(this,"暂无补丁包",Toast.LENGTH_SHORT).show();
        }

        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                pathLocation);
    }

    private void loadLib() {
        // #method 1, hack classloader library path
        TinkerLoadLibrary.installNavitveLibraryABI(getApplicationContext(), "armeabi");
        System.loadLibrary("stlport_shared");

        // #method 2, for lib/armeabi, just use TinkerInstaller.loadLibrary
//                TinkerLoadLibrary.loadArmLibrary(getApplicationContext(), "stlport_shared");

        // #method 3, load tinker patch library directly
//                TinkerInstaller.loadLibraryFromTinker(getApplicationContext(), "assets/x86", "stlport_shared");

    }

    private void clearPatch() {
        Tinker.with(getApplicationContext()).cleanPatch();
    }

    private void killSelf() {
        ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public boolean showInfo(TextView textView, Context context) {
        // add more Build Info
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

        } else {
            sb.append(String.format("[patch is not loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }
        sb.append(String.format("[BaseBuildInfo Message] %s \n", BaseBuildInfo.TEST_MESSAGE));

        textView.setText(sb);
        textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        textView.setTextColor(0xFF000000);
        textView.setTypeface(Typeface.MONOSPACE);
        final int padding = 16;
        textView.setPadding(padding, padding, padding, padding);
        return true;
    }
}
