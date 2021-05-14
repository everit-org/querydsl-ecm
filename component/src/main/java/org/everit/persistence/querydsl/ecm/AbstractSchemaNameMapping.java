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
package org.everit.persistence.querydsl.ecm;

import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.namemapping.NameMapping;

public abstract class AbstractSchemaNameMapping implements NameMapping {

  @Override
  public com.google.common.base.Optional<String> getColumnOverride(final SchemaAndTable key,
      final String column) {
    return com.google.common.base.Optional.absent();
  }

  @Override
  public com.google.common.base.Optional<SchemaAndTable> getOverride(
      final SchemaAndTable original) {
    SchemaAndTable schemaAndTable = new SchemaAndTable(getSchemaName(), original.getTable());
    return com.google.common.base.Optional.of(schemaAndTable);
  }

  protected abstract String getSchemaName();

}
