import { typeDefs } from './typeDefs'
import { makeExecutableSchema, IResolvers } from 'graphql-tools';
import resolvers from './resolvers';

export default makeExecutableSchema({ typeDefs, resolvers: resolvers as IResolvers});