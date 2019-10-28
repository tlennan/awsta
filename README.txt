This project demonstrates how to create a project which implements a repository and web interface while abstracting the
implementation details.  Core is where business logic would be places.  Premises uses spring boot to run an application
with an embedded webserver which will connect to a locally running mongodb instance.  The intent of the aws module is
to run the core app with something like dynamodb and a different web server.