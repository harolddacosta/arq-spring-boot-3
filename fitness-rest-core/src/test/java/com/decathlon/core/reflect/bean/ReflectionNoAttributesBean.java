/* Decathlon (C)2021 */
package com.decathlon.core.reflect.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReflectionNoAttributesBean implements Serializable {

    private static final long serialVersionUID = 6631033103320985986L;

    public static String staticField;
    public static final String finalField = "";
    public String normalField;
}
