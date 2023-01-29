# GDSRT  
## Introduction  
This toolkit is a service meant to assist with handling security incidents in online games. This is meant to assist with performing an CIRP and quickly mitigate against issues & exploits.  
Currently, this is targetted against games with trading services, as these usually have elements that needs to be tracked and handled when an exploit happens.   
This could for example be when a inidividual is found to be cheating, the trades of that user can be tracked, and action can be taken against then.  

## How this works
When an incident, such as somebody who has illegally been selling currency in a game gets discovered. One can when performing bans, etc use this to find out exactly which trades happened, and which trades were "suspicious". This gives the admin a very easy way to see which individuals this user traded items to. Then they can use that to perform mitigation actions for that specific incident.  
Furthermore, it also allows chain-scanning, meaning it can produce a "chain" of suspicious trades if one where to for example be muling something to prevent discovery.  
**It should be noted that this is not a tool for preventing hacking/exploiting in an application, like services such as EAC does. This service is only for providing a toolkit that individuals who handle security response incidents can use to quickly perform mitigation actions.**

## Deployment  
### Config file  
First of all, the service has a config.properties file that needs to be defined with various values:  
**All of the properties need to be defined, and the options for each option is split by |**  
- DB_TYPE=TEMP|POSTGRESQL - Type of database. Temp means a transient data source, will be cleared upon end of run.  
- DB_HOST=localhost - Db ip/domain, ignored if transient  
- DB_USERNAME=username - ignored if transient  
- DB_PASSWORD=password - ignored if transient  
- STRICTNESS_LEVEL=1  
- CONNECTION_TYPE=SOCKET - Connection type, only available type is SOCKET  
- DB_NAME=gdsrt  
- DB_PORT=5432  
- SERVER_PORT=1234 - Port of the server  
- ENCRYPTION=false - Whether or not to enable encryption  
- PRIVATE_KEY_PATH=key - Location on system to where the private key is located (if enabled)  
**It should be noted that using encryption is not recommended, as the data it processes is not very vulnerable, and the service is meant to be containerized and running locally on the same network as the connecting service. Furthermore, only the Java version of the client supports encryption currently.**  
- FLOOD_ITEMS=true|false - Whether or not to inject prices of items into the service.  
- FLOOD_ITEMS_PATH=items.csv - Location on system to where the price of items is located (if enabled)  
**File is .csv, with data being given as: itemname,price**  

### Running  
This can be deployed by compiling the compile into a jar file yourself or running the stable jar, or running the project itself.  
This service is meant to be containerized if taken into use, and not just testing. Ideally this would be ran using docker, and be on the  
same network as the service feeding it data. This not only makes the exchange very simple, but also removes any possible security concerns.  

### Guide  
A guide for making a connection to the service can be found on the client libaries repository: https://github.com/Morten-JO/GDSRT-C  

## Currently implemented:  
  - Web graph report export - Retrieve a .json file of a specific requested user, tracking suspicious trades related to that user, filtered by a warning level.  
  - Algorithm for checking a trade for potenial issues  
  - Routine checks on users, updating their warning level, if suspicious activity is found  
  - Python & Java client libraries which can be found at: https://github.com/Morten-JO/GDSRT-C  
  - Socket connection from client library to service for trade communication  

##Future work:  
  - Tracking through other means, suchas exp, levels etc  
  - More communication types  
  - More client libraries in different languages  
  - More indepth algorithm  
  
