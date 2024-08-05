package com.liuyuncen.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserTableDef extends TableDef {

    /**
     * @belongsProject: redis-module-study-yun
 @belongsPackage: com.liuyuncen.entities
 @author: Xiang想
 @createTime: 2024-08-05  14:34
 @description: TODO
 @version: 1.0
     */
    public static final UserTableDef USER = new UserTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn AGE = new QueryColumn(this, "age");

    public final QueryColumn CITY = new QueryColumn(this, "city");

    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, AGE, CITY, PASSWORD, USERNAME};

    public UserTableDef() {
        super("", "r_user");
    }

    private UserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserTableDef("", "r_user", alias));
    }

}
