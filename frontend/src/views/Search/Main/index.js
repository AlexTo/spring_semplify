import React, {Fragment} from 'react';
import {makeStyles} from '@material-ui/styles';
import {Container, Grid} from '@material-ui/core';
import Page from 'src/components/Page';
import SearchHits from './SearchHits';
import {useSelector} from "react-redux";
import {useQuery} from "@apollo/react-hooks";
import {indexerQueries} from "../../../queries/indexerQueries";
import Buckets from "./Buckets";

const useStyles = makeStyles((theme) => ({
  root: {
    height: '100%',
    backgroundColor: theme.palette.common.white,
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  },
  container: {
    paddingLeft: theme.spacing(3),
    paddingRight: theme.spacing(3)
  },
  results: {}
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

  const {buckets} = data.search;


  return (
    <Page
      className={classes.root}
      title="Search">
      <Grid container className={classes.container} spacing={3}>
        <Grid item xs={3}><Buckets buckets={buckets}/></Grid>
        <Grid item xs={9}><SearchHits result={data.search} className={classes.results}/></Grid>
      </Grid>
    </Page>
  );
}

export default Main;
