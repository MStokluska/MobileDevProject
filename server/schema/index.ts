import { typeDefs } from './typeDefs'
import { makeExecutableSchema } from 'graphql-tools';
import resolvers from './resolvers';

export default makeExecutableSchema({ typeDefs, resolvers});