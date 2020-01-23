import React from "react";
import {makeStyles} from '@material-ui/styles';
import {Container} from '@material-ui/core';
import Header from "./Header";
import Page from "../../../components/Page";
import SearchBar from "../../../components/SearchBar";
import Results from "./Results";


const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  },
  results: {
    marginTop: theme.spacing(3)
  }
}));

function IndexedDocuments() {
  const classes = useStyles();

  const handleFilter = () => {
  };

  const handleSearch = () => {
  };

  return (
    <Page
      className={classes.root}
      title="Indexed Documents">
      <Container maxWidth={false}>
        <Header/>
        <SearchBar
          onFilter={handleFilter}
          onSearch={handleSearch}/>
        <Results
          className={classes.results}
          customers={[]}/>
      </Container>
    </Page>
  );

}

export default IndexedDocuments;
