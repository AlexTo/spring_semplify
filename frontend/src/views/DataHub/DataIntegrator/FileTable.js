import React, {useEffect, useReducer, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {makeStyles} from "@material-ui/styles";
import {useMutation, useQuery} from "@apollo/react-hooks";
import {fileQueries} from "../../../queries/fileQueries";
import {FILE_ANNOTATION_STATUS_PENDING_REVIEW, fileAnnotationActions} from "../../../actions";
import clsx from "clsx";
import {
  Card, CardActions,
  CardContent,
  CardHeader,
  Divider,
  Table,
  TableBody,
  TableCell,
  TableHead, TablePagination,
  TableRow, colors, Button
} from "@material-ui/core";
import PerfectScrollbar from "react-perfect-scrollbar";
import GenericMoreButton from "../../../components/GenericMoreButton";
import Label from "../../../components/Label";
import FileReviewDialog from "./FileReviewDialog";

const useStyles = makeStyles((theme) => ({
  root: {},
  content: {
    padding: 0
  },
  inner: {
    minWidth: 700
  },
  nameCell: {
    display: 'flex',
    alignItems: 'center'
  },
  avatar: {
    height: 42,
    width: 42,
    marginRight: theme.spacing(1)
  },
  actions: {
    padding: theme.spacing(1),
    justifyContent: 'flex-end'
  }
}));

const statusColors = {
  FILE_ANNOTATION_STATUS_PENDING_REVIEW: colors.orange[600],
  FILE_ANNOTATION_STATUS_REVIEWED: colors.green[600]
};

function FileTable({className, ...rest}) {
  const classes = useStyles();
  const dispatch = useDispatch();

  const [fileAnnotationId, setFileAnnotationId] = useState(null);
  const [fileName, setFileName] = useState(null);
  const [fileReviewDialogOpen, setFileReviewDialogOpen] = useState(false);
  const fileAnnotationReducer = useSelector(state => state.fileAnnotationReducer);

  const [updateFileAnnotation, {}] = useMutation(fileQueries.updateFileAnnotation);

  const {loading, error, data, startPolling, stopPolling} = useQuery(fileQueries.fileAnnotations, {
    variables: {
      fileAnnotationQueryInput: {
        page: fileAnnotationReducer.page,
        size: fileAnnotationReducer.pageSize
      }
    },
    pollInterval: 500
  });

  useEffect(() => {
    if (fileReviewDialogOpen) {
      stopPolling();
    } else {
      startPolling(3000);
    }
  }, [fileReviewDialogOpen]);

  useEffect(() => {
    if (!fileAnnotationId)
      return;
    setFileReviewDialogOpen(true);
  }, [fileAnnotationId]);

  const handleChangePage = (event, page) => {
    dispatch(fileAnnotationActions.setPage(page));
  };

  const handleChangeRowsPerPage = (event) => {
    dispatch(fileAnnotationActions.setPageSize(event.target.value));
  };

  const handleReview = (fileAnnotationId, fileName) => {
    setFileAnnotationId(fileAnnotationId);
    setFileName(fileName)
  };

  const handleApprove = (fileAnnotation) => {
    updateFileAnnotation({
      variables: {
        fileAnnotationInput: fileAnnotation
      }
    }).then(() => {
      setFileReviewDialogOpen(false);
    });
  };

  if (loading || error) return (<></>);

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}>

      <Card>
        <CardHeader
          action={<GenericMoreButton/>}/>
        <Divider/>
        <CardContent className={classes.content}>
          <PerfectScrollbar>
            <div className={classes.inner}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>File Name</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell></TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {data.fileAnnotations.fileAnnotations.map(a =>
                    <TableRow key={a.id}>
                      <TableCell>{a.file.name}</TableCell>
                      <TableCell>
                        <Label
                          color={statusColors[a.status]}
                          variant="outlined">
                          {a.status}
                        </Label>
                      </TableCell>
                      <TableCell>
                        {a.status === FILE_ANNOTATION_STATUS_PENDING_REVIEW && <Button
                          color="primary"
                          size="small"
                          variant="outlined" onClick={() => handleReview(a.id, a.file.name)}>
                          Review
                        </Button>}
                      </TableCell>
                    </TableRow>
                  )}
                </TableBody>
              </Table>
            </div>
          </PerfectScrollbar>
        </CardContent>
        <CardActions className={classes.actions}>
          <TablePagination
            component="div"
            count={data.fileAnnotations.totalElements}
            onChangePage={handleChangePage}
            onChangeRowsPerPage={handleChangeRowsPerPage}
            page={fileAnnotationReducer.page}
            rowsPerPage={fileAnnotationReducer.pageSize}
            rowsPerPageOptions={[10, 20, 50]}/>
        </CardActions>
      </Card>
      <FileReviewDialog open={fileReviewDialogOpen}
                        onClose={() => setFileReviewDialogOpen(false)}
                        onApprove={handleApprove}
                        fileAnnotationId={fileAnnotationId}
                        fileName={fileName}/>
    </div>
  )
}

export default FileTable;
