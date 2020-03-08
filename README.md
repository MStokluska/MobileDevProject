# MobileDevProject
A project for Mobile App Development in IT - Year 3


### Server
In server directory run:

Run following commands:

1. Install dependencies
 ```sh 
$ yarn install
```
2. Run docker db container
```sh
$ docker run --name mobileapp-postgres -e POSTGRES_DB=mobileapp -e POSTGRES_USER=testuser -e POSTGRES_PASSWORD=testpassword --rm -p5432:5432 postgres 
```
OR 
```
$ docker-compose up
```
3. Create database & use server
```sh
$ CREATE_DB=True yarn start
```
4. Navigate to: `localhost:4000/graphql`

4. Alternatively provide PORT env variable to start server at different 

### Client: