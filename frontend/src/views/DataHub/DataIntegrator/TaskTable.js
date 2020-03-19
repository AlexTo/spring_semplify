import React, {useState} from 'react';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {makeStyles} from '@material-ui/styles';
import {
  Avatar,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Checkbox,
  Divider,
  Button,
  Link,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TablePagination,
  TableRow,
  Typography
} from '@material-ui/core';
import GenericMoreButton from 'src/components/GenericMoreButton';
import {useQuery} from "@apollo/react-hooks";
import {taskQueries} from "../../../queries/taskQueries";
import {useDispatch, useSelector} from "react-redux";
import {taskActions} from "../../../actions";

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

function TaskTable({className, ...rest}) {
  const classes = useStyles();

  const taskReducer = useSelector(state => state.taskReducer);
  const dispatch = useDispatch();

  const {loading, error, data} = useQuery(taskQueries.tasks, {
    variables: {
      taskQueryInput: {
        page: taskReducer.page,
        size: taskReducer.pageSize
      }
    },
    pollInterval: 500
  });


  const handleChangePage = (event, page) => {
    dispatch(taskActions.setPage(page));
  };

  const handleChangeRowsPerPage = (event) => {
    dispatch(taskActions.setPageSize(event.target.value));
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
                    <TableCell>Type</TableCell>
                    <TableCell>SubTasks (Finished/Total)</TableCell>
                    <TableCell>Scheduled</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Error</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {data.tasks.tasks.map(t =>
                    <TableRow key={t.id}>
                      <TableCell>{t.type}</TableCell>
                      <TableCell>{t.numberOfSubtasks && `${t.numberOfFinishedSubtasks}/${t.numberOfSubtasks}`}</TableCell>
                      <TableCell>{t.scheduled}</TableCell>
                      <TableCell>{t.taskStatus}</TableCell>
                      <TableCell>{t.error}</TableCell>
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
            count={data.tasks.totalElements}
            onChangePage={handleChangePage}
            onChangeRowsPerPage={handleChangeRowsPerPage}
            page={taskReducer.page}
            rowsPerPage={taskReducer.pageSize}
            rowsPerPageOptions={[10, 20, 50]}/>
        </CardActions>
      </Card>
    </div>
  );
}

TaskTable.propTypes = {
  className: PropTypes.string,
  result: PropTypes.object
};

export default TaskTable;
