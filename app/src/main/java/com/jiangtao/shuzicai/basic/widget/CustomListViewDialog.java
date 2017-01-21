package com.jiangtao.shuzicai.basic.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.BaseAdapterHelper;
import com.jiangtao.shuzicai.basic.adpter.base_adapter_helper_listview.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomListViewDialog extends Dialog {

    private Context mContext;
    // layout
    private ListView itemListView;
    // item 列表
    private List<String> listContent = new ArrayList<String>();
    // 回调监听事件
    private IClickCallBack clickCallBack;
    // 适配器
    private QuickAdapter<String> itemAdapter;

    public CustomListViewDialog(Context context, List<String> list) {
        super(context, R.style.item_dialog);
        this.listContent = list;
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View rootView = inflater.inflate(R.layout.dialog_custom_list_layout, null);
        itemListView = (ListView) rootView
                .findViewById(R.id.listView_dialog_item);
        dialogItemSet();
        DisplayMetrics displayMet = this.getContext().getResources()
                .getDisplayMetrics();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                displayMet.widthPixels * 2 / 3,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setContentView(rootView, params);
        super.onCreate(savedInstanceState);
    }

    private void dialogItemSet() {
        itemAdapter = new QuickAdapter<String>(mContext, R.layout.dialog_custom_listview_item_layout, listContent) {
            @Override
            protected void convert(BaseAdapterHelper helper, String item) {
                helper.setText(R.id.tv_item_dialog, item);
            }
        };
        itemListView.setAdapter(itemAdapter);
        itemListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                clickCallBack.Onclick(view, position);
            }
        });
    }

    // 设置回调
    public void setClickCallBack(IClickCallBack clickBack) {
        this.clickCallBack = clickBack;
    }

    /**
     * 点赞回调接口
     */
    public interface IClickCallBack {
        public void Onclick(View view, int which);
    }
}
