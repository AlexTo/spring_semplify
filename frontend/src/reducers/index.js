import {combineReducers} from 'redux';
import searchReducer from './searchReducer';

const rootReducer = combineReducers({
  searchReducer: searchReducer
});

export default rootReducer;
