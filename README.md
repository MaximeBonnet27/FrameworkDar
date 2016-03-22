## WASP - README ##

Build & run
-------
- build : mvn package
- run: java -jar wasp-1.0-SNAPSHOT-jar-with-dependencies.jar /path/to/wasp-conf.json

WASP server configuration
-------
To run WASP-server you need to passe a json file like the following exemple.
exemple:

    {
    "port": "1234",
    "applications": [
      {
        "context":"/app1",  
        "location":"/path/to/app1.jar",
        "description":"application do something"
      },
      {
        "context":"/app2",  
        "location":"/path/to/app2.jar",
        "description":"application do something"
      },
    ]
    }

Application Configuration
-------

- With wasp.son
wasp.json should be directly in the WASP- INF directory of the jar of your application. All resources of your application should be located there.
exemple:

        {"controllers": [
             {"className": "com.exemple.controllers.Controller",
             "requestMappings": [
                {"resource": "/p/(id)/x", "methods": ["GET"], "callback": "getX",
                   "produceType": ["application/json","text/plain"],
                   "arguments": [
         	          {"sourceType": "path-variable", 
         	          "sourceRef": "id", 
         	          "type": "java.lang.Integer"}
         	        ]
         	 }]
         }],
         "viewMapping":{"prefix": "view/", "suffix":".html"}
         }

  