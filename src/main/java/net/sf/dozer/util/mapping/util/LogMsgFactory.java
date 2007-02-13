/*
 * Copyright 2005-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.dozer.util.mapping.util;


import java.util.Collection;

import net.sf.dozer.util.mapping.fieldmap.FieldMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author tierney.matt
 * @author garsombke.franz
 */
public class LogMsgFactory {
  private static final Log log = LogFactory.getLog(LogMsgFactory.class);
  
  private final MappingUtils mappingUtils = new MappingUtils();
  
  public String createFieldMappingErrorMsg(Object sourceObj, FieldMap fieldMapping, 
      Object sourceFieldValue, Object destObj, Throwable t) {
    String sourceClassName = null;
    if (sourceObj != null) {
      sourceClassName = sourceObj.getClass().getName();
    }

    String sourceValueType = null;
    if (sourceFieldValue != null) {
      sourceValueType = sourceFieldValue.getClass().toString();
    } else {
      sourceValueType = fieldMapping.getSourceField().getType();
    }

    String destClassName = null;
    if (destObj != null) {
      destClassName = destObj.getClass().getName();
    }

    String destFieldTypeName = null;
    try {
      destFieldTypeName = fieldMapping.getDestFieldType(destObj.getClass()).getName();
    } catch (Exception e) {
      log.warn("unable to determine dest field type when build log.error message");
    }

    return "Field mapping error -->" + "\n  MapId: " + fieldMapping.getMapId() + "\n  Type: " + fieldMapping.getType()
        + "\n  Source parent class: " + sourceClassName + "\n  Source field name: "
        + fieldMapping.getSourceField().getName() + "\n  Source field type: " + sourceValueType
        + "\n  Source field value: " + sourceFieldValue + "\n  Dest parent class: " + destClassName
        + "\n  Dest field name: " + fieldMapping.getDestField().getName() + "\n  Dest field type: "
        + destFieldTypeName;
  }
  
  public String createFieldMappingSuccessMsg(Class sourceClass, Class destClass, String sourceFieldName, String destFieldName,
    Object sourceFieldValue, Object destFieldValue) {
    String sourceClassStr = mappingUtils.getClassNameWithoutPackage(sourceClass);
    String destClassStr = mappingUtils.getClassNameWithoutPackage(destClass);
      
    return "MAPPED: " + sourceClassStr + "." + sourceFieldName + " --> " + destClassStr + "." + destFieldName
        + "  VALUES: " + getLogOutput(sourceFieldValue) + " --> "  + getLogOutput(destFieldValue);
  }
  
  private static String getLogOutput(Object object) {
    String output = "NULL";
    if (object == null) {
      return output;
    }
    try {
      if (object.getClass().isArray() || Collection.class.isAssignableFrom(object.getClass())) {
        output = ReflectionToStringBuilder.toString(object, ToStringStyle.MULTI_LINE_STYLE);
      } else {
        output = object.toString();
      }
    } catch (RuntimeException e) {
      output = object.toString();
    }
    return output;
  }
}