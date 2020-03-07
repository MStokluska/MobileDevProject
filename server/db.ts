import { Pool } from 'pg';
import { createDb as envCreateDb } from './env';
import sql from 'sql-template-strings';

export const dbConfig = {
  host: 'localhost',
  port: process.env.DB_PORT ? parseInt(process.env.DB_PORT) : 5432,
  user: 'testuser',
  password: 'testpassword',
  database: 'mobileapp',
};
export let pool: Pool = new Pool(dbConfig);

if (envCreateDb) {
  create();
}

async function create() {
  await pool.query(sql`CREATE TABLE users(
                        id integer PRIMARY KEY,
                        name varchar(50) not null,
                        password varchar(50) not null
                        );`);
  await pool.query(sql`CREATE TABLE chats(
                        id integer PRIMARY KEY,
                        creator integer NOT NULL REFERENCES users(id),
                        recipent integer NOT NULL REFERENCES users(id)
    );`);
  await pool.query(sql`CREATE TABLE messages(
                         id integer PRIMARY KEY,
                         mcreator integer REFERENCES users(id),
                         mchat integer REFERENCES chats(id),
                         content varchar(255) not null
    );`);
}

export const users = [
  {
    id: '1',
    name: 'Michael',
    password: 'alamo12!',
  },
  {
    id: '2',
    name: 'Ela',
    password: 'alamo12!',
  },
];

export const chats = [
  {
    id: '1',
    creator: '1',
    recipent: '2',
  },
];

export const message = [
  {
    id: '1',
    mcreator: '1',
    mchat: '1',
    content: 'Hi there Ela',
  },
  {
    id: '2',
    mcreator: '2',
    mchat: '1',
    content: 'hi Michael',
  },
];
