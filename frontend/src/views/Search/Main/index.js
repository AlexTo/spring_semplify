import React, {Fragment} from 'react';
import {makeStyles} from '@material-ui/styles';
import {Container} from '@material-ui/core';
import Page from 'src/components/Page';
import SearchHits from './SearchHits';
import {useSelector} from "react-redux";
import {useQuery} from "@apollo/react-hooks";
import {indexerQueries} from "../../../queries/indexerQueries";

const useStyles = makeStyles((theme) => ({
  root: {
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  },
  results: {
    marginTop: theme.spacing(6)
  }
}));

function Main() {
  const classes = useStyles();
  const searchReducer = useSelector(state => state.searchReducer);
  const {query, page, size} = searchReducer;

  const {loading, error, data} = useQuery(indexerQueries.search, {
    variables: {
      query: {
        q: searchReducer.query,
        page: page,
        size: size
      }
    }
  });

  if (loading) return <Fragment>Loading ... </Fragment>;
  if (error) return <Fragment>`Error! ${error.message}`</Fragment>;
  return (
    <Page
      className={classes.root}
      title="Search">
      <Container maxWidth="lg">
        <SearchHits result={data.search} className={classes.results}/>
      </Container>
    </Page>
  );
}

export default Main;
