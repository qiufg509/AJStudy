package com.qiufengguang.ajstudy.dialog.manager;

import com.qiufengguang.ajstudy.dialog.Dialog;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 对话框管理类
 * 支持多个Dialog依次弹出
 *
 * @author qiufengguang
 * @since 2025/12/22 17:17
 */
public class DialogsManager {

    /**
     * 是否有dialog在展示
     */
    private volatile boolean showing = false;

    /**
     * dialog队列
     */
    private final ConcurrentLinkedQueue<DialogWrapper> dialogQueue = new ConcurrentLinkedQueue<>();

    private DialogsManager() {
    }

    public static DialogsManager getInstance() {
        return DialogHolder.instance;
    }

    private static class DialogHolder {
        private static final DialogsManager instance = new DialogsManager();
    }

    /**
     * 请求加入队列并展示
     *
     * @param dialogWrapper DialogWrapper
     */
    public synchronized void requestShow(DialogWrapper dialogWrapper) {
        dialogQueue.offer(dialogWrapper);
        checkAndDispatch();
    }

    /**
     * 结束一次展示 并且检查下一个弹窗
     */
    public synchronized void over() {
        showing = false;
        next();
    }

    private synchronized void checkAndDispatch() {
        if (!showing) {
            next();
        }
    }

    /**
     * 弹出下一个弹窗
     */
    private synchronized void next() {
        DialogWrapper poll = dialogQueue.poll();
        if (poll == null) {
            return;
        }
        Dialog.Builder dialog = poll.getDialog();
        if (dialog != null) {
            showing = true;
            dialog.show();
        }
    }
}
