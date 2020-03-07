export const typeDefs = ` 
type User{
    id: ID!
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
    getAUser(userId: ID!): User!

    getAllChats: [Chat!]!
    getAChat(chatId: ID!): Chat!

    getAllMessages: [Message!]!
    getAMessage(messageId: ID!): Message!
    getMessagesForChatRoom(chatRoomId:ID!): [Message!]!
}

type Mutation{
    createUser(id: ID!, name: String!, password: String!): User
    deleteUser(id: ID!): ID!
    editUser(id: ID!, name: String!, password: String!): User

    createChat(id:ID!, creator:ID!, recipent:ID!): Chat!
    deleteChat(id:ID!): ID!

    createMessage(id:ID!,mcreator:ID!,mchat:ID!,content:String!): Message!
}

type Subscription {
    messageAdded: Message!
  }
`;
