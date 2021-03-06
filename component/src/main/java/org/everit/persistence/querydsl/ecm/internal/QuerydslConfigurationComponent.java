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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.BooleanAttribute;
import org.everit.osgi.ecm.annotation.attribute.BooleanAttributes;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.ecm.QuerydslConfigurationConstants;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.namemapping.NameMapping;

/**
 * Simple component that registers Querydsl configuration as an OSGi service.
 */
@ExtendComponent
@Component(componentId = QuerydslConfigurationConstants.SERVICE_FACTORYPID_QUERYDSL_CONFIGURATION,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit Querydsl Configuration",
    description = "Registers Querydsl Configuration instance as OSGi service.")
@BooleanAttributes({
    @BooleanAttribute(attributeId = QuerydslConfigurationConstants.ATTR_USE_LITERALS,
        defaultValue = false, priority = QuerydslConfigurationComponent.P_USE_LITERALS,
        label = "Use literals",
        description = "Whether to use literals in SQL statements or not.") })
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = QuerydslConfigurationConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = QuerydslConfigurationComponent.P_SERVICE_DESCRIPTION,
        label = "Service description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualServices(@ManualService(Configuration.class))
public class QuerydslConfigurationComponent {

  private static final int P_NAME_MAPPING = 3;

  public static final int P_SERVICE_DESCRIPTION = 0;

  public static final int P_SQL_TEMPLATES = 1;

  public static final int P_USE_LITERALS = 2;

  private NameMapping nameMapping;

  private ServiceRegistration<Configuration> serviceRegistration;

  /**
   * SQLTemplates reference.
   */
  private SQLTemplates sqlTemplates;

  /**
   * Component activator method.
   */
  @Activate
  public void activate(final ComponentContext<QuerydslConfigurationComponent> componentContext) {
    Configuration configuration = new Configuration(this.sqlTemplates);

    Map<String, Object> componentPropeties = componentContext.getProperties();
    Object useLiteralsProp =
        componentPropeties.get(QuerydslConfigurationConstants.ATTR_USE_LITERALS);
    if (useLiteralsProp != null) {
      configuration.setUseLiterals(Boolean.valueOf(useLiteralsProp.toString()));
    }
    if (this.nameMapping != null) {
      configuration.setDynamicNameMapping(this.nameMapping);
    }

    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentPropeties);
    this.serviceRegistration =
        componentContext.registerService(Configuration.class, configuration, serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (this.serviceRegistration != null) {
      this.serviceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = QuerydslConfigurationConstants.ATTR_NAME_MAPPING_TARGET,
      attributePriority = QuerydslConfigurationComponent.P_NAME_MAPPING,
      optional = true,
      label = "NameMapping OSGi filter",
      description = "OSGi filter for the nameMapping reference that will be used for "
          + "dynamic name mapping if available.")
  public void setNameMapping(final NameMapping nameMapping) {
    this.nameMapping = nameMapping;
  }

  @ServiceRef(attributeId = QuerydslConfigurationConstants.ATTR_SQL_TEMPLATES_TARGET,
      defaultValue = "", attributePriority = QuerydslConfigurationComponent.P_SQL_TEMPLATES,
      label = "SQLTemplates OSGi filter",
      description = "OSGi filter for the sqlTemplates reference that will be embedded into the "
          + "configuration.")
  public void setSqlTemplates(final SQLTemplates sqlTemplates) {
    this.sqlTemplates = sqlTemplates;
  }
}
