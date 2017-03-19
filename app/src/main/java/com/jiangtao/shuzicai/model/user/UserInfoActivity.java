package com.jiangtao.shuzicai.model.user;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jiangtao.shuzicai.Application;
import com.jiangtao.shuzicai.R;
import com.jiangtao.shuzicai.basic.base.BaseActivityWithToolBar;
import com.jiangtao.shuzicai.basic.widget.CustomListViewDialog;
import com.jiangtao.shuzicai.common.event_message.EditUserInfoMsg;
import com.jiangtao.shuzicai.common.event_message.SelectGalleryPhotoMsg;
import com.jiangtao.shuzicai.common.view.city_selecter.ChangeAddressPopWindow;
import com.jiangtao.shuzicai.common.view.photo_gallery.imageloader.GalleyActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class UserInfoActivity extends BaseActivityWithToolBar {

    //昵称
    @BindView(R.id.nickNameEdt)
    EditText nickNameEdt;
    //城市
    @BindView(R.id.cityTxt)
    TextView cityTxt;
    //性别
    @BindView(R.id.sexTxt)
    TextView sexTxt;
    //头像
    @BindView(R.id.uerHeadImg)
    SimpleDraweeView uerHeadImg;
    //头像路径
    private String filePath;
    //性别
    private int sexValue = 0;
    //地址
    private String location = "广东-深圳-福田";
    private String nickName, imageUrl;

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
        if (null != Application.userInstance) {
            sexValue = Application.userInstance.getGender();
            location = Application.userInstance.getAddress();
            nickName = Application.userInstance.getNickName();
            imageUrl = Application.userInstance.getHeadImageUrl();
        }
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
                startSaveInfo();
            }
        });

        setCenterTitle("个人信息");
        //初始化状态
        cityTxt.setText(location);
        sexTxt.setText(sexValue == 0 ? "男" : "女");
        if (null != imageUrl && !imageUrl.equals("") && URLUtil.isNetworkUrl(imageUrl)) {
            Uri uri = Uri.parse(imageUrl);
            uerHeadImg.setImageURI(uri);
        } else {
            Uri uri = Uri.parse("res:///" + R.mipmap.add_head);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setTapToRetryEnabled(true)
                    .build();
            uerHeadImg.setController(controller);
        }

        if (null != nickName && !nickName.equals("")) {
            nickNameEdt.setText(nickName);
        } else {
            nickNameEdt.setHint("输入昵称...");
        }
    }

    @Override
    public void initPresenter() {
    }

    /**
     * 选择照片
     */
    private void selectPhotos() {
        // 相册
        Intent intentAlbum = new Intent(UserInfoActivity.this, GalleyActivity.class);
        intentAlbum.putExtra(GalleyActivity.INTENT_KEY_SELECTED_COUNT, 0);
        intentAlbum.putExtra(GalleyActivity.INTENT_KEY_ONE, true);
        startActivity(intentAlbum);
    }

    /**
     * 选择城市
     */
    private void selectCity() {
        ChangeAddressPopWindow mChangeAddressPopwindow = new ChangeAddressPopWindow(UserInfoActivity.this);
        mChangeAddressPopwindow.setAddress("广东", "广州", "天河区");
        mChangeAddressPopwindow.showAtLocation(cityTxt, Gravity.BOTTOM, 0, 0);
        mChangeAddressPopwindow
                .setAddresskListener(new ChangeAddressPopWindow.OnAddressCListener() {

                    @Override
                    public void onClick(String province, String city, String area) {
                        location = province + "-" + city + "-" + area;
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
        final CustomListViewDialog dialog = new CustomListViewDialog(UserInfoActivity.this, datas);
        dialog.setClickCallBack(new CustomListViewDialog.IClickCallBack() {
            @Override
            public void Onclick(View view, int which) {
                sexTxt.setText(datas.get(which));
                sexValue = which;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 保存信息
     */
    private void startSaveInfo() {
        if (Application.userInstance != null) {
            showProgress("上传中...");
            if (null != filePath && filePath.length() > 10) {
                //只有头像有修改就先上传头像
                startUploadImage();
            } else {
                //没有修改头像
                String name = nickNameEdt.getText().toString().trim();
                Application.userInstance.setNickName(name);
                Application.userInstance.setAddress(location);
                Application.userInstance.setGender(sexValue);
                Application.userInstance.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        hideProgress();
                        if (e == null) {
                            ToastUtils.showShortToast("操作成功");
                            EventBus.getDefault().post(new EditUserInfoMsg());
                            finish();
                        } else {
                            ToastUtils.showShortToast("上传数据失败");
                        }
                    }
                });
            }
        }
    }

    /**
     * 上传头像
     */
    private void startUploadImage() {
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    String imageUrl = bmobFile.getFileUrl();
                    if (UriUtil.isNetworkUri(UriUtil.parseUriOrNull(imageUrl))) {
                        String name = nickNameEdt.getText().toString().trim();
                        if (null != Application.userInstance) {
                            updateInfo(imageUrl, name);
                        }
                    } else {
                        hideProgress();
                        ToastUtils.showLongToast("上传头像失败");
                    }
                } else {
                    hideProgress();
                    ToastUtils.showShortToast("上传头像失败");
                }
            }
        });
    }

    /**
     * 上传信息成功
     *
     * @param imageUrl
     * @param name
     */
    public void updateInfo(String imageUrl, String name) {
        Application.userInstance.setNickName(name);
        Application.userInstance.setAddress(location);
        Application.userInstance.setHeadImageUrl(imageUrl);
        Application.userInstance.setGender(sexValue);
        Application.userInstance.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                hideProgress();
                if (e == null) {
                    ToastUtils.showShortToast("操作成功");
                    EventBus.getDefault().post(new EditUserInfoMsg());
                    finish();
                } else {
                    LogUtils.e("上传数据失败：" + e.toString());
                    ToastUtils.showShortToast("上传数据失败");
                }
            }
        });
    }

    /**
     * 主线程处理接收到的数据
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(SelectGalleryPhotoMsg event) {
        if (null == event) {
            return;
        }
        List<String> photoList = event.getPhotoList();
        if (null != photoList && photoList.size() > 0) {
            filePath = photoList.get(0);
            // RegisterSetInfoPresenter.replayPlace(filePath);

            Uri uri = Uri.parse("file://" + filePath);
            LogUtils.i("选择的图片路径：" + uri.toString());
            uerHeadImg.setImageURI(uri);
        }
    }
}
