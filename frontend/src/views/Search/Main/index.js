import React, {Fragment, useEffect} from 'react';
import {makeStyles} from '@material-ui/styles';
import {Grid, CircularProgress} from '@material-ui/core';
import Page from 'src/components/Page';
import SearchHits from './SearchHits';
import {useDispatch, useSelector} from 'react-redux';
import {useQuery} from '@apollo/react-hooks';
import {indexerQueries} from '../../../queries/indexerQueries';
import Buckets from './Buckets';
import {SEARCH_STATE_NEW_SEARCH, searchActions} from '../../../actions';
import _ from 'lodash';
import Backdrop from "@material-ui/core/Backdrop";

const useStyles = makeStyles((theme) => ({
  root: {
    height: '100%',
    backgroundColor: theme.palette.common.white,
    paddingTop: theme.spacing(3),
    paddingBottom: theme.spacing(3)
  },
  backdrop: {
    zIndex: theme.zIndex.drawer + 1,
  },
  container: {
    paddingLeft: theme.spacing(3),
    paddingRight: theme.spacing(3)
  },
  results: {}
}));

function Main() {
  const classes = useStyles();
  const searchReducer = useSelector((state) => state.searchReducer);
  const {
    query, buckets, selectedAnnotations, searchState, page, size
  } = searchReducer;
  const dispatch = useDispatch();
  const {loading, error, data} = useQuery(indexerQueries.search, {
    variables: {
      query: {
        q: query,
        selectedAnnotations: selectedAnnotations,
        page: page,
        size: size
      }
    },
    fetchPolicy: "network-only"
  });

  useEffect(() => {
    if (data && data.search) {
      const {buckets} = data.search;
      if (searchState === SEARCH_STATE_NEW_SEARCH) {
        buckets.forEach(b => {
          b.checked = false;
          b.indeterminate = false;
          b.buckets.forEach(sb => sb.checked = false);
        });
        dispatch(searchActions.updateBuckets(buckets));
      }
    }
  }, [data]);

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
    const selectedAnnotations = buckets.flatMap(b => b.buckets.filter(sb => sb.checked === true).map(sb => sb.uri));
    dispatch(searchActions.applyFilters(selectedAnnotations, page, size));
  };

  if (loading) return <Fragment>
    <Backdrop className={classes.backdrop} open={loading}>
      <CircularProgress/>
    </Backdrop>
  </Fragment>;

  if (error) return <Fragment>`Error! ${error.message}`</Fragment>;

  return (
    <Page className={classes.root} title="Search">
      <Grid container className={classes.container} spacing={2}>
        <Grid item xs={2}>
          <Buckets buckets={buckets}
                   onApplyFilter={handleApplyFilter}
                   onBucketCheckboxChange={handleBucketCheckboxChange}
                   onSubBucketCheckBoxChange={handleSubBucketCheckboxChange}/>
        </Grid>
        <Grid item xs={10}>
          <SearchHits result={data.search} className={classes.results}/>
        </Grid>
      </Grid>

    </Page>
  );
}

export default Main;
