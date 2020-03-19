import {combineReducers} from 'redux';
import searchReducer from './searchReducer';
import entityReducer from "./entityReducer";
import taskReducer from "./taskReducer";
import fileAnnotationReducer from "./fileAnnotationReducer";

const rootReducer = combineReducers({
  searchReducer: searchReducer,
  entityReducer: entityReducer,
  taskReducer: taskReducer,
  fileAnnotationReducer: fileAnnotationReducer
});

export default rootReducer;
