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
import com.qiufengguang.ajstudy.data.DetailRecommend;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.FileSizeFormatter;
import com.qiufengguang.ajstudy.utils.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 详情页ViewModel
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
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

    /**
     * 推荐列表
     */
    private final MutableLiveData<List<DetailRecommend>> recommendations = new MutableLiveData<>();

    /**
     * 介绍信息
     */
    private final MutableLiveData<List<Introduction>> introduction = new MutableLiveData<>();

    /**
     * 默认选中评论tab
     */
    private final MutableLiveData<Integer> selectedTab = new MutableLiveData<>(1);

    public DetailViewModel() {
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

    public LiveData<List<DetailRecommend>> getRecommendations() {
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
            JSONObject pageObject;
            try {
                pageObject = new JSONObject(pageStr);
            } catch (JSONException e) {
                return;
            }
            JSONArray layoutData = pageObject.optJSONArray("layoutData");
            if (layoutData == null || layoutData.length() == 0) {
                return;
            }

            // 模拟介绍页数据
            loadIntroductionData();

            List<DetailRecommend> detailRecommends = new ArrayList<>();
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
                if (TextUtils.equals(layoutName, DetailRecommend.LAYOUT_NAME)) {
                    List<DetailRecommend> recommends = parseDetailRecommends(dataListObj);
                    detailRecommends.addAll(recommends);
                }
            }

            recommendations.postValue(detailRecommends);
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
            long fullSize = dataListObj.optLong("fullSize");
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

        private List<DetailRecommend> parseDetailRecommends(@NonNull JSONObject dataListObj) {
            JSONArray listArr = dataListObj.optJSONArray("list");
            if (listArr == null || listArr.isNull(0)) {
                return Collections.emptyList();
            }
            List<DetailRecommend> detailRecommends = new ArrayList<>();
            for (int pos = 0, sum = listArr.length(); pos < sum; pos++) {
                JSONObject commentObj = listArr.optJSONObject(pos);
                DetailRecommend recommend = new DetailRecommend();
                recommend.setIcon(commentObj.optString("icon"));
                recommend.setName(commentObj.optString("name"));
                String stars = commentObj.optString("stars");
                recommend.setStars(Float.parseFloat(stars));
                recommend.setScore(commentObj.optString("score"));
                recommend.setMemo(commentObj.optString("memo"));
                detailRecommends.add(recommend);
            }
            return detailRecommends;
        }
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

        introduction.postValue(introList);
    }


    public static class Introduction {
        private String title;
        private String content;

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