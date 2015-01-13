realm-platform
==============

The Realm Platform integrates components like objectof and DCM into a platform with a simple account system, device logging, etc...

Release 0.1
-----------
Version 0.1 includes server-side components. Deployment sould be done through a maven webapp using an XML configuration to set up corc flows.

The data model used in this release can be modified by changing the xml schema definition and regenerating the java code with a `maven install` command. Custom API components will need to be adjusted accordingly.
