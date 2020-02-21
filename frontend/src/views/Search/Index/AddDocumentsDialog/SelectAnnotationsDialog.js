import React, {Fragment, useEffect, useState} from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Card
} from "@material-ui/core";
import {makeStyles} from "@material-ui/styles";
import EntityChip from "../../../../components/Entity/EntityChip";

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

function SelectAnnotationsDialog({label, suggestedAnnotations, open, onCancel, onUpdate}) {
  const classes = useStyles();
  const [selectedAnnotations, setSelectedAnnotations] = useState(suggestedAnnotations);

  // to hold the temporarily selected annotations in case the user cancels
  const [tmpSelectedAnnotations, setTmpSelectedAnnotations] = useState(suggestedAnnotations);

  useEffect(() => {
    setTmpSelectedAnnotations(selectedAnnotations);
  }, [selectedAnnotations]);

  const handleUnselectAnnotation = (annotation) => {
    const selected = tmpSelectedAnnotations.filter(t => t.uri !== annotation.uri);
    setTmpSelectedAnnotations(selected);
  };

  const handleSelectAnnotation = (annotation) => {
    setTmpSelectedAnnotations([...tmpSelectedAnnotations, annotation]);
  };

  const handleSelectAllAnnotations = () => {
    setTmpSelectedAnnotations(suggestedAnnotations);
  };

  const handleDeselectAllAnnotations = () => {
    setTmpSelectedAnnotations([]);
  };

  const handleCancel = () => {
    setTmpSelectedAnnotations(selectedAnnotations);
    onCancel();
  };

  const handleSave = () => {
    setSelectedAnnotations(tmpSelectedAnnotations);
    onUpdate(tmpSelectedAnnotations);
    onCancel();
  };

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
              {tmpSelectedAnnotations.map((annotation) => (
                <EntityChip
                  color="primary"
                  className={classes.chip}
                  size="small"
                  key={annotation.uri}
                  uri={annotation.uri}
                  surfaceForm={annotation.surfaceForm}
                  prefLabel={annotation.prefLabel}
                  onDelete={() => handleUnselectAnnotation(annotation)}/>
              ))}
              {suggestedAnnotations.filter(t => !tmpSelectedAnnotations.includes(t)).map((annotation) => (
                <EntityChip
                  className={classes.chip}
                  size="small"
                  key={annotation.uri}
                  uri={annotation.uri}
                  surfaceForm={annotation.surfaceForm}
                  prefLabel={annotation.prefLabel}
                  onClick={() => handleSelectAnnotation(annotation)}/>
              ))}
            </div>
          </Card>
          <Button onClick={handleDeselectAllAnnotations}>Deselect all</Button>
          <Button onClick={handleSelectAllAnnotations}>Select all</Button>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={handleCancel}>Cancel</Button>
          <Button
            color="primary"
            onClick={handleSave}>Update</Button>
        </DialogActions>
      </Dialog>
    </Fragment>
  )
}

export default SelectAnnotationsDialog;
