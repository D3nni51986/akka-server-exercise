## akka-server-exercise
The project contains two main components: EventsProducer and EventsConsumer.
EventsProducer reads the output of "generator-windows-amd64.exe" file and sends it's output as HttpRequest to the EventsConsumer that is running as Http server.
## Assumptions
Generator file - "generator-windows-amd64.exe" is located at the main path of the project.
Path can be configured at application.conf config/application.conf
## Instructions
 1)In order to run the cosumer please run : EventsConsumer file.
        By default the service is running on localhost and listening to port 8085
</br>2)In order to run the producer please run : EventsProducer file
## Tests
</br>You can post and event data by sending http request to : http://localhost:8085/service/
</br>example :
</br>{
</br>   "event_type":"bar",
</br>   "data":"test_data",
</br>   "timestamp":1521993383
</br>}
</br>You can retrieve stats from the service by running GET requests:
</br>example 1 = http://localhost:8085/service/?numOfEvents=bar
</br>example 2 = http://localhost:8085/service/?numOfDataPerEvent=bar
