import {
  AppBar,
  Button, Card, CardActions, CardContent, Checkbox,
  Dialog,
  DialogActions,
  DialogContent,
  IconButton,
  Slide, Table, TableBody, TableCell, TableHead, TablePagination, TableRow,
  Toolbar,
  Typography
} from "@material-ui/core";
import React, {useEffect, useState} from "react";
import CloseIcon from "@material-ui/icons/Close";
import {makeStyles} from "@material-ui/core/styles";
import {useKeycloak} from "@react-keycloak/web";
import {useQuery} from "@apollo/react-hooks";
import {fileQueries} from "../../../../queries/fileQueries";
import Link from "@material-ui/core/Link";
import {entityActions, FILE_ANNOTATION_STATUS_REVIEWED} from "../../../../actions";
import Highlighter from "react-highlight-words";
import {useDispatch} from "react-redux";
import Grid from "@material-ui/core/Grid";
import DeleteIcon from '@material-ui/icons/Delete';
import SubjectSuggestion from "../../../../components/Entity/SubjectSuggestion";
import AnnotationTableEditBar from "./AnnotationTableEditBar";

const useStyles = makeStyles(theme => ({
  appBar: {
    position: 'relative',
  },
  title: {
    marginLeft: theme.spacing(2),
    flex: 1,
    color: theme.palette.common.white
  },
  backButton: {
    marginRight: theme.spacing(1),
  },
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

const Transition = React.forwardRef(function Transition(props, ref) {
  return <Slide direction="down" ref={ref} {...props} />;
});

function FileReviewDialog({open, onClose, onApprove, fileAnnotationId, fileName, onSubmit}) {
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const classes = useStyles();
  const dispatch = useDispatch();

  const [allAnnotations, setAllAnnotations] = useState([]);
  const [addedAnnotations, setAddedAnnotations] = useState([]);
  const [selectedAnnotations, setSelectedAnnotations] = useState([]);

  const {loading, error, data} = useQuery(fileQueries.fileAnnotationResources, {
    variables: {
      fileAnnotationId
    },
    skip: !fileAnnotationId
  });

  useEffect(() => {
    if (!data) {
      return;
    }
    const {fileAnnotationResources} = data;
    setAllAnnotations(fileAnnotationResources);
  }, [data]);

  useEffect(() => {
    setAllAnnotations([...addedAnnotations, ...allAnnotations]);
  }, [addedAnnotations]);

  const handleChangePage = (event, page) => {
    setPage(page);
  };

  const handleChangeRowsPerPage = (event) => {
    setSize(event.target.value);
  };

  const handleAnnotationAdded = (annotation) => {
    setAddedAnnotations([annotation, ...addedAnnotations]);
  };
  const handleSelectAll = (e) => {
    const selected = e.target.checked ? allAnnotations : [];
    setSelectedAnnotations(selected);
  };

  const handSelectOne = (e, annotation) => {
    if (e.target.checked) {
      setSelectedAnnotations([...selectedAnnotations, annotation]);
    } else {
      setSelectedAnnotations(selectedAnnotations.filter(a => a !== annotation));
    }
  };

  const handleDelete = (annotations) => {
    setAllAnnotations(allAnnotations.filter(a => !annotations.includes(a)));
    setSelectedAnnotations(selectedAnnotations.filter(a => !annotations.includes(a)));
  };

  const handleApprove = () => {
    const approvedAnnotations = allAnnotations.map(a => {
      return {
        id: a.id,
        uri: a.uri
      }
    });
    onApprove({
      id: fileAnnotationId,
      status: FILE_ANNOTATION_STATUS_REVIEWED,
      annotationResources: approvedAnnotations
    })
  };

  if (loading || error || !data) return (<></>);


  return (
    <>
      <Dialog open={open} TransitionComponent={Transition} maxWidth="lg" fullWidth>
        <AppBar className={classes.appBar}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={onClose} aria-label="close">
              <CloseIcon/>
            </IconButton>
            <Typography variant="h4" className={classes.title}>
              {fileName}
            </Typography>
          </Toolbar>
        </AppBar>
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
                            <Checkbox checked={selectedAnnotations.length === allAnnotations.length}
                                      indeterminate={selectedAnnotations.length > 0 && selectedAnnotations.length !== allAnnotations.length}
                                      onChange={handleSelectAll}/>
                          </TableCell>
                          <TableCell>Entity</TableCell>
                          <TableCell>Context</TableCell>
                          <TableCell>Source</TableCell>
                          <TableCell></TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {allAnnotations
                          .slice(page * size, Math.min((page + 1) * size, allAnnotations.length))
                          .map(a =>
                            <TableRow key={a.uri}>
                              <TableCell padding="checkbox">
                                <Checkbox checked={selectedAnnotations.includes(a)}
                                          onChange={e => handSelectOne(e, a)}/>
                              </TableCell>
                              <TableCell>
                                <Link href={a.uri}
                                      target="_blank"
                                      rel="noopener"
                                      onClick={(e) => {
                                        e.preventDefault();
                                        dispatch(entityActions.showPopupSummary(a.uri))
                                      }}>
                                  {a.prefLabel}
                                </Link>
                              </TableCell>
                              <TableCell>
                                {a.context && <Highlighter
                                  searchWords={[a.surfaceForm]}
                                  textToHighlight={a.context}/>}
                              </TableCell>
                              <TableCell>{a.source}</TableCell>
                              <TableCell>
                                <IconButton aria-label="delete" onClick={() => handleDelete([a])}>
                                  <DeleteIcon/>
                                </IconButton>
                              </TableCell>
                            </TableRow>)}
                      </TableBody>
                    </Table>
                  </div>
                </CardContent>
                <CardActions>
                  <TablePagination
                    component="div"
                    count={allAnnotations.length}
                    onChangePage={handleChangePage}
                    page={page}
                    rowsPerPage={size}
                    onChangeRowsPerPage={handleChangeRowsPerPage}
                    rowsPerPageOptions={[5, 10]}/>
                </CardActions>
              </Card>
              <AnnotationTableEditBar selected={selectedAnnotations}
                                      onDelete={() => handleDelete(selectedAnnotations)}/>
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button
            variant="text"
            onClick={onClose}>Cancel</Button>
          <Button variant="contained"
                  color="primary" onClick={handleApprove}>
            Approve
          </Button>
        </DialogActions>
      </Dialog>
    </>
  )
}

export default FileReviewDialog;
