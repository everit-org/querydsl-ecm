querydsl-ecm
==========================

The Everit - QueryDSL ECM module contains three Components that makes
it possible to create SQLTemplates instances via configuration and register QueryDSL Configuration.

 - SQLTemplatesComponent: It is possible to select the database type via
   configuration.
 - AutoSQLTemplatesComponent: This component needs a DataSource. Based on
   the connection metadata it decides which type of SQLTemplates it should
   instantiate.
 - QuerydslConfigurationComponent: This component register QueryDSL Configuration instance.

To see the all the configuration possibilities, deploy the module to your
OSGi container and check the configuration possibilities in webconsole.

[![Analytics](https://ga-beacon.appspot.com/UA-15041869-4/everit-org/querydsl-ecm)](https://github.com/igrigorik/ga-beacon)
