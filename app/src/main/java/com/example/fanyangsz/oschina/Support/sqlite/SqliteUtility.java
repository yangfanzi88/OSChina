package com.example.fanyangsz.oschina.Support.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.example.fanyangsz.oschina.Support.sqlite.extra.AutoIncrementTableColumn;
import com.example.fanyangsz.oschina.Support.sqlite.extra.Extra;
import com.example.fanyangsz.oschina.Support.sqlite.extra.TableColumn;
import com.example.fanyangsz.oschina.Support.sqlite.extra.TableInfo;
import com.example.fanyangsz.oschina.Support.sqlite.utils.SqlUtils;
import com.example.fanyangsz.oschina.Support.sqlite.utils.TableInfoUtils;
import com.example.fanyangsz.oschina.Support.util.Logger;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;


/**
 * 面向对象的数据库操作帮助类，支持App内部、Sdcard建库，dbName表示不同的库<br/>
 * DB版本升级，即version发生升级的时候，默认清空数据库<br/>
 * DB版本不变，Class字段增加字段时，Table会自动增加相对应的一列，Sqlite不支持Table的自动删除操作。<br/>
 *
 * @author wangdan
 */
public class SqliteUtility {

    public static final String TAG = "SqliteUtility";

    private static Hashtable<String, SqliteUtility> dbCache = new Hashtable<String, SqliteUtility>();

    private SQLiteDatabase db;

    SqliteUtility(String dbName, SQLiteDatabase db) {
        this.db = db;

        dbCache.put(dbName, this);
        Logger.d(TAG, "将库 %s 放到缓存中", dbName);
    }

    public static SqliteUtility getInstance() {
        return getInstance(SqliteUtilityBuilder.DEFAULT_DB);
    }

    public static SqliteUtility getInstance(String dbName) {
        return dbCache.get(dbName);
    }

    /**
     * ****************************************开始Select系列方法***************************************************
     */

    public <T> T selectById(Extra extra, Class<T> clazz, Object id) {
        try {
            TableInfo tableInfo = checkTable(clazz);

            String selection = String.format(" %s = ? ", tableInfo.getPrimaryKey().getColumn());
            String extraSelection = SqlUtils.appendExtraWhereClause(extra);
            if (!TextUtils.isEmpty(extraSelection))
                selection = String.format("%s and %s", selection, extraSelection);

            List<String> selectionArgList = new ArrayList<String>();
            selectionArgList.add(String.valueOf(id));
            String[] extraSelectionArgs = SqlUtils.appendExtraWhereArgs(extra);
            if (extraSelectionArgs != null && extraSelectionArgs.length > 0)
                selectionArgList.addAll(Arrays.asList(extraSelectionArgs));
            String[] selectionArgs = selectionArgList.toArray(new String[0]);

            List<T> list = select(clazz, selection, selectionArgs, null, null, null, null);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            printStackTrace(e);
        }

        return null;
    }

    public <T> List<T> select(Extra extra, Class<T> clazz) {
        String selection = SqlUtils.appendExtraWhereClause(extra);
        String[] selectionArgs = SqlUtils.appendExtraWhereArgs(extra);

        return select(clazz, selection, selectionArgs, null, null, null, null);
    }

    public <T> List<T> select(Class<T> clazz, String selection, String[] selectionArgs) {
        return select(clazz, selection, selectionArgs, null, null, null, null);
    }

