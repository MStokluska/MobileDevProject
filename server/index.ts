import { ApolloServer } from 'apollo-server-express';
import { pool, pubsub } from './db';
import schema from './schema';
import http from 'http';
import { port, origin } from './env';
import { app } from './app';
import { MyContext } from './context';

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
  formatResponse: (res: any, { context }: { context: any }) => {
    context.db.release();
    return res;
  },
});

server.applyMiddleware({
  app,
  path: '/graphql',
  cors: { origin },
});

const httpServer = http.createServer(app);
server.installSubscriptionHandlers(httpServer);

httpServer.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});
