/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.persistence.querydsl.ecm.internal;

import java.util.Hashtable;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.attribute.BooleanAttribute;
import org.everit.osgi.ecm.annotation.attribute.CharacterAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.persistence.querydsl.ecm.DBMSType;
import org.everit.persistence.querydsl.ecm.SQLTemplatesConstants;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLTemplates.Builder;

/**
 * Abstract SQL Templates component information and process.
 */
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = SQLTemplatesConstants.DEFAULT_SERVICE_DESCRIPTION_SQL_TEMPLATES,
        priority = AutoSQLTemplatesComponent.P_SERVICE_DESCRIPTION,
        label = "Service description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
public abstract class AbstractSQLTemplatesComponent {

  public static final int P_ESCAPE = 8;

  public static final int P_NEW_LINE_TO_SINGLE_SPACE = 7;

  public static final int P_PRINT_SCHEMA = 5;

  public static final int P_QUOTE = 6;

  public static final int P_SERVICE_DESCRIPTION = 0;

  private char escape;

  private boolean newLineToSingleSpace;

  private boolean printSchema;

  private boolean quote;

  /**
   * SQLTemplates OSGi service registration instance.
   */
  private ServiceRegistration<?> serviceRegistration;

  /**
   * Generates the SQLTemplates instance and registers it as an OSGi service.
   *
   * @param componentContext
   *          The context of the component.
   */
  @Activate
  public void activate(final ComponentContext<?> componentContext) {
    DBMSType dbmsType = getDBMSType();
    Builder sqlTemplatesBuilder = dbmsType.getSQLTemplatesBuilder();
    sqlTemplatesBuilder.escape(escape);
    if (newLineToSingleSpace) {
      sqlTemplatesBuilder.newLineToSingleSpace();
    }
    if (printSchema) {
      sqlTemplatesBuilder.printSchema();
    }
    if (quote) {
      sqlTemplatesBuilder.quote();
    }
    SQLTemplates sqlTemplates = sqlTemplatesBuilder.build();
    Hashtable<String, Object> serviceProperties = new Hashtable<>(componentContext.getProperties());
    serviceProperties.put(SQLTemplatesConstants.ATTR_DB_TYPE, dbmsType.toString());
    serviceRegistration = componentContext.registerService(SQLTemplates.class, sqlTemplates,
        serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (serviceRegistration != null) {
      serviceRegistration.unregister();
    }
  }

  protected abstract DBMSType getDBMSType();

  @CharacterAttribute(attributeId = SQLTemplatesConstants.ATTR_ESCAPE, defaultValue = '\\',
      priority = AbstractSQLTemplatesComponent.P_ESCAPE, label = "Escape character",
      description = "Escape character.")
  public void setEscape(final char escape) {
    this.escape = escape;
  }

  @BooleanAttribute(attributeId = SQLTemplatesConstants.ATTR_NEW_LINE_TO_SINGLE_SPACE,
      defaultValue = false, priority = AbstractSQLTemplatesComponent.P_NEW_LINE_TO_SINGLE_SPACE,
      label = "New line to single space",
      description = "Replaces new line characters with a single space.")
  public void setNewLineToSingleSpace(final boolean newLineToSingleSpace) {
    this.newLineToSingleSpace = newLineToSingleSpace;
  }

  @BooleanAttribute(attributeId = SQLTemplatesConstants.ATTR_PRINT_SCHEMA, defaultValue = false,
      priority = AbstractSQLTemplatesComponent.P_PRINT_SCHEMA, label = "Print schema",
      description = "This property determines whether or not the SQLTemplate will insert the "
          + "schema name before the table name in the SQL expressions. "
          + "/select * from schemaname.tablename;/.")
  public void setPrintSchema(final boolean printSchema) {
    this.printSchema = printSchema;
  }

  @BooleanAttribute(attributeId = SQLTemplatesConstants.ATTR_QUOTE, defaultValue = false,
      priority = AbstractSQLTemplatesComponent.P_QUOTE, label = "Quote",
      description = "This property determines whether or not the SQLTemplate will be quoting in "
          + "SQL strings.")
  public void setQuote(final boolean quote) {
    this.quote = quote;
  }
}
