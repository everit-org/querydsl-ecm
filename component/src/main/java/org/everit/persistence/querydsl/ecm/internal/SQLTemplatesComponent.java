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

import java.util.Objects;

import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributeOption;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.ecm.DBMSType;
import org.everit.persistence.querydsl.ecm.SQLTemplatesConstants;
import org.everit.persistence.querydsl.ecm.UnknownDatabaseTypeException;

import com.querydsl.sql.SQLTemplates;

/**
 * Component that instantiates and registers SQLTemaplates objects as OSGi service.
 */
@ExtendComponent
@Component(componentId = SQLTemplatesConstants.SERVICE_FACTORY_PID_SQL_TEMPLATES, metatype = true,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit Querydsl SQLTemplates",
    description = "By configuring this component, the user will get an SQLTemplate as an "
        + "OSGi service.")
@ManualServices(@ManualService(SQLTemplates.class))
public class SQLTemplatesComponent extends AbstractSQLTemplatesComponent {

  public static final int P_DB_TYPE = 1;

  private String dbType;

  @Override
  protected DBMSType getDBMSType() {
    Objects.requireNonNull(dbType, "dbType cannot be null");
    for (DBMSType dbmsType : DBMSType.values()) {
      if (dbmsType.toString().equals(dbType)) {
        return dbmsType;
      }
    }
    throw new UnknownDatabaseTypeException("database type [" + dbType + "] is not supported");
  }

  @StringAttribute(attributeId = SQLTemplatesConstants.ATTR_DB_TYPE,
      priority = SQLTemplatesComponent.P_DB_TYPE, label = "Database type",
      description = "Type of the SQLTemplate which will be created.",
      defaultValue = DBMSType.TYPE_H2, options = {
          @StringAttributeOption(label = DBMSType.TYPE_H2,
              value = DBMSType.TYPE_H2),
          @StringAttributeOption(label = DBMSType.TYPE_POSTGRES,
              value = DBMSType.TYPE_POSTGRES),
          @StringAttributeOption(label = DBMSType.TYPE_MYSQL,
              value = DBMSType.TYPE_MYSQL),
          @StringAttributeOption(label = DBMSType.TYPE_ORACLE,
              value = DBMSType.TYPE_ORACLE),
          @StringAttributeOption(label = DBMSType.TYPE_SQLITE,
              value = DBMSType.TYPE_SQLITE),
          @StringAttributeOption(label = DBMSType.TYPE_CUBRID,
              value = DBMSType.TYPE_CUBRID),
          @StringAttributeOption(label = DBMSType.TYPE_DERBY,
              value = DBMSType.TYPE_DERBY),
          @StringAttributeOption(label = DBMSType.TYPE_HSQLDB,
              value = DBMSType.TYPE_HSQLDB),
          @StringAttributeOption(label = DBMSType.TYPE_TERADATA,
              value = DBMSType.TYPE_TERADATA),
          @StringAttributeOption(label = DBMSType.TYPE_SQLSERVER,
              value = DBMSType.TYPE_SQLSERVER),
          @StringAttributeOption(label = DBMSType.TYPE_SQLSERVER_2005,
              value = DBMSType.TYPE_SQLSERVER_2005),
          @StringAttributeOption(label = DBMSType.TYPE_SQLSERVER_2008,
              value = DBMSType.TYPE_SQLSERVER_2008),
          @StringAttributeOption(label = DBMSType.TYPE_SQLSERVER_2012,
              value = DBMSType.TYPE_SQLSERVER_2012) })
  public void setDbType(final String dbtype) {
    this.dbType = dbtype;
  }

}
