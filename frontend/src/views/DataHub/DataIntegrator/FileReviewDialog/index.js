import {
  AppBar,
  Button, Checkbox,
  Dialog,
  DialogActions,
  DialogContent,
  IconButton,
  Slide, Table, TableBody, TableCell, TableHead, TableRow,
  Toolbar,
  Typography
} from "@material-ui/core";
import React from "react";
import CloseIcon from "@material-ui/icons/Close";
import {makeStyles} from "@material-ui/core/styles";
import {useKeycloak} from "@react-keycloak/web";
import {useQuery} from "@apollo/react-hooks";
import {fileQueries} from "../../../../queries/fileQueries";
import Link from "@material-ui/core/Link";
import {entityActions} from "../../../../actions";
import Highlighter from "react-highlight-words";
import {useDispatch} from "react-redux";

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

function FileReviewDialog({open, onClose, onCancel, fileAnnotationId, onSubmit}) {
  const classes = useStyles();
  const {keycloak} = useKeycloak();
  const dispatch = useDispatch();
  const {loading, error, data} = useQuery(fileQueries.fileAnnotationResources, {
    variables: {
      fileAnnotationId
    },
    skip: !fileAnnotationId
  });

  if (loading || error || !data) return (<></>);

  const {fileAnnotationResources} = data;

  return (
    <>
      <Dialog open={open} TransitionComponent={Transition} maxWidth="lg" fullWidth>
        <AppBar className={classes.appBar}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={onClose} aria-label="close">
              <CloseIcon/>
            </IconButton>
            <Typography variant="h4" className={classes.title}>
              Review
            </Typography>
          </Toolbar>
        </AppBar>
        <DialogContent>
          <div className={classes.inner}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell padding="checkbox">

                  </TableCell>
                  <TableCell>Entity</TableCell>
                  <TableCell>Context</TableCell>
                  <TableCell>Source</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {fileAnnotationResources
                  .map(annotation =>
                    <TableRow key={annotation.id}>
                      <TableCell padding="checkbox">

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
        </DialogContent>
        <DialogActions>
          <DialogActions>
            <Button
              variant="text"
              onClick={onCancel}>Cancel</Button>
          </DialogActions>
          <Button variant="contained"
                  color="primary">
            Approve
          </Button>
        </DialogActions>
      </Dialog>
    </>
  )
}

export default FileReviewDialog;
