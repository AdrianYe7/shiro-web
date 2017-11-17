package com.xyz.utils;

import com.xyz.common.ColumnProperties;
import com.xyz.constant.ConfigConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class JdbcHelper {
    private static final Logger logger = LoggerFactory.getLogger(JdbcHelper.class);

    private JdbcHelper() {}

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final ThreadLocal<Connection> CONNECTION = new ThreadLocal<>();

    static {
        Properties prop = PropertiesUtil.loadProps(ConfigConstant.CONFIG_FILE);
        DRIVER = prop.getProperty(ConfigConstant.JDBC_DRIVER_KEY);
        URL = prop.getProperty(ConfigConstant.JDBC_URL_KEY);
        USERNAME = prop.getProperty(ConfigConstant.JDBC_USERNAME_KEY);
        PASSWORD = prop.getProperty(ConfigConstant.JDBC_PASSWORD_KEY);

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            logger.error("can not load jdbc driver.", e);
        }
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static final<T> T querySingle(Class<T> entity, String sql, Object...params) {
        T t = null;
        ResultSet rs = null;
        try {
            rs = query(sql, params);
            int rows = getResultSetRows(rs);
            if(rows > 1)
                throw new SQLException("result row count more than 1!");
            List<ColumnProperties> columnProperties = getColumnProperties(rs);
            t = toEntity(entity, columnProperties, rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(CONNECTION.get());
        }
        return t;
    }

    public static final <T> List<T> queryList(Class<T> entity, String sql, Object...params) {
        List<T> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            rs = query(sql, params);
            int rows = getResultSetRows(rs);
            List<ColumnProperties> columnProperties = getColumnProperties(rs);
            for(int i = 0; i < rows; i++) {
                T t = toEntity(entity, columnProperties, rs);
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(CONNECTION.get());
            CONNECTION.remove();
        }
        return list;
    }

    public static final void insert(String sql, Object...params) {
        try {
            dml(sql, params);
        } catch (SQLException e) {
            logger.error("insert error.", e);
        }
    }

    public static final void update(String sql, Object...params) {
        try {
            dml(sql, params);
        } catch (SQLException e) {
            logger.error("update error.", e);
        }
    }

    private static final void delete(String sql, Object...params) {
        try {
            dml(sql, params);
        } catch (SQLException e) {
            logger.error("delete error.", e);
        }
    }

    private static final void dml(String sql, Object...params) throws SQLException {
        PreparedStatement ps = null;
        try {
            Connection conn = getConnection();
            CONNECTION.set(conn);
            ps = conn.prepareStatement(sql);
            for(int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new SQLException(e);
        } finally {
            close(CONNECTION.get(), ps);
            CONNECTION.remove();
        }
    }

    private static int getResultSetRows(ResultSet rs) throws SQLException {
        rs.last();
        int rows = rs.getRow();
        rs.beforeFirst();
        return rows;
    }

    private static List<ColumnProperties> getColumnProperties(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        List<ColumnProperties> columnProperties = ColumnProperty.convertRSColumnToProperty(metaData);
        return columnProperties;
    }

    private static ResultSet query(String sql, Object...params) throws SQLException{
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection conn = getConnection();
        CONNECTION.set(conn);
        ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        rs = ps.executeQuery();
        return rs;
    }

    private static<T> T toEntity(Class<T> entity, List<ColumnProperties> columnProperties, ResultSet rs) {
        T t = null;
        try {
            rs.next();
            String simpleName = entity.getSimpleName();
            switch (simpleName) {
                case "String":
                    t = (T)rs.getString(1);
                    return t;
                case "Integer" :
                case "int" :
                    t = (T)(Integer)rs.getInt(1);
                    return t;
                case "Long" :
                case "long" :
                    t = (T)(Long)rs.getLong(1);
                    return t;
                case "Double" :
                case "double":
                    t = (T)(Double)rs.getDouble(1);
                    return t;
                case "Float" :
                case "float" :
                    t = (T)(Float)rs.getFloat(1);
                    return t;
                case "Byte" :
                case "byte" :
                    t = (T)(Byte)rs.getByte(1);
                    return t;
                case "Short":
                case "short":
                    t = (T)(Short)rs.getShort(1);
                case "Boolean" :
                case "boolean" :
                    t = (T)(Boolean)rs.getBoolean(1);
                    return t;
                default:
                    t = entity.newInstance();
            }
            for(ColumnProperties prop : columnProperties) {
                PropertyDescriptor pd = new PropertyDescriptor(prop.getPropertyName(), entity);
                Method setMethod = pd.getWriteMethod();
                Object value = rs.getObject(prop.getColumnName());
                switch (prop.getColumnTypeNum()) {
                    case Types.INTEGER:
                        setMethod.invoke(t, Integer.parseInt(value.toString()));
                        break;
                    case Types.TINYINT:
                        setMethod.invoke(t, Integer.parseInt(value.toString()));
                        break;
                    case Types.BIGINT:
                        setMethod.invoke(t, Long.parseLong(value.toString()));
                        break;
                    case Types.FLOAT:
                        setMethod.invoke(t, Double.parseDouble(value.toString()));
                        break;
                    case Types.DOUBLE:
                        setMethod.invoke(t, Double.parseDouble(value.toString()));
                        break;
                    case Types.SMALLINT:
                        setMethod.invoke(t, Integer.parseInt(value.toString()));
                        break;
                    default:
                        setMethod.invoke(t, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("toEntity error!");
        }
        return t;
    }

    public static void close(Connection conn) {
        if(conn!=null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void close(Statement stat) {
        if(stat!=null)
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void close(ResultSet rs) {
        if(rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public static void close(Connection conn, Statement stat) {
        close(stat);
        close(conn);
    }

    public static void close(Connection conn, Statement stat, ResultSet rs) {
        close(rs);
        close(conn, stat);
    }

    public static final class ColumnProperty {
        private ColumnProperty(){}

        public static String columnName2PropertyName(String columnName) {
            int index = columnName.indexOf("_");
            if(index == -1)
                return columnName;
            String[] singleName = columnName.split("_");
            StringBuilder builder = new StringBuilder(singleName[0]);
            for(int i = 1; i < singleName.length; i++) {
                String s = StringUtil.upperFirstChar(singleName[i]);
                builder.append(s);
            }
            return builder.toString();
        }

        /**
         * 将resultset的结果集MetaData的列名转换为实体类的属性名
         * @param data
         * @return
         */
        public static List<ColumnProperties> convertRSColumnToProperty(ResultSetMetaData data) {
            List<ColumnProperties> list = new ArrayList<>();
            try {
                int columnCount = data.getColumnCount();
                for(int i = 1; i < columnCount + 1; i++) {
                    ColumnProperties cp = new ColumnProperties();

                    String columnName = data.getColumnName(i);
                    String propertyName = columnName2PropertyName(columnName);

                    int columnTypeNum = data.getColumnType(i);
                    String columnTypeName = data.getColumnTypeName(i);
                    String propertyTypeName = JdbcJavaTypes.getJavaTypeName(columnTypeNum);
                    Class<?> propertyType = JdbcJavaTypes.getJavaType(columnTypeNum);
                    int columnSize = data.getColumnDisplaySize(i);
                    String columnLabel = data.getColumnLabel(i);

                    cp.setColumnName(columnName);
                    cp.setPropertyName(propertyName);
                    cp.setColumnDisplaySize(columnSize);
                    cp.setColumnLabel(columnLabel);
                    cp.setColumnTypeName(columnTypeName);
                    cp.setColumnTypeNum(columnTypeNum);
                    cp.setPropertyType(propertyType);
                    cp.setPropertyTypeName(propertyTypeName);

                    list.add(cp);
                }
            } catch (SQLException e) {

            }
            return list;
        }
    }
}
