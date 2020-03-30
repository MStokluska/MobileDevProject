import { Pool } from 'pg';
import { createDb as envCreateDb } from './env';
import sql from 'sql-template-strings';
const { PostgresPubSub } = require('graphql-postgres-subscriptions');

export const dbConfig = {
  host: 'localhost',
  port: process.env.DB_PORT ? parseInt(process.env.DB_PORT) : 5432,
  user: 'testuser',
  password: 'testpassword',
  database: 'mobileapp',
};

export let pool: Pool = new Pool(dbConfig);

export const pubsub = new PostgresPubSub({
  host: 'localhost',
  port: process.env.DB_PORT ? parseInt(process.env.DB_PORT) : 5432,
  user: 'testuser',
  password: 'testpassword',
  database: 'mobileapp',
});

if (envCreateDb) {
  create();
}

async function create() {
  await pool.query(sql`DROP TABLE IF EXISTS users CASCADE;`);
  await pool.query(sql`DROP TABLE IF EXISTS chats CASCADE;`);
  await pool.query(sql`DROP TABLE IF EXISTS messages CASCADE;`);
  await pool.query(sql`CREATE TABLE users(
                        id SERIAL PRIMARY KEY,
                        username varchar(50) not null,
                        name varchar(50) not null,
                        password varchar(50) not null
                        );`);
  await pool.query(sql`CREATE TABLE chats(
                        id SERIAL PRIMARY KEY,
                        creator integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        recipent integer NOT NULL REFERENCES users(id) ON DELETE CASCADE
    );`);
  await pool.query(sql`CREATE TABLE messages(
                         id SERIAL PRIMARY KEY,
                         mcreator integer REFERENCES users(id) ON DELETE CASCADE,
                         mchat integer REFERENCES chats(id) ON DELETE CASCADE,
                         content varchar(255) not null
    );`);
}
