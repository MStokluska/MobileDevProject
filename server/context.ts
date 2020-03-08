import { PubSub } from 'apollo-server-express';
import { PoolClient } from 'pg';

export type MyContext = {
  pubsub: PubSub;
  db: PoolClient;
};
