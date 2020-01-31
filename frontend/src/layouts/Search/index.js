import React, {Suspense, useState} from 'react';
import {renderRoutes} from 'react-router-config';
import PropTypes from 'prop-types';
import {makeStyles} from '@material-ui/styles';
import {LinearProgress} from '@material-ui/core';
import TopBar from "../Dashboard/TopBar";

const useStyles = makeStyles((theme) => ({
  container: {
    minHeight: '100vh',
    display: 'flex',
    '@media all and (-ms-high-contrast:none)': {
      height: 0 // IE11 fix
    }
  },
  content: {
    flexGrow: 1,
    maxWidth: '100%',
    overflowX: 'hidden',
    paddingTop: 64,
    [theme.breakpoints.down('xs')]: {
      paddingTop: 56
    }
  }
}));

function Search({route}) {
  const classes = useStyles();
  const [openNavBarMobile, setOpenNavBarMobile] = useState(false);

  return (
    <>
      <TopBar onOpenNavBarMobile={() => setOpenNavBarMobile(true)}/>
      <div className={classes.container}>
        <div className={classes.content}>
          <Suspense fallback={<LinearProgress/>}>
            {renderRoutes(route.routes)}
          </Suspense>
        </div>
      </div>
    </>
  );
}

Search.propTypes = {
  route: PropTypes.object
};

export default Search;