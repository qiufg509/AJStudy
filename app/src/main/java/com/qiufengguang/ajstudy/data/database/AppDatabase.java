// data/database/AppDatabase.java
package com.qiufengguang.ajstudy.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.qiufengguang.ajstudy.data.dao.ChatMessageDao;
import com.qiufengguang.ajstudy.data.dao.ConversationDao;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.model.Conversation;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;

/**
 * AppDatabase
 *
 * @author qiufengguang
 * @since 2026/4/22 23:48
 */
@Database(
    entities = {ChatMessage.class, Conversation.class,},
    version = 1,
    exportSchema = false  // 生产环境建议设为true用于版本迁移
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    public abstract ChatMessageDao chatMessageDao();

    public abstract ConversationDao conversationDao();

    public static AppDatabase getInstance() {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase();
                }
            }
        }
        return instance;
    }

    private static AppDatabase buildDatabase() {
        Context context = GlobalApp.getContext();
        if (context == null) {
            return null;
        }
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                Constant.DATABASE_NAME
            )
            // 开发阶段：版本升级时重建（会丢失数据）
            .fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_1_2)
            .setQueryCallback((query, args) -> {
                // 开发阶段可打印SQL日志，便于调试
                // Log.d("RoomQuery", "SQL: " + query);
            }, java.util.concurrent.Executors.newSingleThreadExecutor())
            .build();
    }

    /**
     * 生产环境数据库迁移示例（从版本1升级到2）
     */
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // database.execSQL("ALTER TABLE notes ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0");
        }
    };
}