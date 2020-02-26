export const ENTITY_POPUP_SUMMARY = 'ENTITY_POPUP_SUMMARY';

export const entityActions = {
  showPopupSummary,
  hidePopupSummary
};

function showPopupSummary(uri) {
  return dispatch => {
    dispatch({type: ENTITY_POPUP_SUMMARY, popupSummaryOpen: true, popupSummaryUri: uri})
  }
}

function hidePopupSummary() {
  return dispatch => {
    dispatch({type: ENTITY_POPUP_SUMMARY, popupSummaryOpen: false, popupSummaryUri: null})
  }
}

