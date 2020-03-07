import { chats, users, message, pool } from '../db';
import { Resolvers } from '../types/graphql';
import sql from 'sql-template-strings';

const resolvers: Resolvers = {
  Chat: {
    creator(chat) {
      return users.find(u => u.id === chat.creator);
    },
    recipent(chat) {
      return users.find(u => u.id === chat.recipent);
    },
  },

  Message: {
    mcreator(message) {
      return users.find(u => u.id === message.mcreator);
    },
    mchat(message) {
      return chats.find(u => u.id === message.mchat);
    },
  },

  Query: {
    async getAllUsers(root, args, { db }) {
      const { rows } = await db.query(sql`
        SELECT * FROM users
      `);
      return rows;
    },
    getAUser(root, { userId }, context) {
      return users.find(u => u.id === userId);
    },
    getAllChats(root, context) {
      return chats;
    },
    getAChat(root, { chatId }, context) {
      return chats.find(c => c.id === chatId);
    },
    getAllMessages(root, context) {
      return message;
    },
    // getAMessage(root, { messageId }, context) {
    //   return message.find(m => m.id === messageId);
    // },
    getMessagesForChatRoom(root, { chatRoomId }, context) {
      const list = message.filter(m => m.mchat === chatRoomId);
      return list;
    },
  },

  Mutation: {
    createUser(root, args, context) {
      const newUser = {
        id: args.id,
        name: args.name,
        password: args.password,
      };

      users.push(newUser);
      return newUser;
    },
    deleteUser(root, args, context) {
      const userIndex = users.findIndex(u => u.id === args.id);
      users.splice(userIndex, 1);
      return args.id;
    },
    editUser(root, args, context) {
      const userIndex = users.findIndex(u => u.id === args.id);
      users.splice(userIndex, 1);
      const newUser = {
        id: args.id,
        name: args.name,
        password: args.password,
      };
      users.push(newUser);

      return newUser;
    },
    createChat(root, args, context) {
      const newChat = {
        id: args.id,
        creator: args.creator,
        recipent: args.recipent,
      };

      chats.push(newChat);

      return newChat;
    },
    deleteChat(root, args, context) {
      const chatIndex = chats.findIndex(c => c.id === args.id);
      chats.splice(chatIndex, 1);
      return args.id;
    },
    createMessage(root, args, { pubsub }) {
      const newMessage = {
        id: args.id,
        mcreator: args.mcreator,
        mchat: args.mchat,
        content: args.content,
      };
      message.push(newMessage);

      pubsub.publish('messageAdded', {
        messageAdded: newMessage,
      });

      return newMessage;
    },
  },
  Subscription: {
    messageAdded: {
      subscribe: (root, args, { pubsub }) =>
        pubsub.asyncIterator('messageAdded'),
    },
  },
};

export default resolvers;
