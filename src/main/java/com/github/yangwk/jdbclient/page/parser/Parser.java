package com.github.yangwk.jdbclient.page.parser;

import com.github.yangwk.jdbclient.page.Page;

/**
 * 处理SQL
 *
 */
public interface Parser {

    /**
     * 获取总数sql - 如果要支持其他数据库，修改这里就可以
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getCountSql(String sql);

    /**
     * 获取分页sql - 如果要支持其他数据库，修改这里就可以
     *
     * @param sql 原查询sql
     * @param page
     * @return 返回分页sql
     */
    public String getPageSql(String sql,Page page);

    /**
     * 设置分页参数
     * @param paras
     * @param page
     */
    public Object[] getPageParameter(Object[] paras, Page page);
}
