# akka-server-exercise
The project contains two main components: EventsProducer and EventsConsumer.
EventsProducer reads the output of "generator-windows-amd64.exe" file and sends it's output as HttpRequest to the EventsConsumer that is running as Http server.
# Assumptions
Generator file - "generator-windows-amd64.exe" is located at the main path of the project.
Path can be configured at application.conf config/application.conf
# Instructions
 1)In order to run the cosumer please run : EventsConsumer file.
        By default the service is running on localhost and listening to port 8085
 2)In order to run the producer please run : EventsProducer file
# Tests
You can post and event data by sending http request to : http://localhost:8085/service/
example :
{
   "event_type":"bar",
   "data":"test_data",
   "timestamp":1521993383
}
You can retrieve stats from the service by running GET requests:
example 1 = http://localhost:8085/service/?numOfEvents=bar
example 2 = http://localhost:8085/service/?numOfDataPerEvent=bar