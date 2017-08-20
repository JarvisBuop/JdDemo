package com.jd.jarvisdemo1.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import com.jd.jarvisdemo1.R;

public class SearchAct extends AppCompatActivity {
    SearchView mSearch;
    SearchView mSearch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        mSearch = (SearchView) findViewById(R.id.searchview);
        mSearch2 = (SearchView) findViewById(R.id.searchview2);
        initView();
    }

    private void initView() {
        mSearch2.setSubmitButtonEnabled(true);
        mSearch2.setQueryRefinementEnabled(true);//提示内容右边提供一个将提示内容放到搜索框的按钮

        int id = mSearch2.getContext().getResources().getIdentifier("search_src_text", "id", getPackageName());
//获取到TextView的控件
        TextView textView = (TextView) mSearch2.findViewById(id);
        if (textView != null) {
//设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//14sp
//设置字体颜色
            textView.setTextColor(getResources().getColor(R.color.black));
//设置提示文字颜色
            textView.setHintTextColor(getResources().getColor(R.color.gray_chart));
        }
        int imgId = mSearch2.getContext().getResources().getIdentifier("search_mag_icon", "id", getPackageName());

        ImageView searchButton = (ImageView) mSearch2.findViewById(imgId);
        if (searchButton != null)
//设置图片
            searchButton.setImageResource(R.mipmap.icon_search);

//        int image = mSearch2.getContext().getResources().getIdentifier("search_go_btn","id",getPackageName());
//        ImageView viewById = (ImageView) mSearch2.findViewById(image);
//        viewById.setImageResource(R.mipmap.ic_help);


//        final Drawable searchIcon = getResources().getDrawable(R.mipmap.icon_search);
//        final int textSize = (int) (textView.getTextSize() * 1.25);//根据文本框大小来确定图标大小
//        searchIcon.setBounds(0, 0, textSize, textSize);
//        final SpannableStringBuilder ssb = new SpannableStringBuilder("   "); // for the icon
//        ssb.append("搜索text");
//        ssb.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////        int padding =getResources().getDimensionPixelSize(5);
//        int padding = 5;
//        textView.setPadding(padding, padding, padding, padding);//设置图标和文字跟四周距离
//        textView.setGravity(Gravity.BOTTOM);
//        textView.setHint(ssb);
    }
}
