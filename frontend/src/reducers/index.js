import {combineReducers} from 'redux';
import searchReducer from './searchReducer';
import entityReducer from "./entityReducer";

const rootReducer = combineReducers({
  searchReducer: searchReducer,
  entityReducer: entityReducer
});

export default rootReducer;
