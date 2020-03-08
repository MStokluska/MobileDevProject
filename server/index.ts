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

const db = pool.connect();

const pubsub = new PubSub();

const server = new ApolloServer({
  schema,
  context: async (session: any) => {
    let db;
    if (!session.connection) {
      db = await pool.connect();
    }
    return {
      pubsub,
      db,
    };
  },
  subscriptions: {
    onConnect(params, ws, ctx) {
      return {
        request: ctx.request,
      };
    },
  },
  formatResponse: (res: any, { context }: any) => {
    context.db.release();
    return res;
  },
});

const httpServer = http.createServer(app);
server.installSubscriptionHandlers(httpServer);

server.applyMiddleware({
  app,
  path: '/graphql',
});

httpServer.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});
