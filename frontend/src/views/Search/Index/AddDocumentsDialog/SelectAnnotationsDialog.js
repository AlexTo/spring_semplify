import React, {Fragment, useEffect, useState} from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle, TextField,
  Card, CardContent, TableHead, TableRow, TableCell, TableBody,
  Table, TablePagination, CardActions, Checkbox, CardHeader
} from "@material-ui/core";
import {makeStyles} from "@material-ui/styles";
import Highlighter from "react-highlight-words";
import Link from "@material-ui/core/Link";
import {useDispatch} from "react-redux";
import {entityActions} from "../../../../actions";
import {Autocomplete} from "@material-ui/lab";
import Grid from "@material-ui/core/Grid";
import SubjectSuggestion from "../../../../components/Entity/SubjectSuggestion";

const useStyles = makeStyles(theme => ({
  content: {
    padding: 0
  },
  inner: {
    minWidth: 700
  },
  actions: {
    padding: theme.spacing(1),
    justifyContent: 'flex-end'
  }
}));

function SelectAnnotationsDialog({label, suggestedAnnotations, open, onCancel, onAnnotationsSelected, onAnnotationAdded}) {
  const classes = useStyles();

  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const dispatch = useDispatch();

  const [selectedAnnotations, setSelectedAnnotations] = useState(suggestedAnnotations);

  // to hold the temporarily selected annotations in case the user cancels
  const [tmpSelectedAnnotations, setTmpSelectedAnnotations] = useState(suggestedAnnotations);

  useEffect(() => {
    setTmpSelectedAnnotations(selectedAnnotations);
  }, [selectedAnnotations]);

  const handleSelectAllAnnotations = (event) => {
    const selected = event.target.checked ? suggestedAnnotations : [];
    setTmpSelectedAnnotations(selected);
  };

  const handSelectOneAnnotation = (event, annotation) => {
    if (event.target.checked) {
      setTmpSelectedAnnotations([...tmpSelectedAnnotations, annotation]);
    } else {
      setTmpSelectedAnnotations(tmpSelectedAnnotations.filter(a => a !== annotation));
    }
  };

  const handleAnnotationAdded = (annotation) => {
    annotation.source = 'Manually added';
    setTmpSelectedAnnotations([annotation, ...tmpSelectedAnnotations]);
    onAnnotationAdded(annotation);
  };

  const handleCancel = () => {
    setTmpSelectedAnnotations(selectedAnnotations);
    onCancel();
  };

  const handleSave = () => {
    setSelectedAnnotations(tmpSelectedAnnotations);
    onAnnotationsSelected(tmpSelectedAnnotations);
    onCancel();
  };

  const handleChangePage = (event, page) => {
    setPage(page);
  };

  const handleChangeRowsPerPage = (event) => {
    setSize(event.target.value);
  };

  return (
    <Fragment>
      <Dialog
        open={open}
        fullWidth={true}
        maxWidth="xl"
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description">
        <DialogTitle>{label}</DialogTitle>
        <DialogContent>
          <Grid container spacing={2}>
            <Grid item md={12}>
              <SubjectSuggestion label={"Add annotations"} onOptionSelected={handleAnnotationAdded}/>
            </Grid>
            <Grid item md={12}>
              <Card>
                <CardContent className={classes.content}>
                  <div className={classes.inner}>
                    <Table>
                      <TableHead>
                        <TableRow>
                          <TableCell padding="checkbox">
                            <Checkbox
                              checked={tmpSelectedAnnotations.length === suggestedAnnotations.length}
                              color="primary"
                              indeterminate={
                                tmpSelectedAnnotations.length > 0
                                && tmpSelectedAnnotations.length < suggestedAnnotations.length
                              }
                              onChange={handleSelectAllAnnotations}/>
                          </TableCell>
                          <TableCell>Entity</TableCell>
                          <TableCell>Context</TableCell>
                          <TableCell>Source</TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {suggestedAnnotations.slice(page * size, Math.min((page + 1) * size, suggestedAnnotations.length))
                          .map(annotation =>
                            <TableRow key={annotation.uri}>
                              <TableCell padding="checkbox">
                                <Checkbox checked={tmpSelectedAnnotations.includes(annotation)}
                                          onChange={(event) => handSelectOneAnnotation(event, annotation)}/>
                              </TableCell>
                              <TableCell>
                                <Link href={annotation.uri}
                                      target="_blank"
                                      rel="noopener"
                                      onClick={(e) => {
                                        e.preventDefault();
                                        dispatch(entityActions.showPopupSummary(annotation.uri))
                                      }}>
                                  {annotation.prefLabel}
                                </Link>
                              </TableCell>
                              <TableCell>
                                {annotation.context && <Highlighter
                                  searchWords={[annotation.surfaceForm]}
                                  textToHighlight={annotation.context}/>}
                              </TableCell>
                              <TableCell>{annotation.source}</TableCell>
                            </TableRow>
                          )}
                      </TableBody>
                    </Table>
                  </div>
                </CardContent>
                <CardActions className={classes.actions}>

                  <TablePagination
                    component="div"
                    count={suggestedAnnotations.length}
                    onChangePage={handleChangePage}
                    page={page}
                    rowsPerPage={size}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                    rowsPerPageOptions={[5, 10]}/>
                </CardActions>
              </Card>
            </Grid>
          </Grid>

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
