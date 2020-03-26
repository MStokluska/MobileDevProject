import { Resolvers, Chat } from '../types/graphql';
import sql from 'sql-template-strings';
import { UserInputError, PubSub } from 'apollo-server-express';
import { withFilter } from 'graphql-subscriptions';
import { pool } from '../db';

const resolvers: Resolvers = {
  Chat: {
    async creator(chat, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM users where users.id = ${chat.creator}
      `);

      return rows[0] || null;
    },

    async recipent(chat, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM users where users.id = ${chat.recipent}
      `);
      return rows[0] || null;
    },
  },

  Message: {
    async mcreator(message, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM users where users.id = ${message.mcreator}
      `);
      return rows[0];
    },
    async mchat(message, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM chats where chats.id = ${message.mchat}
      `);
      return rows[0];
    },
  },

  Query: {
    async getAllUsers(root, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM users
      `);
      return rows;
    },
    async getUser(root, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM users WHERE users.username = ${args.userName}
      `);
      return rows[0];
    },

    async checkUser(root, args, { db }) {
      const { rows } = await db.query(
        sql`SELECT * FROM users WHERE username = ${args.userName} AND password = ${args.password}`
      );

      if (rows[0] == null) {
        throw new UserInputError('Incorrect username or password');
      }

      return rows[0];
    },

    async getAllChats(root, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM chats
      `);
      return rows;
    },
    async getChat(root, args, { db }) {
      const { rows } = await db.query(sql`
      SELECT * FROM chats WHERE chats.id = ${args.chatId}
    `);
      return rows[0];
    },
    async getChatForUser(root, args, { db }) {
      const { rows } = await db.query(sql`
      SELECT * FROM chats WHERE chats.creator = ${args.userId}
      `);
      return rows;
    },
    async getAllMessages(root, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM messages
      `);
      return rows;
    },
    async getAMessage(root, args, { db }) {
      const { rows } = await db.query(sql`
      SELECT * FROM messages WHERE messages.id = ${args.messageId}
    `);
      return rows[0];
    },

    async getMessagesForChatRoom(root, args, { db }) {
      const { rows } = await db.query(sql`
      SELECT * FROM messages WHERE messages.mchat = ${args.chatRoomId}
    `);
      return rows;
    },
  },

  Mutation: {
    async createUser(root, args, { db }) {
      const existingUserQuery = await db.query(
        sql`SELECT * FROM users WHERE username = ${args.username}`
      );
      if (existingUserQuery.rows[0]) {
        throw new UserInputError('username already exists');
      }

      const createUserQuery = await db.query(sql`
        INSERT INTO users(username, name, password) values(${args.username},${args.name},${args.password})  RETURNING *;
      `);

      const user = createUserQuery.rows[0];
      return user;
    },
    async deleteUser(root, args, { db }) {
      const deleteUserQuery = await db.query(sql`
      DELETE FROM users where users.username = ${args.username} RETURNING *;
      `);
      return deleteUserQuery.rows[0].username;
    },

    async editUser(root, args, { db }) {
      const updateQuery = await db.query(sql`
      UPDATE users
      SET username = ${args.username},
          name = ${args.name},
          password = ${args.password}
      WHERE
          users.id = ${args.id} RETURNING *;
      `);
      return updateQuery.rows[0];
    },

    async createChat(root, args, { pubsub, db }) {
      const { rows } = await db.query(sql`
        INSERT INTO chats(creator, recipent) values(${args.creator},${args.recipent})  RETURNING *
      `);

      const chatAdded = rows[0];

      pubsub.publish('chatAdded', {
        chatAdded,
      });

      console.log(chatAdded);
      return chatAdded;
    },
    async deleteChat(root, args, { pubsub, db }) {
      const deleteChatQuery = await db.query(sql`
      DELETE FROM chats where chats.id = ${args.id} RETURNING *;
      `);

      const chatDeleted = deleteChatQuery.rows[0].id;
      pubsub.publish('chatDeleted', {
        chatDeleted,
      });

      return chatDeleted;
    },

    async createMessage(root, args, { pubsub, db }) {
      const createMessageQuery = await db.query(sql`
        INSERT INTO messages(mcreator, mchat, content) values(${args.mcreator},${args.mchat}, ${args.content})  RETURNING *;
      `);

      const message = createMessageQuery.rows[0];

      pubsub.publish('messageAdded', {
        messageAdded: message,
      });

      return message;
    },
  },
  Subscription: {
    chatDeleted: {
      subscribe: (root, args, { pubsub }) =>
        pubsub.asyncIterator('chatDeleted'),
    },
    messageAdded: {
      subscribe: (root, args, { pubsub }) =>
        pubsub.asyncIterator('messageAdded'),
    },
    chatAdded: {
      subscribe: (root, args, { pubsub }) => pubsub.asyncIterator('chatAdded'),
    },
  },
};

export default resolvers;
