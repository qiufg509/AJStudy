package com.qiufengguang.ajstudy.data.model;

import java.util.Locale;

/**
 * 详情页应用数据bean
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailAppData {
    public static final String LAYOUT_NAME = "appdetaildatacard";

    String fullSize;

    String scoredBy;

    String stars;

    String downloads;

    String minAge;

    String downloadUnit;

    GradeInfo gradeInfo;

    public String getFullSize() {
        return fullSize;
    }

    public void setFullSize(String fullSize) {
        this.fullSize = fullSize;
    }

    public String getScoredBy() {
        return scoredBy;
    }

    public void setScoredBy(String scoredBy) {
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
