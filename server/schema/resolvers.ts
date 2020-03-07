import { chats, users, message } from '../db';

const resolvers = {

    Chat : {
        creator(chat: any){
            return users.find(u => u.id === chat.creator)
        },
        recipent(chat: any){
            return users.find(u => u.id === chat.recipent)
        }
    },

    Message : {
        mcreator(message: any){
            return users.find(u => u.id === message.mcreator)
        },
        mchat(message: any){
            return chats.find(u => u.id === message.mchat)
        }
    },

  Query: {
    getAllUsers(){
        return users;
    },
    getAllChats(){
        return chats;
    },
    getAllMessages(){
        return message;
    }

  },
};
export default resolvers;