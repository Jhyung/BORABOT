import { combineReducers } from 'redux';

import { logInOut } from './logInOut';
import { strategy } from './strategy';
import { exchange } from './exchange';
import { sales } from './sales';

const reducers = combineReducers({
  logInOut,
  strategy,
  exchange,
  sales
});

export default reducers;