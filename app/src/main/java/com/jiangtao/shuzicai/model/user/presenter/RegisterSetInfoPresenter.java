package com.jiangtao.shuzicai.model.user.presenter;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.jiangtao.shuzicai.basic.network.APIInteractive;
import com.jiangtao.shuzicai.basic.network.INetworkResponse;
import com.jiangtao.shuzicai.model.user.entry.UpdateInfoBean;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetInfoPresenter;
import com.jiangtao.shuzicai.model.user.interfaces.IRegisterSetInfoView;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Nicky on 2017/1/21.
 */

public class RegisterSetInfoPresenter implements IRegisterSetInfoPresenter {

    //上下文
    private Context mContext;
    //view对象
    private IRegisterSetInfoView registerSetInfoView;

    public RegisterSetInfoPresenter(Context mContext, IRegisterSetInfoView registerSetInfoView) {
        this.mContext = mContext;
        this.registerSetInfoView = registerSetInfoView;
    }

    @Override
    public void startPostHeadImg(String filePath) {
        //上传图片
        APIInteractive.postFile(filePath, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                registerSetInfoView.onHeadImgPostFailed();
            }

            @Override
            public void onSucceed(JSONObject result) {
                String url = result.optString("url");
                registerSetInfoView.onHeadImgPostSucceed(url);
            }
        });
    }

    @Override
    public void startSetUserInfo(String token, String onjectID, UpdateInfoBean bean) {
        //修改用户信息
        APIInteractive.updateUserInfo(token, onjectID, bean, new INetworkResponse() {
            @Override
            public void onFailure(int code) {
                registerSetInfoView.onUpdateInfoFailed();
            }

            @Override
            public void onSucceed(JSONObject result) {
                registerSetInfoView.onUpdateInfoSucceed();
            }
        });
    }


    /**
     * 路径名称处理
     *
     * @param path
     * @return
     */
    public static String replayPlace(String path) {
        String result = path.substring(0, path.lastIndexOf("/"));
        String name = path.substring(path.lastIndexOf("/"), path.length());
        LogUtils.i("result："+result);
        LogUtils.i("name："+name);
        String reg = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(reg);
        Matcher mat = pat.matcher(name);
        int value = (int) (1 + Math.random() * (10 - 1 + 1));
        String repickStr = mat.replaceAll("" + value);

        LogUtils.i("结果："+result + repickStr);
        return result + repickStr;
    }
}
