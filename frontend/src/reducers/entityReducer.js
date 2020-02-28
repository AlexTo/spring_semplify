import {ENTITY_POPUP_SUMMARY} from "../actions";

const initialState = {
  popupSummaryOpen: false,
  popupSummaryUri: null
};

const entityReducer = (state = initialState, action) => {
  switch (action.type) {
    case ENTITY_POPUP_SUMMARY:
      const {popupSummaryOpen, popupSummaryUri, ...others} = state;
      return {
        popupSummaryOpen: action.popupSummaryOpen,
        popupSummaryUri: action.popupSummaryUri,
        ...others
      };
    default: {
      return state;
    }
  }

};

export default entityReducer;
