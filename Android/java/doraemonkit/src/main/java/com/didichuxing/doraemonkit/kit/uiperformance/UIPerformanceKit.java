package com.didichuxing.doraemonkit.kit.uiperformance;

import android.content.Context;

import com.didichuxing.doraemonkit.R;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.core.DokitIntent;
import com.didichuxing.doraemonkit.kit.core.DokitViewManager;
import com.didichuxing.doraemonkit.kit.core.SimpleDokitStarter;
import com.google.auto.service.AutoService;

/**
 * Created by wanglikun on 2019-06-27
 * UI渲染性能kit
 */
@AutoService(AbstractKit.class)
public class UIPerformanceKit extends AbstractKit {


    @Override
    public int getName() {
        return R.string.dk_kit_ui_performance;
    }

    @Override
    public int getIcon() {
        return R.mipmap.dk_ui_performance;
    }

    @Override
    public void onClick(Context context) {
        UIPerformanceManager.getInstance().start(context);

        DokitViewManager.getInstance().detachToolPanel();

        SimpleDokitStarter.startFloating(UIPerformanceDisplayDokitView.class);
        SimpleDokitStarter.startFloating(UIPerformanceInfoDokitView.class);

        //直接显示层级
        UIPerformanceManager.getInstance().initRefresh();
    }

    @Override
    public void onAppInit(Context context) {

    }

    @Override
    public boolean isInnerKit() {
        return true;
    }

    @Override
    public String innerKitId() {
        return "dokit_sdk_performance_ck_hierarchy";
    }
}