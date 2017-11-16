package com.xyz.common;

public class ColumnProperties {
    private String propertyName;
    private String propertyTypeName;
    private Class<?> propertyType;

    private String columnName;
    private int columnDisplaySize;
    private String columnLabel;
    private int columnTypeNum;
    private String columnTypeName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public void setPropertyTypeName(String propertyTypeName) {
        this.propertyTypeName = propertyTypeName;
    }

    public Class<?> getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Class<?> propertyType) {
        this.propertyType = propertyType;
    }

    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    public String getColumnLabel() {
        return columnLabel;
    }

    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    public int getColumnTypeNum() {
        return columnTypeNum;
    }

    public void setColumnTypeNum(int columnTypeNum) {
        this.columnTypeNum = columnTypeNum;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    @Override
    public String toString() {
        return "ColumnProperties{" +
                "propertyName='" + propertyName + '\'' +
                ", propertyTypeName='" + propertyTypeName + '\'' +
                ", propertyType=" + propertyType +
                ", columnName='" + columnName + '\'' +
                ", columnDisplaySize=" + columnDisplaySize +
                ", columnLabel='" + columnLabel + '\'' +
                ", columnTypeNum=" + columnTypeNum +
                ", columnTypeName='" + columnTypeName + '\'' +
                '}';
    }
}
