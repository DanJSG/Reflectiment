# Reflectiment
An automated text analysis web application for determining sentiment, author mood, and reflectivity in a piece of text. The core analysis system is comprised of two parts: an algorithmic, lexicon-based analyzer; and a machine learning based analyser.

The lexicon-based analyzer is written in Java and makes use of the SentiWords dictionary for sentiment analysis, the NRC Word-Emotion Association Lexicon for mood analysis, and a personally devised lexicon built based on the research done in a paper by Thomas Ullman. These lexicons are combined with a set of rules for negation and modification.

The machine learning based analyzer is written in Python and makes use of the Keras API for Tensorflow to implement the neural networks. The sentiment analysis used a deep Convolutional-LSTM network trained on the Stanford Sentiment Treebank (SST) dataset. The mood analysis used a deep multi-output Bidirectional-LSTM network which was trained using the dataset from the WASSA-2017 Shared Task on Emotion Intensity. The reflection analysis used another multi-output Bidirectional-LSTM network, and was trained using a dataset manually created for this project.

Below is a guide for setting up your own local development environment of this application. <b>Please note that the machine learning based analyzer makes use of the Google News word2vec word embeddings and so requires a lot of memory to run, usually utilising around 7GB of RAM in normal operation.</b>

Nothing within the `/scripts` folder is used directly within the application - these are simply scripts and files that were used during the dataset and lexicon processing, and during the neural network training process.

This application was developed as a final year project for an MEng in Electronic Engineering from the University of York.

## Setting up a Local Development Environment

### 1. Prerequisites
On your local system you will need to have the following installed:

* JDK for Java 11 or above (I recommend <a href="https://openjdk.java.net/install/">OpenJDK</a>)
* A Java IDE (I recommend <a href="https://www.jetbrains.com/idea/">IntelliJ IDEA</a>)
* <a href="https://www.python.org/downloads/">Python</a> 3.7 or above
* <a href="https://nodejs.org/en/download/">Node.js</a> Version 10 or above
* <a href="https://classic.yarnpkg.com/en/docs/install/#windows-stable">Yarn</a> Version 1.22 or above
* <a href="https://mariadb.org/download/">MariaDB server</a>
* A MySQL compatible database management system (I use <a href="https://www.mysql.com/products/workbench/">MySQL Workbench</a>)

### 2. Application Architecture
The application is split up into four microservices:

* The Lexical Analyzer (`/lexicalanalyzer`) - an algorithmic text analyzer based on lexicons and language rules
* The Machine Learning Analyzer (`/mlanalyzer`) - a machine learning based text analyzer using Keras and Tensorflow
* The API Gateway (`/gateway`) - a REST API gateway which acts as an interface to both text analyzers
* The Web User Interface (`/web-ui`) - a web-based UI built using the ReactJS framework

Local setup requirements for each of these services will now be covered.

### 3. Lexical Analyzer
The first step for the lexical analyzer is to configure and restore the database. Create the user using the following SQL query:
```SQL
CREATE USER 'localdev'@'%' IDENTIFIED BY 'password';
```
If you want to use a different username and password, then you must also change the username and password within the application properties file `/lexicalanalyzer/src/main/resources/application.properties`.

Then use your database management system to give the user Database Administrator (DBA) permissions on the server. Once this has been done, please restore the SQL databases from the schema file `/lexicalanalyzer/schema.sql`.

Once this has been done, open the `/lexicalanalyzer` project folder in your Java IDE, build and run it. Please note that the first build may take upwards of 10 minutes as it will need to pull all of the Maven dependencies. The application will then start on local port 8081.

### 4. Machine Learning Analyzer
Using the command line, navigate to the `/mlanalyzer` folder and run the following commands:
```bash
python -m venv venv
./venv/Scripts/activate
./venv/Scripts/pip install -r requirements.txt
```
This will set up a virtual environment and install all of the required packages. Then, once all the libraries have been installed, run the following command:
```bash
./venv/Scripts/python main.py
```
This will start the application on local port 8082.

### 5. API Gateway
Simply open up the API Gateway folder `/gateway` in your Java IDE and build and run it. This will start the application running on port 8080.

### 6. Web UI
Using the command line, navigate to the `/web-ui` folder. Then run the following command to install all the necessary dependencies:
```bash
yarn install
```
Then run the following to start the application:
```bash
yarn start
```
This will start the application and automatically open your default browser to the application running locally. 
