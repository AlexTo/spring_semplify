import React, {useState, useRef, useEffect} from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import {makeStyles} from '@material-ui/styles';
import {
  Grid,
  MenuItem,
  ListItemText,
  Typography
} from '@material-ui/core';
import {ToggleButtonGroup, ToggleButton} from '@material-ui/lab';
import ViewModuleIcon from '@material-ui/icons/ViewModule';
import Paginate from 'src/components/Paginate';
import SearchHitCard from './SearchHitCard';
import {useDispatch, useSelector} from "react-redux";
import {searchActions} from "../../../actions";

const useStyles = makeStyles((theme) => ({
  root: {},
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    flexWrap: 'wrap',
    marginBottom: theme.spacing(2)
  },
  title: {
    position: 'relative',
    '&:after': {
      position: 'absolute',
      bottom: -8,
      left: 0,
      content: '" "',
      height: 3,
      width: 48,
      backgroundColor: theme.palette.primary.main
    }
  },
  actions: {
    display: 'flex',
    alignItems: 'center'
  },
  sortButton: {
    textTransform: 'none',
    letterSpacing: 0,
    marginRight: theme.spacing(2)
  },
  paginate: {
    marginTop: theme.spacing(3),
    display: 'flex',
    justifyContent: 'center'
  }
}));

function SearchHits({className, result, ...rest}) {
  const classes = useStyles();
  const searchReducer = useSelector((state) => state.searchReducer);
  const {
    selectedAnnotations, page, size
  } = searchReducer;
  const dispatch = useDispatch();

  const [mode, setMode] = useState('grid');

  const {totalHits, searchHits} = result;

  const handleModeChange = (event, value) => {
    setMode(value);
  };

  return (
    <div {...rest} className={clsx(classes.root, className)}>
      <div className={classes.header}>
        <Typography
          className={classes.title}
          variant="h5">
          About
          {' '}
          {totalHits}
          {' '}
          results
        </Typography>
        <div className={classes.actions}>
          <ToggleButtonGroup
            exclusive
            onChange={handleModeChange}
            size="small"
            value={mode}>
            <ToggleButton value="grid">
              <ViewModuleIcon/>
            </ToggleButton>
          </ToggleButtonGroup>
        </div>
      </div>
      <Grid container spacing={3}>
        {searchHits.map((searchHit) => (
          <Grid
            item
            key={searchHit.id}
            md={mode === 'grid' ? 4 : 12}
            sm={mode === 'grid' ? 6 : 12}
            xs={12}>
            <SearchHitCard searchHit={searchHit}/>
          </Grid>
        ))}
      </Grid>
      <div className={classes.paginate}>
        <Paginate pageCount={Math.ceil(totalHits / size)}
                  forcePage={page}
                  onPageChange={(data) => dispatch(searchActions.applyFilters(selectedAnnotations, data.selected, size))}/>
      </div>
    </div>
  );
}

SearchHits.propTypes = {
  className: PropTypes.string,
  result: PropTypes.object
};

export default SearchHits;
