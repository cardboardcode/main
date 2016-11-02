<!--@@author A0139422J -->
![T-T](images/T-T.png)

# **USER GUIDE** 
---
##**Contents**
*   [About](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#about)
*   [Quick Start](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#quick-start)
*   [Commands](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#commands)
    -   [Add a Task](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#1-add-a-task)
    -   [Edit a Task](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#2-edit-a-task)
    -   [Delete a Task](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#3-delete-a-task)
    -   [Done Task]
    -   [List Tasks](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#4-list-tasks)
    -   [Undo]
    -   [Redo]
    -   [Search for Tasks]
    -   [Storage]
    -   [Help Command](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#5-help-command)
*   [Unique Features]
    -   [List Statistics]
    -   [Overdue Detection]
    -  [Autocompletion]
    -   [Color Toggling]
    -   [Minimize window]
*   [Cheat Sheet](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#liststatistics)
*   [FAQ](https://github.com/CS2103AUG2016-T09-C3/main/blob/documents/docs/UserGuide.md#liststatistics)

## **About**

**_Task Tracker_ (T-T)** is an easy-to-use software which helps you manage your daily tasks. It is a simple and fun product which uses a creative Graphical User Interface to make the operations quick, easy and intuitive.

## **Quick Start**

T-T is a lightweight app which does not require much time to install and run. 

> Ensure you have Java version `1.8.0_60` or later installed on your PC.

In order to install T-T, 

1. Download the T-T files from https://github.com/t09-c3/task-tracker
2. Double-click on the T-T.jar file to start the app.<br>

## **Commands**

`<>` _indicates that field is compulsory_ and `[]` _indicates that field is optional._ 

### 1. **ADD** a Task

The format for adding a new task is: <br>
&nbsp; &nbsp;   `add <task> [date1] [date2] [-h | -m | -l]` <br>

<!-- @@author A0144132W -->
*  T-T can accept flexible date formats. Examples are:
    *  `today`
    *  `monday`(to set the date as next monday)
    *  `31 Oct`
*   If **no date** is specified, the task will be assumed to be a **floating task** which is an action whose deadline is indefinite.
*   You do not need to specify any dates. You can specify only 1 date or 2 dates.
    *   If **1 date** is written, it would be taken as the deadline.
    -   If **2 date** is written, they would be the start and end times for the task.

*   `[-h | -l]` refers to the priority of the task. 
    *   `-h` means **high** priority. 
    *   `-m` means **medium** priority.
    *   `-l` means **low** priority.
    *   If not specified, priority is **medium**.
    *   Except for `<task>`, the other parameters can be inputed in any order.

<br>For example:<br>
*   `•  add wash dishes monday 4pm -h ` will create a high priority `wash dishes` task to be done on the **next Monday**.
*   `•  add call mum tmr ` will create a task named `call mum` to be done anytime on **the next day** with **normal** priority.<br>
![Add Screen](images/Add_Command2.png)
<br>
![Add Screen](images/Add_Command.png)

### 2. **EDIT** a Task

The format to edit an existing task is : <br>
&nbsp; &nbsp; `edit <task index> <new task> [new date1] [new date2] [-h | -m | -l] ` <br>

  * `<task index>` can be obtained from the list panel, or by using the list command (See **List** command).

<br> For example: <br>

Let’s say task index “**1**” refers to the task `wash dishes monday 4pm`

*  `edit 1 wash dishes tuesday 6pm -h` will edit the existing `wash dishes` task to `use dishes` and change the date from **monday** to **tuesday** , time from **4pm** to **6pm** and priority from **medium** to **high**.

### 3. **DELETE** a Task

The format to delete an existing task is : <br>
&nbsp; &nbsp; `delete <task index>` <br>

>   *  `<task index>` can be obtained from the list panel, or by using the list 


<br> For example: <br>

Let’s say task index “**1**” refers to the task `wash dishes monday 4pm`

*   `delete 1` will delete the aforementioned `wash dishes` task.

<!--@@author A0142686X -->

### 4. **DONE** TASK

Tasks can be marked done after they have been completed. Tasks marked as done will not appear in the main list, but can be viewed by the list command (see LIST).<br>

The format to mark a task as done is : <br>
&nbsp; &nbsp; `done <task index>` <br>

>   *  `<task index>` can be obtained from the list panel, or by using the list 

<br> For example: <br>

Let’s say task index “**1**” refers to the task `do laundry monday 4pm`

*   `done 1` will mark the `do laundry` task as done.

### 5. **LIST** Tasks

Tasks can be listed according to their priority, date when they are due or if they are marked done.
The format to edit an existing task is : <br>
&nbsp; &nbsp; `list [date | priority | done]`<br>

>   *   The tasks will be shown based on the parameters specified. If no parameters given, the tasks are organized by date.
>   *   done parameter will list all the tasks marked done.
>   *   Both date and priority can be specified at the same time.

<br> For example : <br>
`list 9 Oct`  will display all tasks due on 9th October.
`list high` will display all tasks of high priority.
`list` will display all tasks organised by date.
`list high today` will display all tasks of high priority due today.
`list done` will display all the tasks marked done.
`list done today` will display all the done tasks which were due today.

<br>
![List Screen](images/List_Command.png)

### 6. **UNDO**

Enter `undo` into the command line to undo the previous commands you have entered in the session.  

<br> For example : <br>
Entering `undo` after executing `delete 2` will bring back the deleted task.

### 7. **REDO**

Enter `redo` into the command line to redo the previous undo you have executed in the session.  

### 8. **SEARCH** for Tasks

The format to search for certain tasks is :  
    `search <keyword>`  

*   The tasks you are looking for can be obtained by typing in any word that is contained within the tasks' messages.

For example:  

Let say task index **1** refers to the task `wash dishes monday 4pm`

*   `search dishes` will show the existing `wash dishes` task.

### 9. **STORAGE** 
The storage command can be used to change the storage location of tasks. Tasks are stored on the PC in an XML file.

The format to change storage location is :
    `storage <filepath>`

<br> For example : <br>

Entering `storage C://users/JohnDoe/NewFile.xml` will change the storage location to NewFile.xml.

>   *  Filepath must be of an XML file.
>   *  If the XML file mentioned at the filepath does not exist, a new XML file will be created by TaskTracker at the specified location.
>   *  All data of the pre-existing tasks will be copied to the new XML file.


### 10. **HELP**

Enter `help` into the command line to list out all the commands available in T-T.
<br>
<br>
<!-- @@author A0139422J -->
## **Unique Features**

### 1. List Statistics
**T-T** also provides an intuitive listing the number of tasks stored, giving you a clearer idea on how to better deal with them. As seen in **the example below**, the numbers are displayed on the right of the task list. 

**T-T**'s list statistics provides data on all the different types of tasks available in storage:

    -      Today's Tasks 
    -      Tomorrow's Tasks
    -      Event Tasks (Tasks with parameters all filled.)
    -      Deadline Tasks (Tasks which are much due much later)
    -      Floating Tasks (Tasks with incomplete parameters)  
<br>
![List Screen](images/List_Statistics.png)
<br>

### 2. Overdue Detection
Apart from the three basic colors shown earlier to indicate different priorities, T-T also indicates to users which tasks have become overdued. If the current time has overshot an event task's endtime or a deadline task's deadline, the corresponding task will be shown with a black tab, as shown below. 

### 3. Autocompletion
Users who find typing in the many commands **T-T** provides too tedious may opt to use the autocompletion function the app has as well. Simply press the `Tab` key and you will be able to cycle through all the different command. 

If you already have a command in mind, the function will be able to know simply from the first few letter you enter. 

For example:  

Let say you were to enter a letter `d` in the **T-T** command box, by pressing `Tab`, the following commands can be toggled to easily.

*   `delete`
*   `done`

However, the autocompletion does not stop there. Sometimes users do need to reenter past commands. By pressing the `UP` and `DOWN` arrow keys, you can also access your most recent commands without having to tediously type it out again. 

### 4. Color Toggling
The user interface is not just limited to just one color. **T-T** offers you a variety of seven color themes to choose from. 

By pressing `F1` and `F2`, you can toggle between the different themes and choose any which suit your preference. 

### 5. Minimize Window
Once you are done with what you need to do in **T-T**, you can simply press `Esc` to minimize the window.

<!-- @@author A0139750B -->
## **Cheat Sheet**
Getting the hang of it? Here’s a quick and dirty summary of all **T-T** commands

| Commands  | Function  | Usage  |
|---|---|---|
| add  |  Adds a new task to the list | `add question life 14 Oct 1045 -l` |
| edit  | Edits an existing task  | `edit 1 review UG tuesday 10am -h`  |
| delete  | Deletes an existing task  |  `delete 1`    |
| done  | Marks a task as done | `done 1` |
| list  | Sorts and displays the list of existing tasks  | `list high today` |
| undo  | Undoes the previous command  | `undo` |
| redo  | Redoes the previous undo | `redo` |
| search  | Searchs for tasks that contain a designated keyword  | `search meaning` |
| storage | Changes the location of the XML file in which tasks are stored |`storage C://Users/Desktop/newfile.xml` |
|  help | Displays the list of commands available on T-T  |  `help` |

<br>
<br>
<!-- @@author A0139422J -->
| Keyboard Shortcuts  | Function  |
|---|---|
| `UP`/`DOWN`   |  Toggles history of recent commands  |
| `F1`/`F2`  | Toggles **T-T** color theme |
| `Tab`  | Autocompletes user input |
| `Esc`  | Minimizes app |

## **FAQ**

**Q**: __How do I transfer my data to another Computer?__

**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Address Book folder. <br>
<br>
**Q**: __How do check if I have the correct Java Version?__ 

**A**:  
     1. Click Start on the task bar. 
     2. Select Control Panel (or Settings > Control Panel) from the Start menu. The Control Panel is displayed. 
     3. Select Java. The Java Control Panel dialog box is displayed.
     4. NOTE: if the Control Panel is in Category mode and you cannot see the Java option, switch the Control Panel to Classic View. 
     5. Click the Java tab. 9 of 9 6. In the Java Application Runtime Setting box, click View. The JNLP Runtime Settings dialog box is displayed.
     6. In the Java Application Runtime Setting box, click View. The JNLP Runtime Settings dialog box is displayed.
<br>

If you encounter any further issues using T-T, please contact us at:
    **tasktrackerT-T@gmail.com**

