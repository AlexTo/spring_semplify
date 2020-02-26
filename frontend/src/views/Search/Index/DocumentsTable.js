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
import DocumentsTableEditBar from "./DocumentsTableEditBar";

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

function DocumentsTable({className, searchResult, page, pageSize, onPageChange, onPageSizeChange, ...rest}) {
  const classes = useStyles();

  const [selectedDocuments, setSelectedDocuments] = useState([]);

  const handleSelectAll = (event) => {
    const selectedDocuments = event.target.checked
      ? searchResult.searchHits.map((doc) => doc.id)
      : [];

    setSelectedDocuments(selectedDocuments);
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
    onPageChange(page);
  };

  const handleChangeRowsPerPage = (event) => {
    onPageSizeChange(event.target.value);
  };

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}>
      <Typography
        color="textSecondary"
        gutterBottom
        variant="body2">
        {searchResult.totalHits}
        {' '}
        Records found. Page
        {' '}
        {page + 1}
        {' '}
        of
        {' '}
        {Math.ceil(searchResult.totalHits / pageSize)}
      </Typography>
      <Card>
        <CardHeader
          action={<GenericMoreButton/>}
          title="All documents"/>
        <Divider/>
        <CardContent className={classes.content}>
          <PerfectScrollbar>
            <div className={classes.inner}>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell padding="checkbox">
                      <Checkbox
                        checked={selectedDocuments.length === searchResult.searchHits.length}
                        color="primary"
                        indeterminate={
                          selectedDocuments.length > 0
                          && selectedDocuments.length < searchResult.searchHits.length
                        }
                        onChange={handleSelectAll}/>
                    </TableCell>
                    <TableCell>URI</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {searchResult.searchHits.map(searchHit => {
                    const {content} = searchHit;
                    return <TableRow
                      hover
                      selected={selectedDocuments.indexOf(searchHit.id) !== -1}
                      key={searchHit.id}>
                      <TableCell padding="checkbox">
                        <Checkbox
                          checked={
                            selectedDocuments.indexOf(searchHit.id) !== -1
                          }
                          color="primary"
                          onChange={(event) => handleSelectOne(event, searchHit.id)}
                          value={selectedDocuments.indexOf(searchHit.id) !== -1}
                        />
                      </TableCell>
                      <TableCell>
                        {content.uri}
                      </TableCell>
                    </TableRow>
                  })
                  }
                </TableBody>
              </Table>
            </div>
          </PerfectScrollbar>
        </CardContent>
        <CardActions className={classes.actions}>
          <TablePagination
            component="div"
            count={searchResult.totalHits}
            onChangePage={handleChangePage}
            onChangeRowsPerPage={handleChangeRowsPerPage}
            page={page}
            rowsPerPage={pageSize}
            rowsPerPageOptions={[10, 20, 50]}/>
        </CardActions>
      </Card>
      <DocumentsTableEditBar selected={selectedDocuments}/>
    </div>
  );
}

DocumentsTable.propTypes = {
  className: PropTypes.string,
  customers: PropTypes.array
};

DocumentsTable.defaultProps = {
  customers: []
};

export default DocumentsTable;
