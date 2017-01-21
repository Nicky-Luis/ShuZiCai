package com.jiangtao.shuzicai.model.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.widget.CustomListViewDialog;
import com.jiangtao.shuzicai.common.view.city_selecter.ChangeAddressPopWindow;
import com.jiangtao.shuzicai.common.view.photo_gallery.imageloader.GalleyActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterSetInfoActivity extends BaseActivityWithToolBar {

    public static final int ALBUM_SELECT = 2;// 相册选取
    public static final int PHOTO_ZOOM = 3;// 缩放
    public static final int PHOTO_RESOULT = 4;// 结果
    //城市
    @BindView(R.id.cityTxt)
    TextView cityTxt;
    //性别
    @BindView(R.id.sexTxt)
    TextView sexTxt;

    //设置点击事件
    @OnClick({R.id.cityTxt, R.id.sexTxt, R.id.uerHeadImg})
    public void OnClick(View view) {
        switch (view.getId()) {

            //选择头像
            case R.id.uerHeadImg:
                selectPhotos();
                break;

            //select city
            case R.id.cityTxt:
                selectCity();
                break;

            //选择性别
            case R.id.sexTxt:
                selectSex();
                break;
        }
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_register_set_info;
    }

    @Override
    protected void onInitialize() {
        //返回
        setLeftImage(R.mipmap.ic_arrow_back_white_24dp, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //右键
        setRightTitle("保存", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void initPresenter() {

    }

    private void selectPhotos() {
        // 相册
        Intent intentAlbum = new Intent(RegisterSetInfoActivity.this, GalleyActivity.class);
        intentAlbum.putExtra(GalleyActivity.INTENT_KEY_SELECTED_COUNT, 0);
        intentAlbum.putExtra(GalleyActivity.INTENT_KEY_ONE, true);
        startActivityForResult(intentAlbum, ALBUM_SELECT);
    }

    /**
     * 选择城市
     */
    private void selectCity() {
        ChangeAddressPopWindow mChangeAddressPopwindow = new ChangeAddressPopWindow(RegisterSetInfoActivity.this);
        mChangeAddressPopwindow.setAddress("广东", "广州", "天河区");
        mChangeAddressPopwindow.showAtLocation(cityTxt, Gravity.BOTTOM, 0, 0);
        mChangeAddressPopwindow
                .setAddresskListener(new ChangeAddressPopWindow.OnAddressCListener() {

                    @Override
                    public void onClick(String province, String city, String area) {
                        String location = province + "-" + city + "-" + area;
                        cityTxt.setText(location);
                    }
                });
    }

    /***
     * 选择性别
     */
    private void selectSex() {
        final List<String> datas = new ArrayList<>();
        datas.add("男");
        datas.add("女");
        final CustomListViewDialog dialog = new CustomListViewDialog(RegisterSetInfoActivity.this, datas);
        dialog.setClickCallBack(new CustomListViewDialog.IClickCallBack() {
            @Override
            public void Onclick(View view, int which) {
                sexTxt.setText(datas.get(which));
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(String event) {
        Log.e("event MainThread", "消息： " + event + "  thread: " + Thread.currentThread().getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                // 检测sd是否可用
                LogUtils.i("SD card is not avaiable/writeable right now.", 1);
                return;
            }
            if (requestCode == ALBUM_SELECT) {
                if (intent != null) {
                    List<String> photos = (List<String>) intent.getSerializableExtra(GalleyActivity
                            .INTENT_KEY_PHOTO_LIST);
                    for (String path : photos) {
                        LogUtils.i("路径：" + photos);
                    }
                }
            }
        }
    }
}
