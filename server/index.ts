import express from 'express';
import { ApolloServer, gql, PubSub } from 'apollo-server-express';
import { chats } from './db';
import cors from 'cors';
import schema from './schema';
import http from 'http';

const app = express();
app.use(express.json());

app.use(cors());

app.get('/chats', (req, res) => {
  res.json(chats);
});


const pubsub = new PubSub();
const server = new ApolloServer({
  schema,
  context: () => ({ pubsub }),
});

const httpServer = http.createServer(app);
server.installSubscriptionHandlers(httpServer);

server.applyMiddleware({
  app,
  path: '/graphql',
});

const port = process.env.PORT || 4000;
httpServer.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});
