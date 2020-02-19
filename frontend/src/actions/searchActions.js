export const SEARCH_QUERY = 'SEARCH_QUERY';
export const SEARCH_STATE_NEW_SEARCH = 'SEARCH_STATE_NEW_SEARCH';
export const SEARCH_BUCKETS_UPDATE = 'SEARCH_BUCKETS_UPDATE';

export const searchActions = {
  search,
  updateBuckets
};


function search(query) {
  return dispatch => {
    dispatch({type: SEARCH_QUERY, query: query})
  }
}

function updateBuckets(buckets) {
  return dispatch => {
    dispatch({type: SEARCH_BUCKETS_UPDATE, buckets})
  }
}




