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
    getAllChats: [Chat!]!
    getAllMessages: [Message!]!
}
`