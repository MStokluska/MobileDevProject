import express from 'express';
import cors from 'cors';
import cookieParser from 'cookie-parser';
import { origin } from './env';

export const app = express();
app.use(express.json());
app.use(cookieParser());
app.use(cors({ origin }));

app.get('/_ping', (req, res) => {
  res.send('pong');
});
