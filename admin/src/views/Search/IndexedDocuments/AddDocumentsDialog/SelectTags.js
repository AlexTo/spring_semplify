import React, {Fragment, useState} from "react";
import {
  Button
} from "@material-ui/core";
import SelectTagsDialog from "./SelectTagsDialog";

function SelectTags({id, label, suggestedTags}) {

  const [selectTagsDialogOpen, setSelectTagsDialogOpen] = useState(false);

  return (
    <Fragment>
      <Button
        color="primary"
        size="small"
        variant="outlined"
        onClick={() => setSelectTagsDialogOpen(true)}>
        {`${suggestedTags.length} Tags`}
      </Button>
      <SelectTagsDialog id={id}
                        label={label}
                        suggestedTags={suggestedTags}
                        open={selectTagsDialogOpen}
                        onCancel={() => setSelectTagsDialogOpen(false)}/>
    </Fragment>)
}

export default SelectTags;
