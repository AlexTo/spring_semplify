import React, {Fragment} from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Card,
  Chip
} from "@material-ui/core";
import {makeStyles} from "@material-ui/styles";

const useStyles = makeStyles(theme => ({
  chips: {
    padding: theme.spacing(2),
    display: 'flex',
    alignItems: 'center',
    flexWrap: 'wrap'
  },
  chip: {
    margin: theme.spacing(1)
  }
}));

function SelectTagsDialog({id, label, suggestedTags, open, onCancel}) {
  const classes = useStyles();

  return (
    <Fragment>
      <Dialog
        open={open}
        fullWidth={true}
        maxWidth="lg"
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description">
        <DialogTitle>Update tags</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {label}
          </DialogContentText>
          <Card>
            <div className={classes.chips}>
              {suggestedTags.map((tag) => (
                <Chip
                  className={classes.chip}
                  key={tag.uri}
                  label={tag.surfaceForm}
                  onDelete={() => {
                  }}
                />
              ))}
            </div>

          </Card>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={onCancel}>Cancel</Button>
          <Button
            color="primary"
            onClick={onCancel}>Update</Button>
        </DialogActions>
      </Dialog>
    </Fragment>
  )
}

export default SelectTagsDialog;
