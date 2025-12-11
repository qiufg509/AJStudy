package com.qiufengguang.ajstudy.data;

import java.util.List;

/**
 * 详情页头部数据
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailHead {
    public static final String LAYOUT_NAME = "detailheadcard";

    String name;

    String icoUri;

    String tariffDesc;

    List<LabelName> labelNames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcoUri() {
        return icoUri;
    }

    public void setIcoUri(String icoUri) {
        this.icoUri = icoUri;
    }

    public String getTariffDesc() {
        return tariffDesc;
    }

    public void setTariffDesc(String tariffDesc) {
        this.tariffDesc = tariffDesc;
    }

    public String getLabelNames() {
        if (labelNames == null || labelNames.isEmpty()) {
            return "";
        }
        StringBuilder labelNameSb = new StringBuilder();
        for (int index = 0, sum = labelNames.size(); index < sum; index++) {
            labelNameSb.append(labelNames.get(index).getName());
            if (index != 0 && index == sum - 1) {
                labelNameSb.append(" · ");
            }
        }
        return labelNameSb.toString();
    }

    public void setLabelNames(List<LabelName> labelNames) {
        this.labelNames = labelNames;
    }

    public static class LabelName {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
