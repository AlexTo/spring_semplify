export const SEARCH_QUERY = 'SEARCH_QUERY';

export const searchActions = {
  search
};


function search(query) {
  return dispatch => {
    dispatch({type: SEARCH_QUERY, query: query})
  }
}
