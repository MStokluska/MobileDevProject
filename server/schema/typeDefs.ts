export const typeDefs = ` 
type User{
    id: ID!
    username: String!
    name: String!
    password: String!
}

type Chat{
    id: ID!
    creator: User!
    recipent: User!
}

type Message{
    id: ID!
    mcreator: User!
    mchat: Chat!
    content: String!
}

type Query{

    getAllUsers: [User!]!
    getUser(userName: String): User!

    getAllChats: [Chat!]!
    getChat(chatId: ID!): Chat!

    getAllMessages: [Message!]!
    getAMessage(messageId: ID!): Message!
    getMessagesForChatRoom(chatRoomId:ID!): [Message!]!
}

type Mutation{
    createUser(username: String!, name: String!, password: String!): User
    deleteUser(username: String!): String
    editUser(id: ID!, username: String!, name: String!, password: String!): User

    createChat(creator:ID!, recipent:ID!): Chat!
    deleteChat(id:ID!): ID!

    createMessage(mcreator:ID!,mchat:ID!,content:String!): Message!
}

type Subscription {
    messageAdded: Message!
  }
`;