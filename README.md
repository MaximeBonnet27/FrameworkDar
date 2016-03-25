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

        {
          "controllers": [
            {"className": "com.exemple.controller.PointController", "requestMappings":
            [
                {"resource": "/list", "methods": ["GET"], "callback": "list",
                  "produceType": ["application/json","text/plain"]
                },
        
                {"resource": "/p/(id)/x", "methods": ["GET"], "callback": "getX",
                  "produceType": ["application/json","text/plain"],
                  "arguments": [{"sourceType": "path-variable", "sourceRef": "id", "type": "java.lang.Integer"}]
                },
        
                {"resource": "/p/(id)/y", "methods": ["GET"], "callback": "getY",
                  "produceType": ["application/json","text/plain"],
                  "arguments": [{"sourceType": "path-variable", "sourceRef": "id", "type": "java.lang.Integer"}]
                },
        
                {"resource": "/p/(id)", "methods": ["PUT"], "callback": "update",
                  "arguments": [
                    {"sourceType": "path-variable", "sourceRef": "id", "type": "java.lang.Integer"},
                    {"sourceType": "request-variable", "sourceRef": "x", "type": "java.lang.Integer"},
                    {"sourceType": "request-variable", "sourceRef": "y", "type": "java.lang.Integer"}
                  ]
                },
        
                {"resource": "/p", "methods": ["POST"], "callback": "addPoint",
                  "contentType": ["application/json","application/x-www-form-urlencoded"],
                  "produceType": ["application/json","text/plain"],
                  "arguments": [
                    {"sourceType": "request-body", "type": "stl.upmc.com.model.Point"}
                  ]
                },
              {"resource": "/p/(id)", "methods": ["DELETE"], "callback": "removePoint",
                "arguments": [{"sourceType": "path-variable", "sourceRef": "id", "type": "java.lang.Integer"}]
              }
              ]
            }
          ],
         "viewMapping":{"prefix": "view/", "suffix":".html"}
         }

- With annotation
first you need maven dependency:

        <dependency>
        <groupId>com.wasp</groupId>
        <artifactId>wasp</artifactId>
        <version>1.0-SNAPSHOT</version>
        </dependency>
      

and extend WaspConfig:

    public class AppConfig extends WaspConfig{
    @Override
    public Class[] getAnnotatedControllers() {
        return new Class[]{PointController.class};
    }

    @Override
    public ViewMapping getViewMapping() {
        return new ViewMapping("views/",".html");
    }
    }

after this, you can annotate your controller class like this:

    @Controller
    public class PointController {
    @RequestMapping(resource = "/p/(id)/x",methods = {MethodType.GET},produceType = {HttpContentTypes.APPLICATION_JSON,HttpContentTypes.TEXT_PLAIN})
        public int getX(@PathVariable("id")Integer id) {...}
    }

Return type of Resquest mapping
-------------------------------
The method that is mapping a request can return:
- a IHttpRequest
- a IView
- or any Object that can be converted to JSON or XML(with jaxb).