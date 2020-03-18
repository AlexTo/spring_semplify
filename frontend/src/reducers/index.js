import {combineReducers} from 'redux';
import searchReducer from './searchReducer';
import entityReducer from "./entityReducer";
import taskReducer from "./taskReducer";

const rootReducer = combineReducers({
  searchReducer: searchReducer,
  entityReducer: entityReducer,
  taskReducer: taskReducer
});

export default rootReducer;
