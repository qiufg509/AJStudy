package com.qiufengguang.ajstudy.data;

import java.util.Locale;

public class DetailAppData {
    public static final String LAYOUT_NAME = "appdetaildatacard";

    String scoredBy;

    String stars;

    String downloads;

    String minAge;

    String downloadUnit;

    GradeInfo gradeInfo;

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
