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

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.ecm.DBMSType;
import org.everit.persistence.querydsl.ecm.SQLTemplatesConstants;

import com.querydsl.sql.SQLTemplates;

/**
 * Component that automatically detects the type of the database based on the referenced DataSource
 * and registers the right type of SQLTemplates instance.
 */
@ExtendComponent
@Component(componentId = SQLTemplatesConstants.SERVICE_FACTORY_PID_AUTO_SQL_TEMPLATES,
    configurationPolicy = ConfigurationPolicy.FACTORY,
    label = "Everit QueryDSL Auto SQLTemplates",
    description = "By configuring this component, the user will get an SQLTemplate as an OSGi "
        + "service.")
@ManualServices(@ManualService(SQLTemplates.class))
public class AutoSQLTemplatesComponent extends AbstractSQLTemplatesComponent {

  public static final int P_DATA_SOURCE = 1;

  public static final int P_LOG_SERVICE = 10;

  /**
   * The datasource that is used to find out the type of the database.
   */
  private DataSource dataSource;

  @Override
  protected DBMSType getDBMSType() {
    String dbProductName = "";
    int dbMajorVersion = 0;

    try (Connection conn = dataSource.getConnection()) {
      dbProductName = conn.getMetaData().getDatabaseProductName();
      dbMajorVersion = conn.getMetaData().getDatabaseMajorVersion();
      return DBMSType.getByProductNameAndMajorVersion(dbProductName, dbMajorVersion);
    } catch (SQLException e) {
      throw new IllegalStateException(
          "Cannot get Database product name of the given DataSource.", e);
    }
  }

  @ServiceRef(attributeId = SQLTemplatesConstants.ATTR_DATA_SOURCE, defaultValue = "",
      label = "DataSource service filter",
      attributePriority = AutoSQLTemplatesComponent.P_DATA_SOURCE,
      description = "An OSGi filter expression to select the right DataSource. The right QueryDSL "
          + "SQLTemplate will be created based on this DataSource's Database type.")
  public void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

}
