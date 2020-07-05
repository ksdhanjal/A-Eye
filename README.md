#  A-Eye

A-Eye is a smart attendance monitoring system which can primarily be used in educational institutions to record attendance. It reduces the time taken in this process drastically.

## System Archetecture

- The app sends REST request to the web-server to get the basic parameters like department, course, subject etc.
- When the user starts taking attendance, the app streams the video to the NGINX streaming media server via RTMP which is sent to web-server
-  On the web-server, machine learning algorithms are hosted which processes the video and generates the attendance report which is saved to the database

## Working

1. Taking Attendance: 
	- The user fills in basic parameters like subject, period, semester etc.
	- Camera opens within the app and the user presses start
	- Phone is moved around the class by the teacher to register all the students and attandance is marked simultaneously

2. Reviewing Attendance
	- The user fills in basic parameters like subject, period, semester etc. and presses the "Review Attandance" button and attendance report is shown

## Acknowledgements

- Server side component by [007akshitsaxena](https://github.com/007akshitsaxena/A-Eye)
- RTMP streaming is made possible with the help of [LiveVideoBroadcaster](https://github.com/ant-media/LiveVideoBroadcaster) by ant-media
