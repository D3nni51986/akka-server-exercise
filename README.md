# akka-server-exercise
The project contains two main components: EventsProducer and EventsConsumer.
EventsProducer reads the output of "generator-windows-amd64.exe" file and sends it's output as HttpRequest to the EventsConsumer that is running as Http server.
# Assumptions
Generator file - "generator-windows-amd64.exe" is located at the main path of the project.
Path can be configured at application.conf config/application.conf
