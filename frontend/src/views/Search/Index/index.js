import React, {useState} from "react";
import {makeStyles} from '@material-ui/styles';
import {Container, Snackbar, SnackbarContent, colors} from '@material-ui/core';

import Header from "./Header";
import Page from "../../../components/Page";
import SearchBar from "../../../components/SearchBar";
import DocumentsTable from "./DocumentsTable";
import AddDocumentsDialog from "./AddDocumentsDialog";
import CheckCircleIcon from '@material-ui/icons/CheckCircleOutlined';
import {useQuery} from "@apollo/react-hooks";
import {indexerQueries} from "../../../queries/indexerQueries";

const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  },
  results: {
    marginTop: theme.spacing(3)
  },
  content: {
    backgroundColor: colors.green[600]
  },
  message: {
    display: 'flex',
    alignItems: 'center'
  },
  icon: {
    marginRight: theme.spacing(2)
  }
}));

function Index() {
  const classes = useStyles();

  const [query, setQuery] = useState("");
  const [page, setPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const {loading, error, data} = useQuery(indexerQueries.search, {
    variables: {
      query: {
        q: query,
        page: page,
        size: pageSize
      }
    }
  });

  const [addDocumentDialogOpen, setAddDocumentDialogOpen] = useState(false);
  const [snackBarOpen, setSnackBarOpen] = useState(false);

  const handleDocumentsSave = () => {
    setAddDocumentDialogOpen(false);
    setSnackBarOpen(true);
  };

  const handleFilter = () => {
  };

  const handleSearch = () => {

  };

  return (
    <Page
      className={classes.root}
      title="Index">
      <Container maxWidth={false}>
        <Header onAddDocument={() => setAddDocumentDialogOpen(true)}/>
        <SearchBar
          onFilter={handleFilter}
          onSearch={handleSearch}/>
        {data && <DocumentsTable
          className={classes.results}
          searchResult={data.search}
          page={page}
          pageSize={pageSize}
          onPageChange={(page) => setPage(page)}
          onPageSizeChange={(pageSize) => setPageSize(pageSize)}/>}
        <AddDocumentsDialog
          open={addDocumentDialogOpen}
          onClose={() => setAddDocumentDialogOpen(false)}
          onSaved={handleDocumentsSave}/>

        <Snackbar
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center'
          }}
          autoHideDuration={5000}
          onClose={() => setSnackBarOpen(false)}
          open={snackBarOpen}>
          <SnackbarContent
            className={classes.content}
            message={(
              <span className={classes.message}>
            <CheckCircleIcon className={classes.icon}/>
            Successfully saved changes!
          </span>
            )}/>
        </Snackbar>
      </Container>
    </Page>
  );

}

export default Index;
