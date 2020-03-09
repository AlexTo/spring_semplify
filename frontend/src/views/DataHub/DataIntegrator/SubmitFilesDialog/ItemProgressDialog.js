import React from "react";
import {Button, Dialog, DialogActions, DialogContent, LinearProgress, DialogContentText} from "@material-ui/core";

function ItemProgressDialog({open, onCancel, currentItem, completed}) {
  return (
    <div>
      <Dialog
        open={open}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        fullWidth={true}
        maxWidth="md">
        <DialogContent>
          <DialogContentText>
            {currentItem}
          </DialogContentText>
          <LinearProgress variant="determinate" value={completed}/>
        </DialogContent>
        <DialogActions>
          <Button
            variant="text"
            onClick={onCancel}>Cancel</Button>
        </DialogActions>
      </Dialog>
    </div>
  )
}

export default ItemProgressDialog;
