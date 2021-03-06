package com.shaubert.protomapper;

import com.shaubert.protomapper.annotations.Field;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.VariableElement;

public class FieldParams {
    private TypeParams type;
    private String name;
    private String protoGetter;
    private String protoSetter;
    private String protoHas;
    private String protoFieldName;
    private boolean optional;

    public FieldParams(VariableElement element, RoundEnvironment roundEnvironment) {
        type = new TypeParams(element.asType(), roundEnvironment);
        name = element.getSimpleName().toString();
        Field field = element.getAnnotation(Field.class);
        if (field.name() == null || field.name().length() == 0) {
            protoFieldName = name;
        } else {
            protoFieldName = field.name();
        }
        optional = field.optional();
        protoGetter = makeProtoGetter(protoFieldName, type.getName());
        String setPrefix = type.isList() ? "add" : "set";
        protoSetter = formatGetSetName(setPrefix, protoFieldName);
        protoHas = formatGetSetName("has", protoFieldName);
    }

    private String makeProtoGetter(String fieldName, String type) {
        String res = formatGetSetName("get", fieldName);
        if (type.equals("java.util.List")) {
            res += "List";
        }
        return res;
    }

    private String makeGetter(String fieldName, String type) {
        if (type.equals("boolean")) {
            return formatGetSetName("is", fieldName);
        } else {
            return formatGetSetName("get", fieldName);
        }
    }

    private String formatGetSetName(String getOrSet, String fieldName) {
        return getOrSet + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
    }

    public TypeParams getType() {
        return type;
    }

    public String getSetterName() {
        return formatGetSetName("set", name);
    }

    public String getGetterName() {
        return makeGetter(name, type.getName());
    }

    public String getName(){
        return name;
    }

    public String getProtoClassHasMethodName(){
        return protoHas;
    }

    public String getProtoClassGetter(){
        return protoGetter;
    }

    public String getProtoClassSetter() {
        return protoSetter;
    }

    public String getProtoFieldName() {
        return protoFieldName;
    }

    public boolean isOptional() {
        return optional;
    }
}
