package com.didichuxing.doraemonkit.kit.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.didichuxing.doraemonkit.constant.DokitConstant;
import com.didichuxing.doraemonkit.config.FloatIconConfig;
import com.didichuxing.doraemonkit.kit.main.MainIconDokitView;
import com.didichuxing.doraemonkit.util.LogHelper;

import java.lang.ref.WeakReference;

/**
 * ================================================
 * 作    者：jint（金台）
 * 版    本：1.0
 * 创建日期：2019-09-20-16:22
 * 描    述：dokit 页面浮标抽象类 一般的悬浮窗都需要继承该抽象接口
 * 修订历史：
 * ================================================
 */
public abstract class AbsDokitView implements DokitView, TouchProxy.OnTouchEventListener, DokitViewManager.DokitViewAttachedListener {
    private String TAG = this.getClass().getSimpleName();

    /**
     * 手势代理
     */
    public TouchProxy mTouchProxy = new TouchProxy(this);

    protected WindowManager mWindowManager = DokitViewManager.getInstance().getWindowManager();
    /**
     * 创建FrameLayout#LayoutParams 内置悬浮窗调用
     */
    private FrameLayout.LayoutParams mFrameLayoutParams;
    /**
     * 创建FrameLayout#LayoutParams 系统悬浮窗调用
     */
    private WindowManager.LayoutParams mWindowLayoutParams;
    private Handler mHandler;

    private AbsDokitView.InnerReceiver mInnerReceiver = new AbsDokitView.InnerReceiver();
    /**
     * 当前dokitViewName 用来当做map的key 和dokitViewIntent的tag一致
     */
    private String mTag = TAG;

    private Bundle mBundle;
    /**
     * weakActivity attach activity
     */
    private WeakReference<Activity> mAttachActivity;

    private FrameLayout mRootView;
    /**
     * rootView的直接子View 一般是用户的xml布局 被添加到mRootView中
     */
    private View mChildView;

    /**
     * 用来保存rootview的LayoutParams
     */
    private DokitViewLayoutParams mDokitViewLayoutParams;
    /**
     * 上一次DoKitview的位置信息
     */
    private LastDokitViewPosInfo mLastDokitViewPosInfo;
    /**
     * 根布局的实际宽
     */
    private int mDokitViewWidth = 0;
    /**
     * 根布局的实际高
     */
    private int mDokitViewHeight = 0;
    private ViewTreeObserver mViewTreeObserver;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            if (mRootView != null) {
                //每次布局发生变动的时候重新赋值
                mDokitViewWidth = mRootView.getMeasuredWidth();
                mDokitViewHeight = mRootView.getMeasuredHeight();
                if (mLastDokitViewPosInfo != null) {
                    mLastDokitViewPosInfo.setDokitViewWidth(mDokitViewWidth);
                    mLastDokitViewPosInfo.setDokitViewHeight(mDokitViewHeight);
                }
            }
        }
    };
    /**
     * 页面启动模式
     */
    private int mode;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * 构造函数
     */
    public AbsDokitView() {
        TAG = this.getClass().getSimpleName();
        if (DokitViewManager.getInstance().getLastDokitViewPosInfo(mTag) == null) {
            mLastDokitViewPosInfo = new LastDokitViewPosInfo();
            DokitViewManager.getInstance().saveLastDokitViewPosInfo(mTag, mLastDokitViewPosInfo);
        } else {
            mLastDokitViewPosInfo = DokitViewManager.getInstance().getLastDokitViewPosInfo(mTag);
        }
        //创建主线程handler
        mHandler = new Handler(Looper.myLooper());
    }

    /**
     * 执行floatPage create
     *
     * @param context 上下文环境
     */
    @SuppressLint("ClickableViewAccessibility")
    void performCreate(Context context) {
        try {
            //调用onCreate方法
            onCreate(context);
            if (!isNormalMode()) {
                DokitViewManager.getInstance().addDokitViewAttachedListener(this);
            }
            if (isNormalMode()) {
                mRootView = new DokitFrameLayout(context);
            } else {
                //系统悬浮窗的返回按键监听
                mRootView = new DokitFrameLayout(context) {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_UP && shouldDealBackKey()) {
                            //监听返回键
                            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME) {
                                return onBackPressed();
                            }
                        }
                        return super.dispatchKeyEvent(event);
                    }
                };
            }