    public <T> List<T> select(Class<T> clazz, String selection,
                              String[] selectionArgs, String groupBy, String having,
                              String orderBy, String limit) {
        TableInfo tableInfo = checkTable(clazz);

        ArrayList<T> list = new ArrayList<T>();

        if (Logger.DEBUG) {
            Logger.d(TAG, " method[select], table[%s], selection[%s], selectionArgs%s, groupBy[%s], having[%s], orderBy[%s], limit[%s] ",
                    tableInfo.getTableName(), selection, new Gson().toJson(selectionArgs), String.valueOf(groupBy), String.valueOf(having), String.valueOf(orderBy), String.valueOf(limit));
        }

        List<String> columnList = new ArrayList<String>();
        columnList.add(tableInfo.getPrimaryKey().getColumn());
        for (TableColumn tableColumn : tableInfo.getColumns())
            columnList.add(tableColumn.getColumn());

        long start = System.currentTimeMillis();
        Cursor cursor = db.query(tableInfo.getTableName(), columnList.toArray(new String[0]),
                selection, selectionArgs, groupBy, having, orderBy, limit);
        Logger.d(TAG, "table[%s] 查询数据结束，耗时 %s ms", tableInfo.getTableName(), String.valueOf(System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        try {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        T entity = clazz.newInstance();

                        // 绑定主键
                        bindSelectValue(entity, cursor, tableInfo.getPrimaryKey());

                        // 绑定其他数据
                        for (TableColumn column : tableInfo.getColumns())
                            bindSelectValue(entity, cursor, column);

                        list.add(entity);
                    } catch (Exception e) {
                        printStackTrace(e);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        Logger.d(TAG, "table[%s], 设置数据结束，耗时 %s ms", tableInfo.getTableName(), String.valueOf(System.currentTimeMillis() - start));

        Logger.d(TAG, "查询到数据 %d 条", list.size());

        return list;
    }

    /**
     * ****************************************开始Insert系列方法***************************************************
     */

    public <T> void insert(Extra extra, T... entities) {
        try {
            if (entities != null && entities.length > 0)
                insert(extra, Arrays.asList(entities));
            else
                Logger.d(TAG, "method[insert(Extra extra, T... entities)], entities is null or empty");
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public <T> void insertOrReplace(Extra extra, T... entities) {
        try {
            if (entities != null && entities.length > 0)
                insert(extra, Arrays.asList(entities), "INSERT OR REPLACE INTO ");
            else
                Logger.d(TAG, "method[insertOrReplace(Extra extra, T... entities)], entities is null or empty");
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public <T> void insert(Extra extra, List<T> entityList) {
        try {
            insert(extra, entityList, "INSERT OR IGNORE INTO ");
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public <T> void insertOrReplace(Extra extra, List<T> entityList) {
        try {
            insert(extra, entityList, "INSERT OR REPLACE INTO ");
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    private <T> void insert(Extra extra, List<T> entityList, String insertInto) {
        if (entityList == null || entityList.size() == 0) {
            Logger.d(TAG, "method[insert(Extra extra, List<T> entityList)], entityList is null or empty");
            return;
        }

        TableInfo tableInfo = checkTable(entityList.get(0).getClass());

        long start = System.currentTimeMillis();
        db.beginTransaction();
        try {
            String sql = SqlUtils.createSqlInsert(insertInto, tableInfo);

            Logger.v(TAG, insertInto + " sql = %s", sql);

            SQLiteStatement insertStatement = db.compileStatement(sql);
            long bindTime = 0;
            long startTime = System.currentTimeMillis();
            for (T entity : entityList) {
                bindInsertValues(extra, insertStatement, tableInfo, entity);
                bindTime += (System.currentTimeMillis() - startTime);
                startTime = System.currentTimeMillis();
                insertStatement.execute();
            }
            Logger.d(TAG, "bindvalues 耗时 %s ms", bindTime + "");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        Logger.d(TAG, "表 %s %s 数据 %d 条， 执行时间 %s ms",
                tableInfo.getTableName(),
                insertInto,
                entityList.size(),
                String.valueOf(System.currentTimeMillis() - start));
    }

    /**
     * ****************************************开始Update系列方法***************************************************
     */
    public <T> int update(Class<T> clazz, ContentValues values, String whereClause, String[] whereArgs) {
        try {
            TableInfo tableInfo = checkTable(clazz);

            return db.update(tableInfo.getTableName(), values, whereClause, whereArgs);
        } catch (Exception e) {
            printStackTrace(e);
        }

        return -1;
    }

    public <T> void update(Extra extra, T... entities) {
        try {
            if (entities != null && entities.length > 0)
                insertOrReplace(extra, entities);
            else
                Logger.d(TAG, "method[update(Extra extra, T... entities)], entities is null or empty");
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public <T> void update(Extra extra, List<T> entityList) {
        try {
            if (entityList != null && entityList.size() > 0)
                insertOrReplace(extra, entityList);
            else
                Logger.d(TAG, "method[update(Extra extra, T... entities)], entities is null or empty");
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /**
     * ****************************************开始Delete系列方法***************************************************
     */

    public <T> void deleteAll(Extra extra, Class<T> clazz) {
        try {
            TableInfo tableInfo = checkTable(clazz);

            String where = SqlUtils.appendExtraWhereClauseSql(extra);
            if (!TextUtils.isEmpty(where))
                where = " where " + where;
            String sql = "DELETE FROM '" + tableInfo.getTableName() + "' " + where;

            Logger.d(TAG, "method[delete] table[%s], sql[%s]", tableInfo.getTableName(), sql);

            long start = System.currentTimeMillis();
            db.execSQL(sql);
            Logger.d(TAG, "表 %s 清空数据, 耗时 %s ms", tableInfo.getTableName(), String.valueOf(System.currentTimeMillis() - start));
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public <T> void deleteById(Extra extra, Class<T> clazz, Object id) {
        try {
            TableInfo tableInfo = checkTable(clazz);

            String whereClause = String.format(" %s = ? ", tableInfo.getPrimaryKey().getColumn());
            String extraWhereClause = SqlUtils.appendExtraWhereClause(extra);
            if (!TextUtils.isEmpty(extraWhereClause))
                whereClause = String.format("%s and %s", whereClause, extraWhereClause);

            List<String> whereArgList = new ArrayList<String>();
            whereArgList.add(String.valueOf(id));
            String[] extraWhereArgs = SqlUtils.appendExtraWhereArgs(extra);
            if (extraWhereArgs != null && extraWhereArgs.length > 0)
                whereArgList.addAll(Arrays.asList(extraWhereArgs));
            String[] whereArgs = whereArgList.toArray(new String[0]);

            if (Logger.DEBUG) {
                Logger.d(TAG, " method[deleteById], table[%s], id[%s], whereClause[%s], whereArgs%s ",
                        tableInfo.getTableName(), String.valueOf(id), whereClause, new Gson().toJson(whereArgs));
            }

            long start = System.currentTimeMillis();
            int rowCount = db.delete(tableInfo.getTableName(), whereClause, whereArgs);

            Logger.d(TAG, "表 %s 删除数据 %d 条, 耗时 %s ms", tableInfo.getTableName(), rowCount, String.valueOf(System.currentTimeMillis() - start));
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    public <T> void delete(Class<T> clazz, String whereClause, String[] whereArgs) {
        try {
            TableInfo tableInfo = checkTable(clazz);

            long start = System.currentTimeMillis();
            int rowCount = db.delete(tableInfo.getTableName(), whereClause, whereArgs);

            if (Logger.DEBUG) {
                Logger.d(TAG, "method[delete], table[%s], whereClause[%s], whereArgs%s ",
                        tableInfo.getTableName(), whereClause, new Gson().toJson(whereArgs));
            }
            Logger.d(TAG, "表 %s 删除数据 %d 条，耗时 %s ms", tableInfo.getTableName(), rowCount, String.valueOf(System.currentTimeMillis() - start));
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /**
     * ****************************************系列绑定数据的方法***************************************************
     */

    private <T> void bindInsertValues(Extra extra, SQLiteStatement insertStatement, TableInfo tableInfo, T entity) {
        int index = 1;

        // 如果是自增主键，不设置值
        if (tableInfo.getPrimaryKey() instanceof AutoIncrementTableColumn)
            ;
        else
            bindInsertValue(insertStatement, index++, tableInfo.getPrimaryKey(), entity);

        for (int i = 0; i < tableInfo.getColumns().size(); i++) {
            TableColumn column = tableInfo.getColumns().get(i);
            bindInsertValue(insertStatement, index++, column, entity);
        }

        // owner
        String owner = extra == null || TextUtils.isEmpty(extra.getOwner()) ? "" : extra.getOwner();
        insertStatement.bindString(index++, owner);
        // key
        String key = extra == null || TextUtils.isEmpty(extra.getKey()) ? "" : extra.getKey();
        insertStatement.bindString(index++, key);
        // createAt
        long createAt = System.currentTimeMillis();
        insertStatement.bindLong(index, createAt);
    }

    private <T> void bindInsertValue(SQLiteStatement insertStatement, int index, TableColumn column, T entity) {
        // 通过反射绑定数据
        try {
            column.getField().setAccessible(true);
            Object value = column.getField().get(entity);
            if (value == null) {
                insertStatement.bindNull(index);
                return;
            }

            if ("object".equalsIgnoreCase(column.getDataType())) {
                insertStatement.bindString(index, new Gson().toJson(value));
            } else if ("INTEGER".equalsIgnoreCase(column.getColumnType())) {
                insertStatement.bindLong(index, Long.parseLong(value.toString()));
            } else if ("REAL".equalsIgnoreCase(column.getColumnType())) {
                insertStatement.bindDouble(index, Double.parseDouble(value.toString()));
            } else if ("BLOB".equalsIgnoreCase(column.getColumnType())) {
                insertStatement.bindBlob(index, (byte[]) value);
            } else if ("TEXT".equalsIgnoreCase(column.getColumnType())) {
                insertStatement.bindString(index, value.toString());
            }
        } catch (Exception e) {
            printStackTrace(e);

            Logger.w(TAG, "属性 %s bindvalue 异常", column.getField().getName());
        }
    }

    private <T> void bindSelectValue(T entity, Cursor cursor, TableColumn column) {
        Field field = column.getField();
        field.setAccessible(true);

        try {
            if (field.getType().getName().equals("int") ||
                    field.getType().getName().equals("java.lang.Integer")) {
                field.set(entity, cursor.getInt(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("long") ||
                    field.getType().getName().equals("java.lang.Long")) {
                field.set(entity, cursor.getLong(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("float") ||
                    field.getType().getName().equals("java.lang.Float")) {
                field.set(entity, cursor.getFloat(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("double") ||
                    field.getType().getName().equals("java.lang.Double")) {
                field.set(entity, cursor.getDouble(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("boolean") ||
                    field.getType().getName().equals("java.lang.Boolean")) {
                field.set(entity, Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(column.getColumn()))));
            } else if (field.getType().getName().equals("char") ||
                    field.getType().getName().equals("java.lang.Character")) {
                field.set(entity, cursor.getString(cursor.getColumnIndex(column.getColumn())).toCharArray()[0]);
            } else if (field.getType().getName().equals("byte") ||
                    field.getType().getName().equals("java.lang.Byte")) {
                field.set(entity, (byte) cursor.getInt(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("short") ||
                    field.getType().getName().equals("java.lang.Short")) {
                field.set(entity, cursor.getShort(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("java.lang.String")) {
                field.set(entity, cursor.getString(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("[B")) {
                field.set(entity, cursor.getBlob(cursor.getColumnIndex(column.getColumn())));
            } else {
                String text = cursor.getString(cursor.getColumnIndex(column.getColumn()));
                field.set(entity, new Gson().fromJson(text, field.getGenericType()));
            }
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /**
     * 检查table是否已经存在<br/>
     * 不存在，就自动创建<br/>
     * 存在，检查Entity字段是否有增加，有则更新表<br/>
     *
     * @param clazz
     */
    private <T> TableInfo checkTable(Class<T> clazz) {
        TableInfo tableInfo = TableInfoUtils.exist(clazz);
        if (tableInfo != null) {
        } else {
            tableInfo = TableInfoUtils.newTable(db, clazz);
        }

        return tableInfo;
    }

    private void printStackTrace(Exception e) {
        e.printStackTrace();
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }

}
