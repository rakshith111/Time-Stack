package com.example.timestackarchitecture.casualmode.data;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StackDAO_Impl implements StackDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StackData> __insertionAdapterOfStackData;

  private final EntityDeletionOrUpdateAdapter<StackData> __deletionAdapterOfStackData;

  private final EntityDeletionOrUpdateAdapter<StackData> __updateAdapterOfStackData;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public StackDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStackData = new EntityInsertionAdapter<StackData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `stack_table` (`stack_id`,`stack_name`,`stack_time`,`stack_active`,`stack_is_playing`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StackData value) {
        stmt.bindLong(1, value.getId());
        if (value.getStackName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getStackName());
        }
        stmt.bindLong(3, value.getStackTime());
        final int _tmp = value.getActiveStack() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        final int _tmp_1 = value.isPlaying() ? 1 : 0;
        stmt.bindLong(5, _tmp_1);
      }
    };
    this.__deletionAdapterOfStackData = new EntityDeletionOrUpdateAdapter<StackData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `stack_table` WHERE `stack_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StackData value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfStackData = new EntityDeletionOrUpdateAdapter<StackData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `stack_table` SET `stack_id` = ?,`stack_name` = ?,`stack_time` = ?,`stack_active` = ?,`stack_is_playing` = ? WHERE `stack_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, StackData value) {
        stmt.bindLong(1, value.getId());
        if (value.getStackName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getStackName());
        }
        stmt.bindLong(3, value.getStackTime());
        final int _tmp = value.getActiveStack() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        final int _tmp_1 = value.isPlaying() ? 1 : 0;
        stmt.bindLong(5, _tmp_1);
        stmt.bindLong(6, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM stack_table";
        return _query;
      }
    };
  }

  @Override
  public Object insertStack(final StackData stack, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfStackData.insert(stack);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteStack(final StackData stack, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfStackData.handle(stack);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object updateStack(final StackData stack, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfStackData.handle(stack);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<StackData>> getStacks() {
    final String _sql = "SELECT * FROM stack_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"stack_table"}, new Callable<List<StackData>>() {
      @Override
      public List<StackData> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "stack_id");
          final int _cursorIndexOfStackName = CursorUtil.getColumnIndexOrThrow(_cursor, "stack_name");
          final int _cursorIndexOfStackTime = CursorUtil.getColumnIndexOrThrow(_cursor, "stack_time");
          final int _cursorIndexOfActiveStack = CursorUtil.getColumnIndexOrThrow(_cursor, "stack_active");
          final int _cursorIndexOfIsPlaying = CursorUtil.getColumnIndexOrThrow(_cursor, "stack_is_playing");
          final List<StackData> _result = new ArrayList<StackData>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final StackData _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpStackName;
            if (_cursor.isNull(_cursorIndexOfStackName)) {
              _tmpStackName = null;
            } else {
              _tmpStackName = _cursor.getString(_cursorIndexOfStackName);
            }
            final long _tmpStackTime;
            _tmpStackTime = _cursor.getLong(_cursorIndexOfStackTime);
            final boolean _tmpActiveStack;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfActiveStack);
            _tmpActiveStack = _tmp != 0;
            final boolean _tmpIsPlaying;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsPlaying);
            _tmpIsPlaying = _tmp_1 != 0;
            _item = new StackData(_tmpId,_tmpStackName,_tmpStackTime,_tmpActiveStack,_tmpIsPlaying);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
