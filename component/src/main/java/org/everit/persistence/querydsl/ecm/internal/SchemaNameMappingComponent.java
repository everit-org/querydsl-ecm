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

import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.ecm.AbstractSchemaNameMapping;
import org.everit.persistence.querydsl.ecm.SchemaNameMappingConfigurationConstants;

import com.querydsl.sql.namemapping.NameMapping;

@Component(
    componentId = SchemaNameMappingConfigurationConstants.SERVICE_FACTORYPID_QUERYDSL_NAME_MAPPING,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit Querydsl SchemaNameMapping",
    description = "Registers Querydsl NameMapping instance as OSGi service.")
@ExtendComponent
@Service(NameMapping.class)
public class SchemaNameMappingComponent extends AbstractSchemaNameMapping {

  private String schemaName;

  @Override
  protected String getSchemaName() {
    return this.schemaName;
  }

  @StringAttribute(attributeId = SchemaNameMappingConfigurationConstants.ATTR_NAME_SCHEMA_NAME,
      defaultValue = "",
      label = "Schema name",
      description = "The schema name that will be used for dynamic name mapping.")
  public void setSchemaName(final String schemaName) {
    this.schemaName = schemaName;
  }
}
