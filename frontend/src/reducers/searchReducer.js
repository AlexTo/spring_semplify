import * as actionTypes from '../actions';

const initialState = {
  query: '',
  page: 0,
  size: 12
};

const searchReducer = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.SEARCH_QUERY: {
      const {query, ...others} = initialState;
      return {
        query: action.query,
        ...others
      };
    }

    default: {
      return state;
    }
  }
};

export default searchReducer;
