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
                        creator varchar(50) NOT NULL,
                        recipent varchar(50) NOT NULL
    );`);
  await pool.query(sql`CREATE TABLE messages(
                         id SERIAL PRIMARY KEY,
                         mcreator varchar(50),
                         mchat integer,
                         content varchar(255) not null
    );`);
}
