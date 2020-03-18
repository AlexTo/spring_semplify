import {TASK_TABLE_SET_PAGE, TASK_TABLE_SET_PAGE_SIZE} from "../actions";

const initialState = {
  page: 0,
  pageSize: 20
};

const taskReducer = (state = initialState, action) => {
  switch (action.type) {
    case TASK_TABLE_SET_PAGE: {
      const {page, ...others} = state;
      return {
        page: action.page,
        ...others
      }
    }
    case TASK_TABLE_SET_PAGE_SIZE: {
      const {pageSize, ...others} = state;
      return {
        pageSize: action.pageSize,
        ...others
      }
    }
    default: {
      return state;
    }
  }
};

export default taskReducer;
