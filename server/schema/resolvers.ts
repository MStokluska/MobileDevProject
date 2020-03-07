import { chats, users, message } from '../db';
import { Resolvers } from '../types/graphql';

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
    getAllUsers() {
      return users;
    },
    getAUser(root, { userId }) {
      return users.find(u => u.id === userId);
    },
    getAllChats() {
      return chats;
    },
    getAChat(root, { chatId }) {
      return chats.find(c => c.id === chatId);
    },
    getAllMessages() {
      return message;
    },
    getAMessage(root, { messageId }) {
      return message.find(m => m.id === messageId);
    },
    getMessagesForChatRoom(root, { chatRoomId }) {
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
    createMessage(root, args, context) {
      const newMessage = {
        id: args.id,
        mcreator: args.mcreator,
        mchat: args.mchat,
        content: args.content,
      };
      message.push(newMessage);

      return newMessage;
    },
  },
};

export default resolvers;
