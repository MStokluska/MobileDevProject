import express from 'express';
import { ApolloServer, gql, PubSub } from 'apollo-server-express';
import { pool } from './db';
import cors from 'cors';
import schema from './schema';
import http from 'http';
import { port } from './env';

const app = express();
app.use(express.json());

app.use(cors());

const pubsub = new PubSub();

const server = new ApolloServer({
  schema,
  context: async () => {
    let db;
    db = await pool.connect();
    
    return {
      pubsub,
      db,
    };
  },
  formatResponse: (res: any, { context }: any) => {
    context.db.release();
    return res;
  },
});

server.applyMiddleware({
  app,
  path: '/graphql',
});

const httpServer = http.createServer(app);
server.installSubscriptionHandlers(httpServer);

httpServer.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});
