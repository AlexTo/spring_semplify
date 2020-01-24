import React, {useState} from 'react';
import {Link as RouterLink} from 'react-router-dom';
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
import getInitials from 'src/utils/getInitials';
import ReviewStars from 'src/components/ReviewStars';
import GenericMoreButton from 'src/components/GenericMoreButton';
import TableEditBar from 'src/components/TableEditBar';

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

function Results({className, documents, ...rest}) {
  const classes = useStyles();
  const [selectedDocuments, setSelectedDocuments] = useState([]);
  const [page, setPage] = useState(0);
  const [rowsPerPage, setRowsPerPage] = useState(10);

  const handleSelectAll = (event) => {
    const selectedCustomers = event.target.checked
      ? documents.map((doc) => doc.id)
      : [];

    setSelectedDocuments(selectedCustomers);
  };

  const handleSelectOne = (event, id) => {
    const selectedIndex = selectedDocuments.indexOf(id);
    let newSelectedDocuments = [];

    if (selectedIndex === -1) {
      newSelectedDocuments = newSelectedDocuments.concat(selectedDocuments, id);
    } else if (selectedIndex === 0) {
      newSelectedDocuments = newSelectedDocuments.concat(
        selectedDocuments.slice(1)
      );
    } else if (selectedIndex === selectedDocuments.length - 1) {
      newSelectedDocuments = newSelectedDocuments.concat(
        selectedDocuments.slice(0, -1)
      );
    } else if (selectedIndex > 0) {
      newSelectedDocuments = newSelectedDocuments.concat(
        selectedDocuments.slice(0, selectedIndex),
        selectedDocuments.slice(selectedIndex + 1)
      );
    }

    setSelectedDocuments(newSelectedDocuments);
  };

  const handleChangePage = (event, page) => {
    setPage(page);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(event.target.value);
  };

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}
    >
      <Typography
        color="textSecondary"
        gutterBottom
        variant="body2"
      >
        {documents.length}
        {' '}
        Records found. Page
        {' '}
        {page + 1}
        {' '}
        of
        {' '}
        {Math.ceil(documents.length / rowsPerPage)}
      </Typography>
      <Card>
        <CardHeader
          action={<GenericMoreButton/>}
          title="All documents"
        />
        <Divider/>
        <CardContent className={classes.content}>
          <PerfectScrollbar>
            <div className={classes.inner}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={selectedDocuments.length === documents.length}
                        color="primary"
                        indeterminate={
                          selectedDocuments.length > 0
                          && selectedDocuments.length < documents.length
                        }
                        onChange={handleSelectAll}
                      />
                    </TableCell>
                    <TableCell>Name</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>

                </TableBody>
              </Table>
            </div>
          </PerfectScrollbar>
        </CardContent>
        <CardActions className={classes.actions}>
          <TablePagination
            component="div"
            count={documents.length}
            onChangePage={handleChangePage}
            onChangeRowsPerPage={handleChangeRowsPerPage}
            page={page}
            rowsPerPage={rowsPerPage}
            rowsPerPageOptions={[5, 10, 25]}
          />
        </CardActions>
      </Card>
      <TableEditBar selected={selectedDocuments}/>
    </div>
  );
}

Results.propTypes = {
  className: PropTypes.string,
  customers: PropTypes.array
};

Results.defaultProps = {
  customers: []
};

export default Results;
