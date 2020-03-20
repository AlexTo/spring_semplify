export const FILE_ANNOTATION_TABLE_SET_PAGE = 'FILE_ANNOTATION_TABLE_SET_PAGE';
export const FILE_ANNOTATION_TABLE_SET_PAGE_SIZE = 'FILE_ANNOTATION_TABLE_SET_PAGE_SIZE';
export const FILE_ANNOTATION_STATUS_REVIEWED = "Reviewed";
export const FILE_ANNOTATION_STATUS_PENDING_REVIEW = "Pending Review";

export const fileAnnotationActions = {
  setPage,
  setPageSize,
};

function setPage(page) {
  return dispatch => {
    dispatch({type: FILE_ANNOTATION_TABLE_SET_PAGE, page});
  }
}

function setPageSize(pageSize) {
  return dispatch => {
    dispatch({type: FILE_ANNOTATION_TABLE_SET_PAGE_SIZE, pageSize});
  }
}
