import React, {Fragment, useState} from "react";
import {
  Button
} from "@material-ui/core";
import SelectAnnotationsDialog from "./SelectAnnotationsDialog";

function SelectAnnotations({label, suggestedAnnotations, onUpdate}) {

  const [selectAnnotationsDialogOpen, setSelectAnnotationsDialogOpen] = useState(false);
  const [selectedAnnotations, setSelectedAnnotations] = useState(suggestedAnnotations);

  const handleUpdate = (selectedAnnotations) => {
    setSelectedAnnotations(selectedAnnotations);
    onUpdate(selectedAnnotations);
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
                               onUpdate={handleUpdate}
                               onCancel={() => setSelectAnnotationsDialogOpen(false)}/>
    </Fragment>)
}

export default SelectAnnotations;
