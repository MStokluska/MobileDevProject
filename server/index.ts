import express from 'express';
import { ApolloServer, gql } from 'apollo-server-express';
import { chats } from './db';
import cors from 'cors';
import schema from './schema';

const app = express();
app.use(express.json());

app.use(cors());

app.get('/_ping', (req, res) => {
  res.send('pong');
});

app.get('/chats', (req, res) => {
  res.json(chats);
});

const server = new ApolloServer({ schema });
server.applyMiddleware({
  app,
  path: '/graphql',
});

const port = process.env.PORT || 4000;
app.listen(port, () => {
  console.log(`Server is listening on port ${port}`);
});
