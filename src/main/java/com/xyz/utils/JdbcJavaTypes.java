package com.xyz.utils;

import java.sql.Types;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class JdbcJavaTypes {
    private static final Map<Integer, Class<?>> TYPES = new HashMap<Integer, Class<?>>();

    static {
        TYPES.put(new Integer(Types.BIGINT), Long.class);
        TYPES.put(new Integer(Types.CHAR), String.class);
        TYPES.put(new Integer(Types.DATE), Date.class);
        TYPES.put(new Integer(Types.VARCHAR), String.class);
        TYPES.put(new Integer(Types.FLOAT), Double.class);
        TYPES.put(new Integer(Types.INTEGER), Integer.class);
        TYPES.put(new Integer(Types.TINYINT), Integer.class);
    }

    public static Class<?> getJavaType(int jdbcTypeNum) {
        Class<?> aClass = TYPES.get(jdbcTypeNum);
        return aClass;
    }

    public static String getJavaTypeName(int jdbcTypeNum) {
        return getJavaType(jdbcTypeNum).getSimpleName();
    }
}
