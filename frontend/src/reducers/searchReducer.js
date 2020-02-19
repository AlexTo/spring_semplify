import * as actionTypes from '../actions';
import {SEARCH_STATE_NEW_SEARCH} from "../actions";

const initialState = {
  query: '',
  searchState: SEARCH_STATE_NEW_SEARCH,
  buckets: [],
  page: 0,
  size: 12
};

const searchReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.SEARCH_QUERY: {
      const {query, searchState, ...others} = state;
      return {
        query: action.query,
        searchState: SEARCH_STATE_NEW_SEARCH,
        ...others
      };
    }
    case actionTypes.SEARCH_BUCKETS_UPDATE: {
      const {buckets, ...others} = state;
      return {
        buckets: action.buckets,
        ...others
      }

    }
    default: {
      return state;
    }
  }
};

export default searchReducer;
