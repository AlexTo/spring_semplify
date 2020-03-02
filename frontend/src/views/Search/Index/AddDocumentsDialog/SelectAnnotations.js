import React, {Fragment, useState} from "react";
import {
  Button
} from "@material-ui/core";
import SelectAnnotationsDialog from "./SelectAnnotationsDialog";

function SelectAnnotations({label, suggestedAnnotations, onAnnotationsSelected, onAnnotationAdded}) {

  const [selectAnnotationsDialogOpen, setSelectAnnotationsDialogOpen] = useState(false);
  const [selectedAnnotations, setSelectedAnnotations] = useState(suggestedAnnotations);

  const handleAnnotationsSelected = (selectedAnnotations) => {
    setSelectedAnnotations(selectedAnnotations);
    onAnnotationsSelected(selectedAnnotations);
  };

  return (
    <Fragment>
      <Button
        color="primary"
        size="small"
        variant="outlined"
        onClick={() => setSelectAnnotationsDialogOpen(true)}>
        {`${selectedAnnotations.length} annotations`}
      </Button>
      <SelectAnnotationsDialog label={label}
                               suggestedAnnotations={suggestedAnnotations}
                               open={selectAnnotationsDialogOpen}
                               onAnnotationsSelected={handleAnnotationsSelected}
                               onCancel={() => setSelectAnnotationsDialogOpen(false)}
                               onAnnotationAdded={onAnnotationAdded}/>
    </Fragment>)
}

export default SelectAnnotations;
