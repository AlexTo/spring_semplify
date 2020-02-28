import * as actionTypes from '../actions';
import {SEARCH_STATE_NEW_SEARCH} from "../actions";
import {SEARCH_STATE_FILTERED_SEARCH} from "../actions";

const initialState = {
  query: '',
  searchState: SEARCH_STATE_NEW_SEARCH,
  buckets: [],
  selectedAnnotations: [],
  page: 0,
  size: 9
};

const searchReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.SEARCH_QUERY: {
      const {query, selectedAnnotations, searchState, ...others} = state;
      return {
        query: action.query,
        selectedAnnotations: [],
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
    case actionTypes.SEARCH_APPLY_FILTER: {
      const {selectedAnnotations, searchState, page, size, ...others} = state;
      return {
        selectedAnnotations: action.selectedAnnotations,
        page: action.page,
        size: action.size,
        searchState: SEARCH_STATE_FILTERED_SEARCH,
        ...others
      }
    }

    default: {
      return state;
    }
  }
};

export default searchReducer;
