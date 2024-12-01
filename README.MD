# Expense Tracker

## Description
**Expense Tracker**  is a project used to manage your expenses by CLI.

This project is part of the [Roadmap.sh](https://roadmap.sh/projects/expense-tracker) backend projects.

## Requirements
Before compiling and running the program, make sure you have the following installed:

- **Java Development Kit (JDK)** 1.8 or higher. You can download it from [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- **Apache Maven** 3.6.0 or higher. You can download it from [here](https://maven.apache.org/download.cgi).


To check that you have Java and Maven installed, you can run the following commands in your terminal:
```bash
java -version
mvn -version

```

## Compilation

To compile the program, navigate to the project's root directory and run the following command:

```bash
mvn clean package
```

This command will compile the program and create a JAR file in the `target` directory.

## Running the Program

To run the program, navigate to the `target` directory and run the following command:

```bash
java -jar ExpenseTracker-1.0-SNAPSHOT-jar-with-dependencies.jar add --description "food" --amount 10
```

This command will add a new expense with the name "food" to the expense list.

if you want to simplify execution, you can create a shell script file with the following content:

### Linux or MacOS

Create a file named `expense-tracker` with the following content:

```bash
#!/bin/bash
java -jar "/path/to/ExpenseTracker-1.0-SNAPSHOT-jar-with-dependencies.jar" "$@"
```
Give the file execution permissions:

```bash
chmod +x expense-tracker
```

Move the file to a directory in your PATH, for example `/usr/local/bin`:

```bash
sudo mv expense-tracker /usr/local/bin
```

Now you can run the program by typing `expense-tracker` in your terminal:

```bash
expense-tracker add --description "food" --amount 10
```

Create a batch file with the following content:
### Windows
```bash
@echo off
java -jar "/path/to/ExpenseTracker-1.0-SNAPSHOT-jar-with-dependencies.jar" %*
```

Save the file as `expense-tracker.bat` and move it to a directory in your PATH.

Now you can run the program by typing `expense-tracker` in your terminal:

```bash
expense-tracker add --description "food" --amount 10
```

## Usage

The program supports the following commands:

- `add --description <description> --amount <amount>`: Add a new expense to the expense list.
- `update --id <expense-id> --description <description>`: Update description of the expense with the given ID.
- `update --id <expense-id> --amount <amount>`: Update amount of the expense with the given ID.
- `update --id <expense-id> --description <description> --amount <amount>`: Update description and amount of the expense with the given ID.
- `delete --id <expense-id>`: Delete the expense with the given ID.
- `list`: List all expenses in the expense list.
- `summary`: Show the total amount of expenses.
- `summary --month <month>`: Show the total amount of expenses for the given month.

## Example

Here is an example of how to use the program:

```bash
expense-tracker add --description "food" --amount 10
expense-tracker add --description "rent" --amount 100
expense-tracker list
expense-tracker summary
expense-tracker update --id 1 --description "groceries"
expense-tracker delete --id 2
expense-tracker list
expense-tracker summary --month 5
```