//            mRootView.setClipChildren(false);
//            mRootView.setClipToPadding(false);
            //添加根布局的layout回调
            addViewTreeObserverListener();


            //调用onCreateView抽象方法
            mChildView = onCreateView(context, mRootView);
            //将子View添加到rootview中
            mRootView.addView(mChildView);
            //设置根布局的手势拦截
            mRootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (getRootView() != null) {
                        return mTouchProxy.onTouchEvent(v, event);
                    } else {
                        return false;
                    }
                }
            });
            //调用onViewCreated回调
            onViewCreated(mRootView);

            mDokitViewLayoutParams = new DokitViewLayoutParams();
            //分别创建对应的LayoutParams
            if (isNormalMode()) {
                mFrameLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                mFrameLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                mDokitViewLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            } else {
                mWindowLayoutParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //android 8.0
                    mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                if (!shouldDealBackKey()) {
                    //参考：http://www.shirlman.com/tec/20160426/362
                    //设置WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE会导致rootview监听不到返回按键的监听失效
                    mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                    mDokitViewLayoutParams.flags = DokitViewLayoutParams.FLAG_NOT_FOCUSABLE;
                }
                mWindowLayoutParams.format = PixelFormat.TRANSPARENT;
                mWindowLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                mDokitViewLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
                //动态注册关闭系统弹窗的广播
                IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.registerReceiver(mInnerReceiver, intentFilter);
            }
            initDokitViewLayoutParams(mDokitViewLayoutParams);
            if (isNormalMode()) {
                onNormalLayoutParamsCreated(mFrameLayoutParams);
            } else {
                onSystemLayoutParamsCreated(mWindowLayoutParams);
            }
        } catch (Exception e) {
            LogHelper.e(TAG, "e===>" + e.getMessage());
            e.printStackTrace();
        }

    }


    void performDestroy() {
        if (!isNormalMode()) {
            getContext().unregisterReceiver(mInnerReceiver);
        }
        //移除布局监听
        removeViewTreeObserverListener();
        mHandler = null;
        mRootView = null;
        onDestroy();
    }

    private void addViewTreeObserverListener() {
        if (mViewTreeObserver == null && mRootView != null && mOnGlobalLayoutListener != null) {
            mViewTreeObserver = mRootView.getViewTreeObserver();
            mViewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }

    }

    private void removeViewTreeObserverListener() {
        if (mViewTreeObserver != null && mOnGlobalLayoutListener != null) {
            if (mViewTreeObserver.isAlive()) {
                mViewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        }

    }


    /**
     * 确定普通浮标的初始位置
     * LayoutParams创建完以后调用
     * 调用时建议放在实现下方
     *
     * @param params
     */
    private void onNormalLayoutParamsCreated(FrameLayout.LayoutParams params) {
        //如果有上一个页面的位置记录 这更新位置
        params.width = mDokitViewLayoutParams.width;
        params.height = mDokitViewLayoutParams.height;
        params.gravity = mDokitViewLayoutParams.gravity;
        portraitOrLandscape(params);
    }

    /**
     * 用于普通模式下的横竖屏切换
     */
    private void portraitOrLandscape(FrameLayout.LayoutParams params) {
        Point point = DokitViewManager.getInstance().getDokitViewPos(mTag);
        if (point != null) {
            //横竖屏切换兼容
            if (ScreenUtils.isPortrait()) {
                if (mLastDokitViewPosInfo.isPortrait()) {
                    params.leftMargin = point.x;
                    params.topMargin = point.y;
                } else {
                    params.leftMargin = (int) (point.x * mLastDokitViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (point.y * mLastDokitViewPosInfo.getTopMarginPercent());
                }
            } else {
                if (mLastDokitViewPosInfo.isPortrait()) {
                    params.leftMargin = (int) (point.x * mLastDokitViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (point.y * mLastDokitViewPosInfo.getTopMarginPercent());
                } else {
                    params.leftMargin = point.x;
                    params.topMargin = point.y;
                }
            }
        } else {
            //横竖屏切换兼容
            if (ScreenUtils.isPortrait()) {
                if (mLastDokitViewPosInfo.isPortrait()) {
                    params.leftMargin = mDokitViewLayoutParams.x;
                    params.topMargin = mDokitViewLayoutParams.y;
                } else {
                    params.leftMargin = (int) (mDokitViewLayoutParams.x * mLastDokitViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (mDokitViewLayoutParams.y * mLastDokitViewPosInfo.getTopMarginPercent());
                }
            } else {
                if (mLastDokitViewPosInfo.isPortrait()) {
                    params.leftMargin = (int) (mDokitViewLayoutParams.x * mLastDokitViewPosInfo.getLeftMarginPercent());
                    params.topMargin = (int) (mDokitViewLayoutParams.y * mLastDokitViewPosInfo.getTopMarginPercent());
                } else {
                    params.leftMargin = mDokitViewLayoutParams.x;
                    params.topMargin = mDokitViewLayoutParams.y;
                }
            }
        }
        mLastDokitViewPosInfo.setPortrait();
        mLastDokitViewPosInfo.setLeftMargin(params.leftMargin);
        mLastDokitViewPosInfo.setTopMargin(params.topMargin);
    }


    /**
     * 确定系统浮标的初始位置
     * LayoutParams创建完以后调用
     * 调用时建议放在实现下方
     *
     * @param params
     */
    private void onSystemLayoutParamsCreated(WindowManager.LayoutParams params) {
        //如果有上一个页面的位置记录 这更新位置
        params.flags = mDokitViewLayoutParams.flags;
        params.gravity = mDokitViewLayoutParams.gravity;
        params.width = mDokitViewLayoutParams.width;
        params.height = mDokitViewLayoutParams.height;
        Point point = DokitViewManager.getInstance().getDokitViewPos(mTag);
        if (point != null) {
            params.x = point.x;
            params.y = point.y;
        } else {
            params.x = mDokitViewLayoutParams.x;
            params.y = mDokitViewLayoutParams.y;
        }
    }


    @Override
    public void onDestroy() {
        if (!isNormalMode()) {
            DokitViewManager.getInstance().removeDokitViewAttachedListener(this);
        }
        DokitViewManager.getInstance().removeLastDokitViewPosInfo(mTag);
        mAttachActivity = null;
    }

    /**
     * 默认实现为true
     *
     * @return
     */
    @Override
    public boolean canDrag() {
        return true;
    }

    /**
     * 搭配shouldDealBackKey使用
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 默认不自己处理返回按键
     *
     * @return
     */
    @Override
    public boolean shouldDealBackKey() {
        return false;
    }

    @Override
    public void onEnterBackground() {
        if (!isNormalMode() && mRootView != null) {
            mRootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEnterForeground() {
        if (!isNormalMode() && mRootView != null) {
            mRootView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onMove(int x, int y, int dx, int dy) {
        if (!canDrag()) {
            return;
        }
        if (isNormalMode()) {
            mFrameLayoutParams.leftMargin += dx;
            mFrameLayoutParams.topMargin += dy;
            //更新图标位置
            updateViewLayout(mTag, false);
        } else {
            mWindowLayoutParams.x += dx;
            mWindowLayoutParams.y += dy;
            mWindowManager.updateViewLayout(mRootView, mWindowLayoutParams);
        }

    }


    /**
     * 手指弹起时保存当前浮标位置
     *
     * @param x
     * @param y
     */
    @Override
    public void onUp(int x, int y) {
        if (!canDrag()) {
            return;
        }


        if (mTag.equals(MainIconDokitView.class.getSimpleName())) {
            if (isNormalMode()) {
                FloatIconConfig.saveLastPosX(mFrameLayoutParams.leftMargin);
                FloatIconConfig.saveLastPosY(mFrameLayoutParams.topMargin);
            } else {
                FloatIconConfig.saveLastPosX(mWindowLayoutParams.x);
                FloatIconConfig.saveLastPosY(mWindowLayoutParams.y);
            }

        } else {
            //保存在内存中
            if (isNormalMode()) {
                DokitViewManager.getInstance().saveDokitViewPos(mTag, mFrameLayoutParams.leftMargin, mFrameLayoutParams.topMargin);
            } else {
                DokitViewManager.getInstance().saveDokitViewPos(mTag, mWindowLayoutParams.x, mWindowLayoutParams.y);
            }
        }

    }

    /**
     * 手指按下时的操作
     *
     * @param x
     * @param y
     */
    @Override
    public void onDown(int x, int y) {
        if (!canDrag()) {
            return;
        }
    }


    /**
     * 广播接收器 系统悬浮窗需要调用
     */
    private class InnerReceiver extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        //点击home键
                        onHomeKeyPress();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        //点击menu按钮
                        onRecentAppKeyPress();
                    }
                }
            }
        }
    }

    /**
     * home键被点击 只有系统悬浮窗控件才会被调用
     */
    public void onHomeKeyPress() {

    }


    /**
     * 菜单键被点击 只有系统悬浮窗控件才会被调用
     */
    public void onRecentAppKeyPress() {

    }


    /**
     * 不能在改方法中进行dokitview的添加和删除 因为处于遍历过程在
     * 只有系统模式下才会调用
     *
     * @param dokitView
     */
    @Override
    public void onDokitViewAdd(AbsDokitView dokitView) {

    }


    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    /**
     * 系统悬浮窗需要调用
     *
     * @return
     */
    public Context getContext() {
        if (mRootView != null) {
            return mRootView.getContext();
        } else {
            return null;
        }
    }


    public Resources getResources() {
        if (getContext() == null) {
            return null;
        }
        return getContext().getResources();
    }

    public String getString(@StringRes int resId) {
        if (getContext() == null) {
            return null;
        }
        return getContext().getString(resId);
    }

    public boolean isShow() {
        return mRootView.isShown();
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    public View getRootView() {
        return mRootView;
    }

    public FrameLayout.LayoutParams getNormalLayoutParams() {
        return mFrameLayoutParams;
    }

    public WindowManager.LayoutParams getSystemLayoutParams() {
        return mWindowLayoutParams;
    }

    /**
     * 将当前dokitView于activity解绑
     */
    public void detach() {
        DokitViewManager.getInstance().detach(this);
    }

    /**
     * 操作DecorView的直接子布局
     * 测试专用
     */
    public void dealDecorRootView(FrameLayout decorRootView) {
        if (isNormalMode()) {
            if (decorRootView == null) {
                return;
            }
        }
    }

    /**
     * 更新view的位置
     *
     * @param isActivityResume 是否是从其他页面返回时更新的位置
     */
    public void updateViewLayout(String tag, boolean isActivityResume) {
        if (mRootView == null || mChildView == null || mFrameLayoutParams == null || !isNormalMode()) {
            return;
        }
        if (isActivityResume) {
            if (tag.equals(MainIconDokitView.class.getSimpleName())) {
                mFrameLayoutParams.leftMargin = FloatIconConfig.getLastPosX();
                mFrameLayoutParams.topMargin = FloatIconConfig.getLastPosY();
            } else {
                Point point = DokitViewManager.getInstance().getDokitViewPos(tag);
                if (point != null) {
                    mFrameLayoutParams.leftMargin = point.x;
                    mFrameLayoutParams.topMargin = point.y;
                }
            }
        } else {
            //非页面切换的时候保存当前位置信息
            mLastDokitViewPosInfo.setPortrait();
            mLastDokitViewPosInfo.setLeftMargin(mFrameLayoutParams.leftMargin);
            mLastDokitViewPosInfo.setTopMargin(mFrameLayoutParams.topMargin);
        }
        if (tag.equals(MainIconDokitView.class.getSimpleName())) {
            mFrameLayoutParams.width = DokitViewLayoutParams.WRAP_CONTENT;
            mFrameLayoutParams.height = DokitViewLayoutParams.WRAP_CONTENT;
//            mFrameLayoutParams.width = ConvertUtils.dp2px(MainIconDokitView.FLOAT_SIZE);
//            mFrameLayoutParams.height = ConvertUtils.dp2px(MainIconDokitView.FLOAT_SIZE);
        } else {
            mFrameLayoutParams.width = mDokitViewWidth;
            mFrameLayoutParams.height = mDokitViewHeight;
        }


        //portraitOrLandscape(mFrameLayoutParams);
        resetBorderline(mFrameLayoutParams);
        mRootView.setLayoutParams(mFrameLayoutParams);
    }

    /**
     * 限制边界 调用的时候必须保证是在控件能获取到宽高德前提下
     */
    private void resetBorderline(FrameLayout.LayoutParams normalFrameLayoutParams) {
        //如果是系统模式或者手动关闭动态限制边界
        if (!restrictBorderline() || !isNormalMode()) {
            return;
        }
        //LogHelper.i(TAG, "topMargin==>" + normalFrameLayoutParams.topMargin + "  leftMargin====>" + normalFrameLayoutParams.leftMargin);
        if (normalFrameLayoutParams.topMargin <= 0) {
            normalFrameLayoutParams.topMargin = 0;
        }

        if (ScreenUtils.isPortrait()) {
            if (normalFrameLayoutParams.topMargin >= getScreenLongSideLength() - mDokitViewHeight) {
                normalFrameLayoutParams.topMargin = getScreenLongSideLength() - mDokitViewHeight;
            }
        } else {
            if (normalFrameLayoutParams.topMargin >= getScreenShortSideLength() - mDokitViewHeight) {
                normalFrameLayoutParams.topMargin = getScreenShortSideLength() - mDokitViewHeight;
            }
        }


        if (normalFrameLayoutParams.leftMargin <= 0) {
            normalFrameLayoutParams.leftMargin = 0;
        }

        if (ScreenUtils.isPortrait()) {
            if (normalFrameLayoutParams.leftMargin >= getScreenShortSideLength() - mDokitViewWidth) {
                normalFrameLayoutParams.leftMargin = getScreenShortSideLength() - mDokitViewWidth;
            }
        } else {
            if (normalFrameLayoutParams.leftMargin >= getScreenLongSideLength() - mDokitViewWidth) {
                normalFrameLayoutParams.leftMargin = getScreenLongSideLength() - mDokitViewWidth;
            }
        }

    }


    /**
     * 是否限制布局边界
     *
     * @return
     */
    public boolean restrictBorderline() {
        return true;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String mTag) {
        this.mTag = mTag;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle mBundle) {
        this.mBundle = mBundle;
    }

    public Activity getActivity() {
        if (mAttachActivity != null) {
            return mAttachActivity.get();
        }
        return ActivityUtils.getTopActivity();
    }

    public void setActivity(Activity activity) {
        this.mAttachActivity = new WeakReference<>(activity);
    }

    public void post(Runnable r) {
        mHandler.post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        mHandler.postDelayed(r, delayMillis);
    }

    /**
     * 设置当前kitView不响应触摸事件
     * 控件默认响应触摸事件
     * 需要在子view的onViewCreated中调用
     */
    public void setDokitViewNotResponseTouchEvent(View view) {
        if (isNormalMode()) {
            if (view != null) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        } else {
            if (view != null) {
                view.setOnTouchListener(null);
            }
        }

    }


    /**
     * 获取屏幕短边的长度 不包含statusBar
     *
     * @return
     */
    public int getScreenShortSideLength() {
        if (ScreenUtils.isPortrait()) {
            return ScreenUtils.getAppScreenWidth();
        } else {
            return ScreenUtils.getAppScreenHeight();
        }
    }

    /**
     * 获取屏幕长边的长度 不包含statusBar
     *
     * @return
     */
    public int getScreenLongSideLength() {
        if (ScreenUtils.isPortrait()) {
            //ScreenUtils.getScreenHeight(); 包含statusBar
            //ScreenUtils.getAppScreenHeight(); 不包含statusBar
            return ScreenUtils.getAppScreenHeight();
        } else {
            return ScreenUtils.getAppScreenWidth();
        }
    }


    /**
     * 是否是普通的浮标模式
     *
     * @return
     */
    public boolean isNormalMode() {
        return DokitConstant.IS_NORMAL_FLOAT_MODE;
    }

    /**
     * 强制刷新当前dokitview
     */
    public void invalidate() {
        if (mRootView != null && mFrameLayoutParams != null) {
            mRootView.setLayoutParams(mFrameLayoutParams);
        }
    }


}
