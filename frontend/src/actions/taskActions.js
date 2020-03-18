export const TASK_TABLE_SET_PAGE = 'TASK_TABLE_SET_PAGE';
export const TASK_TABLE_SET_PAGE_SIZE = 'TASK_TABLE_SET_PAGE_SIZE';

export const taskActions = {
  setPage,
  setPageSize,
};

function setPage(page) {
  return dispatch => {
    dispatch({type: TASK_TABLE_SET_PAGE, page});
  }
}

function setPageSize(pageSize) {
  return dispatch => {
    dispatch({type: TASK_TABLE_SET_PAGE_SIZE, pageSize});
  }
}
