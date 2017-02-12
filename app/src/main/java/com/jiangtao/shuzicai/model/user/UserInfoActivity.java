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
import com.jiangtao.shuzicai.model.user.entry.UpdateInfoBean;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetInfoView;
import com.jiangtao.shuzicai.model.user.presenter.RegisterSetInfoPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInfoActivity extends BaseActivityWithToolBar implements IRegisterSetInfoView {

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
    //上传图片、
    private RegisterSetInfoPresenter registerSetInfoPresenter;
    //头像路径
    private String filePath;
    //性别
    private int sexValue = 0;
    //地址
    private String location = "广东-深圳-福田";

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
        this.sexValue = Application.userInstance.getGender();
        this.location = Application.userInstance.getAddress();
        String nickName = Application.userInstance.getNickName();
        String imageUrl = Application.userInstance.getHeadImageUrl();
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
        registerSetInfoPresenter = new RegisterSetInfoPresenter(this, this);
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
            if (null != filePath && filePath.length() > 0) {
                Application.AppThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        registerSetInfoPresenter.startPostHeadImg(filePath);
                    }
                });
            } else {
                String name = nickNameEdt.getText().toString().trim();
                UpdateInfoBean bean = new UpdateInfoBean(name,
                        Application.userInstance.getHeadImageUrl(), sexValue, location);
                String token = Application.userInstance.getToken();
                String objectID = Application.userInstance.getObjectId();
                registerSetInfoPresenter.startSetUserInfo(token, objectID, bean);
            }
        } else {
            if (null == filePath || filePath.length() <= 0) {
                ToastUtils.showShortToast("请先选择头像");
                return;
            }
            showProgress("上传中...");
            //上传头像文件
            Application.AppThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    registerSetInfoPresenter.startPostHeadImg(filePath);
                }
            });
        }
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

    @Override
    public void onHeadImgPostSucceed(String imageUrl) {
        if (UriUtil.isNetworkUri(UriUtil.parseUriOrNull(imageUrl))) {
            String name = nickNameEdt.getText().toString().trim();
            UpdateInfoBean bean = new UpdateInfoBean(name, imageUrl, sexValue, location);

            if (null != Application.userInstance) {
                //d6d170c440dc5bd480e52e25c0ab12c9
                String token = Application.userInstance.getToken();
                //"27c5dd1f48"
                String objectID = Application.userInstance.getObjectId();
                registerSetInfoPresenter.startSetUserInfo(token, objectID, bean);
            }
        } else {
            hideProgress();
            ToastUtils.showLongToast("上传头像失败");
        }
    }

    @Override
    public void onHeadImgPostFailed() {
        hideProgress();
        ToastUtils.showShortToast("上传失败");
    }

    @Override
    public void onUpdateInfoFailed() {
        hideProgress();
        ToastUtils.showShortToast("上传数据失败");
    }

    @Override
    public void onUpdateInfoSucceed() {
        hideProgress();
        ToastUtils.showShortToast("操作成功");
        EventBus.getDefault().post(new EditUserInfoMsg());
        finish();
    }
}
