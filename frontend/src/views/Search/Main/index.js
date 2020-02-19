import React, {Fragment, useEffect, useState} from 'react';
import {makeStyles} from '@material-ui/styles';
import {Container, Grid} from '@material-ui/core';
import Page from 'src/components/Page';
import SearchHits from './SearchHits';
import {useDispatch, useSelector} from "react-redux";
import {useQuery} from "@apollo/react-hooks";
import {indexerQueries} from "../../../queries/indexerQueries";
import Buckets from "./Buckets";
import {SEARCH_STATE_NEW_SEARCH, searchActions} from "../../../actions";
import _ from 'lodash';

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
  const {query, buckets, searchState, page, size} = searchReducer;
  const dispatch = useDispatch();
  const {loading, error, data} = useQuery(indexerQueries.search, {
    variables: {
      query: {
        q: query,
        page: page,
        size: size
      }
    }
  });

  useEffect(() => {
    if (data && data.search) {
      const {buckets} = data.search;
      if (searchState === SEARCH_STATE_NEW_SEARCH) {
        buckets.forEach(b => {
          b.checked = true;
          b.indeterminate = false;
          b.buckets.forEach(sb => sb.checked = true);
        });
        dispatch(searchActions.updateBuckets(buckets));
      }
    }
  }, [data]);

  useEffect(() => {
    //console.log(buckets);
  }, [buckets]);

  const handleBucketCheckboxChange = (uri) => {
    const newBuckets = _.cloneDeep(buckets);
    const bucket = newBuckets.find(b => b.uri === uri);
    if (bucket.indeterminate) {
      bucket.checked = false;
    } else {
      bucket.checked = !bucket.checked;
    }
    bucket.indeterminate = false;
    bucket.buckets.forEach(sb => sb.checked = bucket.checked);
    dispatch(searchActions.updateBuckets(newBuckets));
  };

  const handleSubBucketCheckboxChange = (bucketUri, subBucketUri) => {
    const newBuckets = _.cloneDeep(buckets);
    const bucket = newBuckets.find(b => b.uri === bucketUri);
    const subBuckets = bucket.buckets;
    const subBucket = subBuckets.find(sb => sb.uri === subBucketUri);
    subBucket.checked = !subBucket.checked;
    bucket.checked = subBuckets.filter(sb => sb.checked).length === subBuckets.length;
    bucket.indeterminate =
      (subBuckets.filter(sb => sb.checked).length !== subBuckets.length) &&
      (subBuckets.filter(sb => !sb.checked).length !== subBuckets.length);
    dispatch(searchActions.updateBuckets(newBuckets));
  };

  const handleApplyFilter = () => {
    console.log(buckets);
  };

  if (loading) return <Fragment>Loading ... </Fragment>;
  if (error) return <Fragment>`Error! ${error.message}`</Fragment>;

  return (
    <Page
      className={classes.root}
      title="Search">
      <Grid container className={classes.container} spacing={3}>
        <Grid item xs={3}>
          <Buckets buckets={buckets}
                   onApplyFilter={handleApplyFilter}
                   onBucketCheckboxChange={handleBucketCheckboxChange}
                   onSubBucketCheckBoxChange={handleSubBucketCheckboxChange}
          /></Grid>
        <Grid item xs={9}><SearchHits result={data.search} className={classes.results}/></Grid>
      </Grid>
    </Page>
  );
}

export default Main;
