package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Locale;

/**
 * 详情页应用数据bean
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailAppData extends BaseCardBean {
    public static final int LAYOUT_ID = 101;

    long fullSize;

    long scoredBy;

    String stars;

    String downloads;

    String minAge;

    String downloadUnit;

    GradeInfo gradeInfo;

    public long getFullSize() {
        return fullSize;
    }

    public void setFullSize(long fullSize) {
        this.fullSize = fullSize;
    }

    public long getScoredBy() {
        return scoredBy;
    }

    public void setScoredBy(long scoredBy) {
        this.scoredBy = scoredBy;
    }

    public String getStars() {
        return String.format(Locale.getDefault(), "%s ★", stars);
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getDownloads() {
        return String.format(Locale.getDefault(), getDownloadUnit(), downloads);
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getDownloadUnit() {
        return downloadUnit;
    }

    public void setDownloadUnit(String downloadUnit) {
        this.downloadUnit = downloadUnit;
    }

    public GradeInfo getGradeInfo() {
        return gradeInfo;
    }

    public void setGradeInfo(GradeInfo gradeInfo) {
        this.gradeInfo = gradeInfo;
    }

    public static class GradeInfo {
        String gradeDesc;

        public String getGradeDesc() {
            return gradeDesc;
        }

        public void setGradeDesc(String gradeDesc) {
            this.gradeDesc = gradeDesc;
        }
    }
}
