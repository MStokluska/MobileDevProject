# MobileDevProject
A project for Mobile App Development in IT - Year 3.
Below are the instructions how to run the app locally and how will be presented during my demo.

## Demo for Mobile App Dev

To demo I am going to provide you with a server URL. Server is running on Amazon and is behind load balancer and autoscaling.
Please paste the server url into helpers/client `serverURL` variable.

## Running locally

To test locally please follow instruction below:

### Server
In server directory run:

Run following commands:

1. Install dependencies
 ```sh 
$ yarn install
```
2. Run docker db container
```
$ docker-compose up
```
3. Create database & use server ( user CREATE_DB only at initial launch as it recreates db tables )
```sh
$ CREATE_DB=True yarn start
```
4. Navigate to: `localhost:4000/graphql`

4. Alternatively provide PORT env variable to start server at different 

### Client:

Server URL is set in helpers/client. Launch Chattie via Android Studio on any amount of emulators. Create users, log in, create chats and chat!
