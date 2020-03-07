import { Pool } from 'pg';

export type User = {
  id: string,
  name: string,
  password: string
}

export type Chat = {
  id: string,
  creator: string,
  recipent: string
}

export type Message = {
  id: string,
  mcreator: string,
  mchat: string,
  content: string
}

export const dbConfig = {
  host: 'localhost',
  port: process.env.DB_PORT ? parseInt(process.env.DB_PORT) : 5432,
  user: 'testuser',
  password: 'testpassword',
  database: 'mobiledb',
};
export let pool: Pool = new Pool(dbConfig);

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
