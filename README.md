# TransportHeaderHandler

TransportHeaderHandler is a global synapse handler which can used to filter out the request and response headers in WSO2 API Manager.

Please follow the steps below to configure the given jar in your environment.

1) Build the Jar (TransportHeaderHandler/target/TransportHeaderHandler-1.0-SNAPHSHOT.jar)
2) Copy Jar into <APIM_HOME>/repository/components/lib
3) Add the following configurations to <APIM_HOME>/repository/conf/synapse-handlers.xml file
```
<handler name = "TransportHeaderHandler" class="com.wso2.apim.transport.headers.handler.TransportHeaderHandler">
    <parameter name="removeRequestHeaders" value="true"/>
    <parameter name="preserveRequestHeaders" value=""/>
    <parameter name="excludeRequestHeaders" value=""/>
    <parameter name="excludeResponseHeaders" value=""/>
</handler>

```
5) Start the server and create an API.
6) When you invoke the API, you will be able to see the logs successfully.


## Configuration

| Parameter Name | Description  |
| ------------- | ------------|
| removeRequestHeaders | `true` or `false` indicating whether to remove the Request headers from the response in a failure scenario. For OPTIONS call, request headers will always be removed. Default value is `false` |
| preserveRequestHeaders      | Comma separated List of headers preserved while removing request headers from the response.  Default is empty. This list of headers will be preserved only when `removeRequestHeaders` is set to true |
| excludeRequestHeaders | Comma separated List of well known request headers that should be removed in a Response to the client. Default list is empty. |
| excludeResponseHeaders |    Comma separated List of well known response headers that should be removed in a Request sent to backend. Default list is empty  |