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

For more details about the objectives, constraints and, please refer to the [lab instructions](Donnee.md).

### Mock server

#### What is it?

#### Docker container

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

##### Run the container

To run the created container, you also need to be in the folder [MockMock-docker/](MockMock-docker/)
and run :

```bash
docker run -p 8080:8282 -p 2525:25000 --rm mockmock-docker
```

*Might need to run it as sudo/administrator.*

This will run the container and expose the ports 8080 and 2525 on the host machine. 
You should now be able to access the MockMock server application by typing `localhost:8080` in your browser.

After closing the container, it will be removed properly. You'll be able run
it again by just running the command again.

### Client application usage

#### IDE

You can clone this repo. and open it in your favorite IDE.

You can then run the application by running the main class `ch.heigvd.res.labo.smtp.SmtpClient`.

#### Command line

You can also run the application from the command line.

Configuration files are placed under the [lab4/src/config/](lab4/src/config/) folder.