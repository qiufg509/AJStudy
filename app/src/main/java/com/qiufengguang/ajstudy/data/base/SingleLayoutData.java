package com.qiufengguang.ajstudy.data.base;

/**
 * 包含单个对象的卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/29 12:19
 */
public class SingleLayoutData<T extends BaseCardBean> extends LayoutData<T> {
    public SingleLayoutData(T bean) {
        super(bean);
        prepareData(bean);
    }

    public SingleLayoutData(T bean, String cardTitle) {
        super(bean, cardTitle);
        prepareData(bean);
    }

    private void prepareData(T bean) {
        setLayoutName(bean.getLayoutName());
        setLayoutId(bean.getLayoutId());
    }


    @Override
    public boolean isCollection() {
        return false;
    }
}