package com.fy.fayou;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.fy.fayou.common.ARoute;
import com.fy.fayou.common.ApiUrl;
import com.fy.fayou.detail.bean.DetailBean;
import com.fy.fayou.detail.bean.LawBean;
import com.fy.fayou.detail.dialog.BottomShareDialog;
import com.fy.fayou.detail.dialog.RelatedDialog;
import com.fy.fayou.event.ReportSuccessEvent;
import com.fy.fayou.legal.bean.LegalEntity;
import com.fy.fayou.legal.bean.LegalRelatedBean;
import com.fy.fayou.utils.ParseUtils;
import com.meis.base.mei.base.BaseActivity;
import com.meis.base.mei.utils.Eyes;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/fy/webview")
public class WebViewActivity extends BaseActivity {

    @Autowired(name = "url")
    public String url = "";

    @Autowired(name = "id")
    public String id = "";

    @Autowired(name = "detail")
    public boolean isDetail = false;

    @Autowired(name = "type")
    public int type = ARoute.ARTICLE_TYPE;

    @BindView(R.id.web_base)
    WebView webBase;
    @BindView(R.id.pb_web_base)
    ProgressBar pbWebBase;
    @BindView(R.id.iv_related)
    ImageView ivRelated;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.view_top)
    View viewTop;

    private long mBackPressed;
    private String webPath = "http://www.baidu.com";
    private static final int TIME_INTERVAL = 2000;
    private BottomShareDialog mShareDialog;

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);
        Eyes.setStatusBarColor(this, getResources().getColor(R.color.color_ffffff), true);
        setToolBarCenterTitle("用户服务协议及隐私政策");
        setLeftBackListener(v -> finish());
        if (isDetail) {
            setRightMoreListener(v -> {
                showBottomDialog();
            });
        }
    }

    private void showBottomDialog() {
        showDialog(mShareDialog = new BottomShareDialog()
                .setArticleId(id)
                .setGoneReport(true)
                .setCollectType(type)
                .setOnItemClickListener(new BottomShareDialog.OnItemClickListener() {
                    @Override
                    public void onDismiss() {
                    }

                    @Override
                    public void onCollect(boolean collected) {
                    }
                }));
    }

    @Override
    protected void initData() {
        setToolBarCenterTitle("");

        if (type == ARoute.JUDGE_TYPE || type == ARoute.GUIDE_TYPE
                || type == ARoute.LEGAL_TYPE || type == ARoute.JUDICIAL_TYPE) {
            ivRelated.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.VISIBLE);
        }

        if (type == ARoute.TEMPLATE_TYPE) {
            viewTop.setVisibility(View.VISIBLE);
            if (url.contains(",")) requestDownLoadCount();
        }

        if (!TextUtils.isEmpty(url)) {
            webPath = url;
        }

        WebSettings webSettings = webBase.getSettings();
        if (Build.VERSION.SDK_INT >= 19) {
            // 加载缓存否则网络
            // webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setLoadsImagesAutomatically(true); //图片自动缩放 打开
        } else {
            webSettings.setLoadsImagesAutomatically(false); //图片自动缩放 关闭
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webBase.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //软件解码
        }
        webBase.setLayerType(View.LAYER_TYPE_HARDWARE, null); //硬件解码

        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        // webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(true);// 设置可以支持缩放
        webSettings.setBuiltInZoomControls(true);// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false

        webSettings.setDisplayZoomControls(false);//隐藏缩放工具
        webSettings.setUseWideViewPort(true);// 扩大比例的缩放

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDatabaseEnabled(true);//
        webSettings.setSavePassword(true);//保存密码
        webSettings.setDomStorageEnabled(true);
        webBase.setSaveEnabled(true);
        webBase.setKeepScreenOn(true);


        // 设置setWebChromeClient对象
        webBase.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                setToolBarCenterTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                pbWebBase.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
        // 设置此方法可在WebView中打开链接，反之用浏览器打开
        webBase.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webBase.getSettings().getLoadsImagesAutomatically()) {
                    webBase.getSettings().setLoadsImagesAutomatically(true);
                }
                pbWebBase.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                pbWebBase.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return false;
                }


                // Otherwise allow the OS to handle things like tel, mailto, etc.
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
        webBase.setDownloadListener((paramAnonymousString1, paramAnonymousString2, paramAnonymousString3, paramAnonymousString4, paramAnonymousLong) -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setData(Uri.parse(paramAnonymousString1));
            startActivity(intent);
        });

        webBase.loadUrl(webPath);

        if (type != ARoute.ARTICLE_TYPE) {
            // 请求浏览记录
            requestScanRecord();
        }

        ivRelated.setOnClickListener(v -> {
            requestRelated();
        });

        tvCount.setOnClickListener(v -> {
            requestDownLoadUrl();
        });
    }

    /**
     * 请求下载
     */
    private void requestDownLoadUrl() {
        EasyHttp.downLoad(ApiUrl.GET_DOWNLOAD_URL + "?id=" + id +
                "&title=合同&type=2&informationOfParties=&signingClause=&contractTermIds=" + url.substring(url.lastIndexOf("ids=") + 4))
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                    }

                    @Override
                    public void onComplete(String path) {
                        Toast.makeText(mContext, "下载成功，期待分享", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(ApiException e) {
                        Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 获取下载次数
     */
    private void requestDownLoadCount() {
        EasyHttp.get(ApiUrl.GET_DOWNLOAD_COUNT)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            String count = ParseUtils.parseJSONObject(s, "limit");
                            tvCount.setText("你的下载次数剩余" + count + "次");
                            tvCount.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 请求关联接口
     */
    private void requestRelated() {
        switch (type) {
            case ARoute.LEGAL_TYPE:
                requestLegalRelated();
                break;
            case ARoute.JUDICIAL_TYPE:
                requestJudicialRelated();
                break;
            case ARoute.GUIDE_TYPE:
                requestCaseRelated();
                break;
            case ARoute.JUDGE_TYPE:
                requestJudgeRelated();
                break;
        }
    }

    /**
     * 请求司法解释关联
     */
    private void requestJudicialRelated() {
        EasyHttp.get(ApiUrl.GET_JUDICIAL_RELATED)
                .params("judicialInterpretationId", id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                        ParseUtils.handlerApiError(e, error -> {
                            Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (s.equals("[]")) {
                            Toast.makeText(mContext, "无关联数据", Toast.LENGTH_SHORT).show();
                        } else {
                            List<LawBean> data = ParseUtils.parseListData(s, LawBean.class);
                            if (!data.isEmpty()) {
                                List<LawBean> list = new ArrayList<>();
                                LawBean lawBean = new LawBean();
                                lawBean.name = "法律法规";
                                list.add(lawBean);
                                for (LawBean entity : data) {
                                    lawBean = new LawBean();
                                    lawBean.collectType = ARoute.LEGAL_TYPE;
                                    lawBean.name = entity.name;
                                    lawBean.itemType = 1;
                                    lawBean.url = entity.toUrl;
                                    lawBean.id = entity.id;
                                    list.add(lawBean);
                                }
                                new RelatedDialog(mContext, list).show();
                            }
                        }
                    }
                });
    }

    /**
     * 获取法律法规关联
     */
    private void requestLegalRelated() {
        EasyHttp.get(ApiUrl.GET_LEGAL_RELATED)
                .params("id", id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (TextUtils.isEmpty(s)) {
                            Toast.makeText(mContext, "无关联数据", Toast.LENGTH_SHORT).show();
                        } else {
                            LegalRelatedBean bean = ParseUtils.parseData(s, LegalRelatedBean.class);
                            List<LawBean> list = new ArrayList<>();
                            if (null != bean.judicialInterpretations && !bean.judicialInterpretations.isEmpty()) {
                                LawBean lawBean = new LawBean();
                                lawBean.name = "司法解释";
                                list.add(lawBean);
                                for (LegalEntity entity : bean.judicialInterpretations) {
                                    lawBean = new LawBean();
                                    lawBean.collectType = ARoute.JUDICIAL_TYPE;
                                    lawBean.name = entity.title;
                                    lawBean.itemType = 1;
                                    lawBean.url = entity.toUrl;
                                    lawBean.id = entity.id;
                                    list.add(lawBean);
                                }
                            }

                            if (null != bean.lawBindCaseAOs && !bean.lawBindCaseAOs.isEmpty()) {
                                LawBean lawBean = new LawBean();
                                lawBean.name = "指导性意见";
                                list.add(lawBean);
                                for (LegalEntity entity : bean.lawBindCaseAOs) {
                                    lawBean = new LawBean();
                                    lawBean.collectType = ARoute.GUIDE_TYPE;
                                    lawBean.name = entity.name;
                                    lawBean.itemType = 1;
                                    lawBean.url = entity.url;
                                    lawBean.id = entity.id;
                                    list.add(lawBean);
                                }
                            }

                            if (null != bean.lawBindJudgeAOs && !bean.lawBindJudgeAOs.isEmpty()) {
                                LawBean lawBean = new LawBean();
                                lawBean.name = "裁判文书";
                                list.add(lawBean);
                                for (LegalEntity entity : bean.lawBindJudgeAOs) {
                                    lawBean = new LawBean();
                                    lawBean.collectType = ARoute.JUDGE_TYPE;
                                    lawBean.name = entity.name;
                                    lawBean.itemType = 1;
                                    lawBean.url = entity.url;
                                    lawBean.id = entity.id;
                                    list.add(lawBean);
                                }
                            }

                            if (list.isEmpty()) {
                                Toast.makeText(mContext, "无关联数据", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            new RelatedDialog(mContext, list).show();
                        }
                    }
                });
    }

    /**
     * 请求指导性意见关联
     */
    private void requestCaseRelated() {
        EasyHttp.get(ApiUrl.GET_CASE_RELATED)
                .params("caseId", id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        parseCaseAndJudgeData(s, "判决文书", ARoute.JUDGE_TYPE);
                    }
                });
    }

    /**
     * 请求裁判文书关联
     */
    private void requestJudgeRelated() {
        EasyHttp.get(ApiUrl.GET_JUDGE_RELATED)
                .params("judgementId", id)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                        parseCaseAndJudgeData(s, "指导性意见", ARoute.GUIDE_TYPE);
                    }
                });
    }

    /**
     * @param s
     */
    private void parseCaseAndJudgeData(String s, String relatedTitle, int collectType) {
        if (!TextUtils.isEmpty(s)) {
            DetailBean bean = ParseUtils.parseData(s, DetailBean.class);
            List<LawBean> list = new ArrayList<>();
            if (bean.lawsUrl != null && !bean.lawsUrl.isEmpty()) {
                LawBean lawBean = new LawBean();
                lawBean.name = "法条";
                list.add(lawBean);

                for (LawBean lb : bean.lawsUrl) {
                    lawBean = new LawBean();
                    lawBean.collectType = ARoute.LEGAL_TYPE;
                    lawBean.name = lb.name;
                    lawBean.itemType = 1;
                    lawBean.url = lb.url;
                    lawBean.id = TextUtils.isEmpty(lb.url) ? "" : lb.url.substring(lb.url.lastIndexOf("/"));
                    list.add(lawBean);
                }
            }

            if (!TextUtils.isEmpty(bean.bindedId) || "0".equals(bean.bindedId)) {
                LawBean lawBean = new LawBean();
                lawBean.name = relatedTitle;
                list.add(lawBean);

                lawBean = new LawBean();
                lawBean.collectType = collectType;
                lawBean.itemType = 1;
                lawBean.name = bean.bindeTitle;
                lawBean.id = bean.bindedId;
                lawBean.url = bean.bindedUrl;
                list.add(lawBean);
            }

            if (list.isEmpty()) {
                Toast.makeText(mContext, "无关联数据", Toast.LENGTH_SHORT).show();
                return;
            }
            new RelatedDialog(mContext, list).show();

        }
    }

    private void requestScanRecord() {
        // 新增浏览记录
        HashMap<String, String> params = new HashMap<>();
        params.put("businessId", id);
        params.put("browseRecordType", ARoute.getCollectType(type));
        JSONObject jsonObject = new JSONObject(params);
        EasyHttp.post(ApiUrl.MY_HISTORY)
                .upJson(jsonObject.toString())
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(String s) {
                    }
                });
    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_fy_webview;
    }

    @Override
    protected void onSaveInstanceState(Bundle paramBundle) {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("url", webBase.getUrl());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        try {
            super.onConfigurationChanged(newConfig);
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.v("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE");
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.v("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT");
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (webBase.canGoBack()) {
            webBase.goBack();
        } else {
            super.onBackPressedSupport();
//            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
//                super.onBackPressedSupport();
//                return;
//            } else {
//                Toast.makeText(getBaseContext(), "再次点击返回键退出", Toast.LENGTH_SHORT).show();
//            }
//            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webBase.removeAllViews();
        webBase.destroy();
        webBase = null;
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReportSuccessEvent(ReportSuccessEvent event) {
        if (mShareDialog != null && mShareDialog.isAdded()) {
            mShareDialog.dismiss();
        }
    }
}
