package com.qiufengguang.ajstudy.activity.detail;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.DetailAppData;
import com.qiufengguang.ajstudy.data.DetailComment;
import com.qiufengguang.ajstudy.data.DetailHead;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.FileSizeFormatter;
import com.qiufengguang.ajstudy.utils.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailViewModel extends ViewModel {
    private static final String TAG = "DetailViewModel";

    /**
     * 头部信息
     */
    private final MutableLiveData<DetailHead> detailHead = new MutableLiveData<>();

    /**
     * 应用信息
     */
    private final MutableLiveData<DetailAppData> appData = new MutableLiveData<>();

    /**
     * 评论信息
     */
    private final MutableLiveData<List<DetailComment>> comments = new MutableLiveData<>();

    private MutableLiveData<List<Recommendation>> recommendations;
    private MutableLiveData<List<Introduction>> introduction;

    /**
     * 默认选中评论tab
     */
    private final MutableLiveData<Integer> selectedTab = new MutableLiveData<>(1);

    public DetailViewModel() {
        recommendations = new MutableLiveData<>();
        introduction = new MutableLiveData<>();
        initData();
    }

    public LiveData<DetailHead> getDetailHead() {
        return detailHead;
    }

    public LiveData<DetailAppData> getAppData() {
        return appData;
    }

    public LiveData<List<DetailComment>> getComments() {
        return comments;
    }

    public LiveData<List<Recommendation>> getRecommendations() {
        return recommendations;
    }

    public LiveData<List<Introduction>> getIntroduction() {
        return introduction;
    }

    public LiveData<Integer> getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(int position) {
        selectedTab.setValue(position);
    }

    private void initData() {
        HandlerThread handlerThread = new HandlerThread(TAG + "-Thread");
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new ParsePageDataTask());
    }

    private class ParsePageDataTask implements Runnable {

        @Override
        public void run() {
            String pageStr = FileUtil.readAssetsToString(GlobalApp.getContext(),
                Constant.DETAIL_PAGE_FILE);
            JSONObject pageObject = null;
            try {
                pageObject = new JSONObject(pageStr);
            } catch (JSONException e) {
                return;
            }
            JSONArray layoutData = pageObject.optJSONArray("layoutData");
            if (layoutData == null || layoutData.length() == 0) {
                return;
            }
            for (int index = 0, size = layoutData.length(); index < size; index++) {
                JSONObject jsonObject = layoutData.optJSONObject(index);
                JSONArray dataList = jsonObject.optJSONArray("dataList");
                String layoutName = jsonObject.optString("layoutName");
                if (dataList == null || dataList.isNull(0)) {
                    continue;
                }
                JSONObject dataListObj = dataList.optJSONObject(0);
                if (TextUtils.equals(layoutName, DetailHead.LAYOUT_NAME)) {
                    parseDetailHead(dataListObj);
                }
                if (TextUtils.equals(layoutName, DetailAppData.LAYOUT_NAME)) {
                    parseDetailAppData(dataListObj);
                }
                if (TextUtils.equals(layoutName, DetailComment.LAYOUT_NAME)) {
                    parseDetailComments(dataListObj);
                }
            }
        }

        private void parseDetailHead(@NonNull JSONObject dataListObj) {
            DetailHead head = new DetailHead();
            head.setName(dataListObj.optString("name"));
            head.setIcoUri(dataListObj.optString("icoUri"));
            head.setTariffDesc(dataListObj.optString("tariffDesc"));
            DetailHead.LabelName labelName = new DetailHead.LabelName();
            JSONArray labelNamesArr = dataListObj.optJSONArray("labelNames");
            if (labelNamesArr != null && !labelNamesArr.isNull(0)) {
                labelName.setName(labelNamesArr.optJSONObject(0).optString("name"));
                List<DetailHead.LabelName> labelNames = new ArrayList<>();
                labelNames.add(labelName);
                head.setLabelNames(labelNames);
            }
            detailHead.postValue(head);
        }

        private void parseDetailAppData(@NonNull JSONObject dataListObj) {
            DetailAppData data = new DetailAppData();
            long fullSize = dataListObj.optLong("setFullSize");
            String appSize = FileSizeFormatter.format(fullSize);
            data.setFullSize(appSize);
            data.setDownloads(dataListObj.optString("downloads"));
            data.setDownloadUnit(dataListObj.optString("downloadUnit"));
            data.setStars(dataListObj.optString("stars"));
            long scoredBy = dataListObj.optLong("scoredBy");
            long scoreDivide = scoredBy / 10000;
            data.setScoredBy(scoreDivide > 0 ? (scoreDivide + "万 人评论") : (scoredBy % 10000 + " 人评论"));
            data.setMinAge(dataListObj.optInt("minAge") + "+");
            DetailAppData.GradeInfo gradeInfo = new DetailAppData.GradeInfo();
            JSONObject gradeInfoObj = dataListObj.optJSONObject("gradeInfo");
            if (gradeInfoObj != null) {
                gradeInfo.setGradeDesc(gradeInfoObj.optString("gradeDesc"));
            }
            data.setGradeInfo(gradeInfo);
            appData.postValue(data);
        }

        private void parseDetailComments(@NonNull JSONObject dataListObj) {
            JSONArray listArr = dataListObj.optJSONArray("list");
            if (listArr == null || listArr.isNull(0)) {
                return;
            }
            JSONArray commentList = listArr.optJSONObject(0).optJSONArray("commentList");
            if (commentList == null || commentList.length() == 0) {
                return;
            }
            List<DetailComment> detailComments = new ArrayList<>();
            for (int pos = 0, sum = commentList.length(); pos < sum; pos++) {
                JSONObject commentObj = commentList.optJSONObject(pos);
                DetailComment comment = new DetailComment();
                comment.setAvatar(commentObj.optString("avatar"));
                comment.setCommentInfo(commentObj.optString("commentInfo"));
                comment.setCommentTime(commentObj.optString("commentTime"));
                comment.setNickName(commentObj.optString("nickName"));
                comment.setStars(commentObj.optString("stars"));
                detailComments.add(comment);
            }
            comments.postValue(detailComments);
        }
    }

    public void loadRecommendationData() {
        // 模拟加载推荐数据
        List<Recommendation> recommendationList = new ArrayList<>();

        String[] appNames = {"腾讯视频", "爱奇艺", "优酷", "抖音", "快手", "B站", "芒果TV", "搜狐视频"};
        String[] descriptions = {"精彩视频内容", "高清画质", "会员专享", "短视频平台", "直播娱乐", "弹幕视频", "综艺影视", "热门影视"};

        for (int i = 0; i < 15; i++) {
            Recommendation rec = new Recommendation();
            rec.setAppName(appNames[i % appNames.length] + " v" + (i + 1));
            rec.setDescription(descriptions[i % descriptions.length]);
            rec.setRating(3.5f + (i % 3) * 0.3f);
            rec.setInstalls("1000万+" + i);
            rec.setIconResId(android.R.drawable.ic_menu_gallery + i);
            recommendationList.add(rec);
        }

        recommendations.setValue(recommendationList);
    }

    public void loadIntroductionData() {
        // 模拟加载介绍数据
        List<Introduction> introList = new ArrayList<>();

        Introduction intro1 = new Introduction();
        intro1.setTitle("应用特色");
        intro1.setContent("好看视频致力于为用户提供高质量的视频内容，涵盖娱乐、教育、生活、科技等多个领域。");
        introList.add(intro1);

        Introduction intro2 = new Introduction();
        intro2.setTitle("主要功能");
        intro2.setContent("• 短视频播放与分享\n• 直播观看与互动\n• 个性化推荐算法\n• 高清视频播放");
        introList.add(intro2);

        Introduction intro3 = new Introduction();
        intro3.setTitle("使用说明");
        intro3.setContent("1. 注册登录账户\n2. 选择感兴趣的内容分类\n3. 观看视频或直播\n4. 发表评论和分享");
        introList.add(intro3);

        introduction.setValue(introList);
    }


    public static class Recommendation {
        private String appName;
        private String description;
        private float rating;
        private String installs;
        private int iconResId;

        // Getters and Setters
        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public float getRating() {
            return rating;
        }

        public void setRating(float rating) {
            this.rating = rating;
        }

        public String getInstalls() {
            return installs;
        }

        public void setInstalls(String installs) {
            this.installs = installs;
        }

        public int getIconResId() {
            return iconResId;
        }

        public void setIconResId(int iconResId) {
            this.iconResId = iconResId;
        }
    }

    public static class Introduction {
        private String title;
        private String content;

        // Getters and Setters
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}