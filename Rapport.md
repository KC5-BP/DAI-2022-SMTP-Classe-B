<!-- Your report MUST include the following sections:
* **A brief description of your project**: if people exploring GitHub find your repo, without a prior knowledge of the API course, they should be able to understand what your repo is all about and whether they should look at it more closely.
* **What is MockMock (or any other mock SMTP server you decided to use)?**
* **Instructions for setting up your mock SMTP server (with Docker - which you will learn all about in the next 2 weeks)**. The user who wants to experiment with your tool but does not really want to send pranks immediately should be able to use a mock SMTP server. For people who are not familiar with this concept, explain it to them in simple terms. Explain which mock server you have used and how you have set it up.
* **Clear and simple instructions for configuring your tool and running a prank campaign**. If you do a good job, an external user should be able to clone your repo, edit a couple of files and send a batch of e-mails in less than 10 minutes.
* **A description of your implementation**: document the key aspects of your code. It is a good idea to start with a **class diagram**. Decide which classes you want to show (focus on the important ones) and describe their responsibilities in text. It is also certainly a good idea to include examples of dialogues between your client and an SMTP server (maybe you also want to include some screenshots here).
-->

# DAI: Labo SMTP

## Brief

DAI (previously API and before RES) is a course about network programming at the HEIG-VD,
which stands for Internet Application Development (Développement d'Application Internet).

This lab will allow any user to:

- Send forged e-mails (in this case, containing jokes) to a list of victims
- Define the number of groups of victims that will receive the same e-mail
    - A group contains 1 sender and at least 2 recipients, so minimum size group is 3
- Define any message content (joke) that will be sent to victims

## Details

This lab will make students develop a TCP client application in Java that send
a joke by email to a group of victims. The client application will use the Socket
API to communicate with a SMTP server.

The SMTP server will be set in place through a mock server
(using [MockMock server](https://github.com/DominiqueComte/MockMock) repo). In this repo.,
the MockMock repository has been duplicated and can be found in the MockMock-docker folder.

For more details about the objectives and constraints, please refer to the [lab instructions](Donnee.md).

### Mock server

#### What is it?

A mock server is a server that simulates the behavior of an actual server.
For that matter, it is used during development to test, present the interface to clients and so on.

In our case, we will use it to test our client application without sending real e-mails.

#### Test it yourself with a Docker container

Docker is a tool that allows to run applications in a container.
A container is a virtual environment that allows to run an application in
isolation from the host machine, but using its resources.

##### Create the container

You need to be in the folder [MockMock-docker/](MockMock-docker/) (because of the Dockerfile being there)
and run the following command:

```bash
docker build --tag mockmock-docker .
```

*Might need to run it as sudo/administrator.*

This will create a container named (tagged) `mockmock-docker` from the Dockerfile.

And need to be done only once.

##### Run the container

To run the created container, you also need to be in the folder [MockMock-docker/](MockMock-docker/)
and run :

```bash
docker run -p 8080:8282 -p 2525:25000 --rm mockmock-docker
```

*Might need to run it as sudo/administrator.*

This will run the container and expose the ports 8080 and 2525 on the host machine. 
You should now be able to access the MockMock server application by typing `localhost:8080` in your browser.

After closing the container (Ctrl+C), it will free resources properly. 

Just run that command again to use the mock server again.

### Client application usage

#### IDE

You can clone this repo. and open the project and run it in your favorite IDE.

#### Command line

If you want to run it from the command line, you need some dependencies to be installed:

- [Maven](https://maven.apache.org/)
- [Java 8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

Then, you can run the following command in the lab4/ folder of the project:

```bash
# Compile the project
mvn clean install

# Run the project

```

#### Configuration files

The configuration files are located in the [config/](lab4/src/config/) folder.

They are in a JSON format and are used to configure multiple options for the client application.

##### Server's

[config/](lab4/src/config/configServer.json) contains the configuration for the server, like:

```json
{
    "config": {
        "ip": "localhost",    // IP address of the server
        "encoding": "UTF-8",  // Encoding used to send messages + for HTML format
        "portSMTP": 25000,    // Port used to connect to the server
        "portHTTP": 8282      // Not used in this application, but given by default
  }
}
```

[config/](lab4/src/config/mailBodies.json) contains the Subject and Bodies of a mail:

```json
{
    "mailBody": {
        "subject": "$ Cool ! LIFE HACKS ! 100 ACCURATE LIFE HACKS ! $",
        "body": "https://youtu.be/-h5WrWncDZw?t=220"
    }
},
```

The client choose randomly one of the mail content (both subject and body) in the list, during execution.

[config/](lab4/src/config/mailList.json) contains the mailing list. 
The sender and recipients are chosen randomly from the list.

The format is as followed:

```json
{
    {"mail":  { "address": "bobby.hermann@gmail.com" } },
    {"mail":  { "address": "bobby.hermann@gmail.com" } },
    ...
}
```
