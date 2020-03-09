import React, {useState} from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import {makeStyles} from '@material-ui/styles';
import {Grid, Typography, ButtonGroup, Button} from '@material-ui/core';

const useStyles = makeStyles(() => ({
  root: {}
}));

function Header({className, onSubmitFiles, onSubmitURLs, ...rest}) {
  const classes = useStyles();

  return (
    <div {...rest} className={clsx(classes.root, className)}>
      <Grid
        alignItems="flex-end"
        container
        justify="space-between"
        spacing={3}>
        <Grid item>
          <Typography
            component="h2"
            gutterBottom
            variant="overline">
            Data Hub
          </Typography>
          <Typography
            component="h1"
            variant="h3">
            Data Integrator
          </Typography>
        </Grid>
        <Grid item>
          <ButtonGroup variant="contained" color="primary" aria-label="contained primary button group">
            <Button onClick={onSubmitFiles}>FILES</Button>
            <Button>URLS</Button>
            <Button>SQL</Button>
          </ButtonGroup>
        </Grid>
      </Grid>
    </div>
  );
}

Header.propTypes = {
  className: PropTypes.string
};

export default Header;
