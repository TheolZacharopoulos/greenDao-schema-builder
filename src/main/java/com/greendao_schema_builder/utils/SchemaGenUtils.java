package com.greendao_schema_builder.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * Util functions.
 */
public class SchemaGenUtils {

    /**
     * Concatenates two Java arrays.
     * @param _firstArray the first array
     * @param _secondArray the second array.
     * @return the concatenated array.
     */
    public static <T> T[] concatenateArrays(T[] _firstArray, T[] _secondArray) {
        int aLen = _firstArray.length;
        int bLen = _secondArray.length;

        @SuppressWarnings("unchecked")
        T[] concatenatedArray = (T[]) Array.newInstance(_firstArray.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(_firstArray, 0, concatenatedArray, 0, aLen);
        System.arraycopy(_secondArray, 0, concatenatedArray, aLen, bLen);

        return concatenatedArray;
    }

    /**
     * Returns all the fields of a class, included all those of it's super classes recursively.
     * @param _class the initial class.
     * @param _fields the list of the fields of this class.
     * @return all the fields of the class included those of it's super classes.
     */
    public static Field[] getSuperFieldsRecursively(Class<?> _class, Field[] _fields) {
        if (_class != null) {
            _fields = concatenateArrays(_fields, _class.getDeclaredFields());
            return getSuperFieldsRecursively(_class.getSuperclass(), _fields);
        }
        return _fields;
    }
}
